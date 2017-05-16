package com.miandui.utils.view.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.miandui.R;
import com.miandui.utils.normal.ScreenUtils;
import com.miandui.utils.view.DrawUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2017/4/29
 */

public class RecordChart extends View {

    private Paint paint;

    private float accessableWidth;          //控件可用空间
    private float accessableHeight;
    private float chatWidth;                //表格实际宽度
    private float chatHeight;               //表格实际高度

    private float chatPaddingLeft;          //表格的padding
    private float chatPaddingRight;
    private float chatPaddingTop;
    private float chatPaddingBottom;

    private List<Float> values;
    private float maxValue;
    private float minValue;

    private int bottomNoticeColor;              //底部文字颜色
    private float bottomNoticeTextSize;         //底部文字大小
    private float bottomNoticePadding;          //底部文字与表格的间距

    private int barColor;                       //柱形图的颜色
    private float barWidth;                    //柱形图的宽度

    private int dividerColor;                   //表格X轴分割线颜色
    private float dividerHeight;                //表格x轴分割线高度

    private int YNoticeColor;                   //Y轴坐标文字颜色
    private float YNoticeTextSize;              //Y轴坐标文字大小
    private int YNoticeCount = 3;               //Y轴坐标文字数目
    private int YMaxValue;                      //Y轴最大值

    private float widthDivider;

    public RecordChart(Context context) {
        super(context);
        init();
    }

    public RecordChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecordChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);

        chatPaddingTop = ScreenUtils.getAdapterPx(R.dimen.y30, getContext());
        chatPaddingBottom = ScreenUtils.getAdapterPx(R.dimen.y70, getContext());
        bottomNoticeColor = getContext().getResources().getColor(R.color.b0);
        bottomNoticeTextSize = ScreenUtils.dip2px(10);
        bottomNoticePadding = ScreenUtils.getAdapterPx(R.dimen.y30, getContext());
        barColor = getContext().getResources().getColor(R.color.yellow);
        barWidth = ScreenUtils.getAdapterPx(R.dimen.y20, getContext());
        dividerColor = getContext().getResources().getColor(R.color.titleBack);
        dividerHeight = ScreenUtils.dip2px(1);
        YNoticeColor = getContext().getResources().getColor(R.color.titleBack);
        YNoticeTextSize = ScreenUtils.dip2px(12);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (values == null || values.isEmpty()) {
            return;
        }
        calculate();
        drawXLines(canvas);
        drawBar(canvas);
        drawBottomLine(canvas);
    }

    private void calculate() {
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        accessableWidth = (getWidth() - paddingLeft - paddingRight);
        accessableHeight = (getHeight() - paddingTop - paddingBottom);
        chatWidth = accessableWidth - chatPaddingLeft - chatPaddingRight;
        chatHeight = accessableHeight - chatPaddingTop - chatPaddingBottom;

        maxValue = minValue = values.get(0);
        for (Float value : values) {
            if (value > maxValue) {
                maxValue = value;
            }
            if (value < minValue) {
                minValue = value;
            }
        }

        YMaxValue = Math.round(maxValue) + 1;
        if (YMaxValue < 7) {
            YMaxValue = 7;
        }
        widthDivider = (chatWidth - barWidth * values.size()) / (values.size() + 1);
    }

    private void drawBar(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -(values.size() - 1));
        float Xleft = 0;
        for (int i = 0; i < values.size(); i++) {
            float top = (YMaxValue - values.get(i)) / YMaxValue * chatHeight + getPaddingTop() + chatPaddingTop;
            Xleft += widthDivider;
            Rect rect = new Rect((int) (Xleft),
                    (int) (top),
                    (int) (Xleft + barWidth),
                    (int) (getPaddingTop() + chatHeight + chatPaddingTop));
            paint.setColor(barColor);
            canvas.drawRect(rect, paint);

            paint.setColor(bottomNoticeColor);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(bottomNoticeTextSize);
//            canvas.drawText(calendar.get(Calendar.MONTH) + 1 + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日",
//                    Xleft + barWidth / 2,
//                    getPaddingTop() + chatHeight + bottomNoticePadding,
////                    getHeight() - getPaddingBottom() - ScreenUtils.getAdapterPx(R.dimen.y6, getContext()),
//                    paint);

            DrawUtils.drawTextByTop(canvas, Xleft + barWidth / 2,
                    getPaddingTop() + chatPaddingTop + chatHeight + bottomNoticePadding,
                    calendar.get(Calendar.MONTH) + 1 + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日",
                    paint);

            calendar.add(Calendar.DAY_OF_MONTH, 1);

            Xleft += barWidth;

            Log.d("RecordChart", "Xleft:" + Xleft);

        }
    }

    private void drawBottomLine(Canvas canvas) {
        paint.setColor(getResources().getColor(R.color.b0));
        paint.setStrokeWidth(dividerHeight);
        canvas.drawLine(getPaddingLeft(), getPaddingTop() + chatHeight + chatPaddingTop,
                getPaddingLeft() + chatWidth, getPaddingTop() + chatHeight + chatPaddingTop, paint);

    }

    private void drawXLines(Canvas canvas) {
        paint.setColor(dividerColor);
        paint.setStrokeWidth(dividerHeight);
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(YNoticeTextSize);

        int noticeDivider = Math.round(maxValue) / YNoticeCount;
        int i = 0;
        for (float currentYValue = noticeDivider; currentYValue < YMaxValue; currentYValue += noticeDivider) {
            i++;
            if (i > 3) {
                break;
            }
            float Y = getPaddingTop() + chatPaddingTop + (YMaxValue - currentYValue) / YMaxValue * chatHeight;
            canvas.drawLine(getPaddingLeft(), Y,
                    getPaddingLeft() + chatWidth, Y, paint);
            canvas.drawText("" + currentYValue + "h", getPaddingLeft() + chatWidth, Y - 3, paint);
        }
    }

    public void setValues(List<Float> values) {
        this.values = values;
        invalidate();
    }
}
