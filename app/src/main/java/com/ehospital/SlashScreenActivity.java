package com.ehospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SlashScreenActivity extends AppCompatActivity {

    //1500 Equals 1.5 seconds.
    private static int SPLASH_SCREEN = 1500;

    //Variables
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_slash_screen);

//        //Animation
//        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
//        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
//
//        //Hooks
//        image = findViewById(R.id.imageView);
//        logo = findViewById(R.id.textView);
//        slogan = findViewById(R.id.textView2);
//
//
//        image.setAnimation(topAnim);
//        logo.setAnimation(bottomAnim);
//        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SlashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);

    }
}
