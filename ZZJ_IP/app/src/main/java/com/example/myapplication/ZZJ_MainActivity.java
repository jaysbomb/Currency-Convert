package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.myapplication.R.id.editText_currency;
import static com.example.myapplication.R.id.spinner;
import static com.example.myapplication.R.id.spinner2;

public class ZZJ_MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView mTextView;
    String[] fromArray, toArray;
    private Button button;
    private EditText editText_money;
    private AQuery aq;
    private ConnectivityManager mConnMgr;
    String from,to;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zzj__main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mConnMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        mTextView = (TextView)findViewById(R.id.textView4);

        fromArray=getResources().getStringArray(R.array.money_array);
        final Spinner fSpinner = (Spinner) findViewById(spinner);
        ArrayAdapter<String> adapterFrom = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, fromArray);
        fSpinner.setAdapter(adapterFrom);

        toArray=getResources().getStringArray(R.array.money_array);
        final Spinner tSpinner = (Spinner) findViewById(spinner2);
        ArrayAdapter<String> adapterTo = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, toArray);
        tSpinner.setAdapter(adapterTo);

        //button = (ImageButton) findViewById(R.id.imageButton);
        editText_money = (EditText)findViewById(editText_currency);
       //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        fSpinner.setOnItemSelectedListener(new
                                                   Spinner.OnItemSelectedListener() {
                                                       @Override
                                                       public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                                                  int arg2, long arg3) {
                                                           from = arg0.getSelectedItem().toString();
                                                       }
                                                       @Override
                                                       public void onNothingSelected(AdapterView<?> arg0) {
                                                       }

                                                   });
        tSpinner.setOnItemSelectedListener(new
                                                   Spinner.OnItemSelectedListener() {
                                                       @Override
                                                       public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                                                  int arg2, long arg3) {
                                                           to = arg0.getSelectedItem().toString();
                                                       }
                                                       @Override
                                                       public void onNothingSelected(AdapterView<?> arg0) {
                                                       }
                                                   });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void click(View v){

        String path = "http://api.fixer.io/latest?base="+from;
        if(editText_money.getText().toString().length() < 1) {
            Toast.makeText(this,"You have to write a value", Toast.LENGTH_LONG).show();
        }else if (mConnMgr != null){
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

                    JSONObject rateObject = reader.getJSONObject("rates");

                    Double cnyRate = rateObject.getDouble(to);
                    Double from_value = Double.valueOf(editText_money.getText().toString());
                    Double result = from_value*cnyRate;
                    mTextView.setText(mTextView.getText()+result.toString());

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



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    /*public void onClick(View v) {
        Intent con = new Intent(ZZJ_MainActivity.this, reportMainActivity.class);
        startActivity(con);
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.zzj__main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intend = new Intent(ZZJ_MainActivity.this,ShowNowMainActivity.class);
            startActivity(intend);
        } else if (id == R.id.nav_gallery) {
            Intent intendh = new Intent(ZZJ_MainActivity.this,historyMainActivity.class);
            startActivity(intendh);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
