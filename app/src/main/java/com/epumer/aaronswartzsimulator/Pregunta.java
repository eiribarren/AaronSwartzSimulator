package com.epumer.aaronswartzsimulator;

import android.graphics.drawable.Drawable;

public class Pregunta {
    protected int idPregunta;
    protected Object respuesta;
    protected int pista;
    protected boolean respondida;

    public Pregunta(int idPregunta, Object respuesta, int pista) {
        this.idPregunta = idPregunta;
        this.respuesta = respuesta;
        this.pista = pista;
    }

    public boolean comprobarRespuesta(Object respuesta) {
        this.respondida = true;
        if ( respuesta instanceof String ) {
            if ( ((String) respuesta).equals(this.respuesta)) {
                return true;
            }
        } else {
            if (this.respuesta == respuesta) {
                return true;
            }
        }
        return false;
    }

    public boolean estaRespondida() {
        return this.respondida;
    }

    public void desResponder() {
        this.respondida = false;
    }

    public int getId() {
        return idPregunta;
    }

    public int getPista() {
        return pista;
    }
}
