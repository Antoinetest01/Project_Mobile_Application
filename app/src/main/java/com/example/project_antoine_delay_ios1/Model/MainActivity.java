package com.example.project_antoine_delay_ios1.Model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.project_antoine_delay_ios1.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Redirection to the login page
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}