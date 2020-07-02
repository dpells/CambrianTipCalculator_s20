package ca.davidpellegrini.tipcalculator_s20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity
    implements OnClickListener, OnKeyListener, OnEditorActionListener, OnCheckedChangeListener,
    OnSeekBarChangeListener{

    private EditText billAmountEditText;
    private TextView tipPercentTextView, tipAmountTextView, totalTextView, splitTextView;
    private Button decreaseButton, increaseButton;
    private SeekBar tipPercentSeekBar;
    private CheckBox rememberTotal;
    private RadioGroup splitBillRadioGroup;
    private TableRow splitRow;

    private String billAmountString;
    private int numPeople, counter;
    private float billAmount, tipPercent, tipAmount, totalAmount, splitAmount;

    private SharedPreferences savedValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tipPercentTextView = findViewById(R.id.tipPercent_textView);
        totalTextView = findViewById(R.id.totalAmount_textview);
        splitRow = findViewById(R.id.splitRow);
        splitTextView = findViewById(R.id.splitAmount_textview);

        tipAmountTextView = findViewById(R.id.tipAmount_textview);
        tipAmountTextView.setOnClickListener(this);

        decreaseButton = findViewById(R.id.decrease_button);
        decreaseButton.setOnClickListener(this);

        increaseButton = findViewById(R.id.increase_button);
        increaseButton.setOnClickListener(this);

        billAmountEditText = findViewById(R.id.billAmount_edittext);
        billAmountEditText.setOnKeyListener(this);
        billAmountEditText.setOnEditorActionListener(this);
        billAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // auto generated. Don't need to do anything
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // auto generated. Don't need to do anything
            }

            @Override
            public void afterTextChanged(Editable s) {
                billAmountString = billAmountEditText.getText().toString();
                calculateAndDisplay();
            }
        });

        tipPercentSeekBar = findViewById(R.id.tipPercent_seekBar);
        tipPercentSeekBar.setOnKeyListener(this);
        tipPercentSeekBar.setOnSeekBarChangeListener(this);

        splitBillRadioGroup = findViewById(R.id.splitRadioGroup);
        splitBillRadioGroup.setOnCheckedChangeListener(this);

        rememberTotal = findViewById(R.id.rememberTip_checkBox);

        savedValues = getSharedPreferences("TipCalculatorSavedVales", MODE_PRIVATE);
    }

    @Override
    public void onResume(){
        super.onResume();

        billAmountString = savedValues.getString("billAmountString", "");
        tipPercent = savedValues.getFloat("tipPercent", 0.15f);
        numPeople = savedValues.getInt("numPeople", 2);
        rememberTotal.setChecked(savedValues.getBoolean("rememberTotal", true));
        switch (numPeople){
            case 1:
                splitBillRadioGroup.check(R.id.split1);
                break;
            case 2:
                splitBillRadioGroup.check(R.id.split2);
                break;
            case 3:
                splitBillRadioGroup.check(R.id.split3);
                break;
            case 4:
                splitBillRadioGroup.check(R.id.split4);
                break;
        }
        calculateAndDisplay();
    }

    public void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();

        if(rememberTotal.isChecked()){
            editor.putString("billAmountString", billAmountString);
            editor.putFloat("tipPercent", tipPercent);
            editor.putInt("numPeople", numPeople);
            editor.putBoolean("rememberTotal", true);
        }
        else{
            editor.clear();
            editor.putBoolean("rememberTotal", false);
        }
        editor.apply();

        super.onPause();
    }

    public void calculateAndDisplay(){
        billAmountString = billAmountEditText.getText().toString();
        if(billAmountString.equals("")){
            billAmount = 0;
        }
        else{
            billAmount = Float.parseFloat(billAmountString);
        }

        if(numPeople == 1){
            splitRow.setVisibility(View.GONE);
        }
        else{
            splitRow.setVisibility(View.VISIBLE);
        }

        int percent = (int)(tipPercent * 100);
        tipPercentTextView.setText(percent + "%");
        tipPercentSeekBar.setProgress(percent);

        tipAmount = billAmount * tipPercent;
        totalAmount = billAmount + tipAmount;
        splitAmount = totalAmount / numPeople;

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tipAmountTextView.setText(currency.format(tipAmount));
        totalTextView.setText(currency.format(totalAmount));
        splitTextView.setText(currency.format(splitAmount));
    }

    @Override
    public void onClick(View v) {
        //to something when a click event happens
        switch (v.getId()){
            case R.id.decrease_button:
                tipPercent = tipPercent - 0.05f;
                break;
            case R.id.increase_button:
                tipPercent = tipPercent + 0.05f;
                break;
            case R.id.tipAmount_textview:
                counter++;
                Toast.makeText(this, Integer.toString(counter), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        calculateAndDisplay();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                calculateAndDisplay();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(billAmountEditText.getWindowToken(), 0);
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if(v.getId() == R.id.tipPercent_seekBar){
                    calculateAndDisplay();
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED)  {
            calculateAndDisplay();
        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.split2:
                numPeople = 2;
                break;
            case R.id.split3:
                numPeople = 3;
                break;
            case R.id.split4:
                numPeople = 4;
                break;
            case R.id.split1:
            default:
                numPeople = 1;
                break;
        }
        calculateAndDisplay();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //tipPercentTextView.setText(progress + "%");
        tipPercent = progress / 100f;
        calculateAndDisplay();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // This was auto generated, we don't need to do any work
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        tipPercent = seekBar.getProgress() / 100f;
        calculateAndDisplay();
    }
}
