package com.gloiot.hygo.ui.activity.shopping;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.PingLunBean;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.PictureUtlis;
import com.zyd.wlwsdk.utils.imagepicker.ImagePreviewSeeActivity;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;
import com.zyd.wlwsdk.widge.pulltorefresh.PullableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class YonghuPingjiaActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {

    @Bind(R.id.pull_refresh_listview)
    PullableListView pull_refresh_listview;

    @Bind(R.id.loadmore_view)
    RelativeLayout loadmore_view;
    @Bind(R.id.ptrl_refresh_layout)
    PullToRefreshLayout ptrl_refresh_layout;

    private int Loadlength, XiayiyeYeshu = 0;
    private boolean refreshOrLoad;

    private CommonAdapter<PingLunBean> commonAdapter;

    private List<PingLunBean> listDatas = new ArrayList<>(5);

    private String id = "";


    @Override
    public int initResource() {
        return R.layout.activity_yonghu_pingjia;
    }

    @Override
    public void initData() {
        initComponent();

        id = getIntent().getStringExtra("id");

        requestHandleArrayList.add(requestAction.shop_sp_appraise(this, id, XiayiyeYeshu + "", ptrl_refresh_layout, 0));
    }

    public void initComponent() {
        CommonUtlis.setTitleBar(this, "用户评价");

        ptrl_refresh_layout.setOnRefreshListener(this);

        commonAdapter = new CommonAdapter<PingLunBean>(this, R.layout.item_list_yonghu_pingjia, listDatas) {
            @Override
            public void convert(ViewHolder holder, final PingLunBean pingLunBean) {
                PictureUtlis.loadCircularImageViewHolder(mContext, pingLunBean.getPinglun_touxiang_url(), R.drawable.default_image, (ImageView) holder.getView(R.id.item_yonghupinglun_img));

                String nicheng = pingLunBean.getPinglun_Name();
                StringBuilder hideStr = new StringBuilder();
                if (nicheng.length() > 3) {
                    for (int j = 0; j < nicheng.length() - 2; j++) {
                        hideStr.append("*");
                    }
                } else if (nicheng.length() >= 2) {
                    hideStr.append("*");
                }
                StringBuilder sb = new StringBuilder(nicheng);
                if (nicheng.length() > 2) {
                    sb.replace(1, nicheng.length() - 1, hideStr.toString());
                } else if (nicheng.length() > 0) {
                    sb.replace(1, 2, hideStr.toString());
                }
                holder.setText(R.id.item_yonghupinglun_title, sb.toString());
                holder.setText(R.id.item_yonghupinglun_time, pingLunBean.getPinglun_time());
                holder.setText(R.id.item_yonghupinglun_guige, pingLunBean.getPinglun_guige());


                TextView item_yonghupinglun_neirong = holder.getView(R.id.item_yonghupinglun_neirong);
                if (!"".equals(pingLunBean.getPinglun_Neirong())) {
                    item_yonghupinglun_neirong.setVisibility(View.VISIBLE);
                    holder.setText(R.id.item_yonghupinglun_neirong, pingLunBean.getPinglun_Neirong());
                } else {
                    item_yonghupinglun_neirong.setVisibility(View.GONE);
                }

                //图片显示
                GridView gridView = holder.getView(R.id.gv_yonghupingjia_tu);
                if (pingLunBean.getPinglun_list().size() > 0) {
                    gridView.setVisibility(View.VISIBLE);
                    gridView.setAdapter(new CommonAdapter<String>(mContext, R.layout.item_img, pingLunBean.getPinglun_list()) {
                        @Override
                        public void convert(ViewHolder holder, String s) {
                            PictureUtlis.loadImageViewHolder(mContext, s, R.drawable.default_image, (ImageView) holder.getView(R.id.iv_img));
                        }
                    });
                } else {
                    gridView.setVisibility(View.GONE);
                }

                //点击图片
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position < 0 || position > 4)
                            position = 0;

                        Intent intentPreview = new Intent(mContext, ImagePreviewSeeActivity.class);
                        intentPreview.putExtra("imageList", (ArrayList<String>)pingLunBean.getPinglun_list());
                        intentPreview.putExtra("position", position);
                        startActivity(intentPreview);
                    }
                });


                //商家回复
                RelativeLayout rl_yonghupingjia_huifu = holder.getView(R.id.rl_yonghupingjia_huifu);
                if (!"".equals(pingLunBean.getPinglun_Neirong_huifu())) {
                    rl_yonghupingjia_huifu.setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_yonghupingjia_huifu, "掌柜回复：" + pingLunBean.getPinglun_Neirong_huifu());
                } else {
                    rl_yonghupingjia_huifu.setVisibility(View.GONE);
                }

                //用户追加评论
                RelativeLayout rl_yonghupingjia_zhuijia = holder.getView(R.id.rl_yonghupingjia_zhuijia);
                if (!"".equals(pingLunBean.getPinglun_zhuijiapinglun()) || pingLunBean.getPinglun_zhuijia_list().size() > 0) {
                    rl_yonghupingjia_zhuijia.setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_yonghupingjia_zhuijiaTime, pingLunBean.getPinglun_zhuijia_time());

                    TextView tv_yonghupingjia_zhuiping = holder.getView(R.id.tv_yonghupingjia_zhuiping);
                    if (!"".equals(pingLunBean.getPinglun_zhuijiapinglun())) {
                        tv_yonghupingjia_zhuiping.setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_yonghupingjia_zhuiping, pingLunBean.getPinglun_zhuijiapinglun());
                    } else {
                        tv_yonghupingjia_zhuiping.setVisibility(View.GONE);
                    }


                    //用户追加评论回复
                    RelativeLayout rl_yonghupingjia_zuijiahuifu = holder.getView(R.id.rl_yonghupingjia_zuijiahuifu);
                    if (!"".equals(pingLunBean.getPinglun_zhuijiahuifu())) {
                        rl_yonghupingjia_zuijiahuifu.setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_yonghupingjia_zuijiahuifu, "掌柜回复：" + pingLunBean.getPinglun_zhuijiahuifu());
                    } else {
                        rl_yonghupingjia_zuijiahuifu.setVisibility(View.GONE);
                    }

                    //追加图片显示
                    GridView gridView_zhuijia = holder.getView(R.id.gv_yonghupingjia_zhuijiatu);
                    if (pingLunBean.getPinglun_zhuijia_list().size() > 0) {
                        gridView_zhuijia.setVisibility(View.VISIBLE);
                        gridView_zhuijia.setAdapter(new CommonAdapter<String>(mContext, R.layout.item_img, pingLunBean.getPinglun_zhuijia_list()) {
                            @Override
                            public void convert(ViewHolder holder, String s) {
                                PictureUtlis.loadImageViewHolder(mContext, s, R.drawable.default_image, (ImageView) holder.getView(R.id.iv_img));
                            }
                        });
                    } else {
                        gridView_zhuijia.setVisibility(View.GONE);
                    }

                    //点击图片
                    gridView_zhuijia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position < 0 || position > 4)
                                position = 0;

                            Intent intentPreview = new Intent(mContext, ImagePreviewSeeActivity.class);
                            intentPreview.putExtra("imageList", (ArrayList<String>) pingLunBean.getPinglun_zhuijia_list());
                            intentPreview.putExtra("position", position);
                            startActivity(intentPreview);
                        }
                    });

                } else {
                    rl_yonghupingjia_zhuijia.setVisibility(View.GONE);
                }

            }
        };
        pull_refresh_listview.setAdapter(commonAdapter);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        switch (requestTag) {
            case RequestAction.TAG_SHOP_SP_APPRAISE: {
                Log.e("137", response.toString());
                if (refreshOrLoad) {
                    Log.e("清除了数据", "true");
                    listDatas.clear();
                    commonAdapter.notifyDataSetChanged();
                } else {
                    ptrl_refresh_layout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }

                Loadlength = response.getInt("条数");

                JSONArray jsonArray = response.getJSONArray("列表");
                JSONObject jsonObject;
                PingLunBean pingLunBean;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    pingLunBean = new PingLunBean();

                    pingLunBean.setPinglun_touxiang_url(jsonObject.getString("头像"));
                    pingLunBean.setPinglun_Name(jsonObject.getString("昵称"));
                    pingLunBean.setPinglun_time(jsonObject.getString("录入时间"));
                    pingLunBean.setPinglun_Neirong(jsonObject.getString("评论"));
                    pingLunBean.setPinglun_guige(jsonObject.getString("商品规格"));
                    pingLunBean.setPinglun_Neirong_huifu(jsonObject.getString("回复内容"));
                    pingLunBean.setPinglun_zhuijiapinglun(jsonObject.getString("追加评论"));
                    pingLunBean.setPinglun_zhuijia_time(jsonObject.getString("追加时间"));
                    pingLunBean.setPinglun_zhuijiahuifu(jsonObject.getString("追加回复"));

                    JSONArray imageArray = jsonObject.getJSONArray("图片");
                    JSONObject imgObj;
                    for (int imgLength = 0; imgLength < imageArray.length(); imgLength++) {
                        imgObj = imageArray.getJSONObject(imgLength);
                        pingLunBean.getPinglun_list().add(imgObj.getString("imgUrl"));
                    }

                    JSONArray zhuijia_imageArray = jsonObject.getJSONArray("追加评论图片");
                    if (zhuijia_imageArray.length() > 0) {
                        JSONObject zhuijia_imgObj;
                        for (int imgLength = 0; imgLength < zhuijia_imageArray.length(); imgLength++) {
                            zhuijia_imgObj = zhuijia_imageArray.getJSONObject(imgLength);
                            pingLunBean.getPinglun_zhuijia_list().add(zhuijia_imgObj.getString("imgUrl"));
                        }
                    }
                    listDatas.add(pingLunBean);
                }
                if (jsonArray.length() > 0)
                    XiayiyeYeshu = Integer.parseInt(response.getString("页数"));

                commonAdapter.notifyDataSetChanged();
                break;
            }

        }
    }

    /**
     * 用户评价列表初始化数据
     */
    private void initListData() {
        Log.e("initListData", "initListData: " + id);
        if (refreshOrLoad)
            requestHandleArrayList.add(requestAction.shop_sp_appraise(this, id, XiayiyeYeshu + "", ptrl_refresh_layout, 1));
        else
            requestHandleArrayList.add(requestAction.shop_sp_appraise(this, id, XiayiyeYeshu + "", ptrl_refresh_layout, 2));

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refreshOrLoad = true;
        XiayiyeYeshu = 0;
        initListData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        //确保有数据才设置上拉加载更多监听
        if (Loadlength > 0) {
            refreshOrLoad = false;
            initListData();
        } else {
            MyToast.getInstance().showToast(mContext, "已无数据加载");
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
        }
    }
}
