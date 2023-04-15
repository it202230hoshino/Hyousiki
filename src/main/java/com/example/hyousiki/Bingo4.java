package com.example.hyousiki;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Bingo4 extends AppCompatActivity {

    Globals globals;

    Button uiReturn,finish;
    ImageButton uiReset;
    ImageButton ib1, ib2, ib3, ib4, ib5, ib6, ib7, ib8, ib9,ib10,ib11,ib12,ib13,ib14,ib15,ib16;
    TextView score;
    MediaPlayer mediaPlayer;

    ArrayList<Integer> list = new ArrayList<Integer>();  //シャッフル用リスト

    BingoJudge judge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingo4);

        //        BGM
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.kiminiageru);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }

        //グローバルの値を持ってくる
        globals = (Globals) this.getApplication();
        judge = new BingoJudge(globals);

        find();//xmlの結びつけ※一番前に書き込む

        setView();//画像をイメージボタンに表示

        //一番最初のビンゴ画面表示時だけ入る
        //ImageJudgeCameraからの戻ったときにシャッフルしないように
        if (globals.g_nIntentCount == 0) {
            randomArray();//ランダムな数値列を生成
        }

        //選択したビンゴ配列数分だけループ
        for (int i = 0; i < globals.lva; i++) {

            //一つでもビンゴが開いていたらシャッフルボタンを無効にする
            if (globals.status[i] == 1) {
                uiReset.setEnabled(false);//シャフルボタンを無効にする
                uiReset.setAlpha(0.25f);//画像を薄くする

                ImageButton[] imageButton = {ib1, ib2, ib3, ib4, ib5, ib6, ib7, ib8, ib9,ib10,ib11,ib12,ib13,ib14,ib15,ib16};//ボタン用配列

                imageButton[i].setImageResource(R.drawable.clear);//クリア画面を表示
                imageButton[i].setBackgroundResource(R.color.gre); // 背景色をcolorAccentに変更
            }
        }

        //ホームへ戻る
        uiReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ダイアログを呼ぶ
                BingoDialogFragment2 dialog = new BingoDialogFragment2();

                dialog.show(getSupportFragmentManager(), "Down");
            }
        });

        //終了ボタン
        finish.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BingoDialogFragment4 dialog = new BingoDialogFragment4();

                        dialog.show(getSupportFragmentManager(), "Down");
                    }
                }
        );


        //ボタン１
        ib1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON01;
                        //インテント
                        goIntent();


                    }
                }
        );

        //ボタン２
        ib2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON02;
                        //インテント
                        goIntent();
                    }
                }
        );

        //ボタン３
        ib3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON03;
                        //インテント
                        goIntent();
                    }
                }
        );

        //ボタン4
        ib4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON04;
                        //インテント
                        goIntent();
                    }
                }
        );

        //ボタン5
        ib5.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON05;
                        //インテント
                        goIntent();
                    }
                }
        );

        //ボタン6
        ib6.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON06;
                        //インテント
                        goIntent();
                    }
                }
        );

        //ボタン7
        ib7.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON07;
                        //インテント
                        goIntent();
                    }
                }
        );

        //ボタン8
        ib8.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON08;
                        //インテント
                        goIntent();
                    }
                }
        );

        //ボタン9
        ib9.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON09;
                        //インテント
                        goIntent();
                    }
                }
        );

        //ボタン10
        ib10.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON10;
                        //インテント
                        goIntent();
                    }
                }
        );

        //ボタン11
        ib11.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON11;
                        //インテント
                        goIntent();
                    }
                }
        );
        //ボタン12
        ib12.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON12;
                        //インテント
                        goIntent();
                    }
                }
        );
        //ボタン13
        ib13.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON13;
                        //インテント
                        goIntent();
                    }
                }
        );
        //ボタン14
        ib14.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON14;
                        //インテント
                        goIntent();
                    }
                }
        );
        //ボタン15
        ib15.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON15;
                        //インテント
                        goIntent();
                    }
                }
        );
        //ボタン16
        ib16.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //インテント時に自分のボタンの場所を示す
                        globals.g_nCurrent = globals.BUTTON16;
                        //インテント
                        goIntent();
                    }
                }
        );

        //リセットボタン
        uiReset.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // シャッフルして、順番を変える
                        randomArray();

                        //画像をイメージボタンにランダム表示
                        setView();
                    }
                }
        );

        //ビンゴ判定/////////////////////////////////
        globals.g_nBingoCount = 0;
        int nDiffBingoCount = judge.check_Bingo();

        //過去のビンゴ数が今のビンゴ数より大きかったらビンゴ画面の表示

        //　ビンゴ数の差異数で分岐
        if(nDiffBingoCount == -1)
        {
            // エラー
        }
        else if (nDiffBingoCount == 0)
        {
            //追加ビンゴ無し
        }
        else {
            globals.g_nBingoLog = globals.g_nBingoLog + nDiffBingoCount;

            if (nDiffBingoCount == 1)
            {
                // ビンゴ
                dialogImage();          // ビンゴ表示
            }
            else if (nDiffBingoCount == 2)
            {
                // ダブルビンゴ表示
                dialogImage2();
            }
            else if (nDiffBingoCount == 3)
            {
                // トリプルビンゴ表示
                dialogImage3();
            }
            else if (nDiffBingoCount == 4)
            {
                // フォースビンゴ表示
                dialogImage4();
            }

            if ( globals.g_nBingoLog== globals.g_nSizeBingo * 2 + 2)
            {
                // ALLビンゴ
                dialogImageAll();
            }
        }


        //標識レベルで計算
        globals.g_nScoreSum = 0;//スコアを初期化
        globals.g_nScoreFinal = 0;//スコアを初期化
        for (int i = 0; i < globals.lva; i++) {

            //もし開いていたら
            if (globals.status[i] == 1) {
                //開いたマス分のスコアの合計を出す
                globals.g_nScoreSum = globals.g_nScoreSum + globals.g_nScore[i];

            }
        }


        //ビンゴしている回数によって点数を加算計算
        globals.g_nScoreFinal = globals.g_nScoreSum * (globals.g_nBingoLog + 1);

        //計算結果を表示
        score.setText(Integer.toString(globals.g_nScoreFinal));
    }


    /////////////////////////////////////////////////////////////////////////////////////////////
  //ビンゴ１
    public void dialogImage() {
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.bingo2);
        iv.setAdjustViewBounds(true);
        new AlertDialog.Builder(this)
                .setView(iv)
                .show();
    }

    //ビンゴ2
    public void dialogImage2() {
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.doublebingo);
        iv.setAdjustViewBounds(true);
        new AlertDialog.Builder(this)
                .setView(iv)
                .show();
    }

    //ビンゴ3
    public void dialogImage3() {
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.bingo2);
        iv.setAdjustViewBounds(true);
        new AlertDialog.Builder(this)
                .setView(iv)
                .show();
    }
    //ビンゴ4
    public void dialogImage4() {
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.bingo2);
        iv.setAdjustViewBounds(true);
        new AlertDialog.Builder(this)
                .setView(iv)
                .show();
    }
    //オールビンゴ
    public void dialogImageAll() {
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.allbingo);
        iv.setAdjustViewBounds(true);
        new AlertDialog.Builder(this)
                .setView(iv)
                .show();
    }

    //レイアウト
    public void find() {
        ib1 = (ImageButton) findViewById(R.id.b1);
        ib2 = (ImageButton) findViewById(R.id.b2);
        ib3 = (ImageButton) findViewById(R.id.b3);
        ib4 = (ImageButton) findViewById(R.id.b4);
        ib5 = (ImageButton) findViewById(R.id.b5);
        ib6 = (ImageButton) findViewById(R.id.b6);
        ib7 = (ImageButton) findViewById(R.id.b7);
        ib8 = (ImageButton) findViewById(R.id.b8);
        ib9 = (ImageButton) findViewById(R.id.b9);
        ib10 = (ImageButton) findViewById(R.id.b10);
        ib11 = (ImageButton) findViewById(R.id.b11);
        ib12 = (ImageButton) findViewById(R.id.b12);
        ib13 = (ImageButton) findViewById(R.id.b13);
        ib14 = (ImageButton) findViewById(R.id.b14);
        ib15 = (ImageButton) findViewById(R.id.b15);
        ib16 = (ImageButton) findViewById(R.id.b16);



        uiReset = (ImageButton) findViewById(R.id.reset);
        uiReturn = (Button) findViewById(R.id.return1);
        score = (TextView)findViewById(R.id.score1);
        finish=(Button) findViewById(R.id.buttonResult);


    }

    /////////配列をシャッフルする//////////////////////////////////////////////////////////////////////////////
    //ランダムな数値列を作る（重複なし）
    //  引数(In)
    //  引数(Out)
    //  return
    public void randomArray() {

        Random rd = new Random();

        //特定の配列の最後の要素から開始し、配列内でランダムに選択された要素と交換し続ける
        for (int i = globals.g_nShuffleLen - 1; i > 0; i--) {
            //ランダムさせる番号の最大数～０の中から一つピックアップ
            int j = rd.nextInt(i + 1);

            int temp = globals.g_nShuffle[i];
            globals.g_nShuffle[i] = globals.g_nShuffle[j];
            globals.g_nShuffle[j] = temp;
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////
    //画像をイメージボタンに表示
    //  引数(In)
    //  引数(Out)
    //  return
    @SuppressLint("ResourceType")
    public void setView() {//jは回数を表す

        list.addAll(Arrays.asList(R.drawable.rds_031, R.drawable.rds_032, R.drawable.rds_061, R.drawable.rds_010,
                R.drawable.rds_083, R.drawable.rds_082,  R.drawable.rds_049,
                R.drawable.rds_001, R.drawable.rds_002, R.drawable.rds_048, R.drawable.rds_003,
                R.drawable.rds_019, R.drawable.rds_051, R.drawable.rds_033, R.drawable.rds_060,
                R.drawable.rds_052, R.drawable.rds_050, R.drawable.rds_085, R.drawable.rds_054));

        ImageButton[] imageButton = {ib1, ib2, ib3, ib4, ib5, ib6, ib7, ib8, ib9,ib10,ib11,ib12,ib13,ib14,ib15,ib16};//ボタン用配列

        for (int i = 0; i < globals.lva; i++) {

            int n = globals.g_nShuffle[i];
            int m = list.get(n);
            Drawable drawable = getResources().getDrawable(m);//jに入れられた数字の配列番号をだす
            imageButton[i].setImageDrawable(drawable);//ボタンに画像を挿入

            globals.bingo[i] = n;
        }

    }

    /////////インテントメソッド///////////////////////////////////////////////////////////////////////////////////////////
    //カメラ認識ページへインテント
    //  引数(In)
    //  引数(Out)
    //  return
    public void goIntent() {
        globals.g_nIntentCount = 1;
        Intent intent = new Intent(Bingo4.this, ImageJudgeCamera.class);
        stopPlayer();
        startActivity(intent);
    }

//////YesNo////////////////////////////////////////////////////////////////////////////////
//    ダイアログ「はい」を押下
public void doYes() {
    {
        Intent intent = new Intent(Bingo4.this, MainActivity.class);
        stopPlayer();
        startActivity(intent);
    }
}
    //ダイアログ「いいえ」を押下
    public void doNo() {

    }
    public  void doYesFinish() {
        //終了画面へインテント※※ダイアログにする
        if (globals.g_nScoreFinal <= 300) {
            Intent intent = new Intent(Bingo4.this, BingoResult3.class);
            stopPlayer();
            startActivity(intent);
        } else if (globals.g_nScoreFinal <= 600) {
            Intent intent = new Intent(Bingo4.this, BingoResult2.class);
            stopPlayer();
            startActivity(intent);
        } else {
            Intent intent = new Intent(Bingo4.this, BingoResult.class);
            stopPlayer();
            startActivity(intent);
        }
    }
        private void stopPlayer() {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;

            }
        }

    }


