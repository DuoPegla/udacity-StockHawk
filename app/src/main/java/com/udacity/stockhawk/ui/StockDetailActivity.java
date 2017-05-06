package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.udacity.stockhawk.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by duopegla on 6.5.2017..
 */

public class StockDetailActivity extends AppCompatActivity
{
    private Stock mStock;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.line_chart_stock)
    private LineChart mChart;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState)
    {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.activity_stock_detail);
        ButterKnife.bind(this);

        List<Entry> entries = new ArrayList<Entry>();
        try
        {
            for (HistoricalQuote data : mStock.getHistory())
            {

            }
        }
        catch (IOException e)
        {

        }
    }
}
