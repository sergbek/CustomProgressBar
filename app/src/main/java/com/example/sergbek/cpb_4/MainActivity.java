package com.example.sergbek.cpb_4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private CustomProgressBar mCustomProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomProgressBar = (CustomProgressBar) findViewById(R.id.viewProgressBar_AM);

        final Button btnStart = (Button) findViewById(R.id.btn_start_AM);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomProgressBar.start();
                btnStart.setEnabled(false);
            }
        });

    }
}
