package com.example.geoquiz_v4_sqlite;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
  Modelo de projeto para a Atividade 1.
  Será preciso adicionar o cadastro das respostas do usuário ao Quiz, conforme
  definido no Canvas.

  GitHub: https://github.com/udofritzke/GeoQuiz
 */

public class MainActivity extends AppCompatActivity {
    private Button mBotaoVerdadeiro;
    private Button mBotaoFalso;
    private Button mBotaoProximo;
    private Button mBotaoMostra;
    private Button mBotaoDeleta;
    private Button mBotaoCola;

    private TextView mTextViewQuestao;
    private TextView mTextViewQuestoesArmazenadas;

    private static final String TAG = "QuizActivity";
    private static final String CHAVE_INDICE = "INDICE";
    private static final int CODIGO_REQUISICAO_COLA = 0;

    private Questao[] mBancoDeQuestoes = new Questao[]{
            new Questao(R.string.questao_suez, true),
            new Questao(R.string.questao_alemanha, false)
    };

    private QuestaoDB mQuestoesDb;
    private RespostasDB mRespostasDb; //  instância do banco de respostas

    private int mIndiceAtual = 0;
    private boolean mEhColador;

    @Override
    protected void onCreate(Bundle instanciaSalva) {
        super.onCreate(instanciaSalva);
        setContentView(R.layout.activity_main);
        //Log.d(TAG, "onCreate()");
        if (instanciaSalva != null) {
            mIndiceAtual = instanciaSalva.getInt(CHAVE_INDICE, 0);
        }

        mTextViewQuestao = (TextView) findViewById(R.id.view_texto_da_questao);
        atualizaQuestao();

        mBotaoVerdadeiro = (Button) findViewById(R.id.botao_verdadeiro);
        // utilização de classe anônima interna
        mBotaoVerdadeiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaResposta(true);
                cadastrarResposta(true);
            }
        });

        mBotaoFalso = (Button) findViewById(R.id.botao_falso);
        mBotaoFalso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaResposta(false);
                cadastrarResposta(false);
            }
        });

        mBotaoProximo = (Button) findViewById(R.id.botao_proximo);
        mBotaoProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIndiceAtual = (mIndiceAtual + 1) % mBancoDeQuestoes.length;
                mEhColador = false;
                atualizaQuestao();
            }
        });

        mBotaoCola = (Button) findViewById(R.id.botao_cola);
        mBotaoCola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inicia ColaActivity
                // Intent intent = new Intent(MainActivity.this, ColaActivity.class);
                boolean respostaEVerdadeira = mBancoDeQuestoes[mIndiceAtual].isRespostaCorreta();
                Intent intent = ColaActivity.novoIntent(MainActivity.this, respostaEVerdadeira);
                //startActivity(intent);
                startActivityForResult(intent, CODIGO_REQUISICAO_COLA);
            }
        });

        mBotaoMostra = (Button) findViewById(R.id.botao_mostra_questoes);
        mBotaoMostra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarRespostas();
            }
        });

        mBotaoDeleta = (Button) findViewById(R.id.botao_deleta);
        mBotaoDeleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 /*
                  Acesso ao SQLite
                */
                if (mRespostasDb != null) {
                    mRespostasDb.removeBanco();
                    if (mTextViewQuestoesArmazenadas == null) {
                        mTextViewQuestoesArmazenadas = (TextView) findViewById(R.id.texto_questoes_a_apresentar);
                    }
                    mTextViewQuestoesArmazenadas.setText("");
                }
            }
        });

        // Inicializa o banco de respostas
        mRespostasDb = new RespostasDB(getBaseContext());
    }

    private void atualizaQuestao() {
        int questao = mBancoDeQuestoes[mIndiceAtual].getTextoRespostaId();
        mTextViewQuestao.setText(questao);
    }

    private void verificaResposta(boolean respostaPressionada) {
        boolean respostaCorreta = mBancoDeQuestoes[mIndiceAtual].isRespostaCorreta();
        int idMensagemResposta = (mEhColador) ? R.string.toast_julgamento :
                (respostaPressionada == respostaCorreta) ? R.string.toast_correto : R.string.toast_incorreto;
        Toast.makeText(this, idMensagemResposta, Toast.LENGTH_SHORT).show();
    }

    private void cadastrarResposta(boolean respostaPressionada) {
        boolean respostaCorreta = mBancoDeQuestoes[mIndiceAtual].isRespostaCorreta();

        // A lógica abaixo compara a resposta correta com a resposta pressionada pelo usuário
        // Caso as duas forem iguais (ambas true ou ambas false), então a variável respostaUsuarioCorreta recebe true (o usuário acertou a pergunta)
        // Caso as duas forem divergentes (uma true e uma false), então a variável respostaUsuarioCorreta recebe false (o usuário errou a pergunta)

        boolean respostaUsuarioCorreta = true;

        if (respostaCorreta && respostaPressionada) {
            respostaUsuarioCorreta = true;
        }

        else if (respostaCorreta && (respostaPressionada == false)) {
            respostaUsuarioCorreta = false;
        }

        else if ((respostaCorreta == false) && respostaPressionada) {
            respostaUsuarioCorreta = false;
        }

        else if ((respostaCorreta == false) && (respostaPressionada == false)) {
            respostaUsuarioCorreta = true;
        }

        Respostas resposta = new Respostas(
                mBancoDeQuestoes[mIndiceAtual].getId(),
                respostaUsuarioCorreta,
                respostaPressionada,
                mEhColador);

        mRespostasDb.addResposta(resposta);
    }

    private void mostrarRespostas() {
        if (mRespostasDb == null) return;

        if (mTextViewQuestoesArmazenadas == null) {
            mTextViewQuestoesArmazenadas = (TextView) findViewById(R.id.texto_questoes_a_apresentar);
        } else {
            mTextViewQuestoesArmazenadas.setText("");
        }

        Cursor cursor = mRespostasDb.queryResposta(null, null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                mTextViewQuestoesArmazenadas.setText("Nenhuma resposta a apresentar");
                Log.i("MSGS", "Nenhum resultado");
                return;
            }

            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String uuid = cursor.getString(cursor.getColumnIndex(RespostasDbSchema.RespostasTbl.Cols.UUID_QUESTAO));
                    boolean respostaCorreta = cursor.getInt(cursor.getColumnIndex(RespostasDbSchema.RespostasTbl.Cols.RESPOSTA_CORRETA)) > 0;
                    boolean respostaOferecida = cursor.getInt(cursor.getColumnIndex(RespostasDbSchema.RespostasTbl.Cols.RESPOSTA_OFERECIDA)) > 0;
                    boolean colou = cursor.getInt(cursor.getColumnIndex(RespostasDbSchema.RespostasTbl.Cols.COLOU)) > 0;

                    String texto = String.format("ID da Questão: %s\n Correta: %s\n Resposta Oferecida: %s\n Colou: %s",
                            uuid,
                            respostaCorreta ? "Sim" : "Não",
                            respostaOferecida ? "Verdadeiro" : "Falso",
                            colou ? "Sim" : "Não");
                    mTextViewQuestoesArmazenadas.append(texto + "\n\n");
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
        } else {
            Log.i("MSGS", "cursor nulo!");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle instanciaSalva) {
        super.onSaveInstanceState(instanciaSalva);
        Log.i(TAG, "onSaveInstanceState()");
        instanciaSalva.putInt(CHAVE_INDICE, mIndiceAtual);
    }

    @Override
    protected void onActivityResult(int codigoRequisicao, int codigoResultado, Intent dados) {
        if (codigoResultado != Activity.RESULT_OK) {
            return;
        }
        if (codigoRequisicao == CODIGO_REQUISICAO_COLA) {
            if (dados == null) {
                return;
            }
            mEhColador = ColaActivity.foiMostradaResposta(dados);
        }
    }
}
