package com.epumer.aaronswartzsimulator;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    implements MainFragment.MainFragmentListener,
               PistaFragment.PistaFragmentListener{


    protected Pregunta[] preguntas = { new Pregunta(R.string.primera_pregunta, false, R.drawable.drop_database_to_the_ground),
            new Pregunta(R.string.segunda_pregunta, true, R.drawable.portaaviones_a_pique),
            new Pregunta(R.string.tercera_pregunta, true, R.drawable.muerto)};
    protected int preguntaActual;
    protected MainFragment main;
    protected PistaFragment pista;
    protected int fragmentoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = new MainFragment();
        pista = new PistaFragment();
        try {
            fragmentoActual = savedInstanceState.getInt("fragmentoActual");
        } catch ( Exception e ) {
            fragmentoActual = 0;
        }
        try {
            preguntaActual = savedInstanceState.getInt("preguntaActual");
        } catch ( Exception e ) {
            preguntaActual = 0;
        }
        if ( fragmentoActual == 0 ) {
            abrirPregunta();
        } else {
            abrirPista();
        }
    }

    public void abrirPista() {
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.mainFragment, pista)
                                   .commit();
        fragmentoActual = 1;
    }

    public void abrirPregunta() {
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.mainFragment, main)
                                   .commit();
        fragmentoActual = 0;
    }

    public void nextButtonListener() {
        if ( preguntaActual < preguntas.length - 1 ) {
            preguntaActual++;
        } else {
            preguntaActual = 0;
        }
    }

    public void respuestaButtonListener(boolean respuesta) {
        if ( preguntas[preguntaActual].comprobarRespuesta(respuesta) ) {
            Toast.makeText(this, "Respuesta correcta!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Respuesta incorrecta!", Toast.LENGTH_SHORT).show();
        }
    }

    public void backButtonListener() {
        if ( preguntaActual > 0 ) {
            preguntaActual--;
        } else {
            preguntaActual = preguntas.length - 1;
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
        outState.putInt("fragmentoActual", fragmentoActual);
        outState.putInt("preguntaActual", preguntaActual);
    }
}
