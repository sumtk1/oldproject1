package com.gloiot.hygo.ui.activity.my.shouhou;

import android.content.Intent;
import android.os.Bundle;
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
import com.gloiot.hygo.ui.activity.my.kefu.SelectKeFuActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
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
import butterknife.OnClick;

public class ShouHouFanKuiActivity extends BaseActivity implements View.OnClickListener {
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

    //售后信息头部说明
    @Bind(R.id.layout_tv_kuituanxinxi)
    TextView layoutTvKuituanxinxi;

    private String myDingdanId;
    private String myShangpinId;
    private String myId;
    private String myLeibie;
    private String dingdanzhuangtai;

    private CommonAdapter commonAdapter;
    private ArrayList<String> imgUrlDatas = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_shouhou_fankui;
    }

    @Override
    public void initData() {

        myLeibie = getIntent().getStringExtra("类别");
        myDingdanId = getIntent().getStringExtra("订单id");
        myShangpinId = getIntent().getStringExtra("商品id");
        myId = getIntent().getStringExtra("id");
        dingdanzhuangtai = getIntent().getStringExtra("订单状态");

        initComponent();

        if ("退款".equals(myLeibie)) {
            CommonUtlis.setTitleBar(this, "退款申请反馈");
            requestHandleArrayList.add(requestAction.shop_rf_overtime(this, myDingdanId, myShangpinId, myId, "0"));
            layoutTvKuituanxinxi.setText("退款信息");
        } else {
            CommonUtlis.setTitleBar(this, "退款退货申请反馈");
            requestHandleArrayList.add(requestAction.shop_rf_overtime(this, myDingdanId, myShangpinId, myId, "1"));
            layoutTvKuituanxinxi.setText("退货退款信息");
        }
    }

    private void initComponent() {
        layoutLlKuituanxinxiTuikuanzhuangtai.setVisibility(View.GONE);
        layoutLlKuituanxinxiTuikuanzhanghu.setVisibility(View.GONE);
        layoutLlKuituanxinxiTongyishijian.setVisibility(View.GONE);

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
                Intent intentPreview = new Intent(mContext, ImagePreviewSeeActivity.class);
                intentPreview.putExtra("imageList", imgUrlDatas);
                intentPreview.putExtra("position", position);
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
            case RequestAction.TAG_SHOP_RF_OVERTIME:
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

    @OnClick({
            R.id.btn_shouhou_fankui_kefujieru, //客服介入
            R.id.btn_shouhou_fankui_zaicishenqing //再次申请
    })
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_shouhou_fankui_kefujieru:
                Intent it = new Intent(mContext, SelectKeFuActivity.class);
                it.putExtra("客服组名称", "商城客服");
                startActivity(it);
                break;

            case R.id.btn_shouhou_fankui_zaicishenqing:
                Intent intent = new Intent(mContext, TuikuanOrTuihuo.class);
                intent.putExtra("订单id", myDingdanId);
                intent.putExtra("商品id", myShangpinId);
                intent.putExtra("id", myId);
                intent.putExtra("订单状态", dingdanzhuangtai);
                mContext.startActivity(intent);
                break;
        }
    }
}
