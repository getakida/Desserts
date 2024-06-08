package com.example.desserts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ProgressBar goalProgressBar;
    private TextView progressText;
    private int progressValue = 0;  // 예시로 사용할 변수 (진행 상태를 나타냅니다)
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 닉네임을 표시하는 TextView
        welcomeTextView = findViewById(R.id.welcomeTextView);

        // 날짜 표시
        TextView dateTextView = findViewById(R.id.dateTextView);
        String currentDate = new SimpleDateFormat("오늘은 yyyy년 MM월 dd일", Locale.getDefault()).format(new Date());
        dateTextView.setText(currentDate);

        // 버튼1 (DiaryActivity로 이동)
        Button diaryButton = findViewById(R.id.diaryBtn);
        diaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
                startActivity(intent);
            }
        });

        // 버튼2 (HealthActivity로 이동)
        Button healthButton = findViewById(R.id.healthBtn);
        healthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HealthActivity.class);
                startActivity(intent);
            }
        });

        // 버튼3 (RestActivity로 이동)
        Button restButton = findViewById(R.id.restBtn);
        restButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RestActivity.class);
                startActivity(intent);
            }
        });

        // 프로그레스 바 초기화 및 텍스트뷰 설정
        goalProgressBar = findViewById(R.id.goalProgressBar);
        progressText = findViewById(R.id.progressText);

        goalProgressBar.setMax(100);  // 최대 값 설정
        goalProgressBar.setProgress(progressValue);  // 초기 값 설정
        updateProgressText(progressValue);  // 텍스트 업데이트

        // 버튼4 (GoalActivity로 이동)
        Button goalButton = findViewById(R.id.goalBtn);
        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, GoalActivity.class), 1);
            }
        });

        // 닉네임을 설정하는 메소드 호출
        setWelcomeText();  // 여기에 닉네임을 설정하세요
    }

    // 진행 상태 텍스트 업데이트 메서드
    private void updateProgressText(int progress) {
        progressText.setText("목표 진행 상태: " + progress + "%");
    }

    // GoalActivity에서 결과를 받아와서 프로그레스 바 업데이트
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            progressValue = data.getIntExtra("progressValue", 0);
            updateProgressText(progressValue);
            goalProgressBar.setProgress(progressValue);

            // 수정된 부분: setWelcomeText 메소드 호출
            setWelcomeText();
        }
    }
    private void setWelcomeText() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();  // FirebaseAuth 초기화
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();  // Firestore 초기화

        String userId = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("users")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // nickname을 사용하여 TextView 업데이트
                                String nicknameFromFirestore = document.getString("nickname");
                                if (nicknameFromFirestore != null) {
                                    welcomeTextView.setText(nicknameFromFirestore + "님, \n오늘 하루도 수고하셨어요!");
                                }
                            }
                        }
                    }
                });
    }


}
