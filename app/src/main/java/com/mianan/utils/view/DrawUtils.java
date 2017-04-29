package com.mianan.utils.view;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by FengChaoQun
 * on 2017/3/2
 */

public class DrawUtils {
    public static void drawTextByCenter(Canvas canvas, float x, float y, String text, Paint paint) {
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int baseLineY = (int) y + (fm.bottom - fm.top) / 2 - fm.bottom;
        canvas.drawText(text, x, baseLineY, paint);
    }

    public static void drawTextByTop(Canvas canvas, float x, float y, String text, Paint paint) {
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int baseLineY = (int) y - fm.ascent;
        canvas.drawText(text, x, baseLineY, paint);
    }
}
