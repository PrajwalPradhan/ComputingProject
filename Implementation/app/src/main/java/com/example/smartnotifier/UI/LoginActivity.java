package com.example.smartnotifier.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;

public class LoginActivity extends AppCompatActivity {

    EditText txt_email, txt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_password = (EditText) findViewById(R.id.txt_password);
    }

    public void processLogin(View v) {
        String email = txt_email.getText().toString();
        String password = txt_password.getText().toString();

        if(email.equals("")){
            txt_email.setError("Email is required");
            txt_email.requestFocus();
        }else if(password.equals("")){
            txt_password.setError("Password is required");
            txt_password.requestFocus();
        }

        if(email.equals("admin@gmail.com") && password.equals("password")){
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("TodoPref", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("auth", true);
            editor.commit();

            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }else {
            txt_email.setError("Email or password invalid!!");
            txt_email.requestFocus();
        }
    }

}
