package ian.malerich.com.calculator;

import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CalcActivity extends AppCompatActivity {

    /** Stack of strings representing the current RPN expression. */
    private ArrayList<String> currentExpression = new ArrayList<>();

    /** The current value we are adding to. */
    private String currentValue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * The user entered a digit (0-9), we need to build up
     * the currentValue until the user either adds an operator
     * or hits the sigma key to input the value.
     *
     * @param view
     */
    public void didEnterDigit(View view) {
        if (currentValue == null) {
            currentValue = new String();
        }

        Button digit = (Button)view;
        currentValue += digit.getText();
        setValueToTextView();
    }

    /**
     * The user selected an operator, add it to the current expression stack.
     * @param view
     *  Calling view, this view will be a button whos text field
     *  represents the operator we are adding.
     */
    public void selectOperator(View view) {
        // if the user hasn't explicitely added the current value, add it here
        if (currentValue != null) {
            addToExpression(null);
        }

        Button operator = (Button)view;
        currentExpression.add(operator.getText().toString());
        evaluateExpression();
        setEquationToTextView();
    }

    /** If available, adds the contents of currentValue to the currentExpression */
    public void addToExpression(View view) {
        if (currentValue == null) {
            return;
        }

        currentExpression.add(currentValue);
        currentValue = null;
        setEquationToTextView();
        setValueToTextView();
    }

    /** Displays the content of currentExpression in the expression textView */
    public void setEquationToTextView() {
        TextView equationView = (TextView)findViewById(R.id.expression_view);
        if (currentExpression.size() == 0) {
            equationView.setText("");
            return;
        }

        String displayText = new String();
        for (String str : currentExpression) {
            displayText += (str + " ");
        }

        equationView.setText(displayText);
    }

    /** Displays the currentValue in the result view */
    public void setValueToTextView() {
        TextView resultView = (TextView)findViewById(R.id.result_view);
        resultView.setText(currentValue == null ? "0" : currentValue);
    }

    /** Applies any operators available in the currentExpression */
    public void evaluateExpression() {
        ArrayList<String> values = new ArrayList<>();

        for (String str : currentExpression) {
            if (!isOperator(str)) {
                values.add(str);
            } else {
                // an error occurred
                if (values.size() < 2) {
                    currentExpression.removeAll(currentExpression);
                    currentValue = null;
                    setEquationToTextView();
                    setValueToTextView();;
                    return;
                }

                double first = Double.parseDouble(values.remove(values.size()-1));
                double second = Double.parseDouble(values.remove(values.size()-1));
                values.add(Double.toString(first + second));
            }
        }

        currentExpression = values;
    }

    /** Determines whether or not the input String is to be considered a valid oeprator" */
    public boolean isOperator(String str) {
        if (str.equals("+") || str.equals("-")) {
            return true;
        }

        return false;
    }
}
