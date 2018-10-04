package com.epumer.aaronswartzsimulator;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected Button trueButton, falseButton, nextButton, backButton;
    protected ArrayList<Pregunta> preguntas;
    protected int preguntaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trueButton = (Button)findViewById(R.id.trueButton);
        falseButton = (Button)findViewById(R.id.falseButton);
        nextButton = (Button)findViewById(R.id.nextButton);
        backButton = (Button)findViewById(R.id.backButton);

        preguntas = new ArrayList();

        preguntas.add(new Pregunta("¿Está Aaron Swartz vivo?", false));
        preguntas.add(new Pregunta("¿Consiguió Aaron Swartz robar en la biblioteca?", true));
        preguntas.add(new Pregunta("¿Es Aaron Swartz el putisimo?", true));

        try {
            preguntaActual = savedInstanceState.getInt("preguntaActual");
        } catch ( Exception e ) {
            preguntaActual = 0;
        }
        ponerPregunta(preguntas.get(preguntaActual));

        trueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                respuestaButtonListener(true);
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respuestaButtonListener(false);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButtonListener();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonListener();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("preguntaActual", preguntaActual);
    }

    public void nextButtonListener() {
        if ( preguntaActual < preguntas.size() - 1 ) {
            preguntaActual++;
        } else {
            preguntaActual = 0;
        }
        ponerPregunta(preguntas.get(preguntaActual));
    }

    public void respuestaButtonListener(boolean respuesta) {
        if ( preguntas.get(preguntaActual).comprobarRespuesta(respuesta) ) {
            Toast.makeText(this, "Respuesta correcta!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Respuesta incorrecta!", Toast.LENGTH_SHORT).show();
        }
    }

    public void backButtonListener() {
        if ( preguntaActual > 0 ) {
            preguntaActual--;
        } else {
            preguntaActual = preguntas.size() - 1;
        }
        ponerPregunta(preguntas.get(preguntaActual));
    }

    public void ponerPregunta(Pregunta pregunta) {
        TextView texto = (TextView)findViewById(R.id.pregunta_text);
        texto.setText(pregunta.getText());
    }

}
