package com.wzx.tranproject.widgit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wzx.tranproject.R;

/**
 * Created by macuser on 16/9/5.
 */
public class SweeptakesView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Thread mThread;
    private boolean isRuning;
    private SurfaceHolder mHodler;
    private Canvas mCanvas;


    private int   mItemCount=6;
    private float mRadius;
    private float mCenter;
    private float mStartAngle;
    private Paint mArcPaint;
    private Bitmap mBgBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.bg);
    private int mPadding;
    private int[] mColors = new int[] { 0xFFFFC300, 0xFFF17E01, 0xFFFFC300,
            0xFFF17E01, 0xFFFFC300, 0xFFF17E01 };
    private RectF mRange;
    public SweeptakesView(Context context) {
        this(context,null);
    }

    public SweeptakesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHodler=getHolder();
        mHodler.addCallback(this);
        //设置透明

        this.setKeepScreenOn(true);


    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        mRange=new RectF(getPaddingLeft(),getPaddingLeft(),mRadius+getPaddingLeft(),mRadius+getPaddingLeft());
        mArcPaint=new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        isRuning=true;
        mThread=new Thread(this);
        mThread.start();



    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
         isRuning=false;
    }

    @Override
    public void run() {
          while (isRuning){

              draw();
          }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=Math.min(getMeasuredHeight(),getMeasuredWidth());
        mPadding=getPaddingLeft();

        mRadius=width-getPaddingLeft()-getPaddingRight();
        mCenter=width/2;

        setMeasuredDimension(width,width);
    }

    private void drawbg(){
         mCanvas.drawColor(0xffffffff);
         mCanvas.drawBitmap(mBgBitmap,null,new Rect(mPadding/2,mPadding/2,getMeasuredWidth()-mPadding/2,getMeasuredWidth()-mPadding/2),null);
    }

    private void draw(){
           try {
               //获取canvas
               mCanvas=mHodler.lockCanvas();
               if(mCanvas!=null){
                   drawbg();
                   float tmpAngle=mStartAngle;
                   float sweepAngle=(float)(360/mItemCount);

                   for (int i=0;i<mItemCount;i++){
                           mArcPaint.setColor(mColors[i]);
                           mCanvas.drawArc(mRange,tmpAngle,sweepAngle,true,mArcPaint);
                           tmpAngle+=sweepAngle;
                   }


               }
           }catch (Exception e){
                   e.printStackTrace();
           }finally {
                  if(mCanvas!=null){
                      mHodler.unlockCanvasAndPost(mCanvas);
                  }
           }
    }
}
