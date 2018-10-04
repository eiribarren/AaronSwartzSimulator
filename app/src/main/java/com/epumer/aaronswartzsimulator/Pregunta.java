package com.epumer.aaronswartzsimulator;

public class Pregunta {
    protected String text;
    protected boolean respuesta;

    public Pregunta(String text, boolean respuesta) {
        this.text = text;
        this.respuesta = respuesta;
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

}
