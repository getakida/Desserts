package com.example.desserts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class GoalActivity extends AppCompatActivity {
    private EditText goalEditText;
    private ProgressBar goalProgressBar;
    private int progressValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        goalEditText = findViewById(R.id.goalEditText);
        goalProgressBar = findViewById(R.id.goalProgressBar);

        Button achieveButton1 = findViewById(R.id.achieveButton1);
        Button achieveButton2 = findViewById(R.id.achieveButton2);
        Button achieveButton3 = findViewById(R.id.achieveButton3);
        Button achieveButton4 = findViewById(R.id.achieveButton4);
        Button achieveButton5 = findViewById(R.id.achieveButton5);

        achieveButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProgress(20);
            }
        });

        achieveButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProgress(20);
            }
        });

        achieveButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProgress(20);
            }
        });

        achieveButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProgress(20);
            }
        });

        achieveButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProgress(20);
            }
        });
    }

    private void updateProgress(int increment) {
        progressValue += increment;
        goalProgressBar.setProgress(progressValue);
    }

    // Save the progress value and goal text and pass it back to the MainActivity
    private void saveAndFinish() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("progressValue", progressValue);
        resultIntent.putExtra("goalText", goalEditText.getText().toString());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        saveAndFinish();
    }
}
