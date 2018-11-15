package com.example.cncs.equationsolver;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ChartActivity extends AppCompatActivity {

    private static final String LOG_TAG=ChartActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Intent intent = getIntent();
        HashMap<Integer, Double> iterationValues = (HashMap<Integer, Double>)intent.getSerializableExtra("map");
        GraphView graph = (GraphView) findViewById(R.id.graph);
        DataPoint[] dataPoint =new DataPoint[iterationValues.size()];
        for(int i=0;i<iterationValues.size();i++){
            Log.i(LOG_TAG,"Key value pair is: key: "+i+"Value: "+iterationValues.get(i));
            dataPoint[i]=new DataPoint(i, iterationValues.get(i));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoint);
        series.setTitle("Random Curve 1");
        series.setColor(Color.GREEN);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);

// custom paint to make a dotted line
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
        series.setCustomPaint(paint);
        graph.addSeries(series);

        TextView iterationTextView= (TextView)findViewById(R.id.iterationCounter);
        iterationTextView.setText("Convergence achieved in "+iterationValues.size()+" iterations");

        TextView solution= (TextView)findViewById(R.id.solution);
        solution.setText("Value of variable is:"+BigDecimal.valueOf(iterationValues.get(iterationValues.size()-1))
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue());
    }

}
