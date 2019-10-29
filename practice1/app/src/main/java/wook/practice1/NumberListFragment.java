package wook.practice1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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


public class NumberListFragment extends Fragment {

    String DB_NAME = "lotto.db"; //디비 파일을 저장 하는 부분

    public static String site0 = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=";
    public static String num1;
    public static String site1;
    //int su2;

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

    Button bring;
    Button put;
    public static TextView putin;
    TextView show;
    int count;

    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_numberlist, container, false);
        //final SharedPreferences su = getActivity().getSharedPreferences("FILE", getActivity().MODE_PRIVATE);
        //SharedPreferences.Editor e = su.edit();
        //su2 = su.getInt("su", 0); //공유프레퍼런스


        File dbFile = getContext().getDatabasePath(DB_NAME);
        if (!dbFile.exists()) copyDatabase(dbFile);


        final EditText editText = (EditText) view.findViewById(R.id.edit);// 입력된 로또 회차 숫자 읽는 부분
        put = (Button) view.findViewById(R.id.put);//버튼 누를시에 횟차랑 url이랑 합치는 부분\
        bring = (Button) view.findViewById(R.id.search);
        putin = (TextView) view.findViewById(R.id.firstnumber);
        show = (TextView)view.findViewById(R.id.mynumberlist); //로또 당첨 번호가 입력 되는 text VIew

        /*final ListView listView = (ListView) view.findViewById(R.id.numberlist);
        final ArrayList<String> thelist1 = new ArrayList<>();*/


        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num1 = editText.getText().toString();
                site1 = site0.concat(num1);
                Toast.makeText(getContext(), "html메시시를 수신했습니다.", Toast.LENGTH_SHORT).show();
            }
        }); //html수신 해오는 부분


        bring.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

               // su.edit().putInt("num",su2).apply(); //su2를 공유프레퍼런스 변수인 su한테 보낸다

                task process = new task();
                process.execute();

                count = Integer.parseInt(num1);
                SQLiteDatabase db = getActivity().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
                Cursor cursor = db.rawQuery("SELECT 당첨번호,회차  FROM lotto WHERE 회차 like'" + count + "%';", null);
                if (cursor.getCount() > 0) {
                    String result = "";
                    while(cursor.moveToNext()) {
                        String strnum1 = cursor.getString(0);
                        int strnum2 = cursor.getInt(1);
                        result += (strnum2 + "회차 : " + strnum1 + "\n");
                }
                    show.setText(result);
                    /*thelist1.add(result);
                    ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, thelist1);
                    listView.setAdapter(listAdapter);*/
                } else Toast.makeText(getContext(), "없습니다", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
