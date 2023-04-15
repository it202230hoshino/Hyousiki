package com.example.hyousiki;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage mStorage;
    private ValueEventListener mDBListener;
    private List<Upload> mUploads;
    LocationManager locationManager;








    //マップ表示
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ImageButton kirokuback;

        kirokuback = findViewById(R.id.kirokuback);



        kirokuback.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MapsActivity.this, KirokuActivity.class);
                        startActivity(intent);
                    }
                }
        );




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 位置情報をとる
        locationStart();

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

                if (mMap == null) {
                    return;
                }

                LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));


            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 50, l);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 50, l);

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {


        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            private View mWindow = getLayoutInflater().inflate(R.layout.info_window_layout, null);

            @Nullable
            @Override
            public View getInfoContents(@NonNull Marker marker) {
                return null;
            }

            @Nullable
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                render(marker, mWindow);
                return mWindow;


            }
//マーカータップ時に画像と説明文とタイトル表記
            private void render(Marker marker, View view) {
                String[] work = marker.getTitle().split(":");
                String wTitle = work[0];
                String wUrl = work[1];
                // 画像
                ImageView badge = (ImageView) view.findViewById(R.id.badge);
                //  FireStrageから画像を取る　→　wUrl
                // 画像取れたら↓
                //badge.setImage(R.drawable.ic_logo);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();

                StorageReference mountainsRef = storageRef.child(wUrl);

                Task<byte[]> task = mountainsRef.getBytes(Long.MAX_VALUE);
                task.addOnSuccessListener(bytes -> {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    //ImageView iv = findViewById(R.id.image);
                    badge.setImageBitmap(bitmap);
                    TextView title = (TextView) view.findViewById(R.id.title);
                    TextView snippet = (TextView) view.findViewById(R.id.snippet);
                    title.setText(wTitle);
                    snippet.setText(marker.getSnippet());


                }).addOnFailureListener(exception -> {

                });

           }
//
        });


        mMap = googleMap;


        mUploads = new ArrayList<>();


        mStorage = FirebaseStorage.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());

                    mUploads.add(upload);
                    LatLng location = new LatLng(upload.getLatitude(), upload.getLongitude());
                    //ロケーションがないのは無視
                    if (location.latitude != 0.0d) {
                        //ピン立てる
                        String str = String.format(Locale.US, "%f, %f", location.latitude, location.longitude);
                        mMap.addMarker(new MarkerOptions().position(location).title(upload.getName() + ":" + upload.getImageUrl()).snippet(messageMap.get(upload.getName())).icon(BitmapDescriptorFactory.fromResource(R.drawable.hiyoyoyoyo)));
                        Log.d("AAAA", postSnapshot.toString());
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        //暫定@@
        LatLng location = new LatLng(35.172698234472456, 136.8847777578377);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));

    }

    private Map<String, String> messageMap = new HashMap<>();

    {
//        putで情報の
        messageMap.put("通行止め", "歩いてる人も車両も通れないよ！");
        messageMap.put("車両通行止め", "車両は通れないよ！");
        messageMap.put("車両進入禁止", "標識がある向きから車両が通れないよ！！");
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
        messageMap.put("学校幼稚園保育所等あり", "がっこうなどに通う子がよくとおるよ！");
        messageMap.put("すべりやすい", "この先すべりやすくてきけんだよ！");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }


}



