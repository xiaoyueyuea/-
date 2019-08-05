package com.yl.yymusic.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.yl.yymusic.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FindMusicActivity extends AppCompatActivity {

    ImageButton backHomeFindButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findmusic);
        init();
        backHomeFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
    }

    private void init(){
        backHomeFindButton=findViewById(R.id.btn_findMusic_back);
    }

    public static void goFindMusicActivity(Context context){
        Intent intent = new Intent(context,FindMusicActivity.class);
        context.startActivity(intent);
    }
}
