package com.example.crawl_practice;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

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
    Toolbar myToolbar;
    TextView part;
    ImageButton toolbarInfo;

    Document doc;

    ArrayList<String> items = new ArrayList<String>();
    ArrayList<String> links = new ArrayList<String>();
    String imagelinks[] = new String[60];
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
        myToolbar = (Toolbar)findViewById(R.id.toolbar);
        part = (TextView)findViewById(R.id.part);
        toolbarInfo = (ImageButton)findViewById(R.id.toolbarInfo);

        forTest.setTypeface(forTest.getTypeface(), Typeface.BOLD);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //@@@dialog로 바꾸기
                Toast.makeText(MainActivity.this, "기사클릭 : 기사 성향, 댓글 확인\n기사롱클릭 : 해당 기사로 이동\n좌우스크롤 : 분야 이동", Toast.LENGTH_LONG).show();
            }
        });

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
                //주소, 링크 파싱
                Connection.Response response = Jsoup.connect("https://news.naver.com/main/ranking/popularDay.nhn").ignoreContentType(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36")
                        .referrer("http://www.google.com")
                        .timeout(5000)
                        .followRedirects(true).execute();
                doc = response.parse();

//                Elements titles = doc.select("table.container tbody tr td.content div.content div.ranking_section dt [title]"); //(메인부분)많이 본 뉴스 부분 파싱
                Elements titles = doc.select("table.container tbody tr td.aside div.aside div.section.section_wide ul.section_list_ranking a[title]"); //(사이드부분)가장 많이 본 뉴스 부분 파싱
                Elements links_ele = doc.select("table.container tbody tr td.aside div.aside div.section.section_wide ul.section_list_ranking a[href]"); //링크부분 파싱

                for (Element e:titles) {
                    items.add(e.text());
                }
                for (Element e:links_ele) {
                    links.add("https://news.naver.com" + e.attr("href"));
                }

                //미리보기 이미지 주소 파싱
                //100:정치, 101:경제, 102:사회, 103:생활/문화, 104: 세계, 105:IT/과학
                String part_string;
                for(int i=0;i<6;i++) {
                    part_string = "https://news.naver.com/main/ranking/popularDay.nhn?rankingType=popular_day&sectionId=" + (100 + i);
                    response = Jsoup.connect(part_string).ignoreContentType(true)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36")
                            .referrer("http://www.google.com")
                            .timeout(5000)
                            .followRedirects(true).execute();
                    doc = response.parse();

                    links_ele = doc.select("table.container tbody tr td.content ol.ranking_list img");
                    Elements thumb_check = doc.select("table.container tbody tr td.content ol.ranking_list div.ranking_thumb a");
                    ArrayList<Integer> thumbIndex = new ArrayList<Integer>();
                    for(int j=0;;j++) {
                        int tmp;
                        tmp = Integer.parseInt(thumb_check.get(j).attr("href").split("rankingSeq=")[1].split("\"")[0]);
                        thumbIndex.add(tmp-1); //thumb counting은 1부터 시작함
                        if (tmp >= 10)
                            break;
                    }
                    int j=0;
                    for(Integer index: thumbIndex)   //이미지링크 rank10까지만 추출
                        imagelinks[i*10+index] = links_ele.get(j++).attr("src");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            viewPager = (ViewPager)findViewById(R.id.viewPager);
            adapter = new PageAdapter(getApplicationContext(),items,links,imagelinks,forTest,comments,emoticon);
            viewPager.setAdapter(adapter);

            //viewPager 바꼈을때
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    //toolbar 메뉴 setting
                    switch (i) {
                        case 0:
                            part.setText("정치");
                            break;
                        case 1:
                            part.setText("경제");
                            break;
                        case 2:
                            part.setText("사회");
                            break;
                        case 3:
                            part.setText("생활/문화");
                            break;
                        case 4:
                            part.setText("세계");
                            break;
                        case 5:
                            part.setText("IT/과학");
                            break;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }
    }
}
