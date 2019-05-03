package com.example.mysic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class playerActivity extends AppCompatActivity {
    Button next, previous, pause;
    TextView songLabel;
    SeekBar sb;
    static MediaPlayer myMediaPayer;
    int position;
    String sname;
    ArrayList<File> mysongs;
    Thread updateSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        next = (Button) findViewById(R.id.next);
        pause = (Button) findViewById(R.id.pause);
        previous = (Button) findViewById(R.id.previous);

        songLabel = (TextView) findViewById(R.id.songLabel);
        sb = (SeekBar) findViewById(R.id.seekBar);


        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = myMediaPayer.getDuration();
                int currentPosition = 0;

                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = myMediaPayer.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                }
            }

        };

        if (myMediaPayer != null) {
            myMediaPayer.stop();
            myMediaPayer.release();
        }
        Intent i= getIntent();
        Bundle bundle=i.getExtras();

        mysongs=(ArrayList)bundle.getParcelableArrayList("songs");
      sname= mysongs.get(position).getName().toString();
      String songName= i.getStringExtra("songname");
      songLabel.setText(songName);
      songLabel.setSelected(true);
      position= bundle.getInt("pos",0);
      Uri u = Uri.parse(mysongs.get(position).toString());

     myMediaPayer= MediaPlayer.create(getApplicationContext(),u);

     myMediaPayer.start();
     sb.setMax(myMediaPayer.getDuration());
     sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
         @Override
         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

         }

         @Override
         public void onStartTrackingTouch(SeekBar seekBar) {

         }

         @Override
         public void onStopTrackingTouch(SeekBar seekBar) {
           myMediaPayer.seekTo(sb.getProgress());

         }
     });

     pause.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             sb.setMax(myMediaPayer.getDuration());
             if(myMediaPayer.isPlaying())
             {
                 pause.setBackgroundResource(R.drawable.play);
                 myMediaPayer.pause();
             }
             else
             {
                 pause.setBackgroundResource(R.drawable.pause);
                 myMediaPayer.start();
             }
         }
     });


     next.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             myMediaPayer.stop();
             myMediaPayer.release();
             position=(position+1)%mysongs.size();
             Uri u= Uri.parse(mysongs.get(position).toString());
             myMediaPayer= MediaPlayer.create(getApplicationContext(),u);
             sname=mysongs.get(position).getName().toString();
             songLabel.setText(sname);
             myMediaPayer.start();

         }
     });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPayer.stop();
                myMediaPayer.release();

                position=(position-1<0)? (mysongs.size()-1): (position-1);
                Uri u= Uri.parse(mysongs.get(position).toString());
                myMediaPayer= MediaPlayer.create(getApplicationContext(),u);
                sname=mysongs.get(position).getName().toString();
                songLabel.setText(sname);
                myMediaPayer.start();

            }
        });





    }
}
