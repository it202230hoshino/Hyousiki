package com.example.hyousiki;

import android.app.Application;

public class Globals<i> extends Application {

    final int LINE_3 = 3;//ビンゴの配列数３
    final int LINE_4 = 4;//ビンゴの配列数４

    int g_nSizeBingo;//ビンゴ配列数指定
    int lva;//〇×〇のビンゴカード

    int[] bingo;//ビンゴ用配列

    int[] status;//判定用配列

    //ボタン制御用インデックス
    final int BUTTON01 = 0;
    final int BUTTON02 = 1;
    final int BUTTON03 = 2;
    final int BUTTON04 = 3;
    final int BUTTON05 = 4;
    final int BUTTON06 = 5;
    final int BUTTON07 = 6;
    final int BUTTON08 = 7;
    final int BUTTON09 = 8;
    final int BUTTON10 = 9;
    final int BUTTON11 = 10;
    final int BUTTON12 = 11;
    final int BUTTON13 = 12;
    final int BUTTON14 = 13;
    final int BUTTON15 = 14;
    final int BUTTON16 = 15;

    //ビンゴ判定
    int g_nClearBingo = 0;

    //今いるボタン位置
    int g_nCurrent = 99;

    //ビンゴ画面からインテントした回数を記録　　　　　
    int g_nIntentCount = 0;



    //シャフル用配列埋め込み(画像枚数分)※画像数が増えたら増やす
    final int[] g_nShuffle = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    final int g_nShuffleLen = g_nShuffle.length;


    //点数用標識レベル配列埋め込み※画像数が増えたら増やす（画像はlistを参照）
    final int[] g_nScore = {10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10};
    //ビンゴした回数を入れていく
    int g_nBingoCount;

    //計算用
    int g_nScoreSum =0;
    //計算結果用
    int g_nScoreFinal = 0;

    //過去のビンゴ数を記録
    int g_nBingoLog;
}
