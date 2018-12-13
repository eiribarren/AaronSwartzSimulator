package com.epumer.aaronswartzsimulator;


public class PreguntaMultiRespuesta extends Pregunta {

    protected int[] respuestas;

    public PreguntaMultiRespuesta(String pregunta, String respuestaCorrecta, int pista, int[] respuestas) {
        super(pregunta, respuestaCorrecta, pista);
        this.respuestas = respuestas;
    }

    public int[] getRespuestas() {
        return respuestas;
    }
}
