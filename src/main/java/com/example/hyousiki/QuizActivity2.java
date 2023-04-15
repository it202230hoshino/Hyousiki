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
import java.util.Collections;
import java.util.Random;

public class QuizActivity2 extends AppCompatActivity {
    private TextView countLabel;
    private TextView questionLabel;
    private ImageView image;
    private Button answerBtn1;
    private Button answerBtn2;
    private Button answerBtn3;
    private Button answerBtn4;
    private String rightAnswer;
    private String commentary;
    private int rightAnswerCount;
    private int quizCount = 1;
    private SoundPool soundPool;
    private int good_se; // 正解の効果音の識別ID
    private int bad_se; // 不正解の効果音の識別ID
    static final private int QUIZ_COUNT = 5;
    MediaPlayer mediaPlayer;




    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();


    String[][] quizData = {
            // {"都道府県名", "正解", "選択肢１", "選択肢２", "選択肢３"}
            {"通行止め",Integer.toString(R.drawable.rds_001), "歩いてる人も車両も通れない", "車両は通れない", "ひょうしきがある向きから車両が通れない", "自転車と歩いてる人だけが通れる",""},
            {"車両通行止め",Integer.toString(R.drawable.rds_002), "車両は通れない", "歩いてる人も車両も通れない", "歩いてる人だけが通る道である", "車が止まれる速さで走ること",""},
            {"車両入禁止",Integer.toString(R.drawable.rds_003), "ひょうしきがある向きから車両が通れない", "この標識より先は自転車のみ通れない", "交差点などの前で一回止まらないといけない", "ここより先に歩いてる人は通れない",""},
            {"自転車通行止め",Integer.toString(R.drawable.rds_010), "この標識より先は自転車のみ通れない", "先にふみきりがある", "歩いてる人は道路をわたれない", "歩いてる人ゆうせんの道である",""},
            {"転回禁止",Integer.toString(R.drawable.rds_019), "車両が道路で反対に回ることができない", "歩いてる人も車両も通れない", "車が止まれる速さで走ること", "車両を止めることができる",""},
            {"自転車専用",Integer.toString(R.drawable.rds_031), "自転車が通るための道である", "車両は通れない", "交差点などの前で一回止まらないといけない", "自転車がよこにならんで走れる",""},
            {"自転車および歩行者専用",Integer.toString(R.drawable.rds_032), "自転車と歩いてる人だけが通れる", "車両が道路で反対に回ることができない", "歩いてる人は道路をわたれない", "自転車はこのみちを通らないといけない",""},
            {"歩行者専用",Integer.toString(R.drawable.rds_033), "歩いてる人だけが通る道である", "ひょうしきがある向きから車両が通れない", "車両を止めることができる", "学校などがあって通学で通る",""},
            {"徐行",Integer.toString(R.drawable.rds_048), "車が止まれる速さで走ること", "車両は通れない", "この標識より先は自転車のみ通れない","自転車と歩いてる人だけが通れる",""},
            {"一時停止",Integer.toString(R.drawable.rds_049), "交差点などの前で一回止まらないといけない", "車両は通れない", "車が止まれる速さで走ること", "歩いてる人ゆうせんの道である",""},
            {"歩行者通行止め",Integer.toString(R.drawable.rds_050), "ここより先に歩いてる人は通れない", "歩いてる人も車両も通れない", "自転車はこのみちを通らないといけない", "学校などがあって通学で通る",""},
            {"歩行者横断禁止",Integer.toString(R.drawable.rds_051), "歩いてる人は道路をわたれない", "車両が道路で反対に回ることができない", "自転車と歩いてる人だけが通れる", "ここより先に歩いてる人は通れない",""},
            {"並進可",Integer.toString(R.drawable.rds_052), "自転車がよこにならんで走れる", "ひょうしきがある向きから車両が通れない", "自転車が通るための道である", "この標識より先は自転車のみ通れない",""},
            {"駐車可",Integer.toString(R.drawable.rds_054), "車両を止めることができる", "車両は通れない", "車が止まれる速さで走ること", "交差点などの前で一回止まらないといけない",""},
            {"横断歩道",Integer.toString(R.drawable.rds_060), "歩いてる人ゆうせんの道である", "この標識より先は自転車のみ通れない", "自転車と歩いてる人だけが通れる", "歩いてる人だけが通る道である",""},
            {"自転車横断帯",Integer.toString(R.drawable.rds_061), "自転車はこのみちを通らないといけない", "自転車が通るための道である", "自転車がよこにならんで走れる", "車両を止めることができる",""},
            {"踏切あり",Integer.toString(R.drawable.rds_082), "先にふみきりがある", "自転車が通るための道である", "ここより先に歩いてる人は通れない", "学校などがあって通学で通る","この標識より先は自転車のみ通れない",""},
            {"学校、幼稚園、保育園などあり",Integer.toString(R.drawable.rds_083), "学校などがあって通学で通る", "先にふみきりがある", "歩いてる人は道路をわたれない", "自転車はこのみちを通らないといけない",""},
            {"すべりやすい",Integer.toString(R.drawable.rds_085), "この先すべりやすくてきけん", "この標識より先は自転車のみ通れない", "歩いてる人だけが通る道である", "自転車がよこにならんで走れる",""},
    };

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);

        findViewById(R.id.modoru).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QuizActivity2.this, MainActivity.class);
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
            tmpArray.add(quizDatum[6]);  //
            tmpArray.add(quizDatum[2]);  // 正解
            tmpArray.add(quizDatum[3]);  // 選択肢１
            tmpArray.add(quizDatum[4]);  // 選択肢２
            tmpArray.add(quizDatum[5]);  // 選択肢３
            tmpArray.add(quizDatum[1]);  // 画像
            tmpArray.add(quizDatum[0]);  // 解説

            // tmpArrayをquizArrayに追加する
            quizArray.add(tmpArray);
        }
        showNextQuiz();
    }

    public void showNextQuiz () {
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
        commentary = quiz.get(6);



        // クイズ配列から問題文（都道府県名）を削除
        quiz.remove(0);

        ArrayList<String> select1 = new ArrayList<>();
        select1.add(quiz.get(0));
        select1.add(quiz.get(1));
        select1.add(quiz.get(2));
        select1.add(quiz.get(3));

        Collections.shuffle(select1);
        answerBtn1.setText(select1.get(0));
        answerBtn2.setText(select1.get(1));
        answerBtn3.setText(select1.get(2));
        answerBtn4.setText(select1.get(3));


        // このクイズをquizArrayから削除
        quizArray.remove(randomNum);
    }

    public void checkAnswer (View view){

        // どの解答ボタンが押されたか
        Button answerBtn = findViewById(view.getId());
        String btnText = answerBtn.getText().toString();

        String alertTitle;
        if (btnText.equals(rightAnswer)) {
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
        builder.setMessage("答え : " + rightAnswer+"\n"+"\n"+commentary);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (quizCount == QUIZ_COUNT) {
                    Intent intent = new Intent(QuizActivity2.this, ResultActivity.class);
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

