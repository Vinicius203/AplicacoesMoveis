package com.example.geoquiz_v4_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RespostasDB {

    private Context mContext;
    private static Context mStaticContext;
    private SQLiteDatabase mDatabase;

    public RespostasDB(Context contexto) {
        mContext = contexto.getApplicationContext();
        mStaticContext = mContext;
        mDatabase = new RespostasDBHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getValoresConteudo(Respostas resposta) {
        ContentValues valores = new ContentValues();

        // pares chave-valor: nomes das colunas - valores
        valores.put(RespostasDbSchema.RespostasTbl.Cols.UUID_QUESTAO, resposta.getUuidQuestao().toString());
        valores.put(RespostasDbSchema.RespostasTbl.Cols.RESPOSTA_CORRETA, resposta.isRespostaCorreta());
        valores.put(RespostasDbSchema.RespostasTbl.Cols.RESPOSTA_OFERECIDA, resposta.getRespostaOferecida());
        valores.put(RespostasDbSchema.RespostasTbl.Cols.COLOU, resposta.isColou());

        return valores;
    }


    public void addResposta(Respostas resposta) {
        ContentValues valores = getValoresConteudo(resposta);
        mDatabase.insert(RespostasDbSchema.RespostasTbl.NOME, null, valores);
    }

    public Cursor queryResposta(String clausulaWhere, String[] argsWhere) {
        Cursor cursor = mDatabase.query(RespostasDbSchema.RespostasTbl.NOME,
                null,  // todas as colunas
                clausulaWhere,
                argsWhere,
                null, // sem group by
                null, // sem having
                null  // sem order by
        );
        return cursor;
    }

    public void removeBanco() {
        int delete;
        delete = mDatabase.delete(
                RespostasDbSchema.RespostasTbl.NOME,
                null, null);
    }
}
