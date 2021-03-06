package com.example.crawling;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String htmlPageUrl = "https://www.yna.co.kr/"; //파싱할 홈페이지의 URL주소
    private TextView textviewHtmlDocument;
    private String htmlContentInStringFormat="";

    int cnt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

                Document doc = Jsoup.connect(htmlPageUrl).get();
                Elements titles;
/*
                //테스트1
                titles= doc.select("div.news-con h1.tit-news");
                System.out.println("titles 첫째 select" + titles.text());

                System.out.println("-------------------------------------------------------------");
                for(Element e: titles){
                    System.out.println("title1: " + e.text());
                    htmlContentInStringFormat += e.text().trim() + "\n";
                }

                //테스트2
                titles= doc.select("div.news-con h2.tit-news");
                System.out.println("titles 둘째 select" + titles.text());

                System.out.println("-------------------------------------------------------------");
                for(Element e: titles){
                    System.out.println("title2: " + e.text());
                    htmlContentInStringFormat += e.text().trim() + "\n";
                }*/

                //테스트3
                //titles= doc.select("li.section02 div.con h2.news-tl");
                titles= doc.select("li.section02 div.con ul.h-related li");
                System.out.println("titles 셋째 select" + titles.text());

                System.out.println("-------------------------------------------------------------");
                for(Element e: titles){
                    System.out.println("title3: " + e.text());
                    htmlContentInStringFormat += e.text().trim() + "\n";
                }
                System.out.println("-------------------------------------------------------------");

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
