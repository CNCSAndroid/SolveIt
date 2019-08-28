package com.cncs.solveit;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button calculateButton = findViewById(R.id.calculate);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout expressionLayout = findViewById(R.id.TextInputLayoutExpression);
                expressionLayout.setErrorEnabled(true);
                TextInputLayout derivativelayout= findViewById(R.id.TextInputLayoutDerivative);
                derivativelayout.setErrorEnabled(true);
                EditText expressionText = findViewById(R.id.expressionText);
                String expression= expressionText.getText().toString();
                EditText derivativeText = findViewById(R.id.derivativeText);
                String derivative= derivativeText.getText().toString();
                int iterations = 0;
                double initialGuess = 0;
                if(TextUtils.isEmpty(expression) && TextUtils.isEmpty(derivative)){
                    expressionLayout.setError("Expression is mandatory");
                    derivativelayout.setError("Derivative is mandatory");
                    return;
                }
                if (expression.contains("=")) {
                    expressionLayout.setError("Kindly provide only expression");
                    return;
                }
                if (TextUtils.isEmpty(expressionText.getText())) {
                    expressionLayout.setError("Expression is mandatory");
                    return;
                }
                if (TextUtils.isEmpty(derivativeText.getText())) {
                    derivativelayout.setError("Derivative is mandatory");
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
            }
        });
    }
}
