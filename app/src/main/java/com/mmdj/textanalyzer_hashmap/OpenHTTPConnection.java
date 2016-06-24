package com.mmdj.textanalyzer_hashmap;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class OpenHTTPConnection extends AsyncTask<String, Void, String> {
    private MainActivity main = new MainActivity();

    @Override
    protected String doInBackground(String... str) {
        String content = null;
        String currentURL = str[0];

        try {

            content = OpenHttpConnection(currentURL);
            Log.d(main.getLogTag(), "currentURL = " + currentURL);
        } catch (IOException ex) {
            Log.d(main.getLogTag(), "IOException in Background");
        }

        return content;
    }



    private String OpenHttpConnection(String strURL) throws IOException {

        HttpURLConnection conn;
        InputStream inputStream;
        StringBuilder content = new StringBuilder();//(--)

        try {
            URL url = new URL(strURL);
            Log.d(main.getLogTag(), "URL = " + url.toString());
            conn = (HttpURLConnection) url.openConnection();

            // just want to do an HTTP GET here
            conn.setRequestMethod("GET");


            // give it 10 seconds to respond
            conn.setReadTimeout(10 * 1000);

            conn.connect();

            inputStream = conn.getInputStream();
            InputStreamReader ISReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(ISReader);

            String line;


            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
