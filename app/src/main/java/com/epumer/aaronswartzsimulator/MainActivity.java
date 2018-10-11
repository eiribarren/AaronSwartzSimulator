package com.epumer.aaronswartzsimulator;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public final static String CLAU_EXTRA_PISTA = "com.epumer.aaronswartzsimulator.pistaPreguntaActual";
    public final static String CLAU_EXTRA_PREGUNTA = "com.epumer.aaronswartzsimulator.preguntaActual";
    protected Button trueButton, falseButton, nextButton, backButton, pistaButton;
    protected Pregunta[] preguntas = { new Pregunta("¿Está Aaron Swartz vivo?", false, R.drawable.drop_database_to_the_ground),
                                       new Pregunta("¿Consiguió Aaron Swartz robar en la biblioteca?", true, R.drawable.portaaviones_a_pique),
                                       new Pregunta("¿Es Aaron Swartz el putisimo?", true, R.drawable.muerto)};
    protected int preguntaActual;
    protected String preguntaAux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        preguntaAux = intent.getStringExtra(CLAU_EXTRA_PREGUNTA);

        trueButton = (Button)findViewById(R.id.trueButton);
        falseButton = (Button)findViewById(R.id.falseButton);
        nextButton = (Button)findViewById(R.id.nextButton);
        backButton = (Button)findViewById(R.id.backButton);
        pistaButton = (Button)findViewById(R.id.pistaButton);

        try {
            preguntaActual = savedInstanceState.getInt("preguntaActual");
        } catch (Exception e) {
            if ( !("").equals(preguntaAux) ) {
                for ( int i = 0 ; i < preguntas.length ; i++ ) {
                    if ( preguntas[i].getText().equals(preguntaAux) ) {
                        preguntaActual = i;
                    }
                }
            } else {
                preguntaActual = 0;
            }
        }
        ponerPregunta(preguntas[preguntaActual]);

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

        pistaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirPista(preguntas[preguntaActual]);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("preguntaActual", preguntaActual);
    }

    public void nextButtonListener() {
        if ( preguntaActual < preguntas.length - 1 ) {
            preguntaActual++;
        } else {
            preguntaActual = 0;
        }
        ponerPregunta(preguntas[preguntaActual]);
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
        ponerPregunta(preguntas[preguntaActual]);
    }

    public void ponerPregunta(Pregunta pregunta) {
        TextView texto = (TextView)findViewById(R.id.pregunta_text);
        texto.setText(pregunta.getText());
    }

    public void abrirPista(Pregunta pregunta) {
        Intent pistaIntent = new Intent(this, PistaActivity2.class);
        pistaIntent.putExtra(CLAU_EXTRA_PISTA,pregunta.getPista());
        pistaIntent.putExtra(CLAU_EXTRA_PREGUNTA,pregunta.getText());

        startActivity(pistaIntent);
    }
}
