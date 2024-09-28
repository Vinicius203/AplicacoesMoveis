package com.example.geoquiz_v4_sqlite;

public class RespostasDbSchema {
    public static final class RespostasTbl {
        public static final String NOME = "Respostas";
        public static final class Cols {
            public static final String UUID_QUESTAO = "uuid_questao";
            public static final String RESPOSTA_CORRETA = "resposta_correta";
            public static final String RESPOSTA_OFERECIDA = "resposta_oferecida";
            public static final String COLOU = "colou";
        }
    }
}
