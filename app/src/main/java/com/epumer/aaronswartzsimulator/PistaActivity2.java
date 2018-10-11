package com.epumer.aaronswartzsimulator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PistaActivity2 extends AppCompatActivity {

    public final static String CLAU_EXTRA_PISTA = "com.epumer.aaronswartzsimulator.pistaPreguntaActual";
    public final static String CLAU_EXTRA_PREGUNTA = "com.epumer.aaronswartzsimulator.preguntaActual";
    protected String preguntaActual;
    protected int pistaPreguntaActual;
    protected Button atrasButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_pista2);

        preguntaActual = intent.getStringExtra(CLAU_EXTRA_PREGUNTA);
        pistaPreguntaActual = intent.getIntExtra(CLAU_EXTRA_PISTA, 0);

        ImageView image = (ImageView)findViewById(R.id.pistaImage);
        image.setImageDrawable(getDrawable(pistaPreguntaActual));

        atrasButton = (Button)findViewById(R.id.atrasButton);
        atrasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atrasListener(preguntaActual);
            }
        });

    }

    public void atrasListener(String preguntaActual) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra(CLAU_EXTRA_PREGUNTA, preguntaActual);
        startActivity(mainIntent);
    }
}
