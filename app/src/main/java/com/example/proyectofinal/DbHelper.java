package com.example.proyectofinal;

import android.app.ActionBar;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper { //Gestor de Base de Datos
    private String Table;

    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Table = "Create table Coords (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Long DOUBLE, " +
                "Lat DOUBLE )";
        db.execSQL(Table);
        //Integer year, Integer month, Integer day, Integer hour, Double height)
        for(int i = 1; i<=10; i++){
            Table = "Create table Buoy"+i+" (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Year INTEGER, " +
                    "Month INTEGER," +
                    "Day INTEGER," +
                    "Hour INTEGER," +
                    "Height DOUBLE )";

            db.execSQL(Table);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table Coords");

        Table = "Create table Coords (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Long DOUBLE, " +
                "Lat DOUBLE )";

        db.execSQL(Table);

        for(int i = 1; i<=10; i++){
            db.execSQL("drop table Buoy"+i+"");
            Table = "Create table Buoy"+i+" (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Year INTEGER, " +
                    "Month INTEGER," +
                    "Day INTEGER," +
                    "Hour INTEGER," +
                    "Height DOUBLE )";

            db.execSQL(Table);
        }
    }
}
