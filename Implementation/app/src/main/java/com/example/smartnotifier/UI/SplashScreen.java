package com.example.smartnotifier.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("TodoPref", 0);
                if(preferences.contains("app_theme")){

                }else{
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("app_theme", "dark");
                    editor.commit();
                }

                Boolean auth = preferences.getBoolean("auth", false);
                if (auth) {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                } else {

                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, 3000);
    }
}
