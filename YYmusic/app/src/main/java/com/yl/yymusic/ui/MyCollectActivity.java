package com.yl.yymusic.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yl.yymusic.R;
import com.yl.yymusic.service.MyService;
import com.yl.yymusic.storage.dao.MusicDao;
import com.yl.yymusic.storage.table.Database;
import com.yl.yymusic.storage.table.MusicEntity;
import com.yl.yymusic.util.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyCollectActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private List<MusicEntity> myCollectList=new ArrayList<>();
    private ImageButton backHomeCollectButton;
    MusicDao dao = Database.getInstance().getMusicDao();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollect);
        init();
        myCollectList=dao.findCollectMusic();
        listViewAdapter =new ListViewAdapter(MyCollectActivity.this,R.layout.item_list,myCollectList);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(MyCollectActivity.this,PlayActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("ALBUM_IMAGE",myCollectList.get(position).getMusicImage());
                intent.putExtra("MUSIC_NAME",myCollectList.get(position).getMusicName());
                intent.putExtra("SINGER_NAME",myCollectList.get(position).getSinger());
                intent.putExtra("KEY",2);
                startActivity(intent);
                Intent serviceIntent=new Intent(MyCollectActivity.this, MyService.class);
                serviceIntent.putExtra("position",position);
                serviceIntent.putExtra("KEY",2);
                startService(serviceIntent);
            }
        });

    }

    private void init(){
        listView=findViewById(R.id.list_myCollect);
        backHomeCollectButton=findViewById(R.id.btn_collect_back);
        backHomeCollectButton.setOnClickListener(this);
    }

    public static void goMyCollectActivity(Context context) {
        Intent intent = new Intent(context, MyCollectActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_collect_back:
               finish();
                break;
        }
    }
}
