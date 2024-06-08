package com.example.desserts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextNickname;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        editTextEmail = findViewById(R.id.editTextEmailSignUp);
        editTextPassword = findViewById(R.id.editTextPasswordSignUp);
        editTextNickname = findViewById(R.id.editTextNickname);

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }

    private void signUpUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String nickname = editTextNickname.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
            Toast.makeText(this, "이메일, 비밀번호, 닉네임을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase Authentication을 사용하여 회원가입
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            saveNicknameToFirestore(nickname);
                            Toast.makeText(SignupActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // 회원가입 실패
                            Toast.makeText(SignupActivity.this, "회원가입 실패\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveNicknameToFirestore(final String nickname) {
        // Firestore에 닉네임 저장
        final String userId = firebaseAuth.getCurrentUser().getUid();

        Map<String, Object> user = new HashMap<>();
        user.put("nickname", nickname);

        firestore.collection("users")
                .document(userId)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Firestore에 저장 성공
                            Toast.makeText(SignupActivity.this, "닉네임 저장 성공", Toast.LENGTH_SHORT).show();

                            // 로그인한 사용자의 정보 가져오기
                            firebaseAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // 현재 사용자 정보 가져오기
                                        String displayName = firebaseAuth.getCurrentUser().getDisplayName();
                                        if (displayName != null && !displayName.isEmpty()) {
                                            // 메인 화면에 사용자 닉네임 표시
                                            updateWelcomeTextView(displayName);
                                        }
                                    }
                                }
                            });
                        } else {
                            // Firestore에 저장 실패
                            Toast.makeText(SignupActivity.this, "닉네임 저장 실패\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateWelcomeTextView(String displayName) {
        // 메인 화면의 welcomeTextView 업데이트
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText(displayName + "님, \n 오늘 하루도 수고하셨어요!");
    }
}
