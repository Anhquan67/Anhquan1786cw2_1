package com.example.exercise_one_cw2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean canAddOperation = false;
    private boolean canAddDecimal = true;
    private TextView workingsTV;
    private TextView resultsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        workingsTV = findViewById(R.id.workingsTV);
        resultsTV = findViewById(R.id.resultsTV);
    }

    public void numberAction(View view) {
        if (view instanceof AppCompatButton) {
            AppCompatButton button = (AppCompatButton) view;
            if (button.getText().equals(".")) {
                if (canAddDecimal)
                    workingsTV.append(button.getText());
                canAddDecimal = false;
            } else
                workingsTV.append(button.getText());
            canAddOperation = true;
        }
    }

    public void operationAction(View view) {
        if (view instanceof AppCompatButton && canAddOperation) {
            AppCompatButton button = (AppCompatButton) view;
            workingsTV.append(button.getText());
            canAddOperation = false;
            canAddDecimal = true;
        }
    }

    public void allClearAction(View view) {
        workingsTV.setText("");
        resultsTV.setText("");
    }

    public void backSpaceAction(View view) {
        int length = workingsTV.getText().length();
        if (length > 0)
            workingsTV.setText(workingsTV.getText().subSequence(0, length - 1));
    }

    public void equalsAction(View view) {
        resultsTV.setText(calculateResults());
    }

    private String calculateResults() {
        List<Object> digitsOperators = digitsOperators();
        if (digitsOperators.isEmpty()) return "";
        List<Object> timesDivision = timesDivisionCalculate(digitsOperators);
        if (timesDivision.isEmpty()) return "";
        float result = addSubtractCalculate(timesDivision);
        return Float.toString(result);
    }

    private float addSubtractCalculate(List<Object> passedList) {
        float result = (float) passedList.get(0);
        for (int i = 0; i < passedList.size(); i++) {
            if (passedList.get(i) instanceof Character && i != passedList.size() - 1) {
                char operator = (char) passedList.get(i);
                float nextDigit = (float) passedList.get(i + 1);
                if (operator == '+')
                    result += nextDigit;
                if (operator == '-')
                    result -= nextDigit;
            }
        }
        return result;
    }

    private List<Object> timesDivisionCalculate(List<Object> passedList) {
        List<Object> list = passedList;
        while (list.contains('x') || list.contains('/')) {
            list = calcTimesDiv(list);
        }
        return list;
    }

    private List<Object> calcTimesDiv(List<Object> passedList) {
        List<Object> newList = new ArrayList<>();
        int restartIndex = passedList.size();
        for (int i = 0; i < passedList.size(); i++) {
            if (passedList.get(i) instanceof Character && i != passedList.size() - 1 && i < restartIndex) {
                char operator = (char) passedList.get(i);
                float prevDigit = (float) passedList.get(i - 1);
                float nextDigit = (float) passedList.get(i + 1);
                switch (operator) {
                    case 'x':
                        newList.add(prevDigit * nextDigit);
                        restartIndex = i + 1;
                        break;
                    case '/':
                        newList.add(prevDigit / nextDigit);
                        restartIndex = i + 1;
                        break;
                    default:
                        newList.add(prevDigit);
                        newList.add(operator);
                        break;
                }
            }
            if (i > restartIndex)
                newList.add(passedList.get(i));
        }
        return newList;
    }

    private List<Object> digitsOperators() {
        List<Object> list = new ArrayList<>();
        String currentDigit = "";
        if (workingsTV != null) {
            for (char character : workingsTV.getText().toString().toCharArray()) {
                if (Character.isDigit(character) || character == '.')
                    currentDigit += character;
                else {
                    list.add(Float.parseFloat(currentDigit));
                    currentDigit = "";
                    list.add(character);
                }
            }
            if (!currentDigit.equals(""))
                list.add(Float.parseFloat(currentDigit));
        }
        return list;
    }
}


