package com.developer.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Music_player extends AppCompatActivity {
    TextView music_titleTv,currentTimeTv ,totalTimeTv;
    SeekBar seekBar;
    ImageView music_image,nextbtn,pause_playbtn,previousbtn;

    ArrayList<AudioStructure>songlist ;
    AudioStructure currentSong;
    MediaPlayer mediaPlayer = MusicPlayer.getInstance();
    int x=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);

        music_titleTv = findViewById(R.id.song_title);
        currentTimeTv = findViewById(R.id.cureent_time);
        totalTimeTv = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        music_image = findViewById(R.id.music_image);
        nextbtn = findViewById(R.id.next);
        pause_playbtn = findViewById(R.id.pause_play);
        previousbtn = findViewById(R.id.previous);

        music_titleTv.setSelected(true);
        songlist = (ArrayList<AudioStructure>) getIntent().getSerializableExtra("List");

        setResourcesWithMusic();


    }


    void setResourcesWithMusic(){
        currentSong = songlist.get(MusicPlayer.currentIndex);
        music_titleTv.setText(currentSong.getTitle());
        totalTimeTv.setText(convertToMMS(currentSong.getDuration()));

        pause_playbtn.setOnClickListener(v -> pausePlay());
        nextbtn.setOnClickListener(v -> Play_NextMusic());
        previousbtn.setOnClickListener(v -> Play_PreviousMusic());

        Play_music();
    }

    private void Play_music(){
      mediaPlayer.reset();
      try {
          mediaPlayer.setDataSource(currentSong.getPath());
          mediaPlayer.prepare();
          mediaPlayer.start();
          seekBar.setProgress(0);
          seekBar.setMax(mediaPlayer.getDuration());
      } catch (IOException e){
          e.printStackTrace();
      }

    }

    private void Play_NextMusic(){
        if (MusicPlayer.currentIndex == songlist.size() -1)
            return;

        MusicPlayer.currentIndex +=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

        Music_player.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeTv.setText(convertToMMS(mediaPlayer.getCurrentPosition()+""));

                    if (mediaPlayer.isPlaying()){
                        pause_playbtn.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                        music_image.setRotation(x++);
                    }else {
                        pause_playbtn.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        music_image.setRotation(0);
                    }
                }

                new Handler().postDelayed(this,100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void Play_PreviousMusic(){
        if (MusicPlayer.currentIndex == 0)
            return;

        MusicPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void pausePlay(){

        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }


    public static String convertToMMS(String duration){
        Long mills = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(mills) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(mills) % TimeUnit.MINUTES.toSeconds(1));
    }

}