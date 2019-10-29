package wook.practice1;


import android.os.AsyncTask;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class task extends AsyncTask<Void, Void, Void>{
    String data=""; //파싱받아온 값 저장 하는 부분
    String num=""; // json값 읽어 들여서 값을 저장 하는 부분
    String date1="";//날짜 값 받아 오는 부분
    int bonus; //보너스 값 받아 오는 부분
    int count; // 조회 부분에 쓰이기 때문에
    String site = NumberListFragment.site1; //url 사이트 주소를 받아 오는 부분
    @Override

    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL(site);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader  bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line="";

            while(line !=null){
                line = bufferedReader.readLine(); //line에 해당 사이트에 있는 값을 가져 오는 부분
                data = data + line; //data 에 받아온 값을 저장 하는 부분
            }
            JSONObject jo= new JSONObject(data);
            bonus=jo.getInt("bnusNo");
            for(int i=1;i<=6;i++){
                num += ("  "+jo.getString("drwtNo"+i)); //당첨숫자 집어 넣는 부분
            }

            date1 = ("                       "+jo.getString("drwNoDate")); //추첨일자 집어 넣는 부분
            count =jo.getInt("drwNo"); //추첨회차 집어 넣는 부분

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        NumberListFragment.putin.setText("\n\n\n"+this.num+"+(보너스 번호)"+this.bonus+"\n"+this.date1+"\n"+"                             "+this.count+"회");
    }
}
