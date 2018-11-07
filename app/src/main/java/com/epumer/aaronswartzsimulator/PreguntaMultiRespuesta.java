package com.epumer.aaronswartzsimulator;


public class PreguntaMultiRespuesta extends Pregunta {

    protected int[] respuestas;

    public PreguntaMultiRespuesta(int idPregunta, String respuestaCorrecta, int pista, int[] respuestas) {
        super(idPregunta, respuestaCorrecta, pista);
        this.respuestas = respuestas;
    }

    public int[] getRespuestas() {
        return respuestas;
    }
}
