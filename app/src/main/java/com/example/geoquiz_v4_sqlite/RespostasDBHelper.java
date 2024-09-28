package com.example.geoquiz_v4_sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RespostasDBHelper extends SQLiteOpenHelper {
    private static final int VERSAO = 1;
    private static final String NOME_DATABASE = "respostasDB";

    public RespostasDBHelper(Context context) {
        super(context, NOME_DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + RespostasDbSchema.RespostasTbl.NOME + " (" +
                "_id integer PRIMARY KEY autoincrement, " +
                RespostasDbSchema.RespostasTbl.Cols.UUID_QUESTAO + ", " +
                RespostasDbSchema.RespostasTbl.Cols.RESPOSTA_CORRETA + ", " +
                RespostasDbSchema.RespostasTbl.Cols.RESPOSTA_OFERECIDA + ", " +
                RespostasDbSchema.RespostasTbl.Cols.COLOU + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int novaVersao) {
        db.execSQL("DROP TABLE IF EXISTS " + RespostasDbSchema.RespostasTbl.NOME);
        onCreate(db);
    }
}
