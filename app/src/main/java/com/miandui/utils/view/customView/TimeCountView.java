package com.miandui.utils.view.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.miandui.R;
import com.miandui.utils.normal.ScreenUtils;
import com.miandui.utils.view.DrawUtils;

/**
 * Created by FengChaoQun
 * on 2017/5/27
 */

public class TimeCountView extends View {

    private Paint paint = new Paint();
    private int paddingValue;
    private int bigRadius;
    private int circleWith = 2;//dp
    private int ballRadius;
    private int ballPadding;
    private int textSize = 23;//sp

    private long seconds;

    public TimeCountView(Context context) {
        super(context);
        init();
    }

    public TimeCountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.yeff));
        paint.setStrokeWidth(ScreenUtils.dip2px(2));
        paint.setAntiAlias(true);


        paddingValue = ScreenUtils.dip2px(circleWith + 1);
        ballRadius = ScreenUtils.getAdapterPx(R.dimen.x11, getContext());
        ballPadding = ScreenUtils.getAdapterPx(R.dimen.x16, getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int with = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int min = Math.min(with, height);
        setMeasuredDimension(min, min);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bigRadius = getWidth() / 2 - paddingValue;

        paint.setColor(getResources().getColor(R.color.yeff));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, bigRadius, paint);

        paint.setStyle(Paint.Style.FILL);

        float angle = (seconds % 3600f / (/*12 * */60 * 60)) * 360f;
        Log.d("TimeCountView", "angle:" + angle);
        int xOff = (int) (Math.sin(angle / 180f * Math.PI) * (bigRadius - ballRadius - ballPadding));
        int yOff = (int) (Math.cos(angle / 180f * Math.PI) * (bigRadius - ballRadius - ballPadding));
        Log.d("TimeCountView", "xOff:" + xOff);
        Log.d("TimeCountView", "yOff:" + yOff);
        canvas.drawCircle(getWidth() / 2 + xOff, getHeight() / 2 - yOff, ballRadius, paint);

        paint.setTextSize(ScreenUtils.sp2px(textSize));
        paint.setColor(getResources().getColor(R.color.b0));
        paint.setTextAlign(Paint.Align.CENTER);
        DrawUtils.drawTextByCenter(canvas, getWidth() / 2, getHeight() / 2, getTimeString(), paint);
    }

    private String getTimeString() {
        int hour = (int) (seconds / 3600);
        int minute = (int) (seconds % 3600 / 60);
        int second = (int) (seconds % 3600 % 60);
        String minuteString;
        if (minute < 10) {
            minuteString = "0" + minute;
        } else {
            minuteString = "" + minute;
        }
        String secondString;
        if (second < 10) {
            secondString = "0" + second;
        } else {
            secondString = "" + second;
        }
        return hour + ":" + minuteString + ":" + secondString;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
        invalidate();
    }
}
