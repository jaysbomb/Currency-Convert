package com.example.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class ShowNowMainActivity extends AppCompatActivity {
    private TextView mTextView;
    private ConnectivityManager mConnMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_now_main);
        mConnMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        mTextView = (TextView)findViewById(R.id.textview1);

    }

    public void loadData(View v){

        String path = "http://api.fixer.io/latest?base=HKD";

        if (mConnMgr != null){
            NetworkInfo networkInfo = mConnMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                new DownloadDataTask().execute(path);
            }else {
                Toast.makeText(this,"Not avaliable",Toast.LENGTH_LONG);
            }

        }

    }
    private class DownloadDataTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String...urls){
            return downloadData(urls[0]);
        }

        protected void onPostExecute(String data){
            if (data != null) {
                try{
                    JSONObject reader = new JSONObject(data);

                    String baseString = reader.getString("base");
                    mTextView.setText("Exchange rates for"+baseString+"\n"+'\n');

                    String dateString = reader.getString("date");
                    mTextView.setText(mTextView.getText()+"Date:"+dateString+'\n'+'\n');

                    JSONObject rateObject = reader.getJSONObject("rates");

                    Double cnyRate = rateObject.getDouble("CNY");
                    Double usdRate = rateObject.getDouble("USD");
                    Double eurRate = rateObject.getDouble("EUR");
                    Double audRate = rateObject.getDouble("AUD");
                    Double gbpRate = rateObject.getDouble("GBP");
                    Double jpyRate = rateObject.getDouble("JPY");

                    mTextView.setText(mTextView.getText()+"CNY:"+cnyRate.toString()+"\n");
                    mTextView.setText(mTextView.getText()+"USD:"+usdRate.toString()+"\n");
                    mTextView.setText(mTextView.getText()+"EUR:"+eurRate.toString()+"\n");
                    mTextView.setText(mTextView.getText()+"AUD:"+audRate.toString()+"\n");
                    mTextView.setText(mTextView.getText()+"GBP:"+gbpRate.toString()+"\n");
                    mTextView.setText(mTextView.getText()+"JPY:"+jpyRate.toString()+"\n");
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }

        public String downloadData(String path){
            String data = null;
            InputStream inStream;

            try{
                URL url = new URL(path);
                HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
                urlConn.setConnectTimeout(5000);
                urlConn.setReadTimeout(2500);
                urlConn.setRequestMethod("GET");
                urlConn.setDoInput(true);

                urlConn.connect();
                inStream =urlConn.getInputStream();
                data = readStream(inStream);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return data;
        }

        private String readStream(InputStream in){
            BufferedReader reader = null;
            StringBuffer data = new StringBuffer("");
            try{
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line=reader.readLine())!=null){
                    data.append(line);
                }

            }catch (IOException e){
                Log.e("HttpGetTask","IOExceprion");
            }finally {
                if(reader != null){
                    try {
                        reader.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            return data.toString();

        }
    }


}
