package com.example.hyousiki;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private TextView countLabel;
    private TextView questionLabel;
    private ImageView image;
    private Button answerBtn1;
    private Button answerBtn2;
    private Button answerBtn3;
    private Button answerBtn4;
    private String rightAnswer;
    private String rightAnswer2;
    private String commentary;
    private int rightAnswerCount;
    private int quizCount = 1;
    private SoundPool soundPool;
    private int good_se; // 正解の効果音の識別ID
    private int bad_se; // 不正解の効果音の識別ID
    private boolean kanji_flg = true;
    static final private int QUIZ_COUNT = 5;
    MediaPlayer mediaPlayer;


    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();


    String[][] quizData = {
            // {"都道府県名", "正解", "選択肢１", "選択肢２", "選択肢３"}
            {"", Integer.toString(R.drawable.rds_001), "通行止め", "車両通行止め", "車両侵入禁止", "転回禁止", "つうこうどめ", "しゃりょうつうこうどめ", "しゃりょうしんにゅうきんし", "てんかいきんし", "歩いてる人も車両も通れないよ！"},
            {"", Integer.toString(R.drawable.rds_002), "車両通行止め", "通行止め", "歩行者通行止め", "自転車横断帯", "しゃりょうつうこうどめ", "つうこうどめ", "ほこうしゃつうこうどめ", "じてんしゃおうだんたい", "車両は通れないよ！"},
            {"", Integer.toString(R.drawable.rds_003), "車両侵入禁止", "転回禁止", "徐行", "駐車可", "しゃりょうしんにゅうきんし", "てんかいきんし", "じょこう", "ちゅうしゃか", "標識がある向きから車両が通れないよ！"},
            {"", Integer.toString(R.drawable.rds_010), "自転車通行止め", "踏切あり", "並進可", "すべりやすい", "じてんしゃつうこうどめ", "ふみきりあり", "へいしんか", "すべりやすい", "標識がある向きから車両が通れないよ！"},
            {"", Integer.toString(R.drawable.rds_019), "転回禁止", "自転車および歩行者専用", "横断歩道", "車両通行止め", "てんかいきんし", "じてんしゃおよびほこうしゃせんよう", "おうだんほどう", "しゃりょうつうこうどめ", "車両が道路で反対に回ることができないよ！"},
            {"", Integer.toString(R.drawable.rds_031), "自転車専用", "自転車および歩行者専用", "歩行者通行止め", "一時停止", "じてんしゃせんよう", "じてんしゃおよびほこうしゃせんよう", "ほこうしゃつうこうどめ", "いちじていし", "自転車が通るための道だよ！"},
            {"", Integer.toString(R.drawable.rds_032), "自転車および歩行者専用", "自転車専用", "自転車通行止め", "横断歩道", "じてんしゃおよびほこうしゃせんよう", "じてんしゃせんよう", "じてんしゃつうこうどめ", "おうだんほどう", "自転車と歩いてる人だけが通れるよ！"},
            {"", Integer.toString(R.drawable.rds_033), "歩行者専用", "歩行者横断禁止", "通行止め", "学校、幼稚園、保育園などあり", "ほこうしゃせんよう", "ほこうしゃおうだんきんし", "つうこうどめ", "がっこう、ようちえん、ほいくえんなどあり", "歩いてる人だけが通る道だよ！"},
            {"", Integer.toString(R.drawable.rds_048), "徐行", "自転車横断帯", "一時停止", "自転車通行止め", "じょこう", "じてんしゃおうだんたい", "いちじていし", "じてんしゃつうこうどめ", "車が止まれる速さで走ることだよ！"},
            {"", Integer.toString(R.drawable.rds_049), "一時停止", "駐車可", "すべりやすい", "転回禁止", "いちじていし", "ちゅうしゃか", "すべりやすい", "てんかいきんし", "停止線や交差点の前で一回止まらないといけないよ！"},
            {"", Integer.toString(R.drawable.rds_050), "歩行者通行止め", "歩行者専用", "横断歩道", "学校、幼稚園、保育園などあり", "ほこうしゃつうこうどめ", "ほこうしゃせんよう", "おうだんほどう", "がっこう、ようちえん、ほいくえんなどあり", "ここより先に歩いてる人は通れないよ！"},
            {"", Integer.toString(R.drawable.rds_051), "歩行者横断禁止", "車両侵入禁止", "歩行者通行止め", "横断歩道", "ほこうしゃおうだんきんし", "しゃりょうしんにゅうきんし", "ほこうしゃつうこうどめ", "おうだんほどう", "歩いてる人は道路をわたれないよ！"},
            {"", Integer.toString(R.drawable.rds_052), "並進可", "踏切あり", "駐車可", "歩行者専用", "へいしんか", "ふみきりあり", "ちゅうしゃか", "ほこうしゃせんよう", "自転車がよこにならんで走れるよ！"},
            {"", Integer.toString(R.drawable.rds_054), "駐車可", "自転車横断帯", "すべりやすい", "転回禁止", "ちゅうしゃか", "じてんしゃおうだんたい", "すべりやすい", "てんかいきんし", "車両を止めることができるよ！"},
            {"", Integer.toString(R.drawable.rds_060), "横断歩道", "車両通行止め", "自転車横断帯.", "学校、幼稚園、保育園などあり", "おうだんほどう", "しゃりょうつうこうどめ", "じてんしゃおうだんたい", "がっこう、ようちえん、ほいくえんなどあり", "歩いてる人ゆうせんの道だよ！"},
            {"", Integer.toString(R.drawable.rds_061), "自転車横断帯", "徐行", "自転車専用", "並進可", "じてんしゃおうだんたい", "じょこう", "じてんしゃせんよう", "へいしんか", "この標識があるときは自転車はこの道を通らないといけないよ！"},
            {"", Integer.toString(R.drawable.rds_082), "踏切あり", "一時停止", "歩行者横断禁止", "自転車通行止め", "ふみきりあり", "いちじていし", "ほこうしゃおうだんきんし", "じてんしゃつうこうどめ", "先にふみきりがあるよ！"},
            {"", Integer.toString(R.drawable.rds_083), "学校、幼稚園、保育園などあり", "横断歩道", "歩行者専用", "歩行者通行止め", "がっこう、ようちえん、ほいくえんなどあり", "おうだんほどう", "ほこうしゃせんよう", "ほこうしゃつうこうどめ", "先に学校とかがあって通学で通るよ！"},
            {"", Integer.toString(R.drawable.rds_085), "すべりやすい", "車両侵入禁止", "歩行者横断禁止", "駐車可", "すべりやすい", "しゃりょうしんにゅうきんし", "ほこうしゃおうだんきんし", "ちゅうしゃか", "この先すべりやすくてきけんだよ！"},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        findViewById(R.id.modoru).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                stopPlayer();
                startActivity(intent);
            }
        });

        //BGM
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.dennnouhyouryuuki);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // Android 5.0(Lolipop)より古いとき
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        } else {
            // Android 5.0(Lolipop)以降
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(2)
                    .build();
        }
        good_se = soundPool.load(this, R.raw.good, 1); // 正解の効果音の識別IDを保存
        bad_se = soundPool.load(this, R.raw.bad, 1); // 不正解の効果音の識別IDを保存

        countLabel = findViewById(R.id.countLabel);
        questionLabel = findViewById(R.id.questionLabel);
        image = findViewById(R.id.question);
        answerBtn1 = findViewById(R.id.answerBtn1);
        answerBtn2 = findViewById(R.id.answerBtn2);
        answerBtn3 = findViewById(R.id.answerBtn3);
        answerBtn4 = findViewById(R.id.answerBtn4);


        // quizDataからクイズ出題用のquizArrayを作成する
        for (String[] quizDatum : quizData) {

            // 新しいArrayListを準備
            ArrayList<String> tmpArray = new ArrayList<>();

            // クイズデータを追加
            tmpArray.add(quizDatum[0]);// 都道府県名
            tmpArray.add(quizDatum[2]);  // 正解
            tmpArray.add(quizDatum[3]);  // 選択肢１
            tmpArray.add(quizDatum[4]);  // 選択肢２
            tmpArray.add(quizDatum[5]);  // 選択肢３
            tmpArray.add(quizDatum[1]);  // 画像
            tmpArray.add(quizDatum[6]);  // 正解(ひらがな)
            tmpArray.add(quizDatum[7]);  // 選択肢１(ひらがな)
            tmpArray.add(quizDatum[8]);  // 選択肢２(ひらがな)
            tmpArray.add(quizDatum[9]);  // 選択肢３(ひらがな)
            tmpArray.add(quizDatum[10]); // 解説

            // tmpArrayをquizArrayに追加する
            quizArray.add(tmpArray);
        }
        showNextQuiz();
    }

    public void showNextQuiz() {
        // クイズカウントラベルを更新
        countLabel.setText(getString(R.string.count_label, quizCount));

        // ランダムな数字を取得
        Random random = new Random();
        int randomNum = random.nextInt(quizArray.size());

        // randomNumを使って、quizArrayからクイズを一つ取り出す
        ArrayList<String> quiz = quizArray.get(randomNum);


        // 問題文（都道府県名）を表示
        //questionLabel.setText(quiz.get(0));
        image.setImageResource(Integer.parseInt(quiz.get(5)));


        // 正解をrightAnswerにセット
        rightAnswer = quiz.get(1);
        rightAnswer2 = quiz.get(6);
        commentary = quiz.get(10);


        // クイズ配列から問題文（都道府県名）を削除
        quiz.remove(0);


        ////////////////////////// //ランダム配列を作る///////////////////////////////////
        int[] shuffleList = {0, 1, 2, 3};
        Random rd = new Random();
        //特定の配列の最後の要素から開始し、配列内でランダムに選択された要素と交換し続ける
        for (int i = shuffleList.length - 1; i > 0; i--) {
            //ランダムさせる番号の最大数～０の中から一つピックアップ
            int j = rd.nextInt(i + 1);
            int temp = shuffleList[i];
            shuffleList[i] = shuffleList[j];
            shuffleList[j] = temp;
        }
        ///////////////////////////////////////////////////////////////////////////////

        // 正解と選択肢３つをシャッフル
        ArrayList<String> select1 = new ArrayList<>();
        select1.add(quiz.get(0));
        select1.add(quiz.get(1));
        select1.add(quiz.get(2));
        select1.add(quiz.get(3));
//            Collections.shuffle(select1);

//            ArrayList<String> select2 = new ArrayList<>();
        select1.add(quiz.get(5));
        select1.add(quiz.get(6));
        select1.add(quiz.get(7));
        select1.add(quiz.get(8));
//            Collections.shuffle(select2);

        // 解答ボタンに正解と選択肢３つを表示
        answerBtn1.setText(select1.get(shuffleList[0]));
        answerBtn2.setText(select1.get(shuffleList[1]));
        answerBtn3.setText(select1.get(shuffleList[2]));
        answerBtn4.setText(select1.get(shuffleList[3]));

        findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kanji_flg) {
                    //ひらがな表示に変更
                    answerBtn1.setText(select1.get(shuffleList[0] + 4));
                    answerBtn2.setText(select1.get(shuffleList[1] + 4));
                    answerBtn3.setText(select1.get(shuffleList[2] + 4));
                    answerBtn4.setText(select1.get(shuffleList[3] + 4));
                    kanji_flg = false;

                } else {
                    //漢字表示に変更
                    answerBtn1.setText(select1.get(shuffleList[0]));
                    answerBtn2.setText(select1.get(shuffleList[1]));
                    answerBtn3.setText(select1.get(shuffleList[2]));
                    answerBtn4.setText(select1.get(shuffleList[3]));
                    kanji_flg = true;
                }
            }
        });

        // このクイズをquizArrayから削除
        quizArray.remove(randomNum);
    }

    public void checkAnswer(View view) {

        // どの解答ボタンが押されたか
        Button answerBtn = findViewById(view.getId());
        String btnText = answerBtn.getText().toString();

        String alertTitle;
        if (btnText.equals(rightAnswer) || btnText.equals(rightAnswer2)) {
            alertTitle = "正解!";
            soundPool.play(good_se, 1F, 1F, 0, 0, 1F);
            rightAnswerCount++;
        } else {
            alertTitle = "不正解...";
            soundPool.play(bad_se, 1F, 1F, 0, 0, 1F);
        }

        // ダイアログを作成
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alertTitle);
        builder.setMessage("答え : " + rightAnswer + "\n" + "\n" + commentary);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (quizCount == QUIZ_COUNT) {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("RIGHT_ANSWER_COUNT", rightAnswerCount);
                    stopPlayer();
                    startActivity(intent);
                    // 結果画面へ移動
                } else {
                    quizCount++;
                    showNextQuiz();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }
}

