package com.jizhang.mjtzn.jizhang;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class HistogramView extends View {
    private Paint xLinePaint;// 坐标轴 轴线 画笔：
    private Paint hLinePaint;// 坐标轴水平内部 虚线画笔
    private Paint titlePaint;// 绘制文本的画笔
    private Paint paint;// 矩形画笔 柱状图的样式信息
    private double[] aniProgress;// 实现动画的值
    private final int TRUE = 1;// 在柱状图上显示数字
    private int[] text;// 设置点击事件，显示哪一条柱状的信息
    private Bitmap bitmap;
    // 坐标轴左侧的数标
    private String[] ySteps;
    // 坐标轴底部的星期数
    private String[] xWeeks;
    private double maxY;



    public HistogramView(Context context) {
        super(context);
    }

    public void setData(double data[]){
        this.aniProgress=data;
    }

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMaxY(double maxy){
        this.maxY=maxy;
    }
    public void setySteps(String ysteps[]){
        this.ySteps=ysteps;
    }
    public void setxWeeks(String xweeks[]){
        this.xWeeks=xweeks;
    }

    public void init() {
        text = new int[] { 1, 100, 100, 10, 100, 100, 100 };//******

        xLinePaint = new Paint();
        hLinePaint = new Paint();
        titlePaint = new Paint();
        paint = new Paint();

        // 给画笔设置颜色
        xLinePaint.setColor(Color.DKGRAY);
        hLinePaint.setColor(Color.LTGRAY);
        titlePaint.setColor(Color.BLACK);

        bitmap = BitmapFactory
                .decodeResource(getResources(), R.mipmap.blue);
    }

    public void redraw(){
        bitmap = BitmapFactory
                .decodeResource(getResources(), R.mipmap.blue);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight() - dp2px(50);
        // 绘制底部的线条
        canvas.drawLine(dp2px(30), height + dp2px(3), width - dp2px(30), height
                + dp2px(3), xLinePaint);

        int leftHeight = height - dp2px(5);// 左侧外周的 需要划分的高度：

        int hPerHeight = leftHeight / 4;// 分成四部分

        hLinePaint.setTextAlign(Paint.Align.CENTER);
        // 设置四条虚线
        for (int i = 0; i < 4; i++) {
            canvas.drawLine(dp2px(30), dp2px(10) + i * hPerHeight, width
                    - dp2px(30), dp2px(10) + i * hPerHeight, hLinePaint);
        }

        // 绘制 Y 周坐标
        titlePaint.setTextAlign(Paint.Align.RIGHT);
        titlePaint.setTextSize(sp2px(10));
        titlePaint.setAntiAlias(true);
        titlePaint.setStyle(Paint.Style.FILL);
        // 设置左部的数字
        for (int i = 0; i < ySteps.length; i++) {
            canvas.drawText(ySteps[i], dp2px(30), dp2px(13) + i * hPerHeight,
                    titlePaint);
        }

        // 绘制 X 周 做坐标
        int xAxisLength = width - dp2px(30);
        int columCount = xWeeks.length + 1;
        int step = xAxisLength / columCount;

        // 设置底部的数字
        for (int i = 0; i < columCount - 1; i++) {
            // text, baseX, baseY, textPaint
            canvas.drawText(xWeeks[i], dp2px(25) + step * (i + 1), height
                    + dp2px(20), titlePaint);
        }

        // 绘制矩形
        if (aniProgress != null && aniProgress.length > 0) {
            for (int i = 0; i < aniProgress.length; i++) {// 循环遍历将7条柱状图形画出来
                double value = aniProgress[i];
                paint.setAntiAlias(true);// 抗锯齿效果
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(sp2px(10));// 字体大小
                paint.setColor(Color.parseColor("#6DCAEC"));// 字体颜色
                Rect rect = new Rect();// 柱状图的形状

                rect.left = step * (i + 1);
                rect.right = dp2px(30) + step * (i + 1);
                int rh = (int) (leftHeight - leftHeight * (value / maxY));
                rect.top = rh + dp2px(10);
                rect.bottom = height;

                canvas.drawBitmap(bitmap, null, rect, paint);
                // 是否显示柱状图上方的数字
                //if********
                canvas.drawText(String.format("%.1f",value), dp2px(15) + step * (i + 1)
                        - dp2px(15), rh + dp2px(5), paint);
            }
        }
        step = (getWidth() - dp2px(30)) / 8;
        for (int i = 0; i < 7; i++) {
                text[i] = 1;
                for (int j = 0; j < 7; j++) {
                    if (i != j) {
                        text[j] = 0;
                    }
                }
        }

    }

    private int dp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }

    private int sp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (v * value + 0.5f);
    }

}
