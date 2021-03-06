package com.gloiot.hygo.ui.activity.my.shouhou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.recyclerview.CommonAdapter;
import com.zyd.wlwsdk.adapter.recyclerview.OnItemClickListener;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.imagepicker.ImagePreviewSeeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;

public class ShouHouSuccessActivity extends BaseActivity {//商品缩略图
    //商品缩略图
    @Bind(R.id.layout_iv_kuituanxinxi_shangpin_img)
    ImageView layoutIvKuituanxinxiShangpinImg;
    //商品title
    @Bind(R.id.layout_tv_kuituanxinxi_shangpin_title)
    TextView layoutTvKuituanxinxiShangpinTitle;
    //商品规格
    @Bind(R.id.layout_iv_kuituanxiangqing_shangpin_guige)
    TextView layoutIvKuituanxiangqingShangpinGuige;
    //商品数量
    @Bind(R.id.layout_iv_kuituanxiangqing_shangpin_shuliang)
    TextView layoutIvKuituanxiangqingShangpinShuliang;
    //退款类型
    @Bind(R.id.layout_tv_tuikuan_xiangqing_leixing_1)
    TextView layoutTvTuikuanXiangqingLeixing1;
    @Bind(R.id.layout_tv_tuikuan_xiangqing_leixing)
    TextView layoutTvTuikuanXiangqingLeixing;
    @Bind(R.id.layout_ll_kuituanxinxi_tuikuanleixing)
    LinearLayout layoutLlKuituanxinxiTuikuanleixing;
    //退款金额
    @Bind(R.id.layout_tv_tuikuan_xiangqing_jine_1)
    TextView layoutTvTuikuanXiangqingJine1;
    @Bind(R.id.layout_tv_tuikuan_xiangqing_jine)
    TextView layoutTvTuikuanXiangqingJine;
    @Bind(R.id.layout_ll_kuituanxinxi_tuikuanjine)
    LinearLayout layoutLlKuituanxinxiTuikuanjine;
    //退款原因
    @Bind(R.id.layout_tv_tuikuan_xiangqing_yuanyin_1)
    TextView layoutTvTuikuanXiangqingYuanyin1;
    @Bind(R.id.layout_tv_tuikuan_xiangqing_yuanyin)
    TextView layoutTvTuikuanXiangqingYuanyin;
    @Bind(R.id.layout_ll_kuituanxinxi_tuikuanyuanyin)
    LinearLayout layoutLlKuituanxinxiTuikuanyuanyin;
    //退款说明
    @Bind(R.id.layout_tv_tuikuan_xiangqing_shuoming_1)
    TextView layoutTvTuikuanXiangqingShuoming1;
    @Bind(R.id.layout_tv_tuikuan_xiangqing_shuoming)
    TextView layoutTvTuikuanXiangqingShuoming;
    @Bind(R.id.layout_ll_kuituanxinxi_tuikuanshuoming)
    LinearLayout layoutLlKuituanxinxiTuikuanshuoming;
    //退款状态
    @Bind(R.id.layout_tv_tuikuan_xiangqing_zhuangtai_1)
    TextView layoutTvTuikuanXiangqingZhuangtai1;
    @Bind(R.id.layout_tv_tuikuan_xiangqing_zhuangtai)
    TextView layoutTvTuikuanXiangqingZhuangtai;
    @Bind(R.id.layout_ll_kuituanxinxi_tuikuanzhuangtai)
    LinearLayout layoutLlKuituanxinxiTuikuanzhuangtai;
    //退款账户
    @Bind(R.id.layout_tv_tuikuan_xiangqing_zhanghu_1)
    TextView layoutTvTuikuanXiangqingZhanghu1;
    @Bind(R.id.layout_tv_tuikuan_xiangqing_zhanghu)
    TextView layoutTvTuikuanXiangqingZhanghu;
    @Bind(R.id.layout_ll_kuituanxinxi_tuikuanzhanghu)
    LinearLayout layoutLlKuituanxinxiTuikuanzhanghu;
    //申请时间
    @Bind(R.id.layout_tv_tuikuan_xiangqing_shijian_1)
    TextView layoutTvTuikuanXiangqingShijian1;
    @Bind(R.id.layout_tv_tuikuan_xiangqing_shijian)
    TextView layoutTvTuikuanXiangqingShijian;
    @Bind(R.id.layout_ll_kuituanxinxi_shenqingshijian)
    LinearLayout layoutLlKuituanxinxiShenqingshijian;
    //商家同意退款时间
    @Bind(R.id.layout_tv_tuikuan_xiangqing_tongyishijian_1)
    TextView layoutTvTuikuanXiangqingTongyishijian1;
    @Bind(R.id.layout_tv_tuikuan_xiangqing_tongyishijian)
    TextView layoutTvTuikuanXiangqingTongyishijian;
    @Bind(R.id.layout_ll_kuituanxinxi_tongyishijian)
    LinearLayout layoutLlKuituanxinxiTongyishijian;
    //订单编号
    @Bind(R.id.layout_tv_tuikuan_xiangqing_bianhao_1)
    TextView layoutTvTuikuanXiangqingBianhao1;
    @Bind(R.id.layout_tv_tuikuan_xiangqing_bianhao)
    TextView layoutTvTuikuanXiangqingBianhao;
    @Bind(R.id.layout_ll_kuituanxinxi_dingdanbianhao)
    LinearLayout layoutLlKuituanxinxiDingdanbianhao;
    //凭证
    @Bind(R.id.layout_rv_tuikuan_xiangqing_pingzheng)
    RecyclerView layoutRvTuikuanXiangqingPingzheng;
    @Bind(R.id.layout_tv_tuikuan_xiangqing_pingzheng)
    TextView layoutTvTuikuanXiangqingPingzheng;

    //退款成功描述
    @Bind(R.id.tv_shouhou_success_miaoshu)
    TextView tvShouhouSuccessMiaoshu;

    private CommonAdapter commonAdapter;
    private ArrayList<String> imgUrlDatas = new ArrayList<>(5);

    private String myDingdanId;
    private String myShangpinId;
    private String myId;
    private String myLeibie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_shouhou_success;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "退款成功");

        myLeibie = getIntent().getStringExtra("类别");
        myDingdanId = getIntent().getStringExtra("订单id");
        myShangpinId = getIntent().getStringExtra("商品id");
        myId = getIntent().getStringExtra("id");

        initComponent();

        if ("退款".equals(myLeibie)) {
            CommonUtlis.setTitleBar(this, "退款成功");
            requestHandleArrayList.add(requestAction.shop_rf_result_success(this, myDingdanId, myShangpinId, myId, null, null, "0"));
        } else {
            requestHandleArrayList.add(requestAction.shop_rf_result_success(this, myDingdanId, myShangpinId, myId, null, null, "3"));
        }
    }

    private void initComponent() {
        commonAdapter = new CommonAdapter<String>(mContext, R.layout.layout_tuikuankuohuo_dingdan_xinxi_item, imgUrlDatas) {
            @Override
            public void convert(ViewHolder holder, String strUrl) {
                ImageView photoView = holder.getView(R.id.item_tuikuankuohuo_dingdan_img);
                Glide.with(mContext).load(strUrl).into(photoView);
            }
        };
        layoutRvTuikuanXiangqingPingzheng.setLayoutManager(new GridLayoutManager(mContext, 5));
        layoutRvTuikuanXiangqingPingzheng.setAdapter(commonAdapter);
        commonAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                ArrayList<ImageItem> imageItemlist = new ArrayList<>();
                for (String path : imgUrlDatas) {
                    ImageItem imageItem = new ImageItem();
                    imageItem.path = path;
                    imageItemlist.add(imageItem);
                }
                Intent intentPreview = new Intent(mContext, ImagePreviewSeeActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, imageItemlist);
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivity(intentPreview);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess : " + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_RFRESULTSUCESS:
                Glide.with(mContext).load(JSONUtlis.getString(response, "缩略图")).into(layoutIvKuituanxinxiShangpinImg);
                layoutTvKuituanxinxiShangpinTitle.setText(JSONUtlis.getString(response, "商品名称"));
                layoutIvKuituanxiangqingShangpinGuige.setText(JSONUtlis.getString(response, "商品规格"));
                layoutIvKuituanxiangqingShangpinShuliang.setText("×" + JSONUtlis.getString(response, "商品数量"));

                layoutTvTuikuanXiangqingLeixing.setText(JSONUtlis.getString(response, "退款类型"));
                layoutTvTuikuanXiangqingJine.setText(JSONUtlis.getString(response, "退款金额"));
                layoutTvTuikuanXiangqingYuanyin.setText(JSONUtlis.getString(response, "退款原因"));
                layoutTvTuikuanXiangqingShuoming.setText(JSONUtlis.getString(response, "退款说明"));
                layoutTvTuikuanXiangqingZhuangtai.setText(JSONUtlis.getString(response, "退款状态"));
                layoutTvTuikuanXiangqingZhanghu.setText(JSONUtlis.getString(response, "退款账户"));
                layoutTvTuikuanXiangqingShijian.setText(JSONUtlis.getString(response, "申请时间"));
                layoutTvTuikuanXiangqingTongyishijian.setText(JSONUtlis.getString(response, "商家同意退款时间"));
                layoutTvTuikuanXiangqingBianhao.setText(JSONUtlis.getString(response, "订单id"));
                switch (layoutTvTuikuanXiangqingZhuangtai.getText().toString()) {
                    case "已撤销":
                        layoutTvTuikuanXiangqingZhuangtai.setTextColor(ContextCompat.getColor(mContext, R.color.cl_666666));
                        break;
                    case "处理中":
                        layoutTvTuikuanXiangqingZhuangtai.setTextColor(ContextCompat.getColor(mContext, R.color.cl_2b9ced));
                        break;
                    case "退款成功":
                        layoutTvTuikuanXiangqingZhuangtai.setTextColor(ContextCompat.getColor(mContext, R.color.cl_965aff));
                        break;
                    case "退款失败":
                        layoutTvTuikuanXiangqingZhuangtai.setTextColor(ContextCompat.getColor(mContext, R.color.cl_ff436f));
                        break;
                    default:
                        layoutTvTuikuanXiangqingZhuangtai.setTextColor(ContextCompat.getColor(mContext, R.color.cl_666666));
                        break;
                }

                tvShouhouSuccessMiaoshu.setText("钱款将返还至您的" + JSONUtlis.getString(response, "退款账户") +
                        "，视系统不同退款到账时间为2-7个工作日");
                JSONArray imgArray = response.getJSONArray("凭证");
                JSONObject obj;
                imgUrlDatas.clear();
                for (int i = 0; i < imgArray.length(); i++) {
                    obj = imgArray.getJSONObject(i);
                    imgUrlDatas.add(obj.getString("imgUrl"));
                }
                commonAdapter.notifyDataSetChanged();
                if (imgUrlDatas.size() == 0) {
                    layoutRvTuikuanXiangqingPingzheng.setVisibility(View.GONE);
                    layoutTvTuikuanXiangqingPingzheng.setVisibility(View.GONE);
                } else {
                    layoutRvTuikuanXiangqingPingzheng.setVisibility(View.VISIBLE);
                    layoutTvTuikuanXiangqingPingzheng.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
