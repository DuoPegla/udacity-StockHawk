package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import au.com.bytecode.opencsv.CSVReader;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import yahoofinance.Stock;

/**
 * Created by duopegla on 6.5.2017..
 */

public class StockDetailActivity extends AppCompatActivity
{
    public static final String EXTRA_SYMBOL = "extra:symbol";

    private Stock mStock;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.tv_detail_symbol)
    TextView mSymbol;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.line_chart_stock)
    LineChart mChart;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Timber.d("Created Detail Activity!");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stock_detail);
        ButterKnife.bind(this);

        Intent parentIntent = getIntent();
        if (parentIntent != null && parentIntent.hasExtra(EXTRA_SYMBOL))
        {
            mSymbol.setText(parentIntent.getExtras().getString(EXTRA_SYMBOL));
            showHistory(parentIntent.getExtras().getString(EXTRA_SYMBOL));
        }
    }

    private void showHistory(String symbol)
    {
        String history = getHistoryString(symbol);

        List<String[]> lines = getLines(history);

        ArrayList<Entry> entries = new ArrayList<>(lines.size());

        final ArrayList<Long> xAxisValues = new ArrayList<>();
        int xAxisPosition = 0;

        for (int i = lines.size() - 1; i >= 0; i--)
        {
            String[] line = lines.get(i);
            xAxisValues.add(Long.valueOf(line[0]));
            xAxisPosition++;

            Entry entry = new Entry(xAxisPosition, Float.valueOf(line[1]));

            entries.add(entry);

        }

        setupChart(symbol, entries, xAxisValues);
    }

    private void setupChart(String symbol, List<Entry> entries, final List<Long> xAxisValues)
    {
        LineData lineData = new LineData(new LineDataSet(entries, symbol));
        mChart.setData(lineData);

        final XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                Long dateLong = xAxisValues.get((int)value);
                Date date = new Date(dateLong);
                return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date);
            }
        });

        final YAxis yAxisRight = mChart.getAxisRight();
        yAxisRight.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                DecimalFormat decimalFormat = new DecimalFormat("$#");
                return decimalFormat.format(value);
            }
        });

        mChart.getAxisLeft().setDrawLabels(false);


        mChart.invalidate();
    }

    @Nullable
    private List<String[]> getLines(String history)
    {
        List<String[]> lines = null;
        CSVReader reader = new CSVReader(new StringReader(history));
        try
        {
            lines = reader.readAll();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return lines;
    }

    private String getHistoryString(String symbol)
    {
        Cursor cursor = getContentResolver().query(Contract.Quote.makeUriForStock(symbol),
                null,
                null,
                null,
                null);

        String history = "";
        if (cursor.moveToFirst())
        {
            history = cursor.getString(cursor.getColumnIndex(Contract.Quote.COLUMN_HISTORY));
            cursor.close();
        }
        return history;
    }
}
