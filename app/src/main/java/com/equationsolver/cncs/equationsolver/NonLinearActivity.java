package com.equationsolver.cncs.equationsolver;

import android.content.Intent;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.LinkedHashMap;

public class NonLinearActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_linear);
        final Button button = findViewById(R.id.calculate);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int iterations = 0;
                double initialGuess = 0;
                TextInputLayout expressionLayout = findViewById(R.id.TextInputLayoutExpression);
                expressionLayout.setErrorEnabled(true);
                TextInputLayout derivativeLayout = findViewById(R.id.TextInputLayoutDerivative);
                derivativeLayout.setErrorEnabled(true);
                EditText expressionText = findViewById(R.id.editText);
                String expression = expressionText.getText().toString();
                EditText derivativeText = findViewById(R.id.editText2);
                String derivative = derivativeText.getText().toString();
                if (TextUtils.isEmpty(expressionText.getText()) && TextUtils.isEmpty(derivativeText.getText())) {
                    expressionLayout.setError(getString(R.string.expression_mandatory));
                    derivativeLayout.setError(getString(R.string.mandatory_derivative));
                    return;
                }

                if (expression.contains("=")) {
                    expressionLayout.setError(getString(R.string.wrong_expression));
                    return;
                }
                if (TextUtils.isEmpty(expressionText.getText())) {
                    expressionLayout.setError(getString(R.string.expression_mandatory));
                    return;
                }
                if (TextUtils.isEmpty(derivativeText.getText())) {
                    derivativeLayout.setError(getString(R.string.mandatory_derivative));
                    return;
                }
                EditText iterationText = findViewById(R.id.iterationText);
                String iterationString = iterationText.getText().toString();
                if (null != iterationString && !"".equals(iterationString)) {
                    iterations = Integer.parseInt(iterationString);
                }


                EditText initialGuessText = (EditText) findViewById(R.id.initialGuess);
                String initialGuessString = initialGuessText.getText().toString();
                if (null != initialGuessString && !"".equals(initialGuessString)) {
                    initialGuess = Double.parseDouble(initialGuessString);
                }


                NewtonRaphsonSolver equationSolver = new NewtonRaphsonSolver();
                LinkedHashMap<Integer, Double> solution = equationSolver.solveNewtonRaphson(expression, derivative, iterations, initialGuess);
                Intent intent = new Intent(getBaseContext(), ChartActivity.class);
                //intent.putExtra("EXTRA_SESSION_ID", sessionId);
                intent.putExtra("map", solution);

                startActivity(intent);
            }
        });


    }


}
