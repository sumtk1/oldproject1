package com.gloiot.hygo.ui.activity.my.zhifubao;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.my.ziliao.shimingrenzheng.RenZheng01Activity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.widge.swipe.MySwipe;
import com.zyd.wlwsdk.widge.swipe.SwipeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class WoDeZhiFuBaoActivity extends BaseActivity {
    @Bind(R.id.lv_my_zhifubao)
    ListView lvMyzhifubao;

    private View view;
    private List<ZhiFuBaoBean> list = new ArrayList<>();
    private ZhiFuBaoAdapter zhiFuBaoAdapter;

    @Override
    public int initResource() {
        return R.layout.activity_wode_zhifubao;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "关联支付宝");
        view = View.inflate(mContext, R.layout.item_tianjiayinhangka, null);
        ((TextView) view.findViewById(R.id.tv_tianjia)).setText("添加支付宝账户");
        lvMyzhifubao.addFooterView(view);
        //添加支付宝
        view.findViewById(R.id.item_listcard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!preferences.getString(ConstantUtlis.SP_USERSMRZ, "").equals("已认证")) {
                    DialogUtlis.twoBtnNormal(getmDialog(), "提示", "请先进行实名认证,一经认证通过，以后只能添加该认证人支付宝", true, "取消", "去认证",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                    Intent intent = new Intent(mContext, RenZheng01Activity.class);
                                    mContext.startActivity(intent);
                                }
                            });
                } else {
                    Intent intent = new Intent(mContext, AddAlipayActivity.class);
                    startActivity(intent);
                }
            }
        });
        zhiFuBaoAdapter = new ZhiFuBaoAdapter(mContext, list){
            @Override
            public void convert(ViewHolder holder, final ZhiFuBaoBean zhiFuBaoBean) {
                super.convert(holder, zhiFuBaoBean);

                SwipeLayout sl = (SwipeLayout) holder.getConvertView();
                sl.close(false, false);
                sl.getFrontView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                int p = holder.getmPosition();//选中的位置
                Button b2 = holder.getView(R.id.bt_alipay_delete);
                sl.setSwipeListener(MySwipe.mSwipeListener);
                b2.setTag(p);
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DialogUtlis.customPwd(getmDialog(), null, false, new MDialogInterface.PasswordInter() {
                            @Override
                            public void callback(String data) {
                                requestHandleArrayList.add(requestAction.shop_aliacc_edit(WoDeZhiFuBaoActivity.this, "del",
                                        zhiFuBaoBean.zhifubao_id, zhiFuBaoBean.zhifubao_zhanghao, zhiFuBaoBean.zhifubao_xingming, data
                                        , ""));
                            }
                        });
                    }
                });
            }
        };
        lvMyzhifubao.setAdapter(zhiFuBaoAdapter);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess: " + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_SHOP_ALIACC_LIST:
                list.clear();
                zhiFuBaoAdapter.shuaXinShuJu();

                JSONArray jsonArray = response.getJSONArray("list");
                ZhiFuBaoBean zhiFuBaoBean;
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    zhiFuBaoBean = new ZhiFuBaoBean();
                    jsonObject = jsonArray.getJSONObject(i);

                    zhiFuBaoBean.zhifubao_id = JSONUtlis.getString(jsonObject, "id");
                    zhiFuBaoBean.zhifubao_xingming = JSONUtlis.getString(jsonObject, "name");
                    zhiFuBaoBean.zhifubao_zhanghao = JSONUtlis.getString(jsonObject, "account");
                    zhiFuBaoBean.zhifubao_wuxiaoka = JSONUtlis.getString(jsonObject, "状态");
                    list.add(zhiFuBaoBean);
                }
                zhiFuBaoAdapter.shuaXinShuJu();
                break;

            case RequestAction.TAG_SHOP_ALIACC_EDIT:
                //"状态": "成功"
                list.clear();
                zhiFuBaoAdapter.shuaXinShuJu();
                requestHandleArrayList.add(requestAction.shop_aliacc_list(this));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.shop_aliacc_list(this));
    }
}
