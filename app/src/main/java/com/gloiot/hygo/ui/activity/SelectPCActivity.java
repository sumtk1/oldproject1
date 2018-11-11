package com.gloiot.hygo.ui.activity;

import android.app.Activity;import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.utils.xmlanalsis.ProvinceAreaHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择省市
 */
public class SelectPCActivity extends BaseActivity {

    private ExpandableListView el_select_pc;
    private SelectPCAdpter selectPCAdpter;
    String city;

    //省集合
    private List<String> mProvinceDatas = new ArrayList<>();
    //市集合的集合
    private List<List<String>> mCityDatas = new ArrayList<>();
    //省市集合
    private Map<String, List<String>> mCitisDatasMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_select_pc;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "更多");
        el_select_pc = (ExpandableListView) findViewById(R.id.el_select_pc);
        setAdapter();
    }


    // 设置省适配器
    private void setAdapter() {
        ProvinceAreaHelper pa = ProvinceAreaHelper.getInstance(mContext);
        mProvinceDatas = pa.getmProvinceDatas();
        mCitisDatasMap = pa.getmCitisDatasMap();

        for (Map.Entry<String, List<String>> entry : mCitisDatasMap.entrySet()) {
            mCityDatas.add(entry.getValue());
        }

        selectPCAdpter = new SelectPCAdpter(mProvinceDatas, mCityDatas, this);
        el_select_pc.setAdapter(selectPCAdpter);

        el_select_pc.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                city = mProvinceDatas.get(groupPosition) + mCityDatas.get(groupPosition).get(childPosition);
//                tv_select_result.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
                        if (city.contains("北京")) {
                            city = "北京市";
                        } else if (city.contains("上海")) {
                            city = "上海市";
                        } else if (city.contains("天津")) {
                            city = "天津市";
                        } else if (city.contains("重庆")) {
                            city = "重庆市";
                        }
                        Intent intent = new Intent();
                        intent.putExtra("city", city);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
//                    }
//                });
                return true;
            }
        });
    }


    /**
     * 选择省市的内部dpter
     */
    class SelectPCAdpter extends BaseExpandableListAdapter {
        public List<String> father;
        public List<List<String>> chilerd;
        private Context context;

        public SelectPCAdpter(List<String> faList, List<List<String>> chList, Context context) {  //初始化数据
            this.father = faList;
            this.chilerd = chList;
            this.context = context;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return chilerd.get(groupPosition).get(childPosition);   //获取父类下面的每一个子类项
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;  //子类位置
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) { //显示子类数据的iew
            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_select_pc, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.tv_pc);
            ImageView iv_click = (ImageView) convertView.findViewById(R.id.iv_click);
            iv_click.setVisibility(View.GONE);
            textView.setText(chilerd.get(groupPosition).get(childPosition));
            textView.setTextColor(Color.parseColor("#555555"));
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return chilerd.get(groupPosition).size();  //子类item的总数
        }

        @Override
        public Object getGroup(int groupPosition) {   //父类数据
            return father.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return father.size();  ////父类item总数
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;   //父类位置
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
            if (null == view) {
                view = LayoutInflater.from(context).inflate(R.layout.item_select_pc, null);
            }
            TextView textView = (TextView) view.findViewById(R.id.tv_pc);
            ImageView iv_click = (ImageView) view.findViewById(R.id.iv_click);
            textView.setText(father.get(groupPosition));

            /*判断是否group张开，来分别设置背景图*/
            if (isExpanded) {
                iv_click.setImageResource(R.mipmap.ic_arrow_up);
            } else {
                iv_click.setImageResource(R.mipmap.ic_arrow_down);
            }

            return view;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {  //点击子类触发事件
            return true;
        }

    }

}
