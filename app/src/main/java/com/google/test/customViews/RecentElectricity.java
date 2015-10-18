package com.google.test.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.test.cache.DayInfo;
import com.google.test.cache.DaysInfoCache;

import java.util.List;

/**
 * Created by 15119 on 2015/10/18.
 */
public class RecentElectricity extends View {

    private List<DayInfo> daysInfo;

    private final Paint p = new Paint();

    private final Path path = new Path();

    private float circleX[];

    private float circleY[];

    private float interval;

    private float k;

    private int r;

    private float[] Y;

    private int position = 6;

    private int canvasH;

    private int canvasW;

    public RecentElectricity(Context context, AttributeSet attrs) {
        super(context, attrs);
        daysInfo = new DaysInfoCache(context).getDaysInfo();
        p.setAntiAlias(true);
        initData();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setColor(Color.rgb(70, 222, 235));
        path.moveTo(0, canvasH);
        path.lineTo(0, Y[0]);
        path.lineTo(r, Y[0]);
        float startX = r;
        for (int i = 0; i <= 5; i++) {
            path.quadTo(startX + interval / 2 + 20, (Y[i + 1] + Y[i]) / 2, startX + interval, Y[i + 1]);
            startX += interval;
        }
        path.lineTo(canvasW, Y[6]);
        path.lineTo(canvasW, canvasH);
        path.close();
        canvas.drawPath(path, p);
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(circleX[position], circleY[position], r, p);
        p.setColor(Color.WHITE);
        p.setTextSize(r / 4);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(daysInfo.get(6 - position).getWeekday(), circleX[position], circleY[position] - r / 2, p);
        canvas.drawText(daysInfo.get(6 - position).getSDate(), circleX[position], circleY[position] - r / 8, p);
        canvas.drawText("kw-h", circleX[position], circleY[position] + r * 3 / 4, p);
        p.setTextSize(r / 3);
        canvas.drawText(String.valueOf(daysInfo.get(6 - position).getElectricity()), circleX[position], circleY[position] + r / 3, p);
        canvas.drawCircle(circleX[position], circleY[position] + r + 50, 35, p);
        p.setColor(Color.rgb(70, 222, 235));
        canvas.drawCircle(circleX[position], circleY[position] + r + 50, 30, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        position = (int) ((x - r + interval / 2) / interval);
        if (position < 0) {
            position = 0;
        } else if (position > 6) {
            position = 6;
        }
        this.invalidate();
        return true;
    }

    public float calRange() {

        float[] e = new float[7];
        for (int i = 0; i <= 6; i++) {
            e[i] = daysInfo.get(i).getElectricity();
        }
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6 - i; j++) {
                if (e[j] > e[j + 1]) {
                    float temp = e[j];
                    e[j] = e[j + 1];
                    e[j + 1] = temp;
                }
            }
        maxElectricity = e[6];
        return e[6] - e[0];
    }

    public void initData() {
        Y = new float[7];
        circleX = new float[7];
        circleY = new float[7];
        canvasH = getHeight();
        canvasW = getWidth();
        r = canvasH / 4;
        interval = (canvasW - 2 * r) / 6;
        for (int i = 0; i <= 6; i++) {
            circleX[i] = r + i * interval;
        }
        k = (canvasH * 2 / 5) / maxElectricity;
        for (int i = 0; i <= 6; i++) {
            Y[6 - i] = canvasH - daysInfo.get(i).getElectricity() * k;
        }
        for (int i = 0; i <= 6; i++) {
            circleY[i] = Y[i] - r - 50;
        }
    }
}



