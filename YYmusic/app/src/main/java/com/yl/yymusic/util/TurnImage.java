package com.yl.yymusic.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yl.yymusic.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TurnImage {
    public static byte[] bitmapToByte(Bitmap bitmap){
       if(bitmap!=null){
           int size = bitmap.getWidth()*bitmap.getHeight()*4;
           ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
           bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
           byte[] bytes= baos.toByteArray();
           bitmap.recycle();
           try {
               baos.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
           return bytes;
       } else {
        return null;
       }
    }

    public static Bitmap byteToBitmap(byte[] bytes, Context context){
        if(bytes!=null){
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.defult_music);
        }

    }
}
