package com.example.kapde_wala;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.strictmode.CredentialProtectedWhileLockedViolation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundWorker extends AsyncTask<String,Void,String>{
    Context context;
    AlertDialog alertDialog;
    BackgroundWorker(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... strings) {
        String type = strings[0];
        String login_url = "http://10.0.2.2/login.php";
        if(type.equals("login")){
            try {
                String user_enno = strings[1];
                String user_pass = strings[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8" ));
                String post_data = URLEncoder.encode("user_enno", "UTF-8")+"="+URLEncoder.encode(user_enno, "UTF-8")+"&"+URLEncoder.encode("user_pass", "UTF-8")+"="+URLEncoder.encode(user_pass, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String line="";
                String result="";
                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        if(!result.equals("Login Success")){
            alertDialog.setMessage(result);
            alertDialog.show();
            return;
        }
        ((LoginActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((LoginActivity)context).Login();
            }
        });
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
