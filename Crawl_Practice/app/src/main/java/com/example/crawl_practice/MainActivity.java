package com.example.crawl_practice;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Context context;  //adapter dialog에서 context 참조하기 위해서.

    PageAdapter adapter;
    ViewPager viewPager;
    TextView forTest;
    TextView comments;
    ImageView emoticon;

    Document doc;

    ArrayList<String> items = new ArrayList<String>();
    ArrayList<String> links = new ArrayList<String>();
//for data save
//    SharedPreferences pref = null; //pref에 현재까지 최신 공지 값 저장할 것임.
//    SharedPreferences.Editor editor = null;// editor에 put 하기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        forTest = (TextView)findViewById(R.id.forTest);
        comments = (TextView)findViewById(R.id.comments);
        comments.setMovementMethod(new ScrollingMovementMethod());
        emoticon = (ImageView)findViewById(R.id.emoticon);

        forTest.setTypeface(forTest.getTypeface(), Typeface.BOLD);

        //생성 동시에 파싱해 뉴스 데이터 가져오기.
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
        //for data save
//        pref = getSharedPreferences("pref", MODE_PRIVATE);
//        editor = pref.edit();
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Connection.Response response = Jsoup.connect("https://news.naver.com/main/ranking/popularDay.nhn").ignoreContentType(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36")
                        .referrer("http://www.google.com")
                        .timeout(5000)
                        .followRedirects(true).execute();
                doc = response.parse();
                //파싱해와 해당 분야의(정치,경제..) 제목 하나씩 items에 append하자. 분야 카테고리 100,101,...체크하기.

//                Elements titles = doc.select("table.container tbody tr td.content div.content div.ranking_section dt [title]"); //(메인부분)많이 본 뉴스 부분 파싱
                Elements titles = doc.select("table.container tbody tr td.aside div.aside div.section.section_wide ul.section_list_ranking a[title]"); //(사이드부분)가장 많이 본 뉴스 부분 파싱
                Elements links_ele = doc.select("table.container tbody tr td.aside div.aside div.section.section_wide ul.section_list_ranking a[href]"); //링크부분 파싱

                for (Element e:titles) {
                    items.add(e.text());
                }
                for (Element e:links_ele) {
                    links.add("https://news.naver.com" + e.attr("href"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            viewPager = (ViewPager)findViewById(R.id.viewPager);
            adapter = new PageAdapter(getApplicationContext(),items,links,forTest,comments,emoticon);
            viewPager.setAdapter(adapter);
        }
    }
}
