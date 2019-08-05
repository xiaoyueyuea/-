package com.yl.yymusic.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yl.yymusic.R;

public class MostPlayActivity extends AppCompatActivity {

    ImageButton backHomeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostplay);
        init();
        backHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init(){
        backHomeButton=findViewById(R.id.back_home_button);
    }

    public static void goMostPlayActivity(Context context){
        Intent intent = new Intent(context,MostPlayActivity.class);
        context.startActivity(intent);
    }
}
