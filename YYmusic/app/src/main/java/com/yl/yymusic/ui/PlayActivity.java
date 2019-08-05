package com.yl.yymusic.ui;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yl.yymusic.R;
import com.yl.yymusic.service.MyService;
import com.yl.yymusic.storage.dao.MusicDao;
import com.yl.yymusic.storage.table.Database;
import com.yl.yymusic.storage.table.MusicEntity;
import com.yl.yymusic.util.CircleRotateView;
import com.yl.yymusic.util.TurnImage;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener{

    private MyService.MyBinder myBinder;
    private ImageButton playButton;
    private ImageButton upButton;
    private ImageButton nextButton;
    private TextView musicName;
    private TextView singerName;
    private CircleRotateView albumImage;
    private SeekBar seekBar;
    private int max=10000;
    private int currentMusicPosition=0;//媒体音乐所播放的位置
    private ImageView recycleStyleButton;
    private ImageView currentListButton;
    private ImageView collectButton;
    private ImageView optionButton;
    private int upPosition=0;
    private int nextPosition=0;
    private HomeActivity.GetEntity getEntity=new HomeActivity.GetEntity();
    private MusicEntity musicEntity;
    private ObjectAnimator mAnimator;
    MusicDao dao = Database.getInstance().getMusicDao();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play_layout);
        init();
        connectService();
        getInformation();
        rotateAnim();
        mAnimator.start();//开始动画
    }

    private void init(){
        playButton=findViewById(R.id.btn_play);
        playButton.setOnClickListener(this);
        upButton=findViewById(R.id.btn_up);
        upButton.setOnClickListener(this);
        nextButton=findViewById(R.id.btn_next);
        nextButton.setOnClickListener(this);
        musicName=findViewById(R.id.txt_playLayout_name);
        singerName=findViewById(R.id.txt_playLayout_singer);
        albumImage =findViewById(R.id.img_playLayout);
        seekBar=findViewById(R.id.seekBar_playLayout);
        recycleStyleButton=findViewById(R.id.btn_style_recycle);
        recycleStyleButton.setOnClickListener(this);
        currentListButton=findViewById(R.id.btn_current_list);
        currentListButton.setOnClickListener(this);
        collectButton=findViewById(R.id.btn_collect);
        collectButton.setOnClickListener(this);
        optionButton=findViewById(R.id.btn_option);
        optionButton.setOnClickListener(this);
    }


    private void getInformation(){
        Intent intent=getIntent();
        byte[] image=intent.getByteArrayExtra("ALBUM_IMAGE");
        String name= intent.getStringExtra("MUSIC_NAME");
        String singer=intent.getStringExtra("SINGER_NAME");
        int key=intent.getIntExtra("KEY",0);
        switch (key){
            case 0:musicEntity=getEntity.getMusicEntity().get(intent.getIntExtra("position",0));break;
            case 2:musicEntity=dao.findCollectMusic().get(intent.getIntExtra("position",0));break;
        }
        musicName.setText(name);
        singerName.setText(singer);
        albumImage.setImageBitmap(TurnImage.byteToBitmap(image,PlayActivity.this));
        int c= dao.isCollect(musicEntity.getMusicId());
        if(c==1){
            collectButton.setImageResource(R.mipmap.collect_yes);
        }
        else{
            collectButton.setImageResource(R.mipmap.collect_no);
        }

    }//设置初始信息

    private void connectService(){
        Intent bindIntent=new Intent(PlayActivity.this,MyService.class);
        bindService(bindIntent,connection,BIND_AUTO_CREATE);
    }

    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder=(MyService.MyBinder) service;
            seekBarOperate();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void rotateAnim(){
        mAnimator = ObjectAnimator.ofFloat(albumImage, "rotation", 0.0f, 360.0f);
        mAnimator.setDuration(12000);//设定转一圈的时间
        mAnimator.setRepeatCount(Animation.INFINITE);//设定无限循环
        mAnimator.setRepeatMode(ObjectAnimator.RESTART);// 循环模式
        mAnimator.setInterpolator(new LinearInterpolator());// 匀速
    }//动画

    public static void goPlayActivity(Context context) {
        Intent intent = new Intent(context, PlayActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        mAnimator.cancel();
    }

    public void setInformation(int position){
        Intent intent = new Intent();
        int key=intent.getIntExtra("KEY",0);
        switch (key){
            case 0:musicEntity=getEntity.getMusicEntity().get(position);break;
            case 2:musicEntity=dao.findCollectMusic().get(position);break;
        }
        musicName.setText(musicEntity.getMusicName());
        singerName.setText(musicEntity.getSinger());
        albumImage.setImageBitmap(TurnImage.byteToBitmap(musicEntity.getMusicImage(),PlayActivity.this));
        int c= dao.isCollect(musicEntity.getMusicId());
        if(c==1){
            collectButton.setImageResource(R.mipmap.collect_yes);
        }
        else{
            collectButton.setImageResource(R.mipmap.collect_no);
        }
    }//上一首下一首更新信息

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:
                if(myBinder.playing()==1){
                    myBinder.pauseMusic();
                    playButton.setImageResource(R.mipmap.playbutton);
                    mAnimator.pause();
                }else {
                    myBinder.startMusic();
                    playButton.setImageResource(R.mipmap.pausebutton);
                    mAnimator.resume();
                }
                break;
            case R.id.btn_up:
                if(myBinder.playing()==0){
                    playButton.setImageResource(R.mipmap.pausebutton);
                }
                myBinder.previousMusic();
                upPosition=myBinder.cPosition();
                setInformation(upPosition);
                mAnimator.start();
                break;
            case R.id.btn_next:
                if(myBinder.playing()==0){
                    playButton.setImageResource(R.mipmap.pausebutton);
                }
                myBinder.nextMusic();
                nextPosition=myBinder.cPosition();
                setInformation(nextPosition);
                mAnimator.start();
                break;
            case R.id.btn_option:
                optionDialog();
                break;
            case R.id.btn_collect:
                int c= dao.isCollect(musicEntity.getMusicId());
                if(c==1){
                    collectButton.setImageResource(R.mipmap.collect_no);
                    updateCollect(0);
                }
                else{
                    collectButton.setImageResource(R.mipmap.collect_yes);
                    updateCollect(1);
                }
                break;
            case R.id.btn_style_recycle:
                if(myBinder.isLooping()==0){
                    recycleStyleButton.setImageResource(R.mipmap.one_recyle);
                } else {
                    recycleStyleButton.setImageResource(R.mipmap.list_recycle);
                }
                break;
        }
    }

    private void optionDialog(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.dialog_play_operate);
        bottomSheetDialog.show();
    }

    private void updateCollect(int isCollect){
        MusicEntity musicEntity1=dao.getEntityByMusicId(musicEntity.getMusicId());
        musicEntity1.setCollect(isCollect);
        dao.updateCollectMusic(musicEntity1);
    }//更新收藏信息

    private void seekBarOperate(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentMusicPosition=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myBinder.seekTo((int)(((double)currentMusicPosition/100)*myBinder.getMusicDuration()));
            }
        });
    }
}
