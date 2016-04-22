package com.turingking.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SlidingDetailsLayout slidingDetailsLayout;
    private TextView tishi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slidingDetailsLayout = (SlidingDetailsLayout) findViewById(R.id.slidingDetailsLayout);
        tishi = (TextView) findViewById(R.id.tishi);

        slidingDetailsLayout.setPositionChangListener(new SlidingDetailsLayout.PositionChangListener() {
            @Override
            public void position(int positon) {
                if(positon==0){
                    tishi.setText("上拉查看更多");
                }else{
                    tishi.setText("下拉返回详情");
                }
            }
        });

    }
}
