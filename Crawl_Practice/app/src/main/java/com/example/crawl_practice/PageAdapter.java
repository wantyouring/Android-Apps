package com.example.crawl_practice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;

public class PageAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    final Context context;
    String items[] = new String[60];
    String links[] = new String[60];
    int characters[] = new int[5];
    TextView forTest;
    String link_uri;
    String articleId;

    //생성자
    public PageAdapter(Context context, ArrayList<String> items,  ArrayList<String> links,
                       TextView forTest) {
        this.context = context;
        for(int i=0;i<60;i++) {
            this.items[i] = items.get(i);
            this.links[i] = links.get(i);
        }
        this.forTest = forTest;
    }

    //PageAdapter implements
    @Override
    public int getCount() {
        return 6;   //정치, 경제, 사회, 생활/문화, 세계, IT/과학
    }

    //PageAdapter implements
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (LinearLayout) o;
    }

    //page생성
    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.slider, container, false);
        ListView listViewOfArticle = (ListView)v.findViewById(R.id.listViewOfArticle);

        String picked_item[] = new String[10];//한 분야의 10개 title 추출
        final String picked_link[] = new String[10];

        for(int i=0;i<10;i++) {
            picked_item[i] = items[10*position + i];
            picked_link[i] = links[10*position + i];
        }

        ListAdapter listAdapter = new listAdapter(context,picked_item);
        listViewOfArticle.setAdapter(listAdapter);
        //롱클릭 시 물어보고 기사로 이동
        listViewOfArticle.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                link_uri = picked_link[position]; //링크uri 저장.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.context);
                builder.setMessage("해당 기사로 이동하시겠습니까?")
                        .setTitle("News")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { //종료
                                forTest.setText(link_uri);
                                //링크 열기
                                Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(link_uri));
                                MainActivity.context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); //adapter에서 activity실행하려면 addFlags해줘야함.
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { //종료 안할거임
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true; // 롱클릭 이후 숏클릭 인식 안되게
            }
        });
        //그냥 클릭 시 기사 요약
        listViewOfArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                link_uri = picked_link[position]; //링크uri 저장.
                //forTest.setText(link_uri);
                //  내용(or 댓글) 가져오기.
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(link_uri,articleId,forTest);
                jsoupAsyncTask.execute();
            }
        });
        container.addView(v);
        return v;
    }

    //page삭제
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    //article short click시 기사 성향 가져오기.
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
        String link_uri;
        String articleId;
        TextView forTest;
        JsonParser jsonParser = new JsonParser();

        //constructor
        public JsoupAsyncTask(String link_uri, String articleId, TextView forTest) {
            super();
            this.link_uri = link_uri;
            this.articleId = articleId;
            this.forTest = forTest;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //기사 부분
                Connection.Response response = Jsoup.connect(link_uri).ignoreContentType(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36")
                        .referrer("http://www.google.com")
                        .timeout(5000)
                        .followRedirects(true).execute();
                Document doc = response.parse();

                //기사 고유번호 파싱.
                articleId = doc.select("div._reactionModule.u_likeit").first().attr("data-cid");
                Elements characters_cnt = doc.select("tbody tr td.content div.content ul.u_likeit_layer._faceLayer span.u_likeit_list_count._count");
                int i=0;
                for (Element e:characters_cnt) {
                    characters[i] = Integer.parseInt(e.text());
                }

                //기사 고유번호로 쿼리보내 기사성향파악.
                Document doc2 = Jsoup.connect("https://news.like.naver.com/v1/search/contents?q=NEWS["+articleId+"]|NEWS_SUMMARY["+articleId+"]")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36")
                        .referrer("http://www.google.com")
                        .ignoreContentType(true)
                        .get();
                String doc_str = doc2.toString();
                //성향count부분 파싱
                int angry_cnt = 0;
                int sad_cnt = 0;
                int want_cnt = 0;
                int warm_cnt = 0;
                int like_cnt = 0;
                if(doc_str.contains("\"angry\",\"count\":"))
                    angry_cnt = Integer.parseInt(doc_str.split("\"angry\",\"count\":",2)[1].split(",",2)[0]);
                if(doc_str.contains("\"sad\",\"count\":"))
                    sad_cnt = Integer.parseInt(doc_str.split("\"sad\",\"count\":",2)[1].split(",",2)[0]);
                if(doc_str.contains("\"want\",\"count\":"))
                    want_cnt = Integer.parseInt(doc_str.split("\"want\",\"count\":",2)[1].split(",",2)[0]);
                if(doc_str.contains("\"warm\",\"count\":"))
                    warm_cnt = Integer.parseInt(doc_str.split("\"warm\",\"count\":",2)[1].split(",",2)[0]);
                if(doc_str.contains("\"like\",\"count\":"))
                    like_cnt = Integer.parseInt(doc_str.split("\"like\",\"count\":",2)[1].split(",",2)[0]);
                characters[0] = angry_cnt;
                characters[1] = sad_cnt;
                characters[2] = want_cnt;
                characters[3] = warm_cnt;
                characters[4] = like_cnt;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            String message = "";
            //최댓값 찾아 기사 성향 파악.
            int max = 0;
            for(int i=0;i<5;i++){
                if(max < characters[i])
                    max = characters[i];
            }

            if (max == characters[0]) {
                message = "화가나는 기사에요 ٩(◦`꒳´◦)۶";
            } else if(max == characters[1]) {
                message = "슬픈 기사에요 ಥ_ಥ";
            } else if(max == characters[2]) {
                message = "후속기사를 원해요!";
            } else if(max == characters[3]) {
                message = "따뜻한 기사에요";
            } else if(max == characters[4]) {
                message = "좋은 기사에요";
            }

            forTest.setText(message);
        }
    }
}
