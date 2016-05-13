package com.turingking.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SlidingDetailsLayout slidingDetailsLayout;
    private TextView tishi,lilv;

    private Button puy;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slidingDetailsLayout = (SlidingDetailsLayout) findViewById(R.id.slidingDetailsLayout);
        tishi = (TextView) findViewById(R.id.tishi);
        lilv = (TextView) findViewById(R.id.lilv);

        puy = (Button) findViewById(R.id.puy);
        puy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lilv.setText(""+i++);
            }
        });

        tishi.setText("上拉查看详情");
        slidingDetailsLayout.setPositionChangListener(new SlidingDetailsLayout.PositionChangListener() {
            @Override
            public void position(int positon) {
                if(positon==0){
                    tishi.setText("上拉查看详情");

                }else{
                    tishi.setText("下拉返回主页");

                }
            }

            @Override
            public void onBottom() {
                tishi.setText("松开查看详情");
            }

            @Override
            public void backBottom() {
                tishi.setText("上拉查看详情");
            }

            @Override
            public void onTop() {
                tishi.setText("松开返回主页");
            }

            @Override
            public void backTop() {
                tishi.setText("下拉返回主页");
            }
        });

    }
}
