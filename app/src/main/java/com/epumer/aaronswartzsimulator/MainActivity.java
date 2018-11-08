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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements MainFragment.MainFragmentListener,
               PistaFragment.PistaFragmentListener,
               MultiRespuestaFragment.MultiRespuestaListener{

    public static final String MY_PREFS = "AaronPreferences";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    protected PreguntaSiONo[] preguntasSiONo = { new PreguntaSiONo(R.string.primera_pregunta, false, R.drawable.drop_database_to_the_ground),
            new PreguntaSiONo(R.string.segunda_pregunta, true, R.drawable.portaaviones_a_pique),
            new PreguntaSiONo(R.string.tercera_pregunta, true, R.drawable.muerto)};

    int[] respuestasPrimeraPregunta = {R.string.primera_multipregunta_primera_respuesta, R.string.primera_multipregunta_segunda_respuesta, R.string.primera_multipregunta_tercera_respuesta, R.string.primera_multipregunta_cuarta_respuesta};
    int[] respuestasSegundaPregunta = {R.string.segunda_multipregunta_primera_respuesta, R.string.segunda_multipregunta_segunda_respuesta, R.string.segunda_multipregunta_tercera_respuesta, R.string.segunda_multipregunta_cuarta_respuesta};
    int[] respuestasTerceraPregunta = {R.string.tercera_multipregunta_primera_respuesta, R.string.tercera_multipregunta_segunda_respuesta, R.string.tercera_multipregunta_tercera_respuesta, R.string.tercera_multipregunta_cuarta_respuesta};
    protected List<PreguntaMultiRespuesta> preguntasmulti;

    protected int preguntaActual;
    protected MainFragment main;
    protected PistaFragment pista;
    protected MultiRespuestaFragment multi;
    protected int fragmentoActual;
    protected AlertDialog ad_final;
    protected AlertDialog ad_principio;
    protected Toolbar mainToolbar;
    protected Pregunta[] preguntas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        editor = prefs.edit();
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
                    editor.putInt("preguntaActual", preguntaActual);

                } else if ( fragmentoActual == 2 ) {
                    multi.ponerPregunta(preguntas[preguntaActual]);
                    editor.putInt("preguntaActual", preguntaActual);
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
        fragmentoActual = prefs.getInt("fragmentoActual", 0 );
        preguntaActual = prefs.getInt("preguntaActual", 0 );
        if ( prefs.getInt("fragmentoAnterior", 0) == 0 ) {
            preguntas = preguntasSiONo;
        } else {
            preguntas = (PreguntaMultiRespuesta[])preguntasmulti.toArray();
        }
        if ( fragmentoActual == 0 ) {
            abrirPregunta();
        } else if ( fragmentoActual == 1 ) {
            abrirPista();
        } else {
            abrirMultiPregunta();
        }
    }

    public void abrirPista() {
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.mainFragment, pista)
                                   .commit();
        if ( fragmentoActual != 1 ) {
            editor.putInt("fragmentoAnterior", fragmentoActual);
        }
        fragmentoActual = 1;
        editor.putInt("fragmentoActual", fragmentoActual);
        editor.apply();
    }

    public void abrirPregunta() {
        preguntas = preguntasSiONo;
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.mainFragment, main)
                                   .commit();
        if ( fragmentoActual != 0 ) {
            editor.putInt("fragmentoAnterior", fragmentoActual);
        }
        fragmentoActual = 0;
        editor.putInt("fragmentoActual", fragmentoActual);
        editor.apply();
    }

    public void abrirMultiPregunta() {
        preguntas = (PreguntaMultiRespuesta[])preguntasmulti.toArray();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, multi)
                .commit();
        if ( fragmentoActual != 2 ) {
            editor.putInt("fragmentoAnterior", fragmentoActual);
        }
        fragmentoActual = 2;
        editor.putInt("fragmentoActual", fragmentoActual);
        editor.apply();
    }

    public void nextButtonListener() {
        if ( preguntaActual < preguntas.length - 1 ) {
            preguntaActual++;
            editor.putInt("preguntaActual", preguntaActual);
            editor.apply();
        } else {
            ad_final.show();
        }
    }

    public void respuestaButtonListener(Object respuesta) {
        if ( preguntas[preguntaActual].comprobarRespuesta(respuesta) ) {
            Toast.makeText(this, "Respuesta correcta!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Respuesta incorrecta!", Toast.LENGTH_SHORT).show();
        }
    }

    public void backButtonListener() {
        if ( preguntaActual > 0 ) {
            preguntaActual--;
            editor.putInt("preguntaActual", preguntaActual);
            editor.apply();
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
        }
        return true;
    }

    public void abrirFragmentoAnterior() {
        int fragmentoAnterior = prefs.getInt("fragmentoAnterior", 0);
        if ( fragmentoAnterior == 0 ) {
            abrirPregunta();
        } else if ( fragmentoAnterior == 1 ){
            abrirPista();
        } else {
            abrirMultiPregunta();
        }
    }
}
