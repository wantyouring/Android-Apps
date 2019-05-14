package com.example.imageslider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Adapter extends PagerAdapter {

    private int[] images = {R.drawable.img1, R.drawable.img2, R.drawable.img3};
    private String[] caption = {"맛있는 꼬기~","맛있는 라볶이~","맛있는 롤케잌~"};
    private LayoutInflater inflater;
    private Context context;

    public Adapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (LinearLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) { // ViewPager에서 사용할 뷰객체 생성 및 등록 한다.
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slider, container, false);
        ImageView imageView = (ImageView)v.findViewById(R.id.imageView);
        TextView textView = (TextView)v.findViewById(R.id.textView2);
        imageView.setImageResource(images[position]);
        textView.setText(caption[position]);
        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object o) {
        container.invalidate();
    }

    public String getCaption(int position) {
        return caption[position];
    }
}
