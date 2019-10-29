package wook.practice1;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/*class WorkerThread extends Thread {
    Handler handler;

    WorkerThread(Handler h) {
        handler = h;
    }

    public void run() {
        try {

        } catch (Exception e) {
        }
    }
}*/


public class StoreFragment extends Fragment {
    Button store;//버튼 누르면 db에 값을 저장
    String DB_NAME = "lotto.db";
    int su1 = 0;//수를 올려주는 부분


    private void copyDatabase(File dbfile) {
        try {
            String folderPath = "/data/data/" + getActivity().getPackageName() + "/databases";
            File folder = new File(folderPath);
            if (!folder.exists()) folder.mkdirs();
            InputStream is = getContext().getAssets().open(DB_NAME);
            OutputStream os = new FileOutputStream(dbfile);
            byte[] buffer = new byte[1024];
            while (is.read(buffer) > 0) os.write(buffer);
            os.flush();
            is.close();
            os.close();
        } catch (Exception e) {
        }
    }//lotto DB를 읽어 오는 부분

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int check = 0;

        final SharedPreferences su = getActivity().getSharedPreferences("FILE", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = su.edit();//공유프레퍼런스
        su1 = su.getInt("num", 0);

        View view = inflater.inflate(R.layout.fragment_numberput, container, false);
        File dbFile = getContext().getDatabasePath(DB_NAME);
        if (!dbFile.exists()) copyDatabase(dbFile);
        final SQLiteDatabase db = getActivity().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);


        final ListView listView = (ListView) view.findViewById(R.id.listview);
        final ArrayList<String> thelist = new ArrayList<>();//리스트뷰에 값을 집어 넣기 위해서 선언한것
        store = (Button) view.findViewById(R.id.storeButton);

        final EditText editText1 = (EditText) view.findViewById(R.id.count);
        final EditText editText2 = (EditText) view.findViewById(R.id.num1);
        final EditText editText3 = (EditText) view.findViewById(R.id.num2);
        final EditText editText4 = (EditText) view.findViewById(R.id.num3);
        final EditText editText5 = (EditText) view.findViewById(R.id.num4);
        final EditText editText6 = (EditText) view.findViewById(R.id.num5);
        final EditText editText7 = (EditText) view.findViewById(R.id.num6);


        Cursor cursor = db.rawQuery("SELECT 당첨번호,회차  FROM lotto", null);
        if (cursor.getCount() > 0) {
            String result = "";
            while (cursor.moveToNext()) {
                String strnum1 = cursor.getString(0);
                int strnum2 = cursor.getInt(1);
                result = (strnum2 + "회차 : " + strnum1 + "\n");
                thelist.add(result);
                ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, thelist);
                listView.setAdapter(listAdapter);
            }
        } //항상 리스트뷰 안에 db와 연동 시켜서 안에 있는 모든것들을 리스트 뷰에 출력시키는 부분
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //공유프레퍼런스인 su에다가 su1값을 대입 하는 부분
                String num1 = editText1.getText().toString();
                String num2 = editText2.getText().toString();
                String num3 = editText3.getText().toString();
                String num4 = editText4.getText().toString();
                String num5 = editText5.getText().toString();
                String num6 = editText6.getText().toString();
                String num7 = editText7.getText().toString();
                String finalNum = num2.concat(" ").concat(num3).concat(" ").concat(num4).concat(" ").concat(num5).concat(" ").concat(num6).concat(" ").concat(num7);
                // 여기는 edit text에 있는 값들을 읽어 온후 그 번호들을 string으로 묶는 부분 num1은 추첨회차 번호 이고 num2부터 num7까지는 복권 번호이다.
                // SQLiteDatabase db = getActivity().openOrCreateDatabase(DB_NAME,Context.MODE_PRIVATE,null);
                db.execSQL("INSERT INTO lotto VALUES('" + finalNum + "','" + num1 + "');"); //db에 값을 집어 넣은 부분
                Toast.makeText(getContext(), "추가됨", Toast.LENGTH_SHORT).show();

                Cursor cursor = db.rawQuery("SELECT 당첨번호,회차  FROM lotto", null); //여기서 부터는 데이터베이스에 접근후에 그값을 리스트 뷰에 출력하는 부분
                if (cursor.getCount() > 0) { //db에 값이 비어 있지 않으면 들어가서 접근한다.
                    String result = "";
                    cursor.moveToPosition(su1); //이건 그 행에 들어가서 접근 하는 부분
                    String strnum1 = cursor.getString(0);
                    int strnum2 = cursor.getInt(1);
                    result = (strnum2 + "회차 : " + strnum1 + "\n");
                    thelist.add(result);
                    ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, thelist);
                    listView.setAdapter(listAdapter);
                } else Toast.makeText(getContext(), "없습니다", Toast.LENGTH_SHORT).show();
                su1++; // su1을 증가시켜서 접근할 부분 만 접근 하는 부분
                su.edit().putInt("num", su1).apply();//공유프레퍼런스인 su에다가 su1값을 대입 하는 부분
            }
        });

        return view;
    }


}
