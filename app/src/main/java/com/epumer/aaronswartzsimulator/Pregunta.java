package com.epumer.aaronswartzsimulator;

import android.graphics.drawable.Drawable;

public class Pregunta {
    protected int idPregunta;
    protected boolean respuesta;
    protected int pista;

    public Pregunta(int idPregunta, boolean respuesta, int pista) {
        this.idPregunta = idPregunta;
        this.respuesta = respuesta;
        this.pista = pista;
    }

    public boolean comprobarRespuesta(boolean respuesta) {
        if ( this.respuesta == respuesta ) {
            return true;
        } else {
            return false;
        }
    }

    public int getId() {
        return idPregunta;
    }

    public int getPista() {
        return pista;
    }
}
