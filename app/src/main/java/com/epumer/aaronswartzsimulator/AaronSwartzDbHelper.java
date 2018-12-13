package com.epumer.aaronswartzsimulator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AaronSwartzDbHelper extends SQLiteOpenHelper {

    public AaronSwartzDbHelper(Context context, String nomDb, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nomDb, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + AaronSwartzDb._TABLA_PUNTUACIONES_ +
                   "(" + AaronSwartzDb._COLUMNA_PUNTUACIONES_FECHA_ + " TEXT, " +
                   AaronSwartzDb._COLUMNA_PUNTUACIONES_NOMBRE_ + " TEXT, " +
                   AaronSwartzDb._COLUMNA_PUNTUACIONES_PUNTUACION_ + " INTEGER )");
        db.execSQL("CREATE TABLE " + AaronSwartzDb._TABLA_PREGUNTAS_ +
                "(" + AaronSwartzDb._COLUMNA_PREGUNTA_PREGUNTA_ + " TEXT, " +
                AaronSwartzDb._COLUMNA_PREGUNTA_RESPUESTA_ + " INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + AaronSwartzDb._TABLA_PUNTUACIONES_ );
        db.execSQL("DROP TABLE " + AaronSwartzDb._TABLA_PREGUNTAS_ );
        onCreate(db);
    }
}
