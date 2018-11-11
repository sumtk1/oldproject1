package com.gloiot.hygo.ui.activity.shopping.wodedingdan;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;

import java.util.ArrayList;
import java.util.List;

public class WodeDingdanActivity extends BaseActivity {

    private TabLayout tl_dingdan_gunli_head;
    private ViewPager vp_dingdan_gunli_page;
    private String[] titles = new String[]{"全部", "待付款", "待发货", "待收货", "待评价"};

    private String startFragment;

    @Override
    public int initResource() {
        return R.layout.activity_dingdan_guanli;
    }

    @Override
    public void initData() {
        initComponent();
        CommonUtlis.setTitleBar(this, "我的订单");
        TabLayout();
    }

    private void TabLayout() {
        //设置TabLayout的模式
        tl_dingdan_gunli_head.setTabMode(TabLayout.MODE_FIXED);

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            tl_dingdan_gunli_head.addTab(tl_dingdan_gunli_head.newTab().setText(titles[i]));
            DingdanFragment dingdanFragment = new DingdanFragment();
            Bundle bundle = new Bundle();
            bundle.putString("text",titles[i]);
            dingdanFragment.setArguments(bundle);
            fragments.add(dingdanFragment);
        }
        vp_dingdan_gunli_page.setAdapter(new TabFragmentAdapter(fragments, titles, getSupportFragmentManager(), this));
        vp_dingdan_gunli_page.setOffscreenPageLimit(4);
        tl_dingdan_gunli_head.setupWithViewPager(vp_dingdan_gunli_page);

        switch (startFragment) {
            case "全部":
                vp_dingdan_gunli_page.setCurrentItem(0);
                break;
            case "待付款":
                vp_dingdan_gunli_page.setCurrentItem(1);
                break;
            case "待发货":
                vp_dingdan_gunli_page.setCurrentItem(2);
                break;
            case "待收货":
                vp_dingdan_gunli_page.setCurrentItem(3);
                break;
            case "待评价":
                vp_dingdan_gunli_page.setCurrentItem(4);
                break;
        }
    }

    public void initComponent() {
        tl_dingdan_gunli_head = (TabLayout) findViewById(R.id.tl_dingdan_gunli_head);
        vp_dingdan_gunli_page = (ViewPager) findViewById(R.id.vp_dingdan_gunli_page);
        startFragment = getIntent().getStringExtra("显示页面");
    }
}
