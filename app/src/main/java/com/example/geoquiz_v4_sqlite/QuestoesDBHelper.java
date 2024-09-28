package com.example.geoquiz_v4_sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuestoesDBHelper extends SQLiteOpenHelper {
    private static final int VERSAO = 2;
    private static final String NOME_DATABASE = "questoesDB";

    public QuestoesDBHelper(Context context) {
        super(context, NOME_DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + QuestoesDbSchema.QuestoesTbl.NOME + " (" +
                "_id integer PRIMARY KEY autoincrement, " +
                QuestoesDbSchema.QuestoesTbl.Cols.UUID + ", " +
                QuestoesDbSchema.QuestoesTbl.Cols.QUESTAO_CORRETA + ", " +
                QuestoesDbSchema.QuestoesTbl.Cols.TEXTO_QUESTAO + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int novaVersao) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestoesDbSchema.QuestoesTbl.NOME);
        onCreate(db);
    }
}
