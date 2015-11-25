package ian.malerich.com.calculator;

import android.os.Bundle;
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

    private static final double epsilon = 0.00000001;

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
     *  The button the user tapped to enter a digit.
     */
    public void didEnterDigit(View view) {
        Button digit = (Button)view;
        if (digit.getText().equals("0") && currentValue == null) {
            // ignore 0s before entering characters
            return;
        }

        if (digit.getText().equals(".")) {
            // Entering a digit < 1, include a preceding 0
            if (currentValue == null) {
                currentValue = "0";
            }

            // the user cannot add another decimal value
            if (!canAddDecimal()) {
                return;
            }
        }

        if (currentValue == null) {
            currentValue = new String();
        }

        currentValue += digit.getText();
        setEquationToTextView();
    }

    /**
     * Swaps the sign of 'currentValue' from positive to negative
     * and back again.
     * @param view
     *  The button the user tapped to swap the sign.
     */
    public void swapSign(View view) {
        if (currentValue == null) {
            currentValue = "-";
            return;
        }

        if (currentValue.charAt(0) == '-') {
            currentValue = new String(currentValue.toCharArray(), 1, currentValue.length() - 1);
        } else {
            currentValue = "-" + currentValue;
        }

        setEquationToTextView();
    }

    /**
     * Clears all contents of the calculator.
     * @param view
     *  The button the user tapped to clear the contents.
     */
    public void clear(View view) {
        currentExpression = new ArrayList<>();
        currentValue = null;

        setEquationToTextView();
    }

    /**
     * Removes the last character from currentValue, if available.
     * If currentValue is null or has a length of 0, this method does nothing.
     *
     * @param view
     *  The button the user tapped to delete a character.
     */
    public void deleteCharFromValue(View view) {
        if (currentValue == null || currentValue.length() == 0) {
            return;
        }

        currentValue = new String(currentValue.toCharArray(), 0, currentValue.length() - 1);
        setEquationToTextView();
    }

    private boolean canAddDecimal() {
        return currentValue.indexOf(".") == -1;
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
    }

    /** Displays the content of currentExpression in the expression textView */
    public void setEquationToTextView() {
        TextView displayView = (TextView)findViewById(R.id.display_view);
        TextView currentView = (TextView)findViewById(R.id.current_view);

        String displayText = new String();
        for (String str : currentExpression) {
            displayText += (str + " ");
        }

        if (displayText.length() > 1) {
            displayText = new String(displayText.toCharArray(), 0, displayText.length() - 1);
        }

        displayView.setText(displayText);

        currentView.setText(currentValue == null ? " " : " " + currentValue);
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
                    return;
                }

                double second = Double.parseDouble(values.remove(values.size()-1));
                double first = Double.parseDouble(values.remove(values.size()-1));
                double result = performOperator(first, second, str);

                // Only print in decimal format for decimal values
                if (Math.abs(result - (int)result) > epsilon) {
                    values.add(Double.toString(result));
                } else {
                    // Otherwise ignore the extra '.0' characters
                    values.add(Integer.toString((int)result));
                }
            }
        }

        currentExpression = values;
    }

    /** Determines whether or not the input String is to be considered a valid oeprator" */
    public boolean isOperator(String str) {
        if (str.equals("+") || str.equals("-") || str.equals("÷")
                || str.equals("×") || str.equals("%")){
            return true;
        }

        return false;
    }

    /** Returns the result of operator 'op' on Operandes 'A' and 'B' */
    public double performOperator(double A, double B, String op) {
        if (op.equals("+")) {
            return A + B;
        } else if (op.equals("-")) {
            return A - B;
        } else if (op.equals("×")) {
            return A * B;
        } else if (op.equals("÷")) {
            return A / B;
        } else if (op.equals("%")) {
            return (int)A % (int)B;
        }

        return 0.0;
    }
}
