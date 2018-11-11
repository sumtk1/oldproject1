package com.gloiot.hygo.ui.activity.login;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.ui.activity.shopping.ShangPinXiangQingActivity;
import com.gloiot.hygo.utlis.ConstantUtlis;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.im_gape_01)
    ImageView imGape01;
    @Bind(R.id.im_gape_02)
    ImageView imGape02;
    @Bind(R.id.im_gape_03)
    ImageView imGape03;
    @Bind(R.id.im_gape_04)
    ImageView imGape04;
    @Bind(R.id.ll_gape_01)
    LinearLayout llGape01;


    //存放引导页
    private List<View> layoutData = new ArrayList<>();
    private ImageView[] icons = new ImageView[4];
    private TextView tvBtn;
    //两点的距离
    private int mPointSpace;
    //移动的点
    private View mFocusPoint;


    @Override
    public int initResource() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置无标题
        return R.layout.activity_guide;
    }

    @Override
    public void initData() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

        viewPager.addOnPageChangeListener(this);
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.page_1, null);
        View view2 = inflater.inflate(R.layout.page_2, null);
        View view3 = inflater.inflate(R.layout.page_3, null);
        View view4 = inflater.inflate(R.layout.page_4, null);
        tvBtn = (TextView) view4.findViewById(R.id.tv_ljty);

        layoutData.add(view1);
        layoutData.add(view2);
        layoutData.add(view3);
        layoutData.add(view4);
        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = getIntent().getStringExtra("msg");
                String id = getIntent().getStringExtra("id");
                editor.putInt(ConstantUtlis.GUIDE, ConstantUtlis.GUIDETIME);
                editor.commit();
                if (msg != null && !msg.isEmpty()) {
                    Intent intent = new Intent(GuideActivity.this, ShangPinXiangQingActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    GuideActivity.this.finish();
                } else {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("selectFlag", 1);
                    startActivity(intent);
                    finish();
                }
            }
        });
        //设置viewpager的适配器
        PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return layoutData.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                View view = layoutData.get(position);
                container.addView(view);
                return view;

            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(layoutData.get(position));
            }

        };

        viewPager.setAdapter(adapter);

        imGape01 = (ImageView) findViewById(R.id.im_gape_01);
        imGape02 = (ImageView) findViewById(R.id.im_gape_02);
        imGape03 = (ImageView) findViewById(R.id.im_gape_03);
        imGape04 = (ImageView) findViewById(R.id.im_gape_04);

        icons[0] = imGape01;
        icons[1] = imGape02;
        icons[2] = imGape03;
        icons[3] = imGape04;
    }

    /**
     * viewpager滚动时
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //        int leftMargin = (int) (mPointSpace * positionOffset + position * mPointSpace + 0.5f);
        //        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mFocusPoint.getLayoutParams();
        //        params.leftMargin = leftMargin;
        //        mFocusPoint.setLayoutParams(params);
        //        /**
        //         * 设置点移动时的监听
        //         */
        //        ll_gape_01.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        //            @Override
        //            public void onGlobalLayout() {
        //                //获得两点见的距离
        //                mPointSpace = ll_gape_01.getChildAt(1).getLeft() - ll_gape_01.getChildAt(0).getLeft();
        //            }
        //        });
    }

    /**
     * 当页面被选中的时候
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        clearDots();
        llGape01.setVisibility(position == layoutData.size() - 1 ? View.GONE : View.VISIBLE);
        icons[position].setImageResource(R.mipmap.ic_dot02);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 将所有点都设置为未选中状态
     */
    private void clearDots() {
        imGape01.setImageResource(R.mipmap.ic_dot01);
        imGape02.setImageResource(R.mipmap.ic_dot01);
        imGape03.setImageResource(R.mipmap.ic_dot01);
        imGape04.setImageResource(R.mipmap.ic_dot01);
    }
}
