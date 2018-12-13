package com.epumer.aaronswartzsimulator;

import android.graphics.drawable.Drawable;

public class Pregunta {
    protected String pregunta;
    protected Object respuesta;
    protected int pista;
    protected boolean respondida;
    protected boolean tienePista = false;

    public Pregunta(String pregunta, Object respuesta) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    public Pregunta(String pregunta, Object respuesta, int pista) {
        this(pregunta, respuesta);
        this.pista = pista;
        this.tienePista = true;
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

    public String getPregunta() {
        return pregunta;
    }

    public int getPista() {
        return pista;
    }

    public boolean tienePista() { return tienePista; };
}
