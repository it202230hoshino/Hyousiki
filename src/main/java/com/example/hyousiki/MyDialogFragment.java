package com.example.hyousiki;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.HashMap;
import java.util.Map;

public class MyDialogFragment extends DialogFragment {

    public String title;

    private ImageAdapter mAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        TextView titleView = new TextView(getActivity());
        titleView.setText(title);
        titleView.setTextSize(24);
        titleView.setTextColor(Color.BLACK);
        titleView.setBackgroundColor(getResources().getColor(R.color.skyblue));
        titleView.setPadding(20, 20, 20, 20);
        titleView.setGravity(Gravity.CENTER);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setCustomTitle(titleView)
                .setMessage(messageMap.get(title))
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((KirokuActivity) getActivity()).doset();
                            }
                        }
                )
                .create();
    }

    private Map<String, String> messageMap = new HashMap<>();
    {
//        putで情報の
        messageMap.put("通行止め", "歩いてる人も車両も通れないよ！");
        messageMap.put("車両通行止め","車両は通れないよ！");
        messageMap.put("車両侵入禁止", "標識がある向きから車両が通れないよ！！");
        messageMap.put("自転車通行止め", "この標識より先は自転車のみ通れないよ！");
        messageMap.put("転回禁止", "車両が道路で反対に回ることができないよ！");
        messageMap.put("自転車専用", "自転車が通るための道だよ！");
        messageMap.put("自転車及び歩行者専用道路", "自転車と歩いてる人だけが通れるよ！");
        messageMap.put("歩行者専用", "歩いてる人だけが通る道だよ！");
        messageMap.put("徐行", "車が止まれる速さで走ることだよ！");
        messageMap.put("一時停止", "停止線や交差点の前で一回止まらないといけないよ！");
        messageMap.put("歩行者通行止め", "ここより先に歩いてる人は通れないよ！");
        messageMap.put("歩行者横断禁止", "歩いてる人は道路をわたれないよ！");
        messageMap.put("並進可", "自転車がよこにならんで走れるよ！");
        messageMap.put("駐車可", "車両を止めることができるよ！");
        messageMap.put("横断歩道", "歩いてる人ゆうせんの道だよ！");
        messageMap.put("自転車横断帯", "この標識があるときは自転車はこの道を通らないといけないよ！");
        messageMap.put("踏切あり", "先にふみきりがあるよ！");
        messageMap.put("学校幼稚園保育所等あり", "先に学校とかがあって通学で通るよ！");
        messageMap.put("すべりやすい", "この先すべりやすくてきけんだよ！");
    }





}


//        通行止め・・・歩いてる人も車両も通れないよ！
//        車両通行止め・・・車両は通れないよ！
//        車両侵入禁止・・・標識がある向きから車両が通れないよ！
//        自転車通行止め・・・この標識より先は自転車のみ通れないよ！
//        転回禁止・・・車両が道路で反対に回ることができないよ！
//        自転車専用・・・自転車が通るための道だよ！
//        自転車及び歩行者専用道路・・・自転車と歩いてる人だけが通れるよ！
//        歩行者専用・・・歩いてる人だけが通る道だよ！
//        徐行・・・車が止まれる速さで走ることだよ！
//        一時停止・・・停止線や交差点の前で一回止まらないといけないよ！
//        歩行者通行止め・・・ここより先に歩いてる人は通れないよ！
//        歩行者横断禁止・・・歩いてる人は道路をわたれないよ！
//        並進可・・・自転車がよこにならんで走れるよ！
//        駐車可・・・車両を止めることができるよ！
//        横断歩道・・・歩いてる人ゆうせんの道だよ！
//        自転車横断帯・・・この標識があるときは自転車はこの道を通らないといけないよ！
//        踏切あり・・・先にふみきりがあるよ！
//        学校幼稚園保育所等あり・・・先に学校とかがあって通学で通るよ！
//        すべりやすい・・・この先すべりやすくてきけんだよ！
