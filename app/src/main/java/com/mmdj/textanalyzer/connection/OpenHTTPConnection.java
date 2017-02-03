package com.mmdj.textanalyzer.connection;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.mmdj.textanalyzer.MainActivity;
import com.mmdj.textanalyzer.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenHTTPConnection extends AsyncTask<String, Void, String> {
    private static final String CHARSET = "UTF-8";
    private static final String LOG_TAG = "httpConnection";
    // private MainActivity main = new MainActivity();
    private ProgressBar progressBar;
    private EditText editTextInput;
    private MainActivity activity;

    public OpenHTTPConnection(EditText editTextInput,MainActivity activity) {
        this.editTextInput = editTextInput;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //show progress bar
        progressBar = (ProgressBar) activity.findViewById(R.id.mainProgressBar);
        if (progressBar != null) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

    }

    @Override
    protected String doInBackground(String... str) {
        StringBuilder content;
        String currentURL = str[0];
        String result = null;


        try {
            content = openHttpConnection(currentURL, CHARSET);
            // Log.d(LOG_TAG, "content1: done");
            // Log.d(LOG_TAG, "currentURL = " + currentURL);
            if (content.toString().contains("harset")) {
                String realCharset = getCharset(content);

                if (!CHARSET.equals(realCharset)) {
                    content = openHttpConnection(currentURL, realCharset);
                    Log.d("analyze", "Encoding HTML: " + realCharset);
                }
            }
            result = getContentWithoutHTML(content);
            Log.d("analyze", "resultWithoutHTML: " + result);

        } catch (IOException ex) {
            Log.d("analyze", "IOException in HttpConnection");
        } catch (NullPointerException e) {
            Log.d("analyze", "NullPointerException in HttpConnection" + e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            //checking why doesn't work
            if(s.equals("")) {
                String command = "ping -c 1 google.com";
                if(!(Runtime.getRuntime().exec (command).waitFor() == 0)){
                    activity.doToast(activity.getString(R.string.ConnectionError));
                }else {
                    activity.doToast(activity.getString(R.string.AddressIsWrong));
                }
                return;
            }
            //hide progress bar
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            //set text to editText from internet
            editTextInput.setText(s);

        } catch (InterruptedException e) {
            Log.d(LOG_TAG, "InterruptedException");
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException");
        }//progress bar gone
    }

    private StringBuilder openHttpConnection(String strURL, String charset) throws IOException {

        HttpURLConnection conn;
        InputStream inputStream;
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(strURL);
            Log.d("analyze", "URL = " + url.toString());

            conn = (HttpURLConnection) url.openConnection();

            //  HTTP GET here
            conn.setRequestMethod("GET");


            // 10 seconds to respond
            conn.setReadTimeout(10 * 1000);

            conn.connect();
            inputStream = conn.getInputStream();

            InputStreamReader ISReader = new InputStreamReader(inputStream, charset);
            BufferedReader bufferedReader = new BufferedReader(ISReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return content;

    }


    private String getContentWithoutHTML(StringBuilder content) {
        //deleting all tags from HTML:         |                          tags                             |      comments      |       other       |   special chars  |
        return content.toString().replaceAll("(<script(\\s|\\S)*?<\\/script>)|(<style(\\s|\\S)*?<\\/style>)|(<!--(\\s|\\S)*?-->)|(<\\/?(\\s|\\S)*?>)|&(?:[a-z]+|#\\d+);|[\\n]", " ");
        //"(<script(\s|\S)*?<\/script>)|(<style(\s|\S)*?<\/style>)|(<!--(\s|\S)*?-->)|(<\/?(\s|\S)*?>)|&(?:[a-z]+|#\d+);|[\n]";
    }

    private String getCharset(StringBuilder content) {

        String[] charsetArray = content.toString().split("harset( *)=");
        //  Log.d(main.getLogTag(), "charsetArray[1]: " + charsetArray[1]);

        String charset = "UTF-8";
        try {
            if (charsetArray[1] != null) {
                charsetArray = charsetArray[1].split("\"");
                charset = charsetArray[0];


                if (charset.equals("")) {
                    charset = charsetArray[1];
                    // Log.d(GET_TAG, "charsetArrayIf[0]: " + charsetArray[0]);
                    //Log.d(GET_TAG, "charsetArrayIf[1]: " + charsetArray[1]);
                }

                // Log.d(GET_TAG, "charset: " + charset);

            }
        } catch (Exception e) {
            Log.d("analyze", "Exception: " + e);
        }
        return charset;
    }


    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
