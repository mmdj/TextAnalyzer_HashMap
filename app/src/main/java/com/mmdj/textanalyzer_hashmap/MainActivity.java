package com.mmdj.textanalyzer_hashmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText EditTextInput;
   // ListView LstVw_Result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /****************************************************************************
     *  function to get text from the editText and put into the resultActivity  *
     ****************************************************************************/
    public void analyzeText(View view) {


        //doing change to weight of ListView
       // LstVw_Result = (ListView) findViewById(R.id.lstVw_result2);
       // LstVw_Result.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1f));



        EditTextInput = (EditText) findViewById(R.id.edtTxt_input);

        String TextInString = null;
        if (EditTextInput != null) {
            TextInString = EditTextInput.getText().toString();
        }

        if (TextInString != null && TextInString.isEmpty()) {                            //checking text
            doToast("There no text for analyze.");
            return;
        }



            Intent intent = new Intent(this, Result_Activity.class);
            intent.putExtra("TextInString", TextInString);
            startActivity(intent);
           // strArray = TextInString.split("[\\p{Punct}\\s]+");





    }




    /************************
     * For toasting!
     ***********************/
    private void doToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public void resetText(View view) {
        EditTextInput.setText("");
    }
}
