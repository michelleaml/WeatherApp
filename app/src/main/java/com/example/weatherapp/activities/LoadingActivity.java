package com.example.weatherapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.bumptech.glide.Glide;
import android.Manifest;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.ActivityLoadingBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LoadingActivity extends AppCompatActivity {

    ActivityLoadingBinding binding;
    MyCountDownTimer myCountDownTimer;
    FusedLocationProviderClient fusedLocationClient;
    final public String TAG = "MICHELLE DEBUGGING";
    private static final int FINE_LOCATION_PERMISSION_CODE = 100; //en mayuscula porque se usan como constantes, no van a cambiar
    private static  final int COARSE_LOCATION_PERMISSION_CODE = 101;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(this).load(R.drawable.giphy).into(binding.weatherImage);

        myCountDownTimer = new MyCountDownTimer(5000, 1000);
        myCountDownTimer.start();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, FINE_LOCATION_PERMISSION_CODE);
        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, COARSE_LOCATION_PERMISSION_CODE);

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
               if (location != null){
                   latitude = location.getLatitude();
                   longitude = location.getLongitude();
               }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error trying to get last GPS location");
                e.printStackTrace();
            }
        });
    }
    public void checkPermission(String permission, int requestCode){
        if (ActivityCompat.checkSelfPermission(LoadingActivity.this, permission) ==
                PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(LoadingActivity.this, new String[]{permission},
                    requestCode);
        } else {
            Toast.makeText(LoadingActivity.this, "Permission already granted",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if(requestCode == FINE_LOCATION_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(LoadingActivity.this, "Fine Location Permission Granted",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoadingActivity.this, "Fine Location Permission Denied",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == COARSE_LOCATION_PERMISSION_CODE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(LoadingActivity.this, "Coarse Location Permission Granted",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoadingActivity.this, "Coarse Location Permission Denied",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Funcion para checar y pedir permisos.
    public class MyCountDownTimer extends CountDownTimer{
        int progress = 0;

        public MyCountDownTimer(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished){
            progress = progress + 20;
            binding.loadingProgressBar.setProgress(progress);
        }

        @Override
        public void onFinish(){
            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
            intent.putExtra("LATITUDE", String.valueOf(latitude));
            intent.putExtra("LONGITUDE", String.valueOf(longitude));
            startActivity(intent);

        }

    }

}