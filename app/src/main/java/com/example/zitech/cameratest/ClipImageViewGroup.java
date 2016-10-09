package com.example.zitech.cameratest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by zhanzhiqiang on 2016/9/29.
 */
public class ClipImageViewGroup extends ViewGroup {

    private ImageView imageView;

    private ClipView clipView;

    private Bitmap bitmap;

    private int width;

    private int height;

    private static final int SCALEIMG=6;

    private static final int TRANSLATEIMAG=5;

    private int status;

    private Matrix matrix;

    public ClipImageViewGroup(Context context) {
        this(context,null);
    }

    public ClipImageViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClipImageViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        imageView=new ImageView(context);
        clipView=new ClipView(context);
        addView(imageView);
        addView(clipView);
        bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.a1);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(bitmap);
        point=new int[2];
        matrix=new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=getMeasuredWidth();
        height=getMeasuredHeight();
        measureChild(clipView,widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        imageView.layout(0,0,width,height);
        clipView.layout(0,0,width,height);
    }

    int startx=0;
    int starty=0;
    float oldDist;
    int[] point;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startx= (int) event.getX();
                starty= (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(status==SCALEIMG){
                    float newDist=spacing(event);
                    float scale=newDist/oldDist;
                    matrix.postScale(scale,scale,point[0],point[1]);
                }else {
                    if (startx == 0) {
                        startx = (int) event.getX();
                    }
                    if (starty == 0) {
                        starty = (int) event.getY();
                    }
                    int movex = (int) event.getX();
                    int movey = (int) event.getY();
                    imageView.scrollBy(startx - movex, starty - movey);
                    startx = movex;
                    starty = movey;
                }
                break;
            case MotionEvent.ACTION_POINTER_2_DOWN:
               oldDist=spacing(event);
                status=SCALEIMG;
                midPoint(event);
                break;
            case MotionEvent.ACTION_POINTER_2_UP:
                status=TRANSLATEIMAG;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        if(status==SCALEIMG){
            imageView.setImageMatrix(matrix);
        }
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint( MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point[0]= (int) (x/2);
        point[1]= (int) (y/2);
    }

    public Bitmap getClipedBitmap(){
        clipView.setCliped(true);
        Rect rect=clipView.getRect();
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bitmap=Bitmap.createBitmap(this.getDrawingCache(),rect.left,rect.top,rect.width(),rect.height());
        clipView.setCliped(false);
        return bitmap;
    }
}
