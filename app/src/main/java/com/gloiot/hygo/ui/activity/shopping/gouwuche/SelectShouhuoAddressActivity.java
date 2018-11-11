package com.gloiot.hygo.ui.activity.shopping.gouwuche;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShouhuoAddress;
import com.gloiot.hygo.ui.activity.shopping.dizhi.BianjiShouhuoDizhiActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.recyclerview.CommonAdapter;
import com.zyd.wlwsdk.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 购物车-选择收货地址（2017.05.12）
 */
public class SelectShouhuoAddressActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.rv_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_add_new_address)
    TextView mTvAddNewAddress;
    @Bind(R.id.rl_null_address)
    RelativeLayout mRlNullAddress;
    @Bind(R.id.toptitle_back)
    ImageView toptitle_back;

    private CommonAdapter commonAdapter;
    private ShouhuoAddress shouhuoAddress;
    private ShouhuoAddress address;
    private List<ShouhuoAddress> addressBeanList = new ArrayList<>();
    private String addressState;//地址类型：是否是默认地址
    private String id = "";

    @Override
    public void onResume() {
        super.onResume();
        addressBeanList.clear();
        requestHandleArrayList.add(requestAction.ShouhuoAddress(this, shouhuoAddress, "show"));
    }

    @Override
    public int initResource() {
        return R.layout.activity_select_shouhuo_address;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "选择收货地址");
        shouhuoAddress = new ShouhuoAddress("", "", "", "", "", "", "", "", "");

        id = getIntent().getStringExtra("id");
        toptitle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressBeanList.size() > 0) {
                    for (int i = 0; i < addressBeanList.size(); i++) {
                        if (id.equals(addressBeanList.get(i).getId())) {
                            Intent intent = new Intent();
                            intent.putExtra("addressInfo", addressBeanList.get(i));
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        switch (requestTag) {
            case RequestAction.TAG_SHOUHUOADDRESS:
                L.e("管理收货地址请求成功", ",返回数据=" + response.toString());
                JSONArray jsonArray = response.getJSONArray("列表");
                int num = jsonArray.length();
                if (num != 0) {
                    mRlNullAddress.setVisibility(View.GONE);
                    mTvAddNewAddress.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.getString("状态").equals("默认地址")) {
                            addressState = "是";
                        } else if (jsonObject.getString("状态").equals("显示")) {
                            addressState = "否";
                        }
                        shouhuoAddress = new ShouhuoAddress(jsonObject.getString("id"),
                                jsonObject.getString("收货人"),
                                jsonObject.getString("手机号"),
                                jsonObject.getString("省"),
                                jsonObject.getString("市"),
                                jsonObject.getString("区"),
                                jsonObject.getString("地址"),
                                addressState,
                                jsonObject.getString("收货地区")
                        );
                        addressBeanList.add(shouhuoAddress);
                    }
                    setAdapter();
                } else {
                    mTvAddNewAddress.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    mRlNullAddress.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        commonAdapter = new CommonAdapter<ShouhuoAddress>(mContext, R.layout.item_select_shouhuo_address, addressBeanList) {
            @Override
            public void convert(final ViewHolder holder, final ShouhuoAddress addressBean) {
                holder.setText(R.id.tv_name, addressBean.getShouhuoren());
                holder.setText(R.id.tv_phonenum, addressBean.getPhoneNum());
                holder.setText(R.id.tv_address, addressBean.getShouhuoArea() + "\n" + addressBean.getDetailedAddress());
                holder.getView(R.id.item_bianji_shouhuodizhi).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SelectShouhuoAddressActivity.this, BianjiShouhuoDizhiActivity.class);
                        intent.putExtra("type", "编辑");
                        intent.putExtra("addressInfo", addressBean);
                        startActivity(intent);
                    }
                });

                holder.getView(R.id.ll_shouhuo_address).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("addressInfo", addressBean);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(commonAdapter);
    }

    @OnClick({R.id.tv_add_new_address, R.id.btn_add_new_address})
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_add_new_address:
                intent = new Intent(mContext, BianjiShouhuoDizhiActivity.class);
                intent.putExtra("type", "添加");
                startActivity(intent);
                break;
            case R.id.btn_add_new_address:
                intent = new Intent(mContext, BianjiShouhuoDizhiActivity.class);
                intent.putExtra("type", "添加");
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (addressBeanList.size() > 0) {
            for (int i = 0; i < addressBeanList.size(); i++) {
                Log.e("0000000",addressBeanList.get(i).getId());
                if (id.equals(addressBeanList.get(i).getId())) {
                    Intent intent = new Intent();
                    intent.putExtra("addressInfo", addressBeanList.get(i));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }
    }

}
