package com.yl.yymusic.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yl.yymusic.R;

public class RecentMusicActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton backHomeRecentButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recentmusic);
        backHomeRecentButton=findViewById(R.id.btn_allMusic_back);
        backHomeRecentButton.setOnClickListener(this);
    }

    public static void goRecentMusicActivity(Context context) {
        Intent intent = new Intent(context, RecentMusicActivity.class);
        context.startActivity(intent);
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
