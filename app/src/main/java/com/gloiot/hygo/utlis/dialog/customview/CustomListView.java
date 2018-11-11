package com.gloiot.hygo.utlis.dialog.customview;

import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.widge.MDialog;

import java.util.ArrayList;

/**
 * @author ljz.
 * @date 2017/11/7
 * ListView单选
 */

public class CustomListView {

    /**
     * @param mDialog           dialog
     * @param title             标题
     * @param datas             数据
     * @param listViewDataInter 点击回调
     */
    public void builder(final MDialog mDialog, String title, final ArrayList<String> datas, final MDialogInterface.ListViewOnClickInter listViewDataInter) {
        mDialog.initDialog().withTitie(title)
                .showCustomPanelLine()
                .setCustomView(R.layout.dialog_son_listview, new MDialog.CustomInter() {
                    @Override
                    public void custom(View customView) {
                        ListView listView = (ListView) customView.findViewById(R.id.dialog_listview);
                        listView.setVerticalScrollBarEnabled(false);
                        listView.addFooterView(new ViewStub(customView.getContext()));
                        listView.setAdapter(new CommonAdapter<String>(customView.getContext(), R.layout.item_textcenter, datas) {
                            @Override
                            public void convert(ViewHolder holder, String s) {
                                holder.setText(R.id.tv_item_center, s);
                            }
                        });
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                listViewDataInter.onItemClick(datas.get(position), position);
                                mDialog.dismiss();
                            }
                        });

                        mDialog.setMaxHeight(customView);
                    }
                })
                .setBtn1Text("取消")
                .setBtn1(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                }).show();
    }
}
