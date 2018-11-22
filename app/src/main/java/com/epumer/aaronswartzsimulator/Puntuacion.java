package com.epumer.aaronswartzsimulator;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Puntuacion implements Serializable, Comparable<Puntuacion>{

    private String nombre;
    private String fecha;
    private int puntuacion;

    public Puntuacion(String fecha, int puntuacion, String nombre) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.puntuacion = puntuacion;
    }

    public String getFecha() {
        return fecha;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public String getPuntuacionAsString() {
        return String.valueOf(puntuacion);
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public int compareTo(@NonNull Puntuacion p) {
        return -Integer.compare(this.puntuacion, p.getPuntuacion());
    }
}
