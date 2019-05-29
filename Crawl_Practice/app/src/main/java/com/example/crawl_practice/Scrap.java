package com.example.crawl_practice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class Scrap extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrap);

        PieChart pieChart = findViewById(R.id.chart1);

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(18.5f, "Green"));
        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));
        PieDataSet set = new PieDataSet(entries, "테스트");

        // 분야별 색깔
        final int[] MY_COLORS = {Color.rgb(192,0,0), Color.rgb(255,0,0), Color.rgb(255,192,0),
                Color.rgb(127,127,127)};
        ArrayList<Integer> colors = new ArrayList<>();
        for(int c:MY_COLORS)
            colors.add(c);
        set.setColors(colors);

        PieData data = new PieData(set);
        data.setValueTextSize(10f);
        pieChart.setData(data);

        //label
        Description description = new Description();
        description.setText("분야별 스크랩 횟수");
        description.setTextSize(15);

        pieChart.setDescription(description);
        pieChart.invalidate(); // refresh
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED,intent);
        finish();
    }
}
