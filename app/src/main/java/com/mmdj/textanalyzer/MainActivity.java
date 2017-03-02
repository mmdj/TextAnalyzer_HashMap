package com.mmdj.textanalyzer;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mmdj.textanalyzer.connection.OpenHTTPConnection;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

//import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public final String LOG_TAG = "analyzer";
    private EditText editTextInput;
    private EditText editTextURLInput;


    // ListView LstVw_Result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextURLInput = (EditText) findViewById(R.id.edtTxt_URL_Input);
        editTextInput = (EditText) findViewById(R.id.edtTxt_input);
        ImageButton btnGetURL = (ImageButton) findViewById(R.id.btn_getURL);
        Button btnAnalyze = (Button) findViewById(R.id.btn_analyze);
        Button btnReset = (Button) findViewById(R.id.btn_reset);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.insertText_fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //sending email:
                    String textToPaste = null;

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                    if (clipboard.hasPrimaryClip()) {
                        ClipData clip = clipboard.getPrimaryClip();

                        // if you need text data only, use:
                        if (clip.getDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))
                            // WARNING: The item could cantain URI that points to the text data.
                            // In this case the getText() returns null and this code fails!
                            textToPaste = clip.getItemAt(0).getText().toString();

                            //  Log.d(LOG_TAG, "textToPaste " + textToPaste);
                            // or you may coerce the data to the text representation:
                        else textToPaste = clip.getItemAt(0).coerceToText(MainActivity.this).toString();
                    }


                    if (editTextURLInput.hasFocus()) {
                        editTextURLInput.setText(textToPaste);
                    } else editTextInput.setText(textToPaste);
                     showHideKeyboard();

                }


            });
        }

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
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        alertDialogBuilder.setTitle(R.string.dialogExit);
        alertDialogBuilder
                .setMessage(R.string.dialogExit_touch)
                .setCancelable(false)
                .setPositiveButton(R.string.dialogExit_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                Process.killProcess(Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton(R.string.dialogExit_No, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /*****************
     * Menu
     **********************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    /*****************
     * End of menu
     **********************/


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getURL:
                get_address();
                break;
            case R.id.btn_analyze:
                getTextFromActivity();
                break;
            case R.id.btn_reset:
                resetText();
                break;
        }

    }

    /****************************************************************************
     * function to get text from the editText and put into the resultActivity  *
     ****************************************************************************/
    public void getTextFromActivity() {

        String textInString = null;

        if (editTextInput != null) {                                               //from text
            textInString = editTextInput.getText().toString().trim();
        }

        if (textInString == null || textInString.isEmpty()) {                      //checking text
            doToast(getString(R.string.CheckText4Analyze));
            return;
        }

        //this give textInString to Result_Activity
        Intent intent = new Intent(this, Result_Activity.class);
        intent.putExtra("textInString", textInString);
        startActivity(intent);
        // strArray = textInString.split("[\\p{Punct}\\s]+");


    }


    public void get_address() {
        OpenHTTPConnection httpConnection;
        if (editTextURLInput.getText().toString().equals("")) {
            doToast(getString(R.string.CheckAddress));
        } else {
            String strURL = editTextURLInput.getText().toString();

            //add "http://" to URL
            if (!strURL.startsWith("http")) {
                String strUrlWithHTTP = "http://";
                strUrlWithHTTP = strUrlWithHTTP.concat(strURL);
                Log.d(LOG_TAG, "strUrlWithHTTP: " + strUrlWithHTTP + " strURL: " + strURL);
                strURL = strUrlWithHTTP;
                Log.d(LOG_TAG, "new strURL: " + strURL);
            }

            Log.d(LOG_TAG, "Connection start");
            httpConnection = new OpenHTTPConnection(editTextInput, this);
            httpConnection.execute(strURL);


        }
    }

    public void resetText() {
        showHideKeyboard();

        editTextInput.setText(R.string.forReset);
        editTextURLInput.setText(R.string.http2TextURLInput);
    }

    /*********
     * Hide/show keyboard
     *****************/
    private void showHideKeyboard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    /************************
     * For toasting!
     ***********************/

    public void doToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
        Log.d(LOG_TAG, "was Toast: " + message);
    }


}
