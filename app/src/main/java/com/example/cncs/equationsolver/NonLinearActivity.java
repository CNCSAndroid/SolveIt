package com.example.cncs.equationsolver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class NonLinearActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_linear);

        final Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               /*
                TextView solutionTextView= (TextView)findViewById(R.id.textView);
                solutionTextView.setText("Value of variable is: "+solution);
            }*/
                EditText expressionText = (EditText)findViewById(R.id.editText);
                String expression=expressionText.getText().toString();
                EditText derivativeText=(EditText)findViewById(R.id.editText2);
                String derivative=derivativeText.getText().toString();
                NewtonRaphsonSolver equationSolver=new NewtonRaphsonSolver();
                LinkedHashMap<Integer,Double> solution=equationSolver.solveNewtonRaphson(expression,derivative);
                Intent intent = new Intent(getBaseContext(), ChartActivity.class);
                //intent.putExtra("EXTRA_SESSION_ID", sessionId);
                intent.putExtra("map", solution);

                startActivity(intent);
            }
        });


    }



}
