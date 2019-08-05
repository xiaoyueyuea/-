package com.yl.yymusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;


import com.yl.yymusic.storage.dao.MusicDao;
import com.yl.yymusic.storage.table.Database;
import com.yl.yymusic.storage.table.MusicEntity;
import com.yl.yymusic.ui.AllMusicActivity;
import com.yl.yymusic.ui.HomeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class MyService extends Service {

    public MediaPlayer mediaPlayer;
    private List<MusicEntity> musicList=new ArrayList<>();
    private int currentPosition=0;
    public MusicEntity currentMusic=null;
    private int position;
    private HomeActivity.GetEntity getEntity=new HomeActivity.GetEntity();
    MusicDao dao = Database.getInstance().getMusicDao();
    private int flag=0;

    private MyBinder myBinder=new MyBinder();
    public class MyBinder extends Binder{
        public int playing(){
            if(mediaPlayer.isPlaying()){return 1;}
            else
                return 0;
        }

        public void pauseMusic(){
            mediaPlayer.pause();
        }

        public void startMusic(){
            mediaPlayer.start();
        }

        //上一首
        public void previousMusic(){
            if(currentPosition>0&&musicList.size()!=1){
                currentPosition -=1;
            }else if(musicList.size()==1){
                Toast.makeText(MyService.this, "此歌单仅有一首歌曲", Toast.LENGTH_SHORT).show();
            } else {
                currentPosition=musicList.size()-1;
            }
            play();
        }

        //下一首
        public void nextMusic(){
            if(currentPosition<(musicList.size()-1)&&musicList.size()!=1){
                currentPosition +=1;
            }else if(musicList.size()==1){
                Toast.makeText(MyService.this, "此歌单仅有一首歌曲", Toast.LENGTH_SHORT).show();
            } else{
                currentPosition=0;
            }
            play();
        }

        //是否循环播放
        public int isLooping(){
            if(!mediaPlayer.isLooping()){
                mediaPlayer.setLooping(true);
                return 0;
            } else {
                mediaPlayer.setLooping(false);
                return 1;
            }
        }

        public int cPosition(){
            return currentPosition;
        }//传入当前位置

        public String currentEntityId(){
            return currentMusic.getMusicId();
        }//传入当前音乐实体Id

        public int mFlag(){
            return flag;
        }//返回flag

        public int getMusicDuration()
        {
            int rtn = 0;
            if (mediaPlayer != null)
            {
                rtn = mediaPlayer.getDuration();
            }

            return rtn;
        }
        //获取当前播放进度
        public int getMusicCurrentPosition()
        {
            int rtn = 0;
            if (mediaPlayer != null)
            {
                rtn = mediaPlayer.getCurrentPosition();

            }

            return rtn;
        }

        public void seekTo(int position)
        {
            if (mediaPlayer != null)
            {
                mediaPlayer.seekTo(position);
            }
        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return  myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        position=intent.getIntExtra("position",0);
        int key=intent.getIntExtra("KEY",2);
        String homeKey=intent.getStringExtra("Home_URL");
        if(homeKey!=null){
            mediaPlayer.reset();
            initMediaPlayer(homeKey);
            mediaPlayer.start();
            currentMusic=dao.getFirstMusic();
        }
        switch (key){
            case 0:musicList=(List<MusicEntity>) getEntity.getMusicEntity();break;
            case 2:musicList=dao.findCollectMusic();break;
        }
        if(!mediaPlayer.isPlaying()){
            setCurrentMusic(position);
            mediaPlayer.reset();
            prepareMusic();
            mediaPlayer.start();
        }
        if(mediaPlayer.isPlaying()&&position!=currentPosition){
            setCurrentMusic(position);
            mediaPlayer.reset();
            prepareMusic();
            mediaPlayer.start();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                myBinder.nextMusic();
            }
        });//自动播放下一曲
        return super.onStartCommand(intent, flags, startId);
    }


    //初始化mediaPlayer
    private void initMediaPlayer(String uri){
        try {
            mediaPlayer.setDataSource(uri);
            mediaPlayer.prepare();
            flag=1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(){
        getCurrentMusic();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        mediaPlayer.reset();
        prepareMusic();
        mediaPlayer.start();
    }

    public void setCurrentMusic(int position){
        currentPosition=position;
        getCurrentMusic();
    }

    public void getCurrentMusic(){
        MusicEntity m=musicList.get(currentPosition);
        if(m!=null){
            currentMusic=m;
        }else{
            Log.e("PlayingActivity","为空");
        }
    }

    public void prepareMusic(){
        initMediaPlayer(currentMusic.getUrl());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

}
