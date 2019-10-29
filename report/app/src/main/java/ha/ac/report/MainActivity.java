package ha.ac.report;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Currency;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    String DB_NAME = "toeic.db";

    String answerWord = "";
    int num = 1;
    int a[] = new int[4]; //랜덤 숫자를 받을 배열
    String b[] = new String[3]; // 랜덤 단어를 받을 배열
    RadioButton r[] = new RadioButton[4];

    public void arrayReset(){ //숫자 배열과 문자 배욜 초기화
        for(int i=0;i<4;i++)
            a[i] = 0;
        for(int i=0;i<3;i++)
            b[i] = "";
    }
    public void radioReset() {//문자열 배열과 라디오 버튼 배열 초기화 시켜주는 부분
        RadioButton r1 = (RadioButton) findViewById(R.id.R1);
        RadioButton r2 = (RadioButton) findViewById(R.id.R2);
        RadioButton r3 = (RadioButton) findViewById(R.id.R3);
        RadioButton r4 = (RadioButton) findViewById(R.id.R4);
        r[0] = r1;
        r[1] = r2;
        r[2] = r3;
        r[3] = r4;
    }

    private void copyDatabase(File dbfile) {
        try {
            String folderPath = "/data/data/" + getPackageName() + "/databases";
            File folder = new File(folderPath);
            if (!folder.exists()) folder.mkdirs();
            InputStream is = getAssets().open(DB_NAME);
            OutputStream os = new FileOutputStream(dbfile);
            byte[] buffer = new byte[1024];
            while (is.read(buffer) > 0) os.write(buffer);
            os.flush();
            is.close();
            os.close();
        } catch (Exception e) {
        }
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File dbFile = getDatabasePath(DB_NAME);
        if (!dbFile.exists()) copyDatabase(dbFile);
        arrayReset();
        radioReset();
        questionRandom();
        putRadio();
        putWord();
        print();
    }

    public String randomWord() { //랜덤으로 단어를 받아와서 단어를 반환시켜주는 부분
        Random rand = new Random();
        int x = rand.nextInt(1100) + 1;
        SQLiteDatabase db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT 단어,해석 FROM 토익 WHERE no='" + x + "';", null);
        TextView text = (TextView) findViewById(R.id.text);
        cursor.moveToNext();
        String word = cursor.getString(0);
        String mean = cursor.getString(1);
        return word;
    }

    public void questionRandom() { //처음 나오는 문제를 랜덤으로 출력하게 하는 부분
        Random rand = new Random();
        int x = rand.nextInt(1100) + 1;
        SQLiteDatabase db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT 단어,해석 FROM 토익 WHERE no='" + x + "';", null);
        TextView text = (TextView) findViewById(R.id.text);
        String result = "";
        cursor.moveToNext();
        String word = cursor.getString(0);
        String mean = cursor.getString(1);
        answerWord = word;
        result += (mean);
        text.setText("(" + num + ")" + result);
    }

    public void putRadio() { //랜덤으로 중복없이 숫자를 받아와서 숫자배열에 삽입하는 부분
        Random random = new Random();
        for (int i = 0; i<4; i++) {
            a[i] = random.nextInt(3);
            for (int j = 0; j < i; j++) {
                if (a[i] == a[j]) {
                    i--;
                }
            }
        }
    }

    public void putWord() { //랜덤으로 단어를 받아오는 함수를 호출하여 랜덤으로 단어를 받아 문자열 배열에 값을 저장 하는 부분
        for (int i = 0; i < 3; i++) {
            b[i] = randomWord();
            for (int j = 0; j < i; j++) {
                if (b[i] == b[j]) {
                    i--;
                }
            }
        }
    }

    public void print(){//랜덤으로 입력된 숫자배열의 숫자에 접근해서 그숫자로 라디오 버튼 배열 접근하고 그리고 랜덤 입력 단어 배열에 접근해서 단어를 출력
        RadioButton radioButton1;
        RadioButton radioButton2;
        RadioButton radioButton3;
        RadioButton radioButton4;
        int su1 = a[0];
        int su2 = a[1];
        int su3 = a[2];
        int su4 = a[3];
        radioButton1 = r[su1];
        radioButton2 = r[su2];
        radioButton3 = r[su3];
        radioButton4 = r[su4];
        radioButton1.setText(answerWord);
        String word1=b[0];
        String word2=b[1];
        String word3=b[2];
        radioButton2.setText(word1);
        radioButton3.setText(word2);
        radioButton4.setText(word3);
    }

    public void answer(View view){
        RadioButton radioButton;
        for(int i=0;i<4;i++){
            radioButton = r[i];
            if(radioButton.isChecked()==true){
                if(radioButton.getText().equals(answerWord)){
                    Toast.makeText(getApplicationContext(), "정답입니다.", Toast.LENGTH_SHORT).show();
                    radioButton.setChecked(false);
                    num++;
                    questionRandom();
                    putRadio();
                    putWord();
                    print();
                }
                else
                    Toast.makeText(getApplicationContext(), "틀렸습니다.", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
