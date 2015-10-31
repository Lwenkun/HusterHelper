package com.google.test.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.test.data.DayInfo;
import com.google.test.data.RecentData;
import com.google.test.ui.ViewRefresher;

import java.util.List;

/**
 * Created by 15119 on 2015/10/18.
 */

//daysInfo
public class DrawGraph extends View implements ViewRefresher {

    private Context context;

    private List<DayInfo> daysInfo;

    private final Paint p = new Paint();

    private final Path path = new Path();

    private float circleX[];

    private float circleY[];

    private float interval;

    private int r;

    private float[] Y;

    private float maxElectricity;

    private int position = 6;

    private int canvasH;

    private int canvasW;

    public DrawGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        p.setAntiAlias(true);
        RecentData.init(context);
        
        //大圆的圆心坐标
        circleX = new float[7];
        circleY = new float[7];

        //初始化数据
        initData();
    }

    public void initData() {

        //获得最近电量数据
        daysInfo = RecentData.getRecentData();

        //获取画布的宽和高
        canvasH = getHeight();
        canvasW = getWidth();

        //最近电量显示中的大圆半径
        r = canvasH / 4;

        //设置最近电量显示图中两点间的间隔
        interval = (canvasW - 2 * r) / 6;

        //计算大圆的横坐标
        for (int i = 0; i <= 6; i++) {
            circleX[i] = r + i * interval;
        }

        //计算图像缩放系数，这里将曲线范围缩放成区域高度的2/5
        float k = (canvasH * 2 / 5) / RecentData.getData(RecentData.DATA_MAX_ELECTRICITY);

        //曲线上表示最近电量的点的坐标
        Y = new float[7];
        for (int i = 0; i <= 6; i++) {
            Y[6 - i] = canvasH - daysInfo.get(i).getElectricity() * k;
        }

        //计算大圆的纵坐标
        for (int i = 0; i <= 6; i++) {
            circleY[i] = Y[i] - r - 50;
        }
    }

    //刷新view
    @Override
    public void refreshView() {
        initData();
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d("test4", "iamdrawing,too");

        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setColor(Color.rgb(70, 222, 235));

        //绘制起点到第一个点的直线
        path.moveTo(0, canvasH);
        path.lineTo(0, Y[0]);
        path.lineTo(r, Y[0]);

        //绘制折线
        float startX = r;
        for (int i = 0; i <= 5; i++) {
            path.quadTo(startX + interval / 2 + 20, (Y[i + 1] + Y[i]) / 2, startX + interval, Y[i + 1]);
            startX += interval;
        }

        //将路径闭合并填充颜色
        path.lineTo(canvasW, Y[6]);
        path.lineTo(canvasW, canvasH);
        path.close();
        canvas.drawPath(path, p);

        //画大圆
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(circleX[position], circleY[position], r, p);

        //每日用电量的文字说明
        p.setColor(Color.WHITE);
        p.setTextSize(r / 4);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(daysInfo.get(6 - position).getWeekday(), circleX[position], circleY[position] - r / 2, p);
        canvas.drawText(daysInfo.get(6 - position).getSDate(), circleX[position], circleY[position] - r / 8, p);
        canvas.drawText("kw-h", circleX[position], circleY[position] + r * 3 / 4, p);
        p.setTextSize(r / 3);
        canvas.drawText(String.valueOf(daysInfo.get(6 - position).getElectricity()), circleX[position], circleY[position] + r / 3, p);

        //画小圆
        canvas.drawCircle(circleX[position], circleY[position] + r + 50, 35, p);
        p.setColor(Color.rgb(70, 222, 235));
        canvas.drawCircle(circleX[position], circleY[position] + r + 50, 30, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();

        //判断手指点击位置
        position = (int) ((x - r + interval / 2) / interval);

        //如果位置在0外侧，则置为0；在6外侧则置为6
        if (position < 0) {
            position = 0;
        } else if (position > 6) {
            position = 6;
        }

        //重绘
        this.invalidate();
        return true;
    }



}



