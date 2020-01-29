package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button play,pause,stop;
    MediaPlayer media;
    int currentPosition;
    SeekBar songSeek;
    SeekBar volumeBar;
    TextView elapsed;
    TextView remaining;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        stop = (Button) findViewById(R.id.stop);

        elapsed = (TextView) findViewById(R.id.elapsed);
        remaining = (TextView) findViewById(R.id.remaining);


        media = media.create(this,R.raw.nazron);
        media.setLooping(true);
        media.setVolume(0.5f,0.5f);
        totalTime = media.getDuration();
        media.start();

        songSeek = (SeekBar) findViewById(R.id.SeekBar);
        songSeek.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser)
                        {
                            media.seekTo(progress);
                            songSeek.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );


        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress /100f ;
                        media.setVolume(volumeNum , volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while ( media != null )
                {
                    try {
                        Message msg = new Message();
                        msg.what = media.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);

                    }
                    catch (InterruptedException e)
                    {}
                }
            }
        }).start();


    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            int currPosition = msg.what;
            songSeek.setProgress(currPosition);

           String elapsedTime = createTimeLabel(currPosition);
           elapsed.setText(elapsedTime);

           String remainingTime = createTimeLabel(totalTime - currPosition);
           remaining.setText("- " + remainingTime);

        }
    };

    public String createTimeLabel(int time)
    {
        String timeLabel = "";
        int min = time / 1000 / 60 ;
        int sec = time / 1000 % 60 ;

        timeLabel = min + ":";
        if(sec < 10)
        {
            timeLabel += "0";

        }
        timeLabel += sec;

        return timeLabel;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.play:
                if(media == null)
                {
                    media = media.create(getApplicationContext(),R.raw.nazron);
                    media.setLooping(true);
                    media.setVolume(0.5f,0.5f);
                    totalTime = media.getDuration();
                    media.start();
                }

                else if(! media.isPlaying())
                {
                    media.seekTo(currentPosition);
                    media.start();
                }
            break;

            case R.id.pause:
                if(media != null)
                {
                    media.pause();
                    currentPosition = media.getCurrentPosition();

                }
                break;

            case R.id.stop:
                if(media != null)
                {
                    media.stop();
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    media = null;
                }
                break;
        }
    }
}
