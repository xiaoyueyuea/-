package com.yl.yymusic.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class CircleRotateView extends ImageView {

    private Paint mPaint;
    private BitmapShader mBitmapShader;
    private float mRadius;
    private Matrix mMatrix;
    private int mWidth;

    public CircleRotateView(Context context) {
        super(context);
        init();
    }

    public CircleRotateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleRotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CircleRotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mMatrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        mWidth = Math.min(width,height);
        mRadius = mWidth /2 * 0.9f;
        setMeasuredDimension(mWidth,mWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null == getDrawable()) {
            return;
        }
        onDrawCircle(canvas);
    }

    private void onDrawCircle(Canvas canvas) {
        setBitmapShader();
        canvas.drawCircle(mWidth/2,mWidth/2,mRadius,mPaint);
    }

    private Bitmap drawableToBitmap(Drawable drawable){
        if (drawable == null){
            return null;
        }
        if (drawable instanceof BitmapDrawable){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void setBitmapShader(){
        Drawable drawable = getDrawable();
        Bitmap bitmap = drawableToBitmap(drawable);
        if (bitmap == null){
            return;
        }
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        float bsize = Math.min(bitmap.getWidth(),bitmap.getHeight());
        scale = mWidth*1.0f/bsize;
        mMatrix.setScale(scale,scale);
        mBitmapShader.setLocalMatrix(mMatrix);
        mPaint.setShader(mBitmapShader);
    }
}
