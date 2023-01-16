package edu.ucsd.cse110.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {
    // The value last entered before pressing plus or times.
    // We use BigDecimal because float/double can have strange results.
    // For example: 3.3 * 3 = 9.89999999...
    private BigDecimal storedValue = BigDecimal.valueOf(0.0);
    // The operation last entered. Optional is a safer alternative to null.
    private Optional<String> pendingOp = Optional.empty();
    // The current string shown on the calculator display.
    private String displayStr = "0";

    private TextView display;
    private Button[] numBtns = new Button[10];
    private Button btnDot, btnPlus, btnTimes, btnEquals, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve a reference to the display.
        display = findViewById(R.id.display);

        // Retrieve references to all of the numerical buttons.
        numBtns[0] = findViewById(R.id.btn_zero);
        numBtns[1] = findViewById(R.id.btn_one);
        numBtns[2] = findViewById(R.id.btn_two);
        numBtns[3] = findViewById(R.id.btn_three);
        numBtns[4] = findViewById(R.id.btn_four);
        numBtns[5] = findViewById(R.id.btn_five);
        numBtns[6] = findViewById(R.id.btn_six);
        numBtns[7] = findViewById(R.id.btn_seven);
        numBtns[8] = findViewById(R.id.btn_eight);
        numBtns[9] = findViewById(R.id.btn_nine);

        // Retrieve references to all of the math operation buttons.
        btnClear = findViewById(R.id.btn_clear);
        btnDot = findViewById(R.id.btn_dot);
        btnPlus = findViewById(R.id.btn_plus);
        btnTimes = findViewById(R.id.btn_times);
        btnEquals = findViewById(R.id.btn_equals);

        // Register the number button click handler.
        for (var numBtn : numBtns) {
            numBtn.setOnClickListener(this::onNumberButtonClicked);
        }

        // Register the operation (plus, times) buttons.
        for (var opBtn : new Button[] {btnPlus, btnTimes}) {
            opBtn.setOnClickListener(this::onOperationButtonClicked);
        }

        // Register the dot button click handler.
        btnDot.setOnClickListener(this::onDotButtonClicked);

        // Register the clear button.
        btnClear.setOnClickListener(this::onClearButtonClicked);

        // Register the equals button (this is where the magic happens!)
        btnEquals.setOnClickListener(this::onEqualsButtonClicked);
    }

    private void onNumberButtonClicked(View view) {
        // Before casting, it's best to be sure we actually have what we think!
        assert view instanceof Button;
        Button btn = (Button) view;

        // Retrieve the text from the button, and append it to the display.
        var num = btn.getText();
        if (displayStr.equals("0")) {
            // If the display is just 0, set it to the digit entered.
            displayStr = num.toString();
        } else {
            // Otherwise, append the next digit.
            displayStr += num;
        }
        updateDisplay();
    }

    private void onDotButtonClicked(View view) {
        assert view instanceof Button;
        Button btn = (Button) view;

        // If we already have a decimal, don't do anything.
        if (displayStr.contains(".")) return;
        displayStr += ".";
        updateDisplay();
    }

    private void onOperationButtonClicked(View view) {
        assert view instanceof Button;
        Button btn = (Button) view;

        // Parse the current number and save it to "previous value".
        storedValue = new BigDecimal(displayStr);
        clearDisplay();

        // Set the pending operation.
        pendingOp = Optional.of(btn.getText().toString());
    }

    private void onClearButtonClicked(View view) {
        clearDisplay();
    }

    private void onEqualsButtonClicked(View view) {
        // Parse the current value.
        var currValue = new BigDecimal(displayStr);

        // Compute the result.
        // The map method calls the given lambda with the value inside
        // the Optional if it exists (is not empty). The orElse provides
        // a fallback value if there is no operation. In this case, it
        // is whatever is already on the screen.
        BigDecimal result = pendingOp.map((opStr) -> {
            if (opStr.equals("+")) {
                return storedValue.add(currValue);
            }
            if (opStr.equals("×")) {
                return storedValue.multiply(currValue);
            }
            throw new RuntimeException("Unknown operation: " + opStr);
        }).orElse(currValue);

        // Clear the pending operation and update the display.
        pendingOp = Optional.empty();
        displayStr = String.valueOf(result);
        trimDisplayStr();
        updateDisplay();
    }

    private void updateDisplay() {
        display.setText(displayStr);
    }

    private void clearDisplay() {
        displayStr = "0";
        updateDisplay();
    }

    private void trimDisplayStr() {
        // If the string does not contain a decimal point, don't do anything.
        if (!displayStr.contains(".")) {
            return;
        }
        // Trim off any extra "0s" at the end.
        var cleanedStr = displayStr;
        while (cleanedStr.endsWith("0")) {
            cleanedStr = cleanedStr.substring(0, cleanedStr.length() - 1);
        }
        // And now if it ends with a ".", trim that too.
        if (cleanedStr.endsWith(".")) {
            cleanedStr = cleanedStr.substring(0, cleanedStr.length() - 1);
        }
        displayStr = cleanedStr;
    }
}