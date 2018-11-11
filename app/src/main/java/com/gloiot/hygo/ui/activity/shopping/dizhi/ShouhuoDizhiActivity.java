package com.gloiot.hygo.ui.activity.shopping.dizhi;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShouhuoAddress;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
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
 * 收货地址列表
 */
public class ShouhuoDizhiActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.rv_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_add_new_address)
    TextView mTvAddNewAddress;
    @Bind(R.id.rl_null_address)
    RelativeLayout mRlNullAddress;
    private CommonAdapter commonAdapter;
    private ShouhuoAddress shouhuoAddress;
    private List<ShouhuoAddress> addressBeanList = new ArrayList<>();
    private int deletePosition = -1;//删除的位置
    private int previousPosition = -1;//之前默认地址的位置,用于局部刷新
    private int setPosition = -1;//当前改动的位置
    private String requestType;//请求类型，取值：show/del/edit/add（这里用到前三个）
    private String addressState;//地址类型：是否是默认地址
    private boolean isResetDefaultOK = true;//是否满足设置默认地址的条件（当默认地址状态为否时，只有条目不变时，才满足条件）

    @Override
    public void onResume() {
        super.onResume();
        addressBeanList.clear();
        requestHandleArrayList.add(requestAction.ShouhuoAddress(this, shouhuoAddress, "show"));
        requestType = "show";
        L.e("请求show", "showrequest");
    }

    @Override
    public int initResource() {
        return R.layout.activity_shouhuo_dizhi;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "收货地址");
        shouhuoAddress = new ShouhuoAddress("", "", "", "", "", "", "", "", "");
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("asdfa", response + "---");
        switch (requestTag) {
            case RequestAction.TAG_SHOUHUOADDRESS:
                L.e("管理收货地址请求成功", "requestType=" + requestType + ",返回数据=" + response.toString());
                if (requestType.equals("show")) {  //查询发货地址
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
                } else if (requestType.equals("del")) {//删除收货地址
                    if (deletePosition != -1) {
                        L.e("删除成功", "deleteposition=" + deletePosition + ",listsize=" + addressBeanList.size());
                        addressBeanList.remove(deletePosition);
                        if (addressBeanList.size() == 0) {
                            mTvAddNewAddress.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.GONE);
                            mRlNullAddress.setVisibility(View.VISIBLE);
                        } else {
//                        commonAdapter.notifyItemRemoved(deletePosition);//有个动画效果，但是整个list没更新。
                            commonAdapter.notifyDataSetChanged();
                        }
                    }
                    deletePosition = -1;
                } else if (requestType.equals("edit")) {//更改默认地址
//                    MToast.showToast(mContext, "更改默认地址成功");
                    if (previousPosition != -1) {
                        if (previousPosition != setPosition) {
                            addressBeanList.get(previousPosition).setDefaultAddress("否");
                            commonAdapter.notifyItemChanged(previousPosition);
                            isResetDefaultOK = false;
                        } else {
                            isResetDefaultOK = true;
                        }
                    }
                    L.e("更改默认地址成功", "previousPosition=" + previousPosition + ",listsize=" + addressBeanList.size() + ",setPosition=" + setPosition);
                    previousPosition = setPosition;
                }
                requestType = "";
                break;
        }
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        commonAdapter = new CommonAdapter<ShouhuoAddress>(mContext, R.layout.item_shouhuo_dizhi_new, addressBeanList) {
            @Override
            public void convert(final ViewHolder holder, final ShouhuoAddress addressBean) {
                holder.setText(R.id.tv_name, addressBean.getShouhuoren());
                holder.setText(R.id.tv_phonenum, addressBean.getPhoneNum());
                holder.setText(R.id.tv_address, addressBean.getShouhuoArea() + addressBean.getDetailedAddress());
                if (addressBean.getDefaultAddress().equals("是")) {
                    holder.setChecked(R.id.cb_set_default, true);
                    holder.setText(R.id.tv_default, "默认地址");
                    previousPosition = holder.getmPosition();
                } else if (addressBean.getDefaultAddress().equals("否")) {
                    holder.setChecked(R.id.cb_set_default, false);
                    holder.setText(R.id.tv_default, "设为默认");
                }
                TextView delete = holder.getView(R.id.tv_delete);
                TextView edit = holder.getView(R.id.tv_edit);
                RelativeLayout rlSetDefault = holder.getView(R.id.rl_checkbox_set_default);
                final CheckBox cbSetDefault = holder.getView(R.id.cb_set_default);
                rlSetDefault.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cbSetDefault.isChecked()) {
                            isResetDefaultOK = true;//主动点击时，重置该状态，注意要先这样设置才能去更改checkbox勾选状态
                            cbSetDefault.setChecked(false);
                        } else {
                            cbSetDefault.setChecked(true);
                        }
                    }
                });
                cbSetDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setPosition = holder.getmPosition();
                        if (buttonView.isChecked()) {

                        } else {
                            isResetDefaultOK = true;//主动点击时，重置该状态，注意要先这样设置才能去更改checkbox勾选状态
                        }
                        if (isChecked) {
                            holder.setText(R.id.tv_default, "默认地址");
                            addressBean.setDefaultAddress("是");
                            requestHandleArrayList.add(requestAction.ShouhuoAddress(ShouhuoDizhiActivity.this, addressBean, "edit"));
                            requestType = "edit";
                        } else {
                            holder.setText(R.id.tv_default, "设为默认");
                            addressBean.setDefaultAddress("否");
                            if (previousPosition == setPosition && isResetDefaultOK) {
                                requestHandleArrayList.add(requestAction.ShouhuoAddress(ShouhuoDizhiActivity.this, addressBean, "edit"));
                                requestType = "edit";
                            }
                        }
                        L.e("准备更改默认地址", "previousPosition==" + previousPosition + ",listsize=" + addressBeanList.size() +
                                ",更改的位置" + holder.getmPosition() + ",setPosition=" + setPosition);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtlis.twoBtnNormal(getmDialog(), "确认删除该地址", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                                deletePosition = holder.getmPosition();
                                requestHandleArrayList.add(requestAction.ShouhuoAddress(ShouhuoDizhiActivity.this, addressBean, "del"));
                                requestType = "del";
                                L.e("准备删除", "deleteposition==" + deletePosition + ",listsize=" + addressBeanList.size());
                            }
                        });
                    }
                });
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShouhuoDizhiActivity.this, BianjiShouhuoDizhiActivity.class);
                        intent.putExtra("type", "编辑");
                        intent.putExtra("addressInfo", addressBean);
                        startActivity(intent);
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

}
