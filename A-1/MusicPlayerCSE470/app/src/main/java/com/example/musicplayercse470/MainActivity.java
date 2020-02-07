package com.example.musicplayercse470;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView timeElapsedLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = (Button) findViewById(R.id.playBtn);
        timeElapsedLabel = (TextView) findViewById(R.id.timeElapsedLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

        //media player

        mp = MediaPlayer.create(this, R.raw.music);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();

        //positionBar

        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mp.seekTo(progress);
                    positionBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Volume Bar

        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumeNum = progress/10f;
                mp.setVolume(volumeNum, volumeNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Thread (Update positionBar & TimeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mp!=null){
                    try{
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);

                        Thread.sleep(1000);
                    } catch(InterruptedException e){}
                }
            }
        }).start();
    }

    private Handler handler = new Handler(){
        @Override
                public void handleMessage(Message msg){
                int currentPosition = msg.what;
                //Update positionBar
                positionBar.setProgress(currentPosition);

                //UpdateLabels
            String elapsedTime = createTimeLabel(currentPosition);
            timeElapsedLabel.setText(elapsedTime);
            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("-"+ remainingTime);
        }
    };

    public String createTimeLabel(int time){
        String timeLabel = "";
        int min = time / 1000 / 60 ;
        int sec = time / 1000 % 60;

        timeLabel = min+":";
        if (sec<10){
            timeLabel +=0;
            timeLabel+=sec;

        }
        return timeLabel;
    }
    public void playBtnClick(View view){
        if(!mp.isPlaying()){
            //Stopping
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);

        }
        else{
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }
}
