package com.bytedance.videoplayer;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;


public class MainActivity extends AppCompatActivity {
    private VideoView videoView;
    private ProgressBar progressBar;
    private MediaController mediaController;
    private int intPositionWhenPause=-1;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(getVideoPath(R.raw.bytedance));
        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.max);
        mediaController =new MediaController(this);
        videoView.setMediaController(mediaController);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
               {setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);}
               else {setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);}
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
            }
        });

        }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setVideoViewLayoutParams(1);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setVideoViewLayoutParams(2);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        videoView.start();
        videoView.setFocusable(true);
    }
    @Override
    protected  void onPause() {
        super.onPause();
        videoView.pause();
        intPositionWhenPause=videoView.getCurrentPosition();
        videoView.stopPlayback();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (intPositionWhenPause >= 0) {
            videoView.seekTo(intPositionWhenPause);
            intPositionWhenPause = -1;
        }
    }
    public void setVideoViewLayoutParams(int paramsType){

        //全屏模式

        if(1==paramsType) {

            //设置充满整个父布局

            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

            //设置相对于父布局四边对齐

            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            //为VideoView添加属性

            videoView.setLayoutParams(LayoutParams);

        }else{

            //窗口模式

            //获取整个屏幕的宽高

            DisplayMetrics DisplayMetrics=new DisplayMetrics();

            this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);

            //设置窗口模式距离边框50

            int videoHeight=DisplayMetrics.heightPixels-50;

            int videoWidth=DisplayMetrics.widthPixels-50;

            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(videoWidth,videoHeight);

            //设置居中

            LayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

            //为VideoView添加属性

            videoView.setLayoutParams(LayoutParams);

        }

    }

    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }
}
