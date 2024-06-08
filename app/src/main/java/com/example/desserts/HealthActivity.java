package com.example.desserts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

public class HealthActivity extends AppCompatActivity {
    DatePicker datePicker;
    EditText calorieEditText, exerciseDiaryEditText;
    Button saveButton;
    TextView viewText1, viewText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        // 뷰에 있는 위젯들 리턴 받아두기
        datePicker = findViewById(R.id.datePicker);
        calorieEditText = findViewById(R.id.calorieEditText);
        exerciseDiaryEditText = findViewById(R.id.exerciseDiaryEditText);
        saveButton = findViewById(R.id.saveButton);
        viewText1 = findViewById(R.id.viewText1);
        viewText2 = findViewById(R.id.viewText2);

        // 오늘 날짜를 받게 해주는 Calender 친구들
        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH);
        int cDay = c.get(Calendar.DAY_OF_MONTH);

        // 첫 시작 시에는 오늘 날짜 운동 일기 읽어주기
        checkedDay(cYear, cMonth, cDay);

        // datePick 기능 만들기
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 이미 선택한 날짜에 운동 일기가 있는지 없는지 체크
                checkedDay(year, monthOfYear + 1, dayOfMonth);
            }
        });

        // 저장/수정 버튼 누르면 실행되는 리스너
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fileName을 넣고 저장 시키는 메소드를 호출
                saveExerciseDiary();
            }
        });
    }

    // 운동 일기 파일 읽기
    private void checkedDay(int year, int monthOfYear, int dayOfMonth) {
        // 받은 날짜로 날짜 보여주는
        String dateText = year + " - " + monthOfYear + " - " + dayOfMonth;
        viewText1.setText("오늘 섭취한 칼로리\n" + dateText);
        viewText2.setText("오늘의 운동 일기\n" + dateText);

        // 파일 이름을 만들어준다. 파일 이름은 "20170318_health.txt" 이런식으로 나옴
        String fileNameCalorie = year + "" + monthOfYear + "" + dayOfMonth + "_health.txt";
        String fileNameExercise = year + "" + monthOfYear + "" + dayOfMonth + "_exercise.txt";

        // 읽어봐서 읽어지면 운동 일기 가져오고
        readDiary(fileNameCalorie, calorieEditText);
        readDiary(fileNameExercise, exerciseDiaryEditText);
    }

    // 운동 일기 저장하는 메소드
    private void saveExerciseDiary() {
        // 날짜로 파일 이름 만들기
        int year = datePicker.getYear();
        int month = datePicker.getMonth() + 1; // Month is 0-based, so add 1
        int day = datePicker.getDayOfMonth();
        String fileNameCalorie = year + "" + month + "" + day + "_health.txt";
        String fileNameExercise = year + "" + month + "" + day + "_exercise.txt";

        // 저장
        saveDiary(fileNameCalorie, calorieEditText.getText().toString());
        saveDiary(fileNameExercise, exerciseDiaryEditText.getText().toString());

        Toast.makeText(getApplicationContext(), "건강 모음집 저장됨", Toast.LENGTH_SHORT).show();
    }

    // 일기 파일 읽기
    private void readDiary(String fileName, EditText editText) {
        FileInputStream fis = null;
        try {
            fis = openFileInput(fileName);
            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();
            String str = new String(fileData, "UTF-8");
            editText.setText(str);
        } catch (Exception e) {
            // 없어서 오류가 나면 운동 일기가 없는 것
            editText.setText("");
        }
    }

    // 운동 일기 저장하는 메소드
    private void saveDiary(String fileName, String content) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
