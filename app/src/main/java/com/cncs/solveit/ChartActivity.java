package com.cncs.solveit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class ChartActivity extends AppCompatActivity {

    private static final String LOG_TAG = ChartActivity.class.getName();

    private TextView mEmptyStateTextView;

    private final int INFINITY_ERROR = 144477;

    private final int NON_INFINITY_ERROR = 144478;

    private final int NON_CONVERGENT_ERROR = 144479;

    private final int INPUT_NOT_SUPPORTED = 144480;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Intent intent = getIntent();
        mEmptyStateTextView = findViewById(R.id.empty_view);

        HashMap<Integer, Double> iterationValues = (HashMap<Integer, Double>) intent.getSerializableExtra("map");
        GraphView graph = findViewById(R.id.graph);
        if (!iterationValues.containsKey(INFINITY_ERROR) && !iterationValues.containsKey(NON_INFINITY_ERROR) && !iterationValues.containsKey(NON_CONVERGENT_ERROR)
                && !iterationValues.containsKey(INPUT_NOT_SUPPORTED)) {
            DataPoint[] dataPoint = new DataPoint[iterationValues.size()];
            for (int i = 0; i < iterationValues.size(); i++) {
                Log.i(LOG_TAG, "Key value pair is: key: " + i + "Value: " + iterationValues.get(i));
                dataPoint[i] = new DataPoint(i, iterationValues.get(i));
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoint);
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
            graph.getViewport().setMaxY(iterationValues.get(0) + 10);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMaxX(iterationValues.size());
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setDrawBorder(true);


            TextView iterationTextView =  findViewById(R.id.iterationCounter);
            iterationTextView.setText(getString(R.string.iterations1) + " " + (iterationValues.size() - 1) + " " + getString(R.string.iterations2));
            iterationTextView.setTypeface(Typeface.DEFAULT_BOLD);

            TextView solution = findViewById(R.id.solution);
            solution.setText(getString(R.string.solution) + BigDecimal.valueOf(iterationValues.get(iterationValues.size() - 1))
                    .setScale(3, RoundingMode.HALF_UP)
                    .doubleValue());



        } else {
            if (iterationValues.containsKey(INFINITY_ERROR)) {
                mEmptyStateTextView.setText(getString(R.string.division_by_zero));
            } else if (iterationValues.containsKey(NON_CONVERGENT_ERROR)) {
                mEmptyStateTextView.setText(getString(R.string.non_convergent));
            } else if (iterationValues.containsKey(INPUT_NOT_SUPPORTED)) {
                mEmptyStateTextView.setText(getString(R.string.not_supported_input));
            } else {
                mEmptyStateTextView.setText(getString(R.string.generalized_error));
            }

            graph.setVisibility(View.GONE);
        }



    }

}
