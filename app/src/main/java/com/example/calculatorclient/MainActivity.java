package com.example.calculatorclient;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calculatorservice.ICaltulatorXAidlInterface;

public class MainActivity extends AppCompatActivity {
    private ICaltulatorXAidlInterface mCalculatorService;
    TextView workingsTV;
    TextView resultsTV;

    String workings = "";
    Double result = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTextViews();
        Log.i("TAG", "onCreate: ");
        bindToCalculatorService();
    }

    private void bindToCalculatorService() {
        Intent intent = new Intent();
        intent.setAction("com.example.calculatorservice.ICaltulatorXAidlInterface");
        intent.setPackage("com.example.calculatorservice");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void initTextViews(){
        workingsTV = (TextView)findViewById((R.id.workingTextView));
        resultsTV = (TextView)findViewById((R.id.resultTextView));
    }

    private void setWorkings(String givenValue){
        if (result != null){
            if (result % 1 == 0){
                workings = String.valueOf(result.intValue());
            } else {
                workings = result.toString();
            }

            result = null;
            resultsTV.setText("");
        }
        workings += givenValue;
        Log.i("TAG", "setWorkings: " + workings);
        workingsTV.setText(workings);
    }



    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mCalculatorService = ICaltulatorXAidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mCalculatorService = null;
        }
    };

    boolean leftBracket = true;
    public void parenthesisOnClick(View view) {
        if (leftBracket){
            setWorkings("(");
            leftBracket = false;
        } else {
            setWorkings(")");
            leftBracket = true;
        }
    }

    public void negateOnClick(View view) {
        if (workings.charAt(0) == '-'){
            workings = workings.substring(1);
            workingsTV.setText(workings);
        } else {
            workings = "-" + workings;
            workingsTV.setText(workings);
        }
    }

    public void clearOnClick(View view) {
        workingsTV.setText("");
        workings = "";
        result = null;
        resultsTV.setText("");
    }

    public void equalOnClick(View view) {
        if (mCalculatorService != null) {
            try {
                double result = mCalculatorService.evaluateExpression(workings);
                if (!Double.isNaN(result)) {
                    if (result % 1 != 0.0) {
                        resultsTV.setText(String.valueOf(result));
                    } else {
                        resultsTV.setText(String.valueOf((int) result));
                    }
                } else {
                    Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
                }
            } catch (RemoteException e) {
                Toast.makeText(this, "Failed to evaluate expression", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Service null", Toast.LENGTH_SHORT).show();
        }
    }

    public void dotOnClick(View view) {
        setWorkings(".");
    }

    public void zeroOnClick(View view) {
        setWorkings("0");
    }

    public void addOnClick(View view) {
        setWorkings("+");
    }

    public void threeOnClick(View view) {
        setWorkings("3");
    }

    public void twoOnClick(View view) {
        setWorkings("2");
    }

    public void oneOnClick(View view) {
        setWorkings("1");
    }

    public void subtractOnClick(View view) {
        setWorkings("-");
    }

    public void sixOnClick(View view) {
        setWorkings("6");
    }

    public void fiveOnClick(View view) {
        setWorkings("5");
    }

    public void fourOnClick(View view) {
        setWorkings("4");
    }

    public void multiplyOnClick(View view) {
        setWorkings("x");
    }

    public void nineOnClick(View view) {
        setWorkings("9");
    }

    public void eightOnClick(View view) {
        setWorkings("8");
    }

    public void sevenOnClick(View view) {
        setWorkings("7");
    }

    public void divideOnClick(View view) {
        setWorkings("/");
    }

    public void modularOnClick(View view) {
        setWorkings("%");
    }


    public void deleteOnClick(View view) {
        if (workings.length() == 0){
            Toast.makeText(this, "Formula already empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("TAG", "deleteOnClick:" + workings.substring(workings.length() - 1));
        if (workings.substring(workings.length() - 1).equals("(")) {
            Log.i("TAG", "deleteOnClick: hi");
            leftBracket = true;
        }
        if (workings.substring(workings.length() - 1).equals(")")) {
            leftBracket = false;
        }
        workings = workings.substring(0, workings.length() - 1);
        workingsTV.setText(workings);
    }
}
