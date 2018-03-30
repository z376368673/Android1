package com.jhobor.apptest.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2017/4/11.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    DrawRunnable drawRunnable;
    SurfaceHolder holder;
    int zoom = 0;//0.放大 1.缩小
    float radius = 0;

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = this.getHolder();
        holder.addCallback(this);
    }

    public void start(){
        if (drawRunnable!=null){
            if(zoom==1){
                drawRunnable.isRun = false;
            }else {
                return;
            }
        }
        zoom = 0;
        drawRunnable = new DrawRunnable(holder);
        Thread drawThread = new Thread(drawRunnable);
        drawThread.start();
    }

    public void reset(){
        if (drawRunnable!=null){
            if(zoom==0){
                drawRunnable.isRun = false;
            }else {
                return;
            }
        }
        zoom = 1;
        drawRunnable = new DrawRunnable(holder);
        Thread drawThread = new Thread(drawRunnable);
        drawThread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (drawRunnable!=null) {
            drawRunnable.isRun = false;
        }
    }

    private class DrawRunnable implements Runnable{
        SurfaceHolder holder;
        boolean isRun = true;
        DrawRunnable(SurfaceHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            int height = getMeasuredHeight();
            int width = getMeasuredWidth();
            while ((zoom==0&&radius*2<height)||(zoom==1&&radius>0)) {
                try {
                    Thread.sleep(10);
                    if (zoom==0) {
                        radius += 1f;
                    }else {
                        radius -= 1f;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Canvas canvas = holder.lockCanvas();
                if (canvas!=null) {
                    canvas.drawColor(Color.BLACK);
                    Paint paint = new Paint();
                    paint.setColor(Color.BLUE);
                    canvas.drawCircle(width / 2, height / 2, radius, paint);
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
