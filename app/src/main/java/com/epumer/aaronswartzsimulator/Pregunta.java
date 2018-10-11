package com.epumer.aaronswartzsimulator;

import android.graphics.drawable.Drawable;

public class Pregunta {
    protected String text;
    protected boolean respuesta;
    protected int pista;

    public Pregunta(String text, boolean respuesta, int pista) {
        this.text = text;
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

    public String getText() {
        return text;
    }

    public int getPista() {
        return pista;
    }
}
