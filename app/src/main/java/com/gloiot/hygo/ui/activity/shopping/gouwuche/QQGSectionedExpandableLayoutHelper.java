package com.gloiot.hygo.ui.activity.shopping.gouwuche;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gloiot.hygo.ui.activity.shopping.Bean.DiYongYouHuiBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.GroupInfoBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.QuerenDingdanYouHuiQuanBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShangpinBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hygo03 on 2017/6/17.
 */

public class QQGSectionedExpandableLayoutHelper {

    //data list
    private LinkedHashMap<GroupInfoBean, ArrayList<ShangpinBean>> mSectionDataMap = new LinkedHashMap<>();
    private ArrayList<Object> mDataArrayList = new ArrayList<>();

    private QuerenDingdanYouHuiQuanBean querenDingdanYouHuiQuanBean;

    private DiYongYouHuiBean ZhangHuDiYong;

    //section map
    //TODO : look for a way to avoid this
    private HashMap<String, GroupInfoBean> mSectionMap = new HashMap<String, GroupInfoBean>();

    //adapter
    private QQGSectionedExpandableGridAdapter mSectionedExpandableGridAdapter;

    public QQGSectionedExpandableLayoutHelper(Context context, RecyclerView recyclerView, int gridSpanCount) {

        //setting the recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, gridSpanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        mSectionedExpandableGridAdapter = new QQGSectionedExpandableGridAdapter(context, mDataArrayList,
                gridLayoutManager);
        recyclerView.setAdapter(mSectionedExpandableGridAdapter);
    }

    public void notifyDataSetChanged() {
        //TODO : handle this condition such that these functions won't be called if the recycler view is on scroll
        generateDataList();
        mSectionedExpandableGridAdapter.notifyDataSetChanged();
    }

    public void addGroup(GroupInfoBean groupInfoBean, ArrayList<ShangpinBean> items) {
        mSectionMap.put(groupInfoBean.getDianpuming(), (groupInfoBean));
        mSectionDataMap.put(groupInfoBean, items);
    }

    public void removeGroup(String section) {
        mSectionDataMap.remove(mSectionMap.get(section));
        mSectionMap.remove(section);
    }

    public void addShangpin(GroupInfoBean groupInfoBean, ShangpinBean shangpinBean) {
        mSectionDataMap.get(mSectionMap.get(groupInfoBean)).add(shangpinBean);
    }

    public void removeShangpin(GroupInfoBean groupInfoBean, ShangpinBean shangpinBean) {
        mSectionDataMap.get(mSectionMap.get(groupInfoBean)).remove(shangpinBean);
    }

    public void addYouHuiQuan(QuerenDingdanYouHuiQuanBean querenDingdanYouHuiQuanBean) {
        this.querenDingdanYouHuiQuanBean = querenDingdanYouHuiQuanBean;
    }

    public void addZhangHuDiYong(DiYongYouHuiBean zhangHuDiYong) {
        this.ZhangHuDiYong = zhangHuDiYong;
    }

    private void generateDataList() {
        mDataArrayList.clear();
        for (Map.Entry<GroupInfoBean, ArrayList<ShangpinBean>> entry : mSectionDataMap.entrySet()) {
            mDataArrayList.add(entry.getKey());
            mDataArrayList.addAll(entry.getValue());
//
//            double kdf = 0;
//            //  double heji = 0;
//
//            for (int i = 0; i < entry.getValue().size(); i++) {
//                Log.e("i size", i + "----size" + entry.getValue().size());
//                if (entry.getValue().get(i).getKuaidifei() > kdf)
//                    kdf = entry.getValue().get(i).getKuaidifei();
////                kdf += entry.getValue().get(i).getKuaidifei()* entry.getValue().get(i).getShangpin_shuliang();
//                // heji += entry.getValue().get(i).getDanjia() * entry.getValue().get(i).getShangpin_shuliang();
//            }
//            mDataArrayList.add(kdf);
//            // mDataArrayList.add(heji);
        }

        if (querenDingdanYouHuiQuanBean != null)
            mDataArrayList.add(querenDingdanYouHuiQuanBean);

        if (ZhangHuDiYong != null)
            mDataArrayList.add(ZhangHuDiYong);

    }
}
