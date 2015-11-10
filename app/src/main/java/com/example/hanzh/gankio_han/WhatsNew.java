package com.example.hanzh.gankio_han;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

public class WhatsNew extends Activity {

    private ViewPager viewPager;
    private ImageView imageView;
    private ArrayList<View> pageViews;   //创建一个数组存放每个页面要显示的View
    private ImageView[] imageViewsPoint; //创建一个imageview类型的数组，用来表示导航小圆点
    private ViewGroup viewPictures; //装显示图片的viewgroup
    private ViewGroup viewPoints;   //导航小圆点viewPager
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //必须在setContentView之前
        // 全屏方法二 ：隐去状态栏部分 (电池等图标和一切修饰部分)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LayoutInflater inflater=getLayoutInflater();
        pageViews = new ArrayList<View>();
        pageViews.add(inflater.inflate(R.layout.whats_new_page1, null));
        pageViews.add(inflater.inflate(R.layout.whats_new_page2, null));
        pageViews.add(inflater.inflate(R.layout.whats_new_page3, null));

        imageViewsPoint=new ImageView[pageViews.size()];    //小圆点数组,大小是图片的个数
        viewPictures=(ViewGroup)inflater.inflate(R.layout.viewpagers, null); //viewPictures是装显示图片的viewgroup
        viewPager=(ViewPager)viewPictures.findViewById(R.id.guidePagers);   //viewPager是一个ViewPager控件
        viewPoints=(ViewGroup)viewPictures.findViewById(R.id.viewPoints);   //导航小圆点的viewPager

        //添加小圆点导航的图片
        for(int i=0;i<pageViews.size();i++){
            imageView=new ImageView(WhatsNew.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(20,20));
            imageView.setPadding(5,0,5,0);
            imageViewsPoint[i]=imageView;   //把小圆点放到数组中
            if(i==0){   //第一个圆点是选中状态
                imageViewsPoint[i].setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));
            }
            else{
                imageViewsPoint[i].setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
            }
            viewPoints.addView(imageViewsPoint[i]); //将imageviews添加到小圆点视图组
        }

        setContentView(viewPictures);

        viewPager.setAdapter(new NavigationPageAdapter());
        viewPager.setOnPageChangeListener(new NavigationPageChangeListener());
    }
    // 导航图片view的适配器，必须要实现的是下面四个方法
    class NavigationPageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // 初始化每个Item
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(pageViews.get(position));
            return pageViews.get(position);
        }

        // 销毁每个Item
        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(pageViews.get(position));
        }
    }

    class NavigationPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            // 循环主要是控制导航中每个小圆点的状态
            for (int i = 0; i < imageViewsPoint.length; i++) {
                // 当前view下设置小圆点为选中状态
                imageViewsPoint[i].setImageDrawable(getResources().getDrawable(
                        R.drawable.page_indicator_focused));
                // 其余设置为飞选中状态
                if (position != i)
                    imageViewsPoint[i].setImageDrawable(getResources().getDrawable(
                            R.drawable.page_indicator_unfocused));
            }
        }
    }

    public void startbutton(View v) {
        Intent intent = new Intent(WhatsNew.this,MainActivity.class);
        startActivity(intent);
        WhatsNew.this.finish();
    }
}
