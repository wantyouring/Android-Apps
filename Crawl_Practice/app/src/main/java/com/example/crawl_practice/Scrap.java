package com.example.crawl_practice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Scrap extends AppCompatActivity {
    private class Scrapped {
        String part;
        String time;
        String title;
        String link;

        public Scrapped(String part, String time, String title, String link) {
            this.part = part;
            this.time = time;
            this.title = title;
            this.link = link;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrap);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("첫번째"));
        tabLayout.addTab(tabLayout.newTab().setText("두번째"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Intent getIntent = getIntent();
        ArrayList<String> scraps = getIntent.getStringArrayListExtra("scraps");

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final ScrapPagerAdapter adapter = new ScrapPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), scraps);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//
//        BarChart barChart = findViewById(R.id.chart1);
//
//        //MainActivity 에서 스크랩 데이터 모두 받기
//        Intent getIntent = getIntent();
//        ArrayList<String> scraps = getIntent.getStringArrayListExtra("scraps");
//        ArrayList<Scrapped> articles = new ArrayList<>();
//
//        for(String scrap:scraps) {
//            //[분야, 저장 시각, 제목, 링크] 순으로 파싱해 Scrapped객체로 하나씩 저장
//            String[] parsed = scrap.split("&&&");
//            articles.add(new Scrapped(parsed[0],parsed[1],parsed[2],parsed[3]));
//        }
//
//        //분야별로 나온 갯수 세기
//        int[] count = {0,0,0,0,0,0};
//        for(Scrapped part:articles) {
//            if(part.part.equals("정치")){
//                count[0]++;
//            } else if(part.part.equals("경제")){
//                count[1]++;
//            } else if(part.part.equals("사회")){
//                count[2]++;
//            }else if(part.part.equals("생활/문화")){
//                count[3]++;
//            }else if(part.part.equals("세계")){
//                count[4]++;
//            }else if(part.part.equals("IT/과학")){
//                count[5]++;
//            }
//        }
//
//
//        //횟수
//        List<BarEntry> entries = new ArrayList<>();
//        for(int i=0;i<6;i++)
//            entries.add(new BarEntry(i,count[i]));
//        BarDataSet set = new BarDataSet(entries, "분야별 스크랩 수");
//
//        //x축 설정
//        final String[] values = { "정치", "경제", "사회", "생활/문화", "세계", "IT/과학"};
//
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setLabelCount(6);
//        xAxis.setTextSize(13f);
//        xAxis.setDrawGridLines(false);
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(values) {
//            String[] mvalues = new String[6];
//            @Override
//            public String getFormattedValue(float value) {
//                for(int i=0;i<6;i++)
//                    mvalues[i] = values[i];
//                return mvalues[(int)value];
//            }
//        });
//
//        //y축 설정
//        YAxis yAxis_left = barChart.getAxisLeft();
//        yAxis_left.setAxisMinimum(0);
//        yAxis_left.setGranularity(1f);
//
//        YAxis yAxis_right = barChart.getAxisRight();
//        yAxis_right.setEnabled(false);
//
//        //기타 차트 설정
//        BarData data = new BarData(set);
//        set.setColors(ColorTemplate.COLORFUL_COLORS);
//        data.setValueTextSize(13f);
//        barChart.setData(data);
//
//        Description description = new Description();
//        description.setText("");
//
//        barChart.setScaleEnabled(false);
//        barChart.setDescription(description);
//        barChart.invalidate(); // refresh
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED,intent);
        finish();
    }
}
