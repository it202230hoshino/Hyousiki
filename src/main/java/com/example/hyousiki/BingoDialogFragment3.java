package com.example.hyousiki;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class BingoDialogFragment3 extends DialogFragment {

    Globals globals;

    Button uiReturn;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder.setMessage("ゲームをおわる？")
                .setPositiveButton("おわる", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                ((Bingo3) getActivity()).doYesFinish();
                                // このボタンを押した時の処理を書きます。


                            }
                        }
                )
                .setNegativeButton("つづける", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                ((Bingo3) getActivity()).doNo();
                            }
                        }
                )

                .create();

    }
}
