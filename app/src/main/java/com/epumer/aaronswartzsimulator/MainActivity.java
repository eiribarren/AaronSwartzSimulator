package com.epumer.aaronswartzsimulator;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements MainFragment.MainFragmentListener,
               PistaFragment.PistaFragmentListener,
               MultiRespuestaFragment.MultiRespuestaListener,
               HistorialFragment.HistorialFragmentListener {

    public static final String MY_PREFS = "AaronPreferences";
    protected List<PreguntaSiONo> preguntasSiONo;

    int[] respuestasPrimeraPregunta = {R.string.primera_multipregunta_primera_respuesta, R.string.primera_multipregunta_segunda_respuesta, R.string.primera_multipregunta_tercera_respuesta, R.string.primera_multipregunta_cuarta_respuesta};
    int[] respuestasSegundaPregunta = {R.string.segunda_multipregunta_primera_respuesta, R.string.segunda_multipregunta_segunda_respuesta, R.string.segunda_multipregunta_tercera_respuesta, R.string.segunda_multipregunta_cuarta_respuesta};
    int[] respuestasTerceraPregunta = {R.string.tercera_multipregunta_primera_respuesta, R.string.tercera_multipregunta_segunda_respuesta, R.string.tercera_multipregunta_tercera_respuesta, R.string.tercera_multipregunta_cuarta_respuesta};
    protected List<PreguntaMultiRespuesta> preguntasmulti;

    protected EditText et_nombre;
    protected int preguntaActual, fragmentoAnterior, fragmentoActual, puntuacion;
    protected MainFragment main;
    protected PistaFragment pista;
    protected MultiRespuestaFragment multi;
    protected AlertDialog ad_final, ad_principio, ad_todorespondido, ad_guardarnombre;
    protected Toolbar mainToolbar;
    protected ArrayList<Pregunta> preguntas;
    AaronSwartzDbHelper asdbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        asdbhelper = new AaronSwartzDbHelper(this, AaronSwartzDb._DB_, null, 9);
        preguntasmulti = Arrays.asList(new PreguntaMultiRespuesta(getResources().getString(R.string.primera_multipregunta), getResources().getString(R.string.primera_multipregunta_primera_respuesta), R.drawable.muerto, respuestasPrimeraPregunta),
                new PreguntaMultiRespuesta(getResources().getString(R.string.segunda_multipregunta), getResources().getString(R.string.segunda_multipregunta_segunda_respuesta), R.drawable.muerto, respuestasSegundaPregunta),
                new PreguntaMultiRespuesta(getResources().getString(R.string.tercera_multipregunta), getResources().getString(R.string.tercera_multipregunta_primera_respuesta), R.drawable.muerto, respuestasTerceraPregunta));
        preguntasSiONo = getSimplePreguntas();
        preguntas = new ArrayList<Pregunta>();
        main = new MainFragment();
        pista = new PistaFragment();
        multi = new MultiRespuestaFragment();
        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        AlertDialog.Builder bob = new AlertDialog.Builder(this);
        bob.setMessage(R.string.preguntar_si_continuar_final);
        bob.setNegativeButton(R.string.negativo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        bob.setPositiveButton(R.string.afirmativo, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                preguntaActual = 0;
                if ( fragmentoActual == 0 ) {
                    main.ponerPregunta(preguntas.get(preguntaActual).getPregunta());

                } else if ( fragmentoActual == 2 ) {
                    multi.ponerPregunta(preguntas.get(preguntaActual));
                }
            }
        });
        ad_final = bob.create();
        bob.setMessage(R.string.preguntar_si_continuar_principio);
        bob.setPositiveButton(R.string.afirmativo, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                preguntaActual = preguntas.size() - 1;
                if ( fragmentoActual == 0 ) {
                    main.ponerPregunta(preguntas.get(preguntaActual).getPregunta());
                } else if ( fragmentoActual == 2 ) {
                    multi.ponerPregunta(preguntas.get(preguntaActual));
                }
            }
        });
        ad_principio = bob.create();
        bob.setMessage(R.string.todo_respondido);
        bob.setPositiveButton(R.string.afirmativo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad_guardarnombre.show();
            }
        });
        bob.setNegativeButton(R.string.negativo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reiniciarPartida();
            }
        });
        ad_todorespondido = bob.create();
        bob.setMessage(R.string.introducir_nombre);
        View nombreView = getLayoutInflater().inflate(R.layout.alerta_guardar_puntuacion, null);
        et_nombre = (EditText) nombreView.findViewById(R.id.guardarNombreText);
        bob.setView(nombreView);
        bob.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                guardarPuntuacion(et_nombre.getText().toString());
                reiniciarPartida();
            }
        });
        bob.setNegativeButton(null, null);
        ad_guardarnombre = bob.create();
        fragmentoActual = prefs.getInt("fragmentoActual", 0 );
        preguntaActual = prefs.getInt("preguntaActual", 0 );
        puntuacion = prefs.getInt("puntuacion", 0);
        if ( prefs.getInt("fragmentoAnterior", 0) == 0 ) {
            for ( PreguntaSiONo pregunta : preguntasSiONo ) {
                preguntas.add(pregunta);
            }
        } else {
            for ( PreguntaMultiRespuesta pregunta : preguntasmulti ) {
                preguntas.add(pregunta);
            }
        }
        if ( fragmentoActual == 0 ) {
            abrirPregunta();
        } else if ( fragmentoActual == 1 ) {
            abrirPista();
        } else if ( fragmentoActual == 2 ){
            abrirMultiPregunta();
        } else if ( fragmentoActual == 3 ) {
            abrirHistorial();
        }
    }

    public void abrirPista() {
        if (getPreguntaActual().tienePista()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFragment, pista)
                    .commit();
            if (fragmentoActual != 1) {
                fragmentoAnterior = fragmentoActual;
            }
            fragmentoActual = 1;
        }
    }

    public void abrirPregunta() {
        preguntas = new ArrayList<Pregunta>();
        for ( PreguntaSiONo pregunta : preguntasSiONo ) {
            preguntas.add(pregunta);
        }
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.mainFragment, main)
                                   .commit();
        if ( fragmentoActual != 0 ) {
            fragmentoAnterior = fragmentoActual;
            if ( fragmentoActual == 2 ) {
                preguntaActual = 0;
            }
        }
        fragmentoActual = 0;
    }

    public void abrirHistorial() {
        HistorialFragment historial = new HistorialFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, historial)
                .commit();
        if ( fragmentoActual != 3 ) {
            fragmentoAnterior = fragmentoActual;
        }
        fragmentoActual = 3;
    }

    public void abrirMultiPregunta() {
        preguntas = new ArrayList<Pregunta>();
        for ( PreguntaMultiRespuesta pregunta : preguntasmulti ) {
            preguntas.add(pregunta);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, multi)
                .commit();
        if ( fragmentoActual != 2 ) {
            fragmentoAnterior = fragmentoActual;
            if ( fragmentoActual == 0 ) {
                preguntaActual = 0;
            }
        }
        fragmentoActual = 2;
    }

    public void nextButtonListener() {
        if ( preguntaActual < preguntas.size() - 1 ) {
            preguntaActual++;
        } else {
            ad_final.show();
        }
    }

    public void respuestaButtonListener(Object respuesta) {
        if ( !preguntas.get(preguntaActual).estaRespondida()) {
            if (preguntas.get(preguntaActual).comprobarRespuesta(respuesta)) {
                Toast.makeText(this, "Respuesta correcta!", Toast.LENGTH_SHORT).show();
                puntuacion++;
            } else {
                Toast.makeText(this, "Respuesta incorrecta!", Toast.LENGTH_SHORT).show();
            }
            boolean todoRespondido = true;
            for ( Pregunta pregunta : preguntas ) {
                if ( !pregunta.estaRespondida() ) {
                    todoRespondido = false;
                }
            }
            if ( todoRespondido ) {
                ad_todorespondido.show();
            }
        } else {
            Toast.makeText(this, "Esta pregunta ya la has respondido!", Toast.LENGTH_SHORT).show();
        }
    }

    public void backButtonListener() {
        if ( preguntaActual > 0 ) {
            preguntaActual--;
        } else {
            ad_principio.show();
        }
    }

    public Pregunta getPreguntaActual() {
        return preguntas.get(preguntaActual);
    }

    public void abrirAlertAddPregunta() {
        AlertDialog.Builder bob = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.alerta_guardar_pregunta, null);
        final EditText etNombrePregunta = (EditText)v.findViewById(R.id.guardarPregunta);
        final CheckBox cbRespuesta = (CheckBox)v.findViewById(R.id.guardarRespuesta);
        bob.setView(v);
        bob.setTitle(R.string.introducir_pregunta);
        bob.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addPregunta(etNombrePregunta.getText().toString(), cbRespuesta.isChecked());
            }
        });
        AlertDialog ad = bob.create();
        ad.show();
    }

    public void addPregunta(String pregunta, boolean respuesta) {
        SQLiteDatabase db = asdbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        int respuestaInt;
        if ( respuesta ) {
            respuestaInt = 1;
        } else {
            respuestaInt = 0;
        }
        cv.put(AaronSwartzDb._COLUMNA_PREGUNTA_PREGUNTA_, pregunta);
        cv.put(AaronSwartzDb._COLUMNA_PREGUNTA_RESPUESTA_, respuestaInt);
        db.insert(AaronSwartzDb._TABLA_PREGUNTAS_, null, cv);
        preguntas.add(new PreguntaSiONo(pregunta, respuesta));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.casualMode:
                abrirPregunta();
                break;
            case R.id.hardcoreMode:
                abrirMultiPregunta();
                break;
            case R.id.historial:
                abrirHistorial();
                break;
            case R.id.addPregunta:
                abrirAlertAddPregunta();
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void abrirFragmentoAnterior() {
        if ( fragmentoAnterior == 0 ) {
            abrirPregunta();
        } else if ( fragmentoAnterior == 1 ){
            abrirPista();
        } else {
            abrirMultiPregunta();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        editor.putInt("fragmentoAnterior", fragmentoAnterior);
        editor.putInt("fragmentoActual", fragmentoActual);
        editor.putInt("preguntaActual", preguntaActual);
        editor.apply();
    }

    public List<Puntuacion> getPuntuaciones() {
        List<Puntuacion> puntuaciones = new ArrayList<>();
        SQLiteDatabase db = asdbhelper.getReadableDatabase();
        String[] columnas = { AaronSwartzDb._COLUMNA_PUNTUACIONES_PUNTUACION_,
                              AaronSwartzDb._COLUMNA_PUNTUACIONES_NOMBRE_,
                              AaronSwartzDb._COLUMNA_PUNTUACIONES_FECHA_ };
        Cursor cursor = db.query(AaronSwartzDb._TABLA_PUNTUACIONES_, columnas, null, null, null, null, AaronSwartzDb._COLUMNA_PUNTUACIONES_PUNTUACION_ + " DESC");
        while ( cursor.moveToNext() ) {
            puntuaciones.add( new Puntuacion( cursor.getString(cursor.getColumnIndex(AaronSwartzDb._COLUMNA_PUNTUACIONES_FECHA_)),
                                              cursor.getInt(cursor.getColumnIndex(AaronSwartzDb._COLUMNA_PUNTUACIONES_PUNTUACION_)),
                                              cursor.getString(cursor.getColumnIndex(AaronSwartzDb._COLUMNA_PUNTUACIONES_NOMBRE_)))
                            );
        }

        return puntuaciones;
    }

    public List<PreguntaSiONo> getSimplePreguntas() {
        ArrayList<PreguntaSiONo> simplePreguntas = new ArrayList<PreguntaSiONo>();
        SQLiteDatabase db = asdbhelper.getReadableDatabase();
        String[] columnas = {AaronSwartzDb._COLUMNA_PREGUNTA_PREGUNTA_,
                AaronSwartzDb._COLUMNA_PREGUNTA_RESPUESTA_};
        Cursor cursor = db.query(AaronSwartzDb._TABLA_PREGUNTAS_, columnas, null, null, null, null, null);
        simplePreguntas.add(new PreguntaSiONo(getResources().getString(R.string.primera_pregunta), false, R.drawable.muerto));
        simplePreguntas.add(new PreguntaSiONo(getResources().getString(R.string.segunda_pregunta), true, R.drawable.drop_database_to_the_ground));
        simplePreguntas.add(new PreguntaSiONo(getResources().getString(R.string.tercera_pregunta), true, R.drawable.portaaviones_a_pique));
        while (cursor.moveToNext()) {
            boolean respuesta;
            if (cursor.getInt(cursor.getColumnIndex(AaronSwartzDb._COLUMNA_PREGUNTA_RESPUESTA_)) > 0) {
                respuesta = true;
            } else {
                respuesta = false;
            }
            simplePreguntas.add(new PreguntaSiONo(cursor.getString(cursor.getColumnIndex(AaronSwartzDb._COLUMNA_PREGUNTA_PREGUNTA_)), respuesta));
        }
        return simplePreguntas;
    }

    public void reiniciarPartida() {
        preguntaActual = 0;
        for ( Pregunta pregunta : preguntas ) {
            pregunta.desResponder();
        }
        puntuacion = 0;
        if ( fragmentoActual == 0 ) {
            main.ponerPregunta(getPreguntaActual().getPregunta());
        } else {
            multi.ponerPregunta(preguntas.get(preguntaActual));
        }
    }

    public void guardarPuntuacion(String nombre) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        Calendar cal = Calendar.getInstance();
        SQLiteDatabase db = asdbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(AaronSwartzDb._COLUMNA_PUNTUACIONES_FECHA_, df.format(cal.getTime()));
        cv.put(AaronSwartzDb._COLUMNA_PUNTUACIONES_NOMBRE_, nombre);
        cv.put(AaronSwartzDb._COLUMNA_PUNTUACIONES_PUNTUACION_, puntuacion);
        db.insert(AaronSwartzDb._TABLA_PUNTUACIONES_, null, cv);
    }


}
