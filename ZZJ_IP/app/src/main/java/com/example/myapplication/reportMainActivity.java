package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.text.DecimalFormat;

public class reportMainActivity extends AppCompatActivity {
    private AQuery aq;
    private TextView textView_from;
    private TextView textView_to;
    private TextView textView_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView_from = (TextView) findViewById(R.id.textView_from);
        textView_to = (TextView) findViewById(R.id.textView_to);
        textView_date = (TextView) findViewById(R.id.textView_date);

        //aq = new AQuery(getActivity()) ;

        setContentView(R.layout.activity_report_main);
        Bundle bundle = getIntent().getExtras();
        final double num_get = Double.parseDouble(bundle.getString("money_in"));
        final String fmoney = bundle.getString("fmoney");
        final String tmoney = bundle.getString("tmoney");
        String url = "http://api.fixer.io/latest?" + "base=" + fmoney;
        final DecimalFormat nf = new DecimalFormat("0.00");



    }
}
