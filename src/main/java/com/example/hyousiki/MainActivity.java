package com.example.hyousiki;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

//メインのクラス　　アプリの最初に表示される
public class MainActivity extends AppCompatActivity {

    private SoundPool soundPool;
    private int mSoubdId;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //BGM
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.hiyokonokakekko);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }

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

        //ビンゴへ
        findViewById(R.id.bingo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // play(ロードしたID, 左音量, 右音量, 優先度, ループ,再生速度)
                soundPool.play(mSoubdId, 1.0f, 1.0f, 0, 0, 1);
                Intent intent = new Intent(MainActivity.this, SizeSelection.class);
                stopPlayer();
                startActivity(intent);
            }
        });
        //クイズへ
        findViewById(R.id.kuizu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // play(ロードしたID, 左音量, 右音量, 優先度, ループ,再生速度)
                soundPool.play(mSoubdId, 1.0f, 1.0f, 0, 0, 1);
                Intent intent = new Intent(MainActivity.this, QuizSelection.class);
                stopPlayer();
                startActivity(intent);
            }
        });
        //ずかんへ
        findViewById(R.id.zukan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // play(ロードしたID, 左音量, 右音量, 優先度, ループ,再生速度)
                soundPool.play(mSoubdId, 1.0f, 1.0f, 0, 0, 1);
                Intent intent = new Intent(MainActivity.this, KirokuActivity.class);
                stopPlayer();
                startActivity(intent);
            }
        });
    }
        private void stopPlayer() {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;

            }
    }
}