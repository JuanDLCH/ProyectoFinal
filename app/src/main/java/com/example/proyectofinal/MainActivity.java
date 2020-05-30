package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        link();
        setListeners();
    }

    private void setListeners() {
        btnMap.setOnClickListener(this);
    }

    private void link() {
        btnMap = findViewById(R.id.btnMap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnMap:
                openActivity(MapsActivity.class);
                break;
        }
    }

    private void openActivity(Class Class) {
        Intent intent = new Intent(getApplicationContext(), Class);
        startActivity(intent);
        finish();
    }
}
