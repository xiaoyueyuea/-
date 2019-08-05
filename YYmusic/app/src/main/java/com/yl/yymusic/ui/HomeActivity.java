package com.yl.yymusic.ui;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yl.yymusic.R;
import com.yl.yymusic.service.MyService;
import com.yl.yymusic.storage.dao.MusicDao;
import com.yl.yymusic.storage.dao.MusicListDao;
import com.yl.yymusic.storage.table.Database;
import com.yl.yymusic.storage.table.ListEntity;
import com.yl.yymusic.storage.table.MusicEntity;
import com.yl.yymusic.util.NewMusicListAdapter;
import com.yl.yymusic.util.TurnImage;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static List<MusicEntity> musicList = new ArrayList<>();
    private ImageButton home_imageButton;
    private TextView home_nameText;
    private TextView home_singerText;
    private ImageButton home_playButton;
    private ImageButton home_findButton;
    private ImageButton home_listButton;
    private ImageButton home_listOperateButton;
    private ListView musicListView;
    private NewMusicListAdapter newMusicListAdapter;
    private List<ListEntity> list = new ArrayList<>();
    private int playTimes = 0;
    private int isCollect = 0;
    private long recentlyTime = 0;
    private MyService.MyBinder myBinder;
    private MusicEntity currentMusicEntity;
    MusicDao dao = Database.getInstance().getMusicDao();
    MusicListDao listDao = Database.getInstance().getMusicListDao();

    public static class GetEntity {
        public List<MusicEntity> getMusicEntity() {
            return musicList;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        connectService();
        init();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            getAllMusic();
            setHomeInformation();
        }

        list = listDao.getAllMusicList();
        if (list != null) {
            newMusicListAdapter = new NewMusicListAdapter(HomeActivity.this, R.layout.item_new_list, list);
            musicListView.setAdapter(newMusicListAdapter);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setHomeInformation();
    }

    public void init() {
        drawerLayout = findViewById(R.id.home_na);
        navigationView = findViewById(R.id.nav);
        home_imageButton = findViewById(R.id.btn_clickImage);
        home_imageButton.setOnClickListener(this);
        home_nameText = findViewById(R.id.txt_home_musicTitle);
        home_singerText = findViewById(R.id.txt_home_musicSinger);
        home_findButton = findViewById(R.id.btn_home_reserach);
        home_findButton.setOnClickListener(this);
        home_playButton = findViewById(R.id.btn_home_play);
        home_playButton.setOnClickListener(this);
        home_listButton = findViewById(R.id.btn_home_list);
        home_listButton.setOnClickListener(this);
        musicListView = findViewById(R.id.newMusicList);
        home_listOperateButton = findViewById(R.id.item_home_option);
    }

    public void getAllMusic() {
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String uri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int time = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                MusicEntity m = new MusicEntity(album_id, name, singer, playTimes, TurnImage.bitmapToByte(getAlbumArt(album_id, HomeActivity.this)), isCollect, time, recentlyTime, uri);
                musicList.add(m);
                if (!dao.findMusic(album_id)) {
                    dao.addMusic(m);
                }
            }
        }
        cursor.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAllMusic();
                    setHomeInformation();
                } else {
                    Toast.makeText(this, "你没有打开相应权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private Bitmap getAlbumArt(String album_id, Context context) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + album_id), projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        Bitmap bm;
        if (album_art != null) {
            bm = BitmapFactory.decodeFile(album_art);
        } else {
            bm = BitmapFactory.decodeResource(getResources(), R.drawable.defult_music);
        }
        return bm;
    }

    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.btn_allMusic:
                new AllMusicActivity().goAllMusicActivity(HomeActivity.this);
                break;
            case R.id.btn_see_more:
                new MostPlayActivity().goMostPlayActivity(HomeActivity.this);
                break;
            case R.id.btn_recentPlay:
                new RecentMusicActivity().goRecentMusicActivity(HomeActivity.this);
                break;
            case R.id.btn_myCollect:
                new MyCollectActivity().goMyCollectActivity(HomeActivity.this);
                break;
            case R.id.btn_home_menu:
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
                break;
            case R.id.btn_newList:
                showNewListDialog();
                break;
            case R.id.item_home_option:
                showListOperateDialog();
                break;
        }
    }

    private void setHomeInformation() {
        if (myBinder == null) {
            currentMusicEntity = dao.getFirstMusic();
            home_imageButton.setImageBitmap(TurnImage.byteToBitmap(currentMusicEntity.getMusicImage(), HomeActivity.this));
            home_nameText.setText(currentMusicEntity.getMusicName());
            home_singerText.setText(currentMusicEntity.getSinger());
        } else if (myBinder.playing() == 0 && myBinder.mFlag() == 0) {
            currentMusicEntity = dao.getFirstMusic();
            home_imageButton.setImageBitmap(TurnImage.byteToBitmap(currentMusicEntity.getMusicImage(), HomeActivity.this));
            home_nameText.setText(currentMusicEntity.getMusicName());
            home_singerText.setText(currentMusicEntity.getSinger());
            home_playButton.setImageResource(R.mipmap.playbutton);
        } else {
            currentMusicEntity = dao.getEntityByMusicId(myBinder.currentEntityId());
            home_imageButton.setImageBitmap(TurnImage.byteToBitmap(currentMusicEntity.getMusicImage(), HomeActivity.this));
            home_nameText.setText(currentMusicEntity.getMusicName());
            home_singerText.setText(currentMusicEntity.getSinger());
            home_playButton.setImageResource(R.mipmap.pausebutton);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        musicList.clear();
    }

    private void connectService() {
        Intent bindIntent = new Intent(HomeActivity.this, MyService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MyService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home_reserach:
                new FindMusicActivity().goFindMusicActivity(HomeActivity.this);
                break;
            case R.id.btn_home_play:
                if (myBinder.mFlag() == 0) {
                    Intent intent = new Intent(HomeActivity.this, MyService.class);
                    intent.putExtra("Home_URL", dao.getFirstMusic().getUrl());
                    startService(intent);//为初始界面准备音乐
                    home_playButton.setImageResource(R.mipmap.pausebutton);
                } else if (myBinder.playing() == 0) {
                    myBinder.startMusic();
                    home_playButton.setImageResource(R.mipmap.pausebutton);
                } else {
                    myBinder.pauseMusic();
                    home_playButton.setImageResource(R.mipmap.playbutton);
                }
                break;
            case R.id.btn_home_list:
                break;
        }
    }

    private void showNewListDialog() {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(HomeActivity.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(HomeActivity.this);
        inputDialog.setTitle("请输入新建歌单名").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListEntity listEntity = new ListEntity(editText.getText().toString(), 0);
                        listDao.addMusicList(listEntity);
                        list = listDao.getAllMusicList();
                        if (list != null) {
                            newMusicListAdapter = new NewMusicListAdapter(HomeActivity.this, R.layout.item_new_list, list);
                            musicListView.setAdapter(newMusicListAdapter);
                        }
                    }
                });
        inputDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        inputDialog.show();
    }

    private String[] item = {"       编辑歌单信息", "       分享此歌单", "       删除"};

    private void showListOperateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeActivity.this, "选择了" + item[which], Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
