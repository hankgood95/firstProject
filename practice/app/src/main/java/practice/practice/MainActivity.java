package practice.practice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Random;

public class MainActivity extends AppCompatActivity{
        @Override
    protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            MyView myview = new MyView(this);
            setContentView(myview);
            }
            class MyView extends View{
            public MyView(Context context){
                super(context);
                setBackgroundColor(Color.YELLOW);
            }
            public void onDraw(Canvas canvas){
                Random rand = new Random;
                Bitmap b= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
                canvas.drawBitmap(b,0,0,null);
                int scx = getWidth();
                int scy = getHeight();

                int x = rand.nextInt(scx);
                int y = rand.nextInt(scy);
                int z = rand.nextInt(3)+1;


                int ix=b.getWidth();
                int iy=b.getHeight();
                Rect src = new Rect(0,0,x,y);

                Rect dst = new Rect(x,y,x+ix*z,y+iy*z);
                canvas.drawBitmap(b,src,dst,null);
            }
            }
}



