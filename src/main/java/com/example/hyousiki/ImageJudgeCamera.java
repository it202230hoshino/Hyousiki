package com.example.hyousiki;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.hyousiki.ml.Model;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ImageJudgeCamera extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private TextView mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private int good_se; // 正解の効果音の識別ID
    private int bad_se; // 不正解の効果音の識別ID

    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private SoundPool soundPool;
    private StorageTask mUploadTask;

    TextView result, confidence, name, text, kanakana, name2, text1;
    ImageView imageView, imageView2, imageView3;
    int imageSize = 224;
    ImageButton picture, check, back, mButtonUpload;


    Globals globals;
    private com.google.android.gms.maps.model.LatLng lastLocation;
    private String longitudeRef;
    private String longitude;
    private String latitudeRef;
    private String latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_judge_camera);

        //グローバルの値を持ってくる
        globals = (Globals) this.getApplication();


        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
        check = findViewById(R.id.check);
        back = findViewById(R.id.back);
        imageView2 = (ImageView) findViewById(R.id.imageView2);//写真を撮る対象を画像表示
        mButtonUpload = findViewById(R.id.button_upload);
        mEditTextFileName = findViewById(R.id.result);
        text = findViewById(R.id.text3);//説明を書く
        name = findViewById(R.id.text2);//標識の名前を表示
        kanakana = findViewById(R.id.text0);//かな表示
        imageView3 = findViewById(R.id.image1);//標識画像インテント後
        name2 = findViewById(R.id.text);//名前表示　インテント後
        text1 = findViewById(R.id.text1);//カメラで撮ろう


        ArrayList<Integer> list = new ArrayList<Integer>();  //シャッフル用リスト※View重複なし用

        //音を持ってくる
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


        //写真判定を行う画像の見本を表示///////////////////////////////////
        list.addAll(Arrays.asList(R.drawable.rds_031, R.drawable.rds_032, R.drawable.rds_061, R.drawable.rds_010,
                R.drawable.rds_083, R.drawable.rds_082, R.drawable.rds_049,
                R.drawable.rds_001, R.drawable.rds_002, R.drawable.rds_048, R.drawable.rds_003,
                R.drawable.rds_019, R.drawable.rds_051, R.drawable.rds_033, R.drawable.rds_060,
                R.drawable.rds_052, R.drawable.rds_050, R.drawable.rds_085, R.drawable.rds_054));

        int n = globals.bingo[globals.g_nCurrent];//ｎにビンゴの配列番号を与える
        int m = list.get(n);//ｍに画像リストからビンゴの配列番号を参照して与える
        Drawable drawable = getResources().getDrawable(m);//ｍに入れられた数字の配列番号をだす
        imageView2.setImageDrawable(drawable);//ボタンに画像を挿入

        //カメラインテント
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, 1);
                    CameraDialogFragment dialog = new CameraDialogFragment();
                    dialog.show(getSupportFragmentManager(), "Down");
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }


        });

        String[] classes = {"自転車専用", "自転車専用及び歩行者専用", "自転車横断帯",
                "自転車通行止め", "学校幼稚園保育所あり", "踏切あり", "一時停止", "通行止め",
                "車両通行止め", "徐行", "車両進入禁止", "転回禁止", "歩行者横断禁止", "歩行者専用", "横断歩道", "並進可", "歩行者通行止め", "すべりやすい", "駐車可"};

        String[] kana = {"じてんしゃせんよう",
                "じてんしゃせんようおよびほこうしゃせんよう",
                "じてんしゃおうだんたい",
                "じてんしゃつうこうどめ",
                "がっこうようちえんほいくじょあり",
                "ふみきりあり",
                "いちじていし",
                "つうこうどめ",
                "しゃりょうつうこうどめ",
                "じょこう",
                "しゃりょうしんにゅうきんし",
                "てんかいきんし",
                "ほこうしゃおうだんきんし",
                "ほこうしゃせんよう",
                "おうだんほどう",
                "へいしんか",
                "ほこうしゃつうこうどめ",
                "",
                "ちゅうしゃか"
        };
        String[] textSort = {"自転車が通るための道だよ！",
                "自転車と歩いてる人だけが通れるよ！",
                "この標識があるときは\n自転車はこの道を通らないと\nいけないよ！",
                "この標識より先は\n自転車は通れないよ！",
                "先に学校があって通学で通るよ！",
                "先にふみきりがあるよ！",
                "停止線や交差点の前で\n止まって安全確認しよう！",
                "歩いてる人も車も通れないよ！",
                "車は通れないよ！",
                "車が止まれる速さで走ることだよ！",
                "標識がある向きから\n車は通れないよ！！",
                "ここで車がUターンできないよ！",
                "歩いてる人は道路をわたれないよ！",
                "歩いてる人だけが通る道だよ！",
                "歩いてる人ゆうせんの道だよ！",
                "自転車がならんで走れるよ！\n周囲には気をつけてね！",
                "歩いてる人はここより先を\n通れないよ！",
                "この先すべりやすくてきけんだよ！",
                "車両を止めることができるよ！"
        };

        //説明をつける
        kanakana.setText(kana[globals.bingo[globals.g_nCurrent]]);
        name.setText(classes[globals.bingo[globals.g_nCurrent]]);
        text.setText(textSort[globals.bingo[globals.g_nCurrent]]);

        //ファイアーベースとファイヤーストレージ
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        //保存ボタン
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.button_upload).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                YesNoDialogFragment dialog = new YesNoDialogFragment();

                                dialog.show(getSupportFragmentManager(), "Down");
                            }
                        }
                );
            }
        });

        //ビンゴ画面に戻る
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (globals.g_nSizeBingo == 3) {
                            Intent intent = new Intent(ImageJudgeCamera.this, Bingo3.class);
                            startActivity(intent);
                        } else if (globals.g_nSizeBingo == 4) {
                            Intent intent = new Intent(ImageJudgeCamera.this, Bingo4.class);
                            startActivity(intent);
                        } else {
                            //エラー
                        }
                    }
                }
        );

        //OK判定を手動でする(長押し)
        check.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.d("APP", "onLongClick");
                globals.status[globals.g_nCurrent] = 1;
                soundPool.play(good_se, 1F, 1F, 0, 0, 1F);//成功音
                dialogImageSuccess();
                return false;
            }
        });

        mButtonUpload.setEnabled(false);//保存ボタンを無効にする

        // 位置情報をとる
        locationStart();
//            mButtonUpload.setAlpha(0.25f);//保存ボタンを画像を薄くする
//        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//        /** 位置情報の通知するための最小時間間隔（ミリ秒） */
//        final long minTime = 500;
//        /** 位置情報を通知するための最小距離間隔（メートル）*/
//        final long minDistance = 1;
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, new LocationListener() {
//            @Override
//            public void onLocationChanged(@NonNull Location location) {
//                lastLocation = location;
//            }
//        });
    }

    private void locationStart() {
        Log.d("debug", "locationStart()");

        // LocationManager インスタンス生成
        LocationManager locationManager =
                (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager != null && locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER)) {

            Log.d("debug", "location manager Enabled");
        } else {
            // GPSを設定するように促す
            Intent settingsIntent =
                    new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            Log.d("debug", "not gpsEnable, startActivity");
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, LocationManager.NETWORK_PROVIDER}, 1000);

            Log.d("debug", "checkSelfPermission false");
            return;
        }


        LocationListener l = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

//                if (mMap == null) {
//                    return;
//                }

                Log.d("debug", "location =" + location);

                lastLocation = new LatLng(location.getLatitude(), location.getLongitude());

//                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("debug", "onStatusChanged =" + provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Log.d("debug", "onProviderDisabled =" + provider);
            }

            @Override
            public void onFlushComplete(int requestCode) {
                Log.d("debug", "onFlushComplete =" + requestCode);
            }

            @Override
            public void onLocationChanged(@NonNull List<Location> locations) {
                final int size = locations.size();
                for (int i = 0; i < size; i++) {
                    onLocationChanged(locations.get(i));
                }
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 50, l);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 50, l);

    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void doYes() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1);
    }

    public void doNo() {

    }


    //ダイアログ「はい」を押下
    public void doDownYes() {
        if (mUploadTask != null && mUploadTask.isInProgress()) {
            Log.d("AAAAA", "ダイアログ「はい」：アップロード中止");
        } else {
            Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            String file = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
            upload(file + ".jpeg", bmp);


        }
        Log.d("AAAAA", "ダイアログ「はい」：アップロード実施");
    }


    //ダイアログ「いいえ」を押下
    public void doDownNo() {

        Log.d("AAAAA", "ダイアログ「いいえ」：アップロード中止");
    }


    //カメラインテントから戻ったときの処理メソッド/////////////////////////////////////////////////////////////////////////
    public void classifyImage(Bitmap image) {


        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++];//RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0.0f;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            String[] classes = {"自転車専用", "自転車専用及び歩行者専用", "自転車横断帯",
                    "自転車通行止め", "学校幼稚園保育所あり", "踏切あり", "一時停止", "通行止め",
                    "車両通行止め", "徐行", "車両進入禁止", "転回禁止", "歩行者横断禁止", "歩行者専用", "横断歩道", "並進可", "歩行者通行止め", "すべりやすい", "駐車可"};


            String s = "";

            //テキストをクリアする
            kanakana.setText("");
            name.setText("");
            text.setText("");
            text1.setText("");
            mButtonUpload.setEnabled(true);//保存ボタンを有効にする

            //画像を貼り付け
            ArrayList<Integer> list = new ArrayList<Integer>();  //シャッフル用リスト※View重複なし用
            list.addAll(Arrays.asList(R.drawable.rds_031, R.drawable.rds_032, R.drawable.rds_061, R.drawable.rds_010,
                    R.drawable.rds_083, R.drawable.rds_082, R.drawable.rds_049,
                    R.drawable.rds_001, R.drawable.rds_002, R.drawable.rds_048, R.drawable.rds_003,
                    R.drawable.rds_019, R.drawable.rds_051, R.drawable.rds_033, R.drawable.rds_060,
                    R.drawable.rds_052, R.drawable.rds_050, R.drawable.rds_085, R.drawable.rds_054));

            int n = globals.bingo[globals.g_nCurrent];//ｎにビンゴの配列番号を与える
            int m = list.get(n);//ｍに画像リストからビンゴの配列番号を参照して与える
            Drawable drawable = getResources().getDrawable(m);//ｍに入れられた数字の配列番号をだす
            imageView3.setImageDrawable(drawable);

            name2.setText(classes[globals.bingo[globals.g_nCurrent]]);

            //撮った標識が目的の標識と同じだったら
            if (globals.bingo[globals.g_nCurrent] == maxPos) {
                result.setText(classes[maxPos]);//判定された最終的な標識名を表示
                //status配列に１（開いている状態）をいれる
                globals.status[globals.g_nCurrent] = 1;
                soundPool.play(good_se, 1F, 1F, 0, 0, 1F);//成功音
                dialogImageSuccess();

            } else {
                //違っていた場合
                result.setText("はんていできなかったよ");
                soundPool.play(bad_se, 1F, 1F, 0, 0, 1F);//失敗音
                dialogImageFailure();
            }

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

    }

    /////////成功画像表示///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void dialogImageSuccess() {
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.maru);
        iv.setAdjustViewBounds(true);
        new AlertDialog.Builder(this)
                .setView(iv)
                .show();
    }

    //////////失敗画像表示/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void dialogImageFailure() {
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.batsu);
        iv.setAdjustViewBounds(true);
        new AlertDialog.Builder(this)
                .setView(iv)
                .show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);


            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void upload(String name, Bitmap bmp) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference mountainsRef = storageRef.child("image/" + name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //com.google.android.gms.maps.model.LatLng loc = new com.google.android.gms.maps.model.LatLng(35.170523255448266d, 136.88005707004731d);
                Upload upload = new Upload(
                        mEditTextFileName.getText().toString().trim(),
                        taskSnapshot.getMetadata().getPath(),
                        lastLocation);


                String uploadId = mDatabaseRef.push().getKey();
                mDatabaseRef.child(uploadId).setValue(upload).addOnSuccessListener(command -> {
                    Log.d("AAAAA", "setValue Success");
                }).addOnFailureListener(e -> {
                    Log.d("AAAAA", "setValue Failure", e);
                });
                Toast.makeText(ImageJudgeCamera.this, "Upload Success!!", Toast.LENGTH_LONG).show();
            }
        });


    }


}



