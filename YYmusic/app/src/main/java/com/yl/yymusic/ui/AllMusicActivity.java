package com.yl.yymusic.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.yl.yymusic.R;
import com.yl.yymusic.service.MyService;
import com.yl.yymusic.storage.table.MusicEntity;
import com.yl.yymusic.util.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AllMusicActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView listView;
    private List<MusicEntity> musicList=new ArrayList<>();
    private ListViewAdapter listViewAdapter;
    private HomeActivity.GetEntity getEntity=new HomeActivity.GetEntity();
    private ImageButton backHomeAllButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allmusic);
        backHomeAllButton=findViewById(R.id.btn_allMusic_back);
        backHomeAllButton.setOnClickListener(this);
        musicList=getEntity.getMusicEntity();
        listView=findViewById(R.id.list_allMusic);
        listViewAdapter =new ListViewAdapter(AllMusicActivity.this,R.layout.item_list,musicList);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(AllMusicActivity.this,PlayActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("ALBUM_IMAGE",musicList.get(position).getMusicImage());
                intent.putExtra("MUSIC_NAME",musicList.get(position).getMusicName());
                intent.putExtra("SINGER_NAME",musicList.get(position).getSinger());
                intent.putExtra("KEY",0);
                startActivity(intent);
                Intent serviceIntent=new Intent(AllMusicActivity.this, MyService.class);
                serviceIntent.putExtra("position",position);
                serviceIntent.putExtra("KEY",0);
                startService(serviceIntent);
            }
        });
    }

    public static void goAllMusicActivity(Context context){
        Intent intent = new Intent(context,AllMusicActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_allMusic_back:
                finish();
                break;
        }
    }
}
