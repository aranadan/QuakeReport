package com.example.android.quakereport;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;



public class HttpHandler {
    static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }

    public static String makeServiceCall(final String reqUrl) {
        String response = null;
        try {
            final URL url = new URL(reqUrl);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response

            final InputStream in = new BufferedInputStream(conn.getInputStream());
            response = HttpHandler.convertStreamToString(in);

        } catch (final MalformedURLException e) {
            Log.e(HttpHandler.TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(HttpHandler.TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(HttpHandler.TAG, "IOException: " + e.getMessage());
        } catch (RuntimeException e) {
            Log.e(HttpHandler.TAG, "Exception: " + e.getMessage());
        }

        return response;
    }


    private static String convertStreamToString(final InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        @SuppressWarnings("StringBufferWithoutInitialCapacity") StringBuilder sb = new StringBuilder();

        String line;
        try{
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
