package com.example.crawl_practice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ScrapPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    ArrayList<String> scraps;

    public ScrapPagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<String> scraps) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.scraps = scraps;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                ScrapChartFrag tab1 = new ScrapChartFrag();
                Bundle bundle = new Bundle(1);
                bundle.putStringArrayList("scraps",scraps);
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                ScrapListFrag tab2 = new ScrapListFrag();
                return tab2;
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
