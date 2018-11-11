package com.gloiot.hygo.ui.activity.listwithheard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.login.LoginActivity;
import com.gloiot.hygo.ui.activity.login.RegisterActivity;
import com.gloiot.hygo.ui.activity.login.WXWanshanZiLiaoActivity;
import com.gloiot.hygo.ui.activity.login.WangjimimaActivity;
import com.gloiot.hygo.utlis.CommonUtlis;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;

public class SelectCountriesActivity extends BaseActivity {
    @Bind(R.id.lv_select_countries)
    ListView lv_select_countries;

    private ArrayList<User> list;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_select_countries;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "选择国家与地区", "", true);

        list = (ArrayList<User>) this.getIntent().getSerializableExtra("list");
        type = this.getIntent().getStringExtra("type");

        Collections.sort(list); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        SelectCountriesAdapter adapter = new SelectCountriesAdapter(this, list);
        lv_select_countries.setAdapter(adapter);

        lv_select_countries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("注册".equals(type)) {
                    setResult(0x666, new Intent(SelectCountriesActivity.this, RegisterActivity.class).
                            putExtra("国家与地区", list.get(position).getName().toString()).
                            putExtra("国家码", list.get(position).getExtra().toString()));
                } else if ("微信登录完善资料".equals(type)) {
                    setResult(0x666, new Intent(SelectCountriesActivity.this, WXWanshanZiLiaoActivity.class).
                            putExtra("国家与地区", list.get(position).getName().toString()).
                            putExtra("国家码", list.get(position).getExtra().toString()));
                } else if ("忘记登录密码".equals(type)) {
                    setResult(0x666, new Intent(SelectCountriesActivity.this, WangjimimaActivity.class).
                            putExtra("国家与地区", list.get(position).getName().toString()).
                            putExtra("国家码", list.get(position).getExtra().toString()));
                } else {
                    Log.e("00=--99", type);
                    setResult(0x666, new Intent(SelectCountriesActivity.this, LoginActivity.class).
                            putExtra("国家与地区", list.get(position).getName().toString()).
                            putExtra("国家码", list.get(position).getExtra().toString()));
                }
                finish();
            }
        });

    }
}
