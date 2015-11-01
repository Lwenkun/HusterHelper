package com.google.test.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.test.R;

/**
 * Created by 15119 on 2015/10/18.
 */
public class DrawCircle extends View {

    private final Paint pGrayCircle = new Paint();

    private final Paint pGreenCircle = new Paint();

    private final Paint pTextL = new Paint();

    private final Paint pTextS = new Paint();

    private final RectF oval = new RectF(25, 25, 0, 0);

    public DrawCircle(Context context, AttributeSet attrs) {
        super(context, attrs);

        pGrayCircle.setStrokeWidth(30);
        pGrayCircle.setStyle(Paint.Style.STROKE);
        pGrayCircle.setColor(Color.LTGRAY);

        pGreenCircle.setColor(getResources().getColor(R.color.mainColor));
        pGreenCircle.setStrokeWidth(45);
        pGreenCircle.setStyle(Paint.Style.STROKE);

        pTextL.setColor(Color.BLACK);
        pTextL.setTextAlign(Paint.Align.CENTER);

        pTextS.setColor(Color.GRAY);
        pTextS.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float electricity = getContext().getSharedPreferences("DaysInfo", Context.MODE_PRIVATE).getFloat("electricity0", 0);

        int canvasH = canvas.getHeight();
        Log.d("test4", "iamdrawing");

        pTextL.setTextSize(canvasH * 5 / 16);
        canvas.drawText(String.format("%.1f", electricity), canvasH / 2, canvasH * 18 / 32, pTextL);

        pTextS.setTextSize(canvasH / 8);
        canvas.drawText("kw-h", canvasH /2, canvasH *3/4, pTextS);

        canvas.drawCircle(canvasH / 2, canvasH / 2, canvasH / 2 - 25, pGrayCircle);

        oval.right = canvasH - 25;
        oval.bottom = canvasH - 25;

        float angle = (float) Math.log10((double) electricity) * 50;

        canvas.drawArc(oval, -90, angle, false, pGreenCircle);

    }

}


