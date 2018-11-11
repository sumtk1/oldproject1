package com.gloiot.hygo.ui.activity.shopping.wodedingdan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.gloiot.hygo.ui.activity.shopping.Bean.TreeNodeBean;
import com.loopj.android.http.RequestHandle;
import com.gloiot.hygo.server.network.RequestAction;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hygo02 on 2016/10/14.
 */

public class WoDeDingDan_DingDanAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private Context context;

    protected ArrayList<RequestHandle> requestHandleArrayList = new ArrayList<>();
    protected RequestAction requestAction = new RequestAction();

    private List<TreeNodeBean> listData;

    public WoDeDingDan_DingDanAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void changeData(List<TreeNodeBean> list) {
        listData = list;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    //固定ListView高度
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
