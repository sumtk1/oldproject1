package com.gloiot.hygo.ui.activity.my.shouhou;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.my.kefu.SelectKeFuActivity;
import com.gloiot.hygo.ui.activity.shopping.wodedingdan.ChakanWuliuActicity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.recyclerview.CommonAdapter;
import com.zyd.wlwsdk.adapter.recyclerview.OnItemClickListener;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.imagepicker.ImagePreviewSeeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dwj on 2017/6/8 .
 * 仅退款，提交申请后查看提交信息
 */

public class TuiKuanXiangQingActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.shouhou_zhuangtai)
    TextView tvTuikuanZhuangtai;
    @Bind(R.id.in_titlebar)
    View inTitleBar;

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

    //剩余时间
    @Bind(R.id.tv_tuikuan_xiangqing_daojishi)
    TextView tvTuikuanXiangqingDaojishi;
    //快递公司
    @Bind(R.id.tv_shouhou_xiangqing_kuaidigongsi)
    TextView tvShouhouXiangqingKuaidigongsi;
    @Bind(R.id.rl_shouhou_xiangqing_kuaidigongsi)
    RelativeLayout rlShouhouXiangqingKuaidigongsi;
    //快递单号
    @Bind(R.id.tv_shouhou_xiangqing_kuaididanhao)
    TextView tvShouhouXiangqingKuaididanhao;
    @Bind(R.id.rl_shouhou_xiangqing_kuaididanhao)
    RelativeLayout rlShouhouXiangqingKuaididanhao;
    //物流
    @Bind(R.id.bt_wuliu)
    Button btWuliu;
    //客服
    @Bind(R.id.bt_kefu)
    Button btKefu;
    //描述
    @Bind(R.id.tv_01)
    TextView tv01;
    @Bind(R.id.tv_02)
    TextView tv02;
    @Bind(R.id.tv_03)
    TextView tv03;


    private CommonAdapter commonAdapter;
    private ArrayList<String> imgUrlDatas = new ArrayList<>(5);

    private String strDingDanId;
    //    private boolean tiaozhuan; // 记录:代表从退款记录页面进入
    private int type;  //1代表重新申请 0代表撤销申请
    private String strShangpinId = "";
    private String strId = "";

    private String xiangqing_leixing = "";

    private String isCheXiao = "";

    @Override
    public int initResource() {
        return R.layout.activity_refund_details;
    }

    @Override
    public void initData() {
        inTitleBar.findViewById(R.id.toptitle_back).setOnClickListener(this);

        xiangqing_leixing = getIntent().getStringExtra("详情类型");

        strDingDanId = getIntent().getStringExtra("订单id");
        strShangpinId = getIntent().getStringExtra("商品id");
        strId = getIntent().getStringExtra("id");

        initComponent();

        Intent intent = new Intent();
        this.setResult(886, intent);


        if ("退款".equals(xiangqing_leixing)) {
            requestHandleArrayList.add(requestAction.shop_rf_details(TuiKuanXiangQingActivity.this, strDingDanId, strShangpinId, strId));
            CommonUtlis.setTitleBar(this, "退款详情");
            tvTuikuanZhuangtai.setText("您已成功发起退款申请，请耐心等待商家处理。");
            tv01.setText("·如果商家同意：申请将达成并退款至你的支付账户");
        } else if ("退货".equals(xiangqing_leixing)) {
            requestHandleArrayList.add(requestAction.shop_rf_goods_detail(TuiKuanXiangQingActivity.this, strDingDanId, strShangpinId, strId));
            CommonUtlis.setTitleBar(this, "退货详情");
            tvTuikuanZhuangtai.setText("您已成功发起退货退款申请，请耐心等待商家处理。");
            tv01.setText("·如果商家同意：申请将达成并需要你退货给商家");
        } else if ("等待商家确认收货".equals(xiangqing_leixing)) {
            requestHandleArrayList.add(requestAction.shop_rf_wait(TuiKuanXiangQingActivity.this, strDingDanId, strShangpinId, strId));
            CommonUtlis.setTitleBar(this, "等待退款");
            tvTuikuanZhuangtai.setText("您已成功退货给商家，请耐心等待商家处理。");
            tv01.setVisibility(View.GONE);
            tv02.setVisibility(View.GONE);
            tv03.setVisibility(View.GONE);
            rlShouhouXiangqingKuaidigongsi.setVisibility(View.VISIBLE);
            rlShouhouXiangqingKuaididanhao.setVisibility(View.VISIBLE);
            btWuliu.setVisibility(View.VISIBLE);
            btKefu.setVisibility(View.VISIBLE);
        }
    }

    private void initComponent() {
        //隐藏多余不必显示的UI界面
        layoutLlKuituanxinxiTuikuanzhuangtai.setVisibility(View.GONE);//退款状态
        layoutLlKuituanxinxiTuikuanzhanghu.setVisibility(View.GONE);//退款账户
        layoutLlKuituanxinxiTongyishijian.setVisibility(View.GONE);//商家同意退款时间

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
        Intent intent;
        super.requestSuccess(requestTag, response, showLoad);
        Log.e("requestSuccess" + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_SHOPRFDETAILS:
                if (response.getString("状态").equals("成功")) {
                    tvTuikuanXiangqingDaojishi.setText("还剩" + JSONUtlis.getString(response, "剩余时间"));

                    Glide.with(mContext).load(JSONUtlis.getString(response, "缩略图")).into(layoutIvKuituanxinxiShangpinImg);
                    layoutTvKuituanxinxiShangpinTitle.setText(JSONUtlis.getString(response, "商品名称"));
                    layoutIvKuituanxiangqingShangpinGuige.setText(JSONUtlis.getString(response, "规格"));
                    layoutIvKuituanxiangqingShangpinShuliang.setText("×" + JSONUtlis.getString(response, "商品数量"));

                    layoutTvTuikuanXiangqingLeixing.setText(JSONUtlis.getString(response, "退款类型"));
                    layoutTvTuikuanXiangqingJine.setText(JSONUtlis.getString(response, "退款金额"));
                    layoutTvTuikuanXiangqingYuanyin.setText(JSONUtlis.getString(response, "退款原因"));
                    layoutTvTuikuanXiangqingShuoming.setText(JSONUtlis.getString(response, "退款说明"));
                    layoutTvTuikuanXiangqingShijian.setText(JSONUtlis.getString(response, "录入时间"));
                    layoutTvTuikuanXiangqingBianhao.setText(JSONUtlis.getString(response, "订单id"));
                    tvTuikuanXiangqingDaojishi.setText("还剩" + JSONUtlis.getString(response, "剩余时间"));
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

                    isCheXiao = JSONUtlis.getString(response, "是否撤销");
                }
                break;

            case RequestAction.TAG_SHOPRFGOODSDETAILS:
                tvTuikuanXiangqingDaojishi.setText("还剩" + JSONUtlis.getString(response, "剩余时间"));

                Glide.with(mContext).load(JSONUtlis.getString(response, "缩略图")).into(layoutIvKuituanxinxiShangpinImg);
                layoutTvKuituanxinxiShangpinTitle.setText(JSONUtlis.getString(response, "商品名称"));
                layoutIvKuituanxiangqingShangpinGuige.setText(JSONUtlis.getString(response, "规格"));
                layoutIvKuituanxiangqingShangpinShuliang.setText("×" + JSONUtlis.getString(response, "商品数量"));

                layoutTvTuikuanXiangqingLeixing.setText(JSONUtlis.getString(response, "退款类型"));
                layoutTvTuikuanXiangqingJine.setText(JSONUtlis.getString(response, "退款金额"));
                layoutTvTuikuanXiangqingYuanyin.setText(JSONUtlis.getString(response, "退款原因"));
                layoutTvTuikuanXiangqingShuoming.setText(JSONUtlis.getString(response, "退款说明"));
                layoutTvTuikuanXiangqingShijian.setText(JSONUtlis.getString(response, "录入时间"));
                layoutTvTuikuanXiangqingBianhao.setText(JSONUtlis.getString(response, "订单id"));
                tvTuikuanXiangqingDaojishi.setText("还剩" + JSONUtlis.getString(response, "剩余时间"));
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
                isCheXiao = JSONUtlis.getString(response, "是否撤销");
                break;

            case RequestAction.TAG_RFWAIT:
                tvTuikuanXiangqingDaojishi.setText("还剩" + JSONUtlis.getString(response, "剩余时间"));

                tvShouhouXiangqingKuaididanhao.setText(JSONUtlis.getString(response, "快递单号"));
                tvShouhouXiangqingKuaidigongsi.setText(JSONUtlis.getString(response, "快递公司"));

                Glide.with(mContext).load(JSONUtlis.getString(response, "缩略图")).into(layoutIvKuituanxinxiShangpinImg);
                layoutTvKuituanxinxiShangpinTitle.setText(JSONUtlis.getString(response, "商品名称"));
                layoutIvKuituanxiangqingShangpinGuige.setText(JSONUtlis.getString(response, "规格"));
                layoutIvKuituanxiangqingShangpinShuliang.setText("×" + JSONUtlis.getString(response, "商品数量"));

                layoutTvTuikuanXiangqingLeixing.setText(JSONUtlis.getString(response, "退款类型"));
                layoutTvTuikuanXiangqingJine.setText(JSONUtlis.getString(response, "退款金额"));
                layoutTvTuikuanXiangqingYuanyin.setText(JSONUtlis.getString(response, "退款原因"));
                layoutTvTuikuanXiangqingShuoming.setText(JSONUtlis.getString(response, "退款说明"));
                layoutTvTuikuanXiangqingShijian.setText(JSONUtlis.getString(response, "申请时间"));
                layoutTvTuikuanXiangqingBianhao.setText(JSONUtlis.getString(response, "订单id"));
                JSONArray imgArray1 = response.getJSONArray("凭证");
                JSONObject obj1;
                imgUrlDatas.clear();
                for (int i = 0; i < imgArray1.length(); i++) {
                    obj1 = imgArray1.getJSONObject(i);
                    imgUrlDatas.add(obj1.getString("imgUrl"));
                }
                commonAdapter.notifyDataSetChanged();
                if (imgUrlDatas.size() == 0) {
                    layoutRvTuikuanXiangqingPingzheng.setVisibility(View.GONE);
                    layoutTvTuikuanXiangqingPingzheng.setVisibility(View.GONE);
                } else {
                    layoutRvTuikuanXiangqingPingzheng.setVisibility(View.VISIBLE);
                    layoutTvTuikuanXiangqingPingzheng.setVisibility(View.VISIBLE);
                }
                isCheXiao = JSONUtlis.getString(response, "是否撤销");
                break;

            //退款撤销
            case RequestAction.TAG_RFEDIT:
                intent = new Intent(mContext, ShouHouChexiaoActivity.class);
                intent.putExtra("订单id", strDingDanId);
                intent.putExtra("商品id", strShangpinId);
                intent.putExtra("id", strId);
                intent.putExtra("类别", "退款");
                startActivity(intent);
                finish();
                break;

            //退货撤销
            case RequestAction.TAG_RFGOODSEDIT:
                intent = new Intent(mContext, ShouHouChexiaoActivity.class);
                intent.putExtra("订单id", strDingDanId);
                intent.putExtra("商品id", strShangpinId);
                intent.putExtra("id", strId);
                intent.putExtra("类别", "退货");
                startActivity(intent);
                finish();
                break;
        }
    }

    @OnClick({R.id.bt_repeal, R.id.bt_wuliu, R.id.bt_kefu})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_repeal: //撤销申请
                if ("是".equals(isCheXiao)) {
                    if ("等待商家确认收货".equals(xiangqing_leixing)) {
                        DialogUtlis.twoBtnNormal(getmDialog(), "你已退货给商家，是否撤销申请？", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                                type = 0;
                                qingQiuShuJu(type);
                            }
                        });
                    } else {
                        DialogUtlis.twoBtnNormal(getmDialog(), "只有一次撤销申请机会，是否撤销申请？", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                                type = 0;
                                qingQiuShuJu(type);
                            }
                        });
                    }
                } else if ("否".equals(isCheXiao)) {
                    DialogUtlis.oneBtnNormal(getmDialog(), "同一订单只允许撤销申请一次");
                }
                break;

            case R.id.bt_wuliu: //物流
                Intent intent = new Intent(TuiKuanXiangQingActivity.this, ChakanWuliuActicity.class);
                intent.putExtra("id", layoutTvTuikuanXiangqingBianhao.getText().toString());
                intent.putExtra("商品id", strShangpinId);
                intent.putExtra("订单销售id", strId);
                intent.putExtra("物流类型", "售后");
                startActivity(intent);
                break;

            case R.id.bt_kefu: //客服
                Intent it = new Intent(mContext, SelectKeFuActivity.class);
                it.putExtra("客服组名称", "商城客服");
                startActivity(it);
                break;
        }
    }

    private void qingQiuShuJu(int type) {
        if ("退款".equals(xiangqing_leixing)) {
            requestHandleArrayList.add(requestAction.shop_rf_edit(TuiKuanXiangQingActivity.this, strDingDanId, strShangpinId, strId, null, null, null, null, null, type + ""));
        } else {
            requestHandleArrayList.add(requestAction.shop_rf_goods_edit(TuiKuanXiangQingActivity.this, strDingDanId, strShangpinId, strId, null, null, null, null, 0 + ""));
        }
    }
}
