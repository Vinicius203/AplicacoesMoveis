package com.example.geoquiz_v4_sqlite;

import java.util.UUID;

public class Respostas {
    private UUID mId;
    private UUID mUuidQuestao; //
    private boolean mRespostaCorreta;
    private boolean mRespostaOferecida;
    private boolean mColou;

    public Respostas(UUID uuidQuestao, boolean respostaCorreta, boolean respostaOferecida, boolean colou) {
        this.mUuidQuestao = uuidQuestao;
        this.mRespostaCorreta = respostaCorreta;
        this.mRespostaOferecida = respostaOferecida;
        this.mColou = colou;
        this.mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public UUID getUuidQuestao() {
        return mUuidQuestao;
    }

    public boolean isRespostaCorreta() {
        return mRespostaCorreta;
    }

    public boolean getRespostaOferecida() {
        return mRespostaOferecida;
    }

    public boolean isColou() {
        return mColou;
    }

}
