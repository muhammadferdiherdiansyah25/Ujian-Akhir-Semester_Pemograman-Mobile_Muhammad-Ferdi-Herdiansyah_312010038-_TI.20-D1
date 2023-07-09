package com.irga.irga_ramadhan_databaseapiproject.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.irga.irga_ramadhan_databaseapiproject.hargaponline.adapter.DetailHandphone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AsyncInvokeURLTask extends AsyncTask<Void, Void, String> {
    public boolean showdialog;
    public String message;
    public DetailHandphone applicationContext;
    public String mNoteItWebUrl;
    private boolean showProgressDialog = false;
    private String progressDialogMessage = "Proses Data";
    private String urlServer = "http://www.bibitbagus.id/xphone/";
    private Context appContext;
    private OnPostExecuteListener postExecuteListener;
    private List<NameValuePair> params;

    public interface OnPostExecuteListener {
        void onPostExecute(String result);
    }

    public AsyncInvokeURLTask(List<NameValuePair> nameValuePairs, OnPostExecuteListener postExecuteListener) throws Exception {
        this.params = nameValuePairs;
        this.postExecuteListener = postExecuteListener;
        if (this.postExecuteListener == null)
            throw new Exception("Param cannot be null.");
    }

    @Override
    protected void onPreExecute() {
        if (showProgressDialog) {
            ProgressDialog progressDialog = ProgressDialog.show(applicationContext, "", "Silakan Menunggu...", true);
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = "timeout";
        HttpURLConnection urlConnection = null;
        try {
            StringBuilder postData = new StringBuilder();
            for (NameValuePair pair : params) {
                if (postData.length() != 0) {
                    postData.append('&');
                }
                postData.append(URLEncoder.encode(pair.getName(), StandardCharsets.UTF_8));
                postData.append('=');
                postData.append(URLEncoder.encode(pair.getValue(), StandardCharsets.UTF_8));
            }
            byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

            URL url = new URL(urlServer + mNoteItWebUrl);  // Menggunakan URL lengkap
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.getOutputStream().write(postDataBytes);

            InputStream inputStream = urlConnection.getInputStream();
            result = convertStreamToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (postExecuteListener != null) {
            try {
                postExecuteListener.onPostExecute(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
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
