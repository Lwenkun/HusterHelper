package com.google.test.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.google.test.R;

/**
 * Created by 15119 on 2015/10/18.
 */
public class DrawCircle extends RelativeLayout{

    private final Paint pGrayCircle = new Paint();

    private final Paint pGreenCircle = new Paint();

    private final RectF oval = new RectF(25, 25, 0, 0);

    private int canvasH;

    public DrawCircle(Context context, AttributeSet attrs) {
        super(context, attrs);

        canvasH = getHeight();
        Log.d("test4", "iamdrawing");
        pGrayCircle.setStrokeWidth(30);
        pGrayCircle.setStyle(Paint.Style.STROKE);
        pGrayCircle.setColor(Color.LTGRAY);

        pGreenCircle.setColor(getResources().getColor(R.color.mainColor));
        pGreenCircle.setStrokeWidth(45);
        pGreenCircle.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(canvasH / 2, canvasH / 2, canvasH / 2 - 25, pGrayCircle);

        oval.right = canvasH - 25;
        oval.bottom = canvasH - 25;

        float angle = (float) Math.log10(Double.parseDouble(getContext().getSharedPreferences("DaysInfo", Context.MODE_PRIVATE).getString("electricity6", ""))) * 50;

        canvas.drawArc(oval, -90, angle, false, pGreenCircle);

    }
}


