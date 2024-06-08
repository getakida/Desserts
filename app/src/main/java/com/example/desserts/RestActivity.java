package com.example.desserts;

import android.content.Context;
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

public class RestActivity extends AppCompatActivity {
    DatePicker datePicker;
    EditText edtSong, edtQuote;
    Button btnSave;
    TextView viewText1, viewText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);
        setTitle("휴식 모음집 앱");

        // 뷰에 있는 위젯들 리턴 받아두기
        datePicker = findViewById(R.id.datePicker);
        edtSong = findViewById(R.id.edtSong);
        edtQuote = findViewById(R.id.edtQuote);
        btnSave = findViewById(R.id.btnSave);
        viewText1 = findViewById(R.id.viewText1);
        viewText2 = findViewById(R.id.viewText2);

        // 오늘 날짜를 받게 해주는 Calender 친구들
        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH);
        int cDay = c.get(Calendar.DAY_OF_MONTH);

        // 첫 시작 시에는 오늘 날짜 휴식 일기 읽어주기
        checkedDay(cYear, cMonth, cDay);

        // datePick 기능 만들기
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 이미 선택한 날짜에 휴식 일기가 있는지 없는지 체크
                checkedDay(year, monthOfYear + 1, dayOfMonth);
            }
        });

        // 저장/수정 버튼 누르면 실행되는 리스너
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fileName을 넣고 저장 시키는 메소드를 호출
                saveRestDiary();
            }
        });
    }

    // 휴식 일기 파일 읽기
    private void checkedDay(int year, int monthOfYear, int dayOfMonth) {
        // 받은 날짜로 날짜 보여주는
        String dateText = year + " - " + monthOfYear + " - " + dayOfMonth;
        viewText1.setText("오늘 들었던 노래 중에 힐링된 노래\n" + dateText);
        viewText2.setText("오늘 내가 들었던 말 중에 가장 좋았던 말\n" + dateText);

        // 파일 이름을 만들어준다. 파일 이름은 "20170318_rest.txt" 이런식으로 나옴
        String fileNameSong = year + "" + monthOfYear + "" + dayOfMonth + "_rest.txt";
        String fileNameQuote = year + "" + monthOfYear + "" + dayOfMonth + "_quote.txt";

        // 읽어봐서 읽어지면 휴식 일기 가져오고
        readDiary(fileNameSong, edtSong);
        readDiary(fileNameQuote, edtQuote);
    }

    // 휴식 일기 저장하는 메소드
    private void saveRestDiary() {
        // 날짜로 파일 이름 만들기
        int year = datePicker.getYear();
        int month = datePicker.getMonth() + 1; // Month is 0-based, so add 1
        int day = datePicker.getDayOfMonth();
        String fileNameSong = year + "" + month + "" + day + "_rest.txt";
        String fileNameQuote = year + "" + month + "" + day + "_quote.txt";

        // 저장
        saveDiary(fileNameSong, edtSong.getText().toString());
        saveDiary(fileNameQuote, edtQuote.getText().toString());

        Toast.makeText(getApplicationContext(), "휴식 모음집 저장됨", Toast.LENGTH_SHORT).show();
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
            // 없어서 오류가 나면 휴식 일기가 없는 것
            editText.setText("");
        }
    }

    // 휴식 일기 저장하는 메소드
    private void saveDiary(String fileName, String content) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
