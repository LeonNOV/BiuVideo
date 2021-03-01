package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.leon.biuvideo.R;

public class CircleImageView extends AppCompatImageView {
    private static final int DEFAULT_BORDER_WIDTH = 5;
    private static final int DEFAULT_BORDER_COLOR = Color.GRAY;

    private int mBorderWidth;
    private int mBorderColro;
    private int mSize;
    private Paint mBorderPaint;

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);

        mBorderColro = typedArray.getColor(R.styleable.CircleImageView_borderColor, DEFAULT_BORDER_COLOR);
        mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.CircleImageView_borderWidth, DEFAULT_BORDER_WIDTH);
        typedArray.recycle();

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColro);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mSize = Math.max(getMeasuredHeight(), getMeasuredWidth());
        setMeasuredDimension(mSize, mSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 获取图片
        if (getDrawable() != null && getDrawable() instanceof BitmapDrawable) {
            // 获取原始bitmap
            Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();

            // 画一个圆形边框
            canvas.drawCircle(mSize >> 1, mSize >> 1, mSize >> 1, mBorderPaint);

            // 画一个圆形bitmap
            canvas.drawBitmap(getCopyBitmap(bitmap), 0, 0, null);
        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 创建圆形Bitmap
     *
     * @param bitmap    原bitmap
     * @return  返回圆形bitmap
     */
    private Bitmap getCopyBitmap(Bitmap bitmap) {
        // 创建一个BitMap副本
        Bitmap copyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

        //创建画板
        Canvas canvas = new Canvas(copyBitmap);

        //创建画笔
        Paint bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);

        //绘制圆形区域
        canvas.drawCircle(mSize >> 1, mSize >> 1, (mSize >> 1) - 2 * mBorderWidth, bitmapPaint);

        //绘制Bitmap并和圆形区域取交集
        bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(copyBitmap, 0, 0, bitmapPaint);

        //返回最终的圆形Bitmap
        return copyBitmap;
    }
}
