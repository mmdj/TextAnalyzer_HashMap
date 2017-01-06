package com.mmdj.textanalyzer.connection;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenHTTPConnection extends AsyncTask<String, Void, String> {
    private static final String CHARSET = "UTF-8";
   // private MainActivity main = new MainActivity();

    @Override
    protected String doInBackground(String... str) {
        StringBuilder content;
        String currentURL = str[0];
        String result = null;


        try {
            content = OpenHttpConnection(currentURL, CHARSET);
            // Log.d(main.getLogTag(), "content1: done");
            // Log.d(main.getLogTag(), "currentURL = " + currentURL);
            if (content.toString().contains("harset")) {
                String realCharset = getCharset(content);
                if (!CHARSET.equals(realCharset)) {
                    content = OpenHttpConnection(currentURL, realCharset);
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


    private StringBuilder OpenHttpConnection(String strURL, String charset) throws IOException {

        HttpURLConnection conn;
        InputStream inputStream;
        StringBuilder content = new StringBuilder();//(--)

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
                    // Log.d(main.getLogTag(), "charsetArrayIf[0]: " + charsetArray[0]);
                    //Log.d(main.getLogTag(), "charsetArrayIf[1]: " + charsetArray[1]);
                }

                // Log.d(main.getLogTag(), "charset: " + charset);

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
