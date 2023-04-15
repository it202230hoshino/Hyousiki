package com.example.hyousiki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BingoResult extends AppCompatActivity {

    Globals globals;
    TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingo_result);


        //グローバルの値を持ってくる
        globals = (Globals) this.getApplication();
        score = (TextView)findViewById(R.id.score);


        //計算結果を表示
        score.setText(Integer.toString(globals.g_nScoreFinal));
        findViewById(R.id.return2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BingoResult.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //クイズへ
        findViewById(R.id.fukushu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BingoResult.this, QuizActivity.class);
                startActivity(intent);
            }
        });

    }}