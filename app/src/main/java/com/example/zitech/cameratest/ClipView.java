package com.example.zitech.cameratest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhanzhiqiang on 2016/9/29.
 */
public class ClipView extends View {


    private Rect rect;

    private int width;

    private int height;

    private Paint rectPaint;

    private Paint backgroudPaint;

    private boolean isCliped;

    private Matrix matrix;

    private static final int TRANSLATELEFT=1;
    private static final int TRANSLATETOP=2;
    private static final int TRANSLATERIGHT=3;
    private static final int TRANSLATEBOTTOM=4;

    private static final int TRANSLATEIMAG=5;
    private static final int SCALEIMG=6;
    private int status;

    public ClipView(Context context) {
        this(context,null);
    }

    public ClipView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ClipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        rect=new Rect();
        rectPaint=new Paint();
        backgroudPaint=new Paint();
        backgroudPaint.setStyle(Paint.Style.FILL);
        backgroudPaint.setColor(getResources().getColor(R.color.backgroung));
        rectPaint.setColor(Color.WHITE);
        matrix=new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=getMeasuredWidth();
        height=getMeasuredHeight();
        Log.e("width:::::",width+"////////");
        Log.e("height::::::",height+"//////////////////");
        rect.set(20,320,width-20,height-320);
    }

    int startx=0;
    int starty=0;
    int endx;
    int endy;
    float oldDist;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                initStatus((int)event.getX(),(int)event.getY());
                startx= (int) event.getX();
                starty= (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                switch (status){
                    case TRANSLATELEFT:
                        rect.set((int)event.getX(),rect.top,rect.right,rect.bottom);
                        break;
                    case TRANSLATETOP:
                        rect.set(rect.left,(int)event.getY(),rect.right,rect.bottom);
                        break;
                    case TRANSLATERIGHT:
                        rect.set(rect.left,rect.top,(int)event.getX(),rect.bottom);
                        break;
                    case TRANSLATEBOTTOM:
                        rect.set(rect.left,rect.top,rect.right,(int)event.getY());
                        break;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_2_DOWN:
                oldDist=spacing(event);
                Log.e("初始长度:",oldDist+"//////////");
                status=SCALEIMG;
                break;

            case MotionEvent.ACTION_POINTER_2_UP:
                status=TRANSLATEIMAG;
            case MotionEvent.ACTION_UP:
                if(status==TRANSLATEIMAG) {
                    endx = endx + (int) event.getX() - startx;
                    endy = endy + (int) event.getY() - starty;
                }
                break;
        }
        if(status==TRANSLATEIMAG){
            return false;
        }else {
            return true;
        }
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void initStatus(int x,int y){
        if(x>rect.centerX()-30&&x<rect.centerX()+30){
            if(y>rect.top-30&&y<rect.top+30){
                status=TRANSLATETOP;
                return;
            }else if(y>rect.bottom-30&&y<rect.bottom+30){
                status=TRANSLATEBOTTOM;
                return;
            }
        }

        if(y>rect.centerY()-30&&y<rect.centerY()+30){
            if(x>rect.left-30&&x<rect.left+30){
                status=TRANSLATELEFT;
                return;
            }else if(x>rect.right-30&&x<rect.right+30){
                status=TRANSLATERIGHT;
                return;
            }
        }
        status=TRANSLATEIMAG;
    }


    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(isCliped){
            drawRectWithoutCycle(canvas);
        }else {
            drawRect(canvas);
        }

    }

    private void drawRect(Canvas canvas){
        int x=rect.centerX();
        int y=rect.centerY();

        int rectwidth=rect.width();
        int rectheight=rect.height();

        canvas.drawRect(0,0,width,rect.top,backgroudPaint);
//        Log.e("上框：：","左:"+0+"上"+0+"右："+width+"下："+(y-rectheight/2));
          canvas.drawRect(0,rect.top,rect.left,rect.bottom,backgroudPaint);
//       // Log.e("左框：：","左:"+0+"上"+(y-rectwidth/2)+"右："+width+"下："+(y-rectheight/2));
        canvas.drawRect(0,rect.bottom,width,height,backgroudPaint);
        canvas.drawRect(rect.right,rect.top,width,rect.bottom,backgroudPaint);

        rectPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rect,rectPaint);
        rectPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x,y-rectheight/2,10,rectPaint);
        canvas.drawCircle(x,y+rectheight/2,10,rectPaint);
        canvas.drawCircle(x-rectwidth/2,y,10,rectPaint);
        canvas.drawCircle(x+rectwidth/2,y,10,rectPaint);
    }



    private void drawRectWithoutCycle(Canvas canvas){
        int x=rect.centerX();
        int y=rect.centerY();

        int rectwidth=rect.width();
        int rectheight=rect.height();

        canvas.drawRect(0,0,width,rect.top,backgroudPaint);
//        Log.e("上框：：","左:"+0+"上"+0+"右："+width+"下："+(y-rectheight/2));
        canvas.drawRect(0,rect.top,rect.left,rect.bottom,backgroudPaint);
//       // Log.e("左框：：","左:"+0+"上"+(y-rectwidth/2)+"右："+width+"下："+(y-rectheight/2));
        canvas.drawRect(0,rect.bottom,width,height,backgroudPaint);
        canvas.drawRect(rect.right,rect.top,width,rect.bottom,backgroudPaint);

        rectPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rect,rectPaint);
    }

//    public Bitmap getClipedBitmap(){
//        isCliped=true;
//        invalidate();
//        this.setDrawingCacheEnabled(true);
//        this.buildDrawingCache();
//        Bitmap bitmap=Bitmap.createBitmap(this.getDrawingCache(),rect.left,rect.top,rect.width(),rect.height());
//        isCliped=false;
//        return bitmap;
//    }

    public Rect getRect(){
        return rect;
    }

    public void setCliped(boolean Cliped){
        isCliped=Cliped;
        invalidate();
    }

}
