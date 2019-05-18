package com.example.crawl_practice;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //private String htmlPageUrl = "https://cs.skku.edu/news/notice/list"; //파싱할 홈페이지의 URL주소

    private String htmlPageUrl = "https://www.skku.edu/skku/campus/skk_comm/notice01.do"; //파싱할 홈페이지의 URL주소
    private TextView textviewHtmlDocument;
    private String htmlContentInStringFormat="";
    SharedPreferences pref = null; //pref에 현재까지 최신 공지 값 저장할 것임.
    SharedPreferences.Editor editor = null;// editor에 put 하기

    int cnt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        textviewHtmlDocument = (TextView)findViewById(R.id.textView);
        textviewHtmlDocument.setMovementMethod(new ScrollingMovementMethod()); //스크롤 가능한 텍스트뷰로 만들기

        Button htmlTitleButton = (Button)findViewById(R.id.button);
        htmlTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println( (cnt+1) +"번째 파싱");
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
                cnt++;
            }
        });
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //Connection.Response response = Jsoup.connect(htmlPageUrl).method(Connection.Method.GET).execute();
                Connection.Response response = Jsoup.connect(htmlPageUrl).ignoreContentType(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36")
                        .referrer("http://www.google.com")
                        .timeout(5000)
                        .followRedirects(true).execute();
                Document doc = response.parse();
                System.out.println(doc.toString());

                //Elements google = doc.select("table.table-hover.dataTable no-footer");    //파싱할 tag이름, attribute명과 값 지정.
                //Elements google = doc.select("board-wrap.board-qa.table_scrollWrap a");    //파싱할 tag이름, attribute명과 값 지정.
                Elements google = doc.select("div.total_wrap span");    //파싱할 tag이름, attribute명과 값 지정.
                htmlContentInStringFormat += google.size() + "\n";
                //htmlContentInStringFormat += google.toString() + '\n'; // Elements 구성 확인 가능.
                //for (Element e:google) { // text부분만 추출.
                //    htmlContentInStringFormat += e.text()+ "\n";
                //}
                for (Element e:google) {
                    htmlContentInStringFormat += e.text()+ "\n";
                    //htmlContentInStringFormat += e.html()+ "\n";
                }

                htmlContentInStringFormat += google.attr("class");
                //Element google = doc.select("div[id=logo-default]").first();
                //htmlContentInStringFormat += google.attr("title");

                //Document doc = Jsoup.connect(htmlPageUrl).get();
                //Elements titles;

                htmlContentInStringFormat += "\n파싱 끝\n" + "최종출력 : " + google.first().text();
                if(!pref.contains("notice")) { //처음 저장
                    htmlContentInStringFormat = "현재 공지 정보 저장 완료";
                } else if(pref.getInt("notice",0) < Integer.parseInt(google.first().text())) { //공지 데이터 있는 경우
                        htmlContentInStringFormat = "새로운 공지 존재"; //@@@@@@버튼 만들어 링크 바로가기 만들기.
                } else {
                    htmlContentInStringFormat = "새로운 공지 없음";
                }
                editor.putInt("notice",Integer.parseInt(google.first().text()));
                editor.apply(); //비동기 저장

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textviewHtmlDocument.setText(htmlContentInStringFormat);
        }
    }
}
