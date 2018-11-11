package com.create.rsfae.futuroprofesional;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.create.rsfae.futuroprofesional.Login.Login_Activity;

public class SplashScreen_Activity extends AppCompatActivity {

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        logo = findViewById(R.id.logo);

        //Codigo para Iniciar la Animacion del logo
        Animation mianimacion = AnimationUtils.loadAnimation(this,R.anim.bounce);
        logo.startAnimation(mianimacion);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen_Activity.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}
