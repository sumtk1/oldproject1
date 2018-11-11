package com.gloiot.hygo.ui.activity.my.kefu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.my.yijianfankui.YiJianFanKuiActivity;
import com.gloiot.hygo.ui.activity.web.WebActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class KeHuZhongXinActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.lv_changjian_wenti)
    ListView lvChangjianWenti;
    @Bind(R.id.banner_kehu_zhongxin)
    Banner bannerKehuZhongxin;

    private CommonAdapter wenTiAdapter;
    private List<WenTiBean> wentiDatas = new ArrayList<>(5);
    private ArrayList<String> lunBoTuDatas = new ArrayList<>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_kehu_zhongxin;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "客户中心", "", true);

        //常见问题 Adapter
        wenTiAdapter = new CommonAdapter<WenTiBean>(this, R.layout.item_kefu_changjianwenti, wentiDatas) {
            @Override
            public void convert(ViewHolder holder, WenTiBean wenTiBean) {
                holder.setText(R.id.tv_kefu_changjianti, wenTiBean.getTitle());
            }
        };
        lvChangjianWenti.setAdapter(wenTiAdapter);
        CommonUtlis.setListViewHeightBasedOnChildren(lvChangjianWenti);

        lvChangjianWenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("url", wentiDatas.get(position).getUrl());
                startActivity(intent);
            }
        });

        requestHandleArrayList.add(requestAction.KeHuZhongXin(this));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess : " + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_KEHU_ZHONGXIN:
                JSONObject jsonObject = response.getJSONObject("列表");

                //常见问题
                JSONArray wenTiArray = jsonObject.getJSONArray("常见问题");
                wentiDatas.clear();
                JSONObject wentiObj;
                WenTiBean wenTiBean;
                for (int i = 0; i < wenTiArray.length(); i++) {
                    wentiObj = wenTiArray.getJSONObject(i);
                    wenTiBean = new WenTiBean();
                    wenTiBean.setTitle(wentiObj.getString("标题"));
                    wenTiBean.setUrl(wentiObj.getString("跳转网址"));
                    wentiDatas.add(wenTiBean);
                }
                wenTiAdapter.notifyDataSetChanged();
                CommonUtlis.setListViewHeightBasedOnChildren(lvChangjianWenti);

                //轮播图
                JSONArray lunBoTuArray = jsonObject.getJSONArray("轮播图");
                JSONObject lunBoTuObj;
                lunBoTuDatas.clear();
                for (int i = 0; i < lunBoTuArray.length(); i++) {
                    lunBoTuObj = lunBoTuArray.getJSONObject(i);
                    lunBoTuDatas.add(lunBoTuObj.getString("图片"));
                }
                initLunbo(lunBoTuDatas);
                break;
            default:
                break;
        }
    }

    /**
     * 轮播图
     *
     * @param list
     */
    public void initLunbo(final ArrayList<String> list) {
        ArrayList<String> imgList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            imgList.add(list.get(i));
        }
        //初始化顶部轮播
        bannerKehuZhongxin.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);//设置轮播样式
        bannerKehuZhongxin.setIndicatorGravity(BannerConfig.CENTER);//设置指示器位置
        bannerKehuZhongxin.setDelayTime(3000);//设置间隔时间
        bannerKehuZhongxin.setImages(imgList);//设置图片
        bannerKehuZhongxin.setImageLoader(new GlideImageLoader()).start();
    }

    @OnClick({R.id.ll_kefu_zhongxin_zhinengkefu, R.id.ll_kefu_zhongxin_rengongkefu,
            R.id.ll_kefu_zhongxin_yijianfankui})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_kefu_zhongxin_zhinengkefu:
                MyToast.getInstance().showToast(mContext, "功能优化中");
                break;
            case R.id.ll_kefu_zhongxin_rengongkefu:
                startActivity(new Intent(KeHuZhongXinActivity.this, RenGongKeFuActivity.class));
                break;
            case R.id.ll_kefu_zhongxin_yijianfankui:
                startActivity(new Intent(KeHuZhongXinActivity.this, YiJianFanKuiActivity.class));
                break;
        }
    }

    //常见问题Bean
    class WenTiBean {
        private String title;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
