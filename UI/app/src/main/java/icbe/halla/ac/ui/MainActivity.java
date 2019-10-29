package icbe.halla.ac.ui;

        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.RadioButton;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.util.Random;

public class MainActivity extends AppCompatActivity {
    String DB_NAME = "toeic.db";

    String answerWord = ""; //정답을 넣은 문자열 변수
    int num = 1;//문제번호
    int a[] = new int[4]; //랜덤 숫자를 받을 배열
    String b[] = new String[4]; // 랜덤 단어를 받을 배열 하지만 항상 첫번째 칸에는 정답이 들어가 있다.
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
        RadioButton r4 = (RadioButton)findViewById(R.id.R4);
        r[0] = r1;
        r[1] = r2;
        r[2] = r3;
        r[3] = r4;
    }

    private void copyDatabase(File dbfile) { //DB에 있는 파일 가져와서 넣는 부분
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
        showNum();
        //putWord();
        showWord();
        //print();
    }

    public String randomWord() { //랜덤으로 단어를 받아와서 단어를 반환시켜주는 부분
        Random rand = new Random();
        int x = rand.nextInt(1100) + 1;
        SQLiteDatabase db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT 단어,해석 FROM 토익 WHERE no='" + x + "';", null);
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

    public void putRadio(){
       Random random = new Random();
        //RadioButton radioButton;

        for (int i = 0; i <4; i++) { //문자열 배열에 랜덤으로 단어를 집어 넣는 부분
            if(i==0){
                b[i]=answerWord;
            }
            else {
                b[i] = randomWord();
                for (int j = 0; j < i; j++) {
                    if (b[i] == b[j]) {
                        i--;
                    }
                }
            }
        }

        for (int i = 0; i<4; i++) { //정수형 배열에 랜덤으로 단어를 집어 넣는 부분.
            a[i]= random.nextInt(3);
            for(int j=0;j<i;j++){
                if(a[i]==a[j]){
                    i--;
                }
            }
        }
        for(int i=0;i<4;i++){ //정수형 배열에서 숫자 가져오고 문자열 배열에서 문자열 가져와서 라디오배열에 가지고온숫자 위치에다가 가져온 단어를 집어 넣는다.
            int su = a[i];
            String word = b[i];
            r[su].setText(word);
            /*radioButton = r[su];
            radioButton.setText(r[su]);*/
        }
    }

    public void showNum(){ //랜덩으로 수 부여하고 숫자 확인하는 부분.
        TextView example = (TextView)findViewById(R.id.show2);
        String num = "";
        for(int i=0; i<4;i++){
            num+=(a[i]+" ");
        }
        example.setText(num);
    }

    /*public void putWord() { //랜덤으로 단어를 받아오는 함수를 호출하여 랜덤으로 단어를 받아 문자열 배열에 값을 저장 하는 부분
        for (int i = 0; i <4; i++) {
            if(i==0){
                b[i]=answerWord;
            }
            else {
                b[i] = randomWord();
                for (int j = 0; j < i; j++) {
                    if (b[i] == b[j]) {
                        i--;
                    }
                }
            }
        }
    }*/

    public void showWord(){ //랜덤으로 들어간 단어 확인하는 부분
        TextView example = (TextView)findViewById(R.id.show1);
        String show = "";
        for(int i=0; i<4;i++){
            show+=(b[i]+" ");
        }
        example.setText(show);
    }



    public void answer(View view){ //확인 버튼 눌렀을때 정답인지 아닌지 체크하고 아니면 정답 나올떄까지 반복 정답 나오면 라디오버튼 해제하고 문제 다시 랜덤으로 받고다시다 받는부분
        RadioButton radioButton;
        for(int i=0;i<4;i++){
            radioButton = r[i];
            if(radioButton.isChecked()==true){
                if(radioButton.getText().equals(answerWord)){
                    Toast.makeText(getApplicationContext(), "정답입니다.", Toast.LENGTH_SHORT).show();
                    r[i].setChecked(false);
                    num++;
                    questionRandom();
                    putRadio();
                    showNum();
                    //putWord();
                    showWord();
                    //print();
                }
                else
                    Toast.makeText(getApplicationContext(), "틀렸습니다.", Toast.LENGTH_SHORT).show();

            }
        }
    }
}

