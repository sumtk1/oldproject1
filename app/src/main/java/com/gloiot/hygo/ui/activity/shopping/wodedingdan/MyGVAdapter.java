package com.gloiot.hygo.ui.activity.shopping.wodedingdan;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hygo02 on 2017/8/23.
 * gridview适配器
 */

public class MyGVAdapter extends BaseAdapter {
    protected final int MAXPICTURES = 4; //图片上传最大数量
    protected boolean isMax;
    protected List<String> list = new ArrayList<>();

    public MyGVAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list.size() >= MAXPICTURES) {
            isMax = true;
            return MAXPICTURES;
        } else {
            isMax = false;
            return list.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {
        return null;
    }
}
