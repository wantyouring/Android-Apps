package com.example.crawl_practice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class Scrap extends AppCompatActivity {

    public static int fragId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrap);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        tabLayout.addTab(tabLayout.newTab().setText("경향분석"));
        tabLayout.addTab(tabLayout.newTab().setText("기사확인"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //action bar setting
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //scrap데이터 가져오기
        Intent getIntent = getIntent();
        ArrayList<String> scraps = getIntent.getStringArrayListExtra("scraps");

        //viewpager, adapter setting
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
    }
    //action bar 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_scrap, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //action bar menu 버튼 클릭 시
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.deleteButton) { //삭제 버튼
            final ScrapListFrag scrapListFrag = (ScrapListFrag) getSupportFragmentManager().findFragmentById(fragId);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(scrapListFrag.returnCheckedItemsCount()+"개 기사를 삭제하시겠습니까?")
                    .setTitle("스크랩 삭제")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //fragent에서 데이터 삭제
                            scrapListFrag.deleteCheckedItems();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return true;
    }

    //뒤로가기 버튼
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED,intent);
        finish();
    }
}
