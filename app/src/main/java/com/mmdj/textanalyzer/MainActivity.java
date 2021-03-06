package com.mmdj.textanalyzer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mmdj.textanalyzer.connection.OpenHTTPConnection;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String LOG_TAG = "analyzer";
    private EditText editTextInput;
    private EditText editTextURLInput;

    public String getLogTag() {
        return LOG_TAG;
    }

    // ListView LstVw_Result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextURLInput = (EditText) findViewById(R.id.edtTxt_URL_Input);
        editTextInput = (EditText) findViewById(R.id.edtTxt_input);
        Button btnGetURL = (Button) findViewById(R.id.btn_getURL);
        Button btnAnalyze = (Button) findViewById(R.id.btn_analyze);
        Button btnReset = (Button) findViewById(R.id.btn_reset);


        if (btnGetURL != null) {
            btnGetURL.setOnClickListener(this);
        }
        if (btnAnalyze != null) {
            btnAnalyze.setOnClickListener(this);
        }
        if (btnReset != null) {
            btnReset.setOnClickListener(this);
        }

    }


    /***************** Menu **********************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    /*****************End of menu **********************/



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getURL:
                get_address(v);
                break;
            case R.id.btn_analyze:
                getTextFromActivity(v);
                break;
            case R.id.btn_reset:
                resetText(v);
                break;
        }

    }

    /****************************************************************************
     * function to get text from the editText and put into the resultActivity  *
     ****************************************************************************/
    public void getTextFromActivity(View view) {

        String textInString = null;

        if (editTextInput != null) {                                               //from text
            textInString = editTextInput.getText().toString();
        }

        if (textInString == null || textInString.isEmpty()) {                      //checking text
            doToast(getString(R.string.CheckText4Analyze));
            return;
        }

        Intent intent = new Intent(this, Result_Activity.class);
        intent.putExtra("textInString", textInString);
        startActivity(intent);
        // strArray = textInString.split("[\\p{Punct}\\s]+");


    }



    public void get_address(View view) {
        OpenHTTPConnection httpConnection;
        if (editTextURLInput.getText().toString().equals("")) {
            doToast(getString(R.string.CheckAddress));
        } else {
            String strURL = editTextURLInput.getText().toString();

            //add "http://" to URL
            if (!strURL.startsWith("http")){
                String strUrlWithHTTP = "http://";
                strUrlWithHTTP = strUrlWithHTTP.concat(strURL);
                Log.d(LOG_TAG, "strUrlWithHTTP: " + strUrlWithHTTP + " strURL: " +strURL);
                strURL = strUrlWithHTTP;
                Log.d(LOG_TAG, "new strURL: " +strURL);
            }

                Log.d(LOG_TAG, "Connection start");
            httpConnection = new OpenHTTPConnection();
            httpConnection.execute(strURL);


            try {
                //checking why doesn't work
                if(httpConnection.get()=="") {
                    String command = "ping -c 1 google.com";
                    if(!(Runtime.getRuntime().exec (command).waitFor() == 0)){
                        doToast(getString(R.string.ConnectionError));
                    }else {
                        doToast(getString(R.string.AddressIsWrong));
                    }
                    return;
                }
                //set text to editText from internet
                editTextInput.setText(httpConnection.get(2, TimeUnit.SECONDS));

            } catch (InterruptedException e) {
                Log.d(LOG_TAG, "InterruptedException");
            } catch (ExecutionException e) {
                Log.d(LOG_TAG, "ExecutionException");
            } catch (TimeoutException e) {
                Log.d(LOG_TAG, "TimeoutException");
            } catch (IOException e) {
                Log.d(LOG_TAG, "IOException");
            }

        }
    }

    public void resetText(View view) {
        ShowHideKeyboard();

        editTextInput.setText(R.string.forReset);
        editTextURLInput.setText(R.string.http2TextURLInput);
    }

   /*********  Hide/show keyboard  *****************/
    private void ShowHideKeyboard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    /************************
     * For toasting!
     ***********************/

    private  void doToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
        Log.d(LOG_TAG, "was Toast: " + message);
    }


}
