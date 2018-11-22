package com.epumer.aaronswartzsimulator;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements MainFragment.MainFragmentListener,
               PistaFragment.PistaFragmentListener,
               MultiRespuestaFragment.MultiRespuestaListener,
               HistorialFragment.HistorialFragmentListener {

    public static final String MY_PREFS = "AaronPreferences";
    protected PreguntaSiONo[] preguntasSiONo = { new PreguntaSiONo(R.string.primera_pregunta, false, R.drawable.drop_database_to_the_ground),
            new PreguntaSiONo(R.string.segunda_pregunta, true, R.drawable.portaaviones_a_pique),
            new PreguntaSiONo(R.string.tercera_pregunta, true, R.drawable.muerto)};

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
    protected Pregunta[] preguntas;
    protected List<Puntuacion> puntuaciones = new ArrayList<Puntuacion>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        preguntasmulti = Arrays.asList(new PreguntaMultiRespuesta(R.string.primera_multipregunta, getResources().getString(R.string.primera_multipregunta_primera_respuesta), R.drawable.muerto, respuestasPrimeraPregunta),
                new PreguntaMultiRespuesta(R.string.segunda_multipregunta, getResources().getString(R.string.segunda_multipregunta_segunda_respuesta), R.drawable.muerto, respuestasSegundaPregunta),
                new PreguntaMultiRespuesta(R.string.tercera_multipregunta, getResources().getString(R.string.tercera_multipregunta_primera_respuesta), R.drawable.muerto, respuestasTerceraPregunta));

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
                    main.ponerPregunta(preguntas[preguntaActual].getId());

                } else if ( fragmentoActual == 2 ) {
                    multi.ponerPregunta(preguntas[preguntaActual]);
                }
            }
        });
        ad_final = bob.create();
        bob.setMessage(R.string.preguntar_si_continuar_principio);
        bob.setPositiveButton(R.string.afirmativo, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                preguntaActual = preguntas.length - 1;
                if ( fragmentoActual == 0 ) {
                    main.ponerPregunta(preguntas[preguntaActual].getId());
                } else if ( fragmentoActual == 2 ) {
                    multi.ponerPregunta(preguntas[preguntaActual]);
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
            preguntas = preguntasSiONo;
        } else {
            preguntas = (PreguntaMultiRespuesta[])preguntasmulti.toArray();
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
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.mainFragment, pista)
                                   .commit();
        if ( fragmentoActual != 1 ) {
            fragmentoAnterior = fragmentoActual;
        }
        fragmentoActual = 1;
    }

    public void abrirPregunta() {
        preguntas = preguntasSiONo;
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
        preguntas = (PreguntaMultiRespuesta[])preguntasmulti.toArray();
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
        if ( preguntaActual < preguntas.length - 1 ) {
            preguntaActual++;
        } else {
            ad_final.show();
        }
    }

    public void respuestaButtonListener(Object respuesta) {
        if ( !preguntas[preguntaActual].estaRespondida()) {
            if (preguntas[preguntaActual].comprobarRespuesta(respuesta)) {
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
        return preguntas[preguntaActual];
    }

    public int getPosicionActual() {
        return preguntaActual;
    }

    public void setPreguntaActual(int preguntaActual) {
        this.preguntaActual = preguntaActual;
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
        return puntuaciones;
    }

    public void reiniciarPartida() {
        preguntaActual = 0;
        for ( Pregunta pregunta : preguntas ) {
            pregunta.desResponder();
        }
        puntuacion = 0;
        if ( fragmentoActual == 0 ) {
            main.ponerPregunta(getPreguntaActual().getId());
        } else {
            multi.ponerPregunta(preguntas[preguntaActual]);
        }
    }

    public void guardarPuntuacion(String nombre) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        Calendar cal = Calendar.getInstance();
        Puntuacion puntuacionActual = new Puntuacion(df.format(cal.getTime()), puntuacion, nombre);
        puntuaciones.add(puntuacionActual);
    }
}
