package com.example.hyousiki;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

//メイン→　　ビンゴのサイズを選択するクラス
public class SizeSelection extends AppCompatActivity {

    Globals globals;
    ImageButton level3, level4;
    private SoundPool soundPool;
    private int mSoubdId;

    Button backMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size_selection);


        //ここからボタンタップ
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                // USAGE_MEDIA
                // USAGE_GAME
                .setUsage(AudioAttributes.USAGE_GAME)
                // CONTENT_TYPE_MUSIC
                // CONTENT_TYPE_SPEECH, etc.
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                // ストリーム数に応じて
                .setMaxStreams(3)
                .build();

        // ロードしておく
        mSoubdId = soundPool.load(this, R.raw.select01, 1);


        // load が終わったか確認する場合
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            Log.d("debug", "sampleId=" + sampleId);
            Log.d("debug", "status=" + status);
        });

        level3 = findViewById(R.id.imageButton);
        level4 = findViewById(R.id.imageButton2);
        backMain = findViewById(R.id.imageButton3);

        //グローバルの値を持ってくる
        globals = (Globals) this.getApplication();

        //３×３のビンゴへ
        level3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // play(ロードしたID, 左音量, 右音量, 優先度, ループ,再生速度)
                        soundPool.play(mSoubdId, 1.0f, 1.0f, 0, 0, 1);
                        globals.g_nSizeBingo = globals.LINE_3;//ビンゴ配列数指定
                        givIntentReset();//リセットする
                        Intent intent = new Intent(SizeSelection.this, Bingo3.class);
                        startActivity(intent);


                    }
                }
        );

        //４×４のビンゴへ
        level4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // play(ロードしたID, 左音量, 右音量, 優先度, ループ,再生速度)
                        soundPool.play(mSoubdId, 1.0f, 1.0f, 0, 0, 1);
                        globals.g_nSizeBingo = globals.LINE_4;//ビンゴ配列数指定
                        givIntentReset();//リセットする
                        Intent intent = new Intent(SizeSelection.this, Bingo4.class);
                        startActivity(intent);


                    }
                }
        );

        //メインに戻る
        backMain.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // play(ロードしたID, 左音量, 右音量, 優先度, ループ,再生速度)
                        soundPool.play(mSoubdId, 1.0f, 1.0f, 0, 0, 1);

                        Intent intent = new Intent(SizeSelection.this, MainActivity.class);
                        startActivity(intent);


                    }
                }
        );



    }

    //リセット処理
    public void givIntentReset() {
        globals.g_nIntentCount = 0;//インテント数をリセット
        globals.lva = globals.g_nSizeBingo * globals.g_nSizeBingo;//〇×〇のビンゴカード

        globals.bingo = new int[globals.lva];//ビンゴ用配列
        globals.status = new int[globals.lva];//判定用配列

        //status配列を初期化
        for (int i = 0; i < globals.lva; i++) {
            globals.status[i] = 0;
        }
        //現在位置初期化
        globals.g_nCurrent = 99;
        //スコアをリセット
        globals.g_nScoreSum = 0;
        //ビンゴ回数をリセット
        globals.g_nClearBingo = 0;
        //過去のビンゴ数を記録
        globals.g_nBingoLog = 0;


    }
}