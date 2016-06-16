package com.mmdj.textanalyzer_hashmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = "analyzer";
    private EditText editTextInput;
    private EditText editTextURLInput;


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


        //doing change to weight of ListView
        // LstVw_Result = (ListView) findViewById(R.id.lstVw_result2);
        // LstVw_Result.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1f));


        String TextInString = null;


        if (editTextInput != null) {                                               //from text
            TextInString = editTextInput.getText().toString();
        }


        if (TextInString == null || TextInString.isEmpty()) {                            //checking text
            doToast("There no text for analyze.");
            return;
        }


        Intent intent = new Intent(this, Result_Activity.class);
        intent.putExtra("TextInString", TextInString);
        startActivity(intent);
        // strArray = TextInString.split("[\\p{Punct}\\s]+");


    }

    /**
     * convert InputStream to String
     */
    private String getStringsFromInputStream(InputStream is) {
        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /** finally block to close the {@link BufferedReader} */
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    /**
     * get stream from URL
     *
     * @throws IOException
     */
    private InputStream OpenHttpConnection(String strURL)
            throws IOException {
        URLConnection conn;
        InputStream inputStream = null;
        URL url = new URL(strURL);
        conn = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) conn;
        httpConn.setRequestMethod("GET");
        httpConn.connect();
        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            inputStream = httpConn.getInputStream();
        }
        return inputStream;
    }


    public void resetText(View view) {
        editTextInput.setText("");
        editTextURLInput.setText("");
    }


    public void get_address(View view) {
        doToast("Get");
        Log.d(LOG_TAG, "get_address ok");


        if (editTextURLInput != null) {
            String strURL = editTextURLInput.getText().toString();
            String TextInString = null;

            try {
                Log.d(LOG_TAG, "Connection ok1");
                InputStream is = OpenHttpConnection(strURL);
                TextInString = getStringsFromInputStream(is);
                Log.d(LOG_TAG, "Connection ok2");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "Connection error");
                doToast("Connection error");
            }
            editTextInput.setText(TextInString);
        } else {
            doToast("Address field is empty.");
        }

    }

    /************************
     * For toasting!
     ***********************/

    private void doToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
        Log.d(LOG_TAG, message);
    }



}
