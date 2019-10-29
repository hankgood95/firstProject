package wook.practice1;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainFragment extends Fragment {
    private TextView txtTimeDay, txtTimeHour, txtTimeMinute, txtTImeSecond;
    private Handler handler;
    private Runnable runnable;
    Calendar calendar;
    Date date;
    Date futureDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        txtTimeDay = (TextView) view.findViewById(R.id.txtTimerDay);
        txtTimeHour = (TextView) view.findViewById(R.id.txtTimerHour);
        txtTimeMinute = (TextView) view.findViewById(R.id.txtTimerMinute);
        txtTImeSecond = (TextView) view.findViewById(R.id.txtTimerSecond);
        calendar = calendar.getInstance();
        date = new Date();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(day==7){
            calendar.add(Calendar.DATE,7);
        }
        else {
            calendar.add(Calendar.DATE,7-day);
        }
        futureDate = new Date(calendar.getTimeInMillis());
        countDownStart();
        return view;
    }

    public void countDownStart() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date currentDate = new Date();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime() - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        txtTimeDay.setText("" + String.format("%02d", days));
                        txtTimeHour.setText("" + String.format("%02d", hours));
                        txtTimeMinute.setText("" + String.format("%02d", minutes));
                        txtTImeSecond.setText("" + String.format("%02d", seconds));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        handler.postDelayed(runnable, 1 * 1000);
    }
}
