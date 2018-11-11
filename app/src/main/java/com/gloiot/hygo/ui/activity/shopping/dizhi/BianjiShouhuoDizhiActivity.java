package com.gloiot.hygo.ui.activity.shopping.dizhi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShouhuoAddress;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.lljjcoder.citypickerview.widget.CityPicker;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 编辑收货地址（添加/修改）
 */
public class BianjiShouhuoDizhiActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_shouhuoren_name)
    EditText mEtShouhuorenName;
    @Bind(R.id.et_shouhuoren_phonenum)
    EditText mEtShouhuorenPhoneNum;
    @Bind(R.id.tv_shouhuoren_area)
    TextView mTvShouhuorenArea;
    @Bind(R.id.et_address_detail)
    EditText mEtAddressDetail;
    @Bind(R.id.rl_set_default_address)
    RelativeLayout mRlSetDefaultAddress;
    @Bind(R.id.cb_set_default)
    CheckBox mCbSetDefault;
    @Bind(R.id.btn_save)
    Button mBtnSave;

    private String typeFlag;//类型：修改/添加
    private ShouhuoAddress addressInfo;
    private String mShouhuorenName, mShouhuorenPhoneNum, mShouhuorenArea, mShouhuorenProvince, mShuohuorenCity,
            mShohuorenDistrict, mShouhuorenAddressDetail, mAddressState;

    //用于判断是否做了修改
    private String Str_Name, Str_Phone, Str_Dizhi, Str_Xiangxi;
    private boolean bool_moren;
    private boolean Bool_Name, Bool_Phone, Bool_Dizhi, Bool_Xiangxi, Bool_MoRen;

    @Override
    public int initResource() {
        return R.layout.activity_bianji_shouhuo_dizhi;
    }

    @Override
    public void initData() {
        typeFlag = getIntent().getStringExtra("type");
        if (typeFlag.equals("编辑")) {
            addressInfo = (ShouhuoAddress) getIntent().getSerializableExtra("addressInfo");
            CommonUtlis.setTitleBar(this, "修改收货地址");
            mBtnSave.setEnabled(false);
//            mBtnSave.setBackgroundResource(R.drawable.bg_no_but_fillet);

            //用于判断是否做了修改     开始
            Str_Name = addressInfo.getShouhuoren();
            Str_Phone = addressInfo.getPhoneNum();
            Str_Dizhi = addressInfo.getShouhuoArea();
            Str_Xiangxi = addressInfo.getDetailedAddress();
            //结束

            mEtShouhuorenName.setText(addressInfo.getShouhuoren());
            mEtShouhuorenName.setSelection(addressInfo.getShouhuoren().length());//光标移到最后
            mEtShouhuorenPhoneNum.setText(addressInfo.getPhoneNum());
            mEtAddressDetail.setText(addressInfo.getDetailedAddress());
            mShouhuorenArea = addressInfo.getShouhuoArea();
            mShouhuorenProvince = addressInfo.getProvince();
            mShuohuorenCity = addressInfo.getCity();
            mShohuorenDistrict = addressInfo.getDistrict();
            mTvShouhuorenArea.setText(mShouhuorenArea);

            if (addressInfo.getDefaultAddress().equals("是")) {
                mCbSetDefault.setChecked(true);
                Bool_MoRen = true;
                bool_moren = true;
            } else {
                mCbSetDefault.setChecked(false);
                Bool_MoRen = false;
                bool_moren = false;
            }

            //判断信息是否改变
            TextWatcher();

            mCbSetDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
//                    requestHandleArrayList.add(requestAction.shop_dz(BianjiShouhuoDizhiActivity.this, addressInfo.getId()));
                        bool_moren = true;
                    } else {
                        bool_moren = false;
                    }

                    if (Bool_Name || Bool_Phone || Bool_Dizhi || Bool_Xiangxi || (bool_moren != Bool_MoRen)) {
                        mBtnSave.setEnabled(true);
//                        mBtnSave.setBackgroundResource(R.drawable.bg_btn_fillet_violet);
                    }else {
                        mBtnSave.setEnabled(false);
//                        mBtnSave.setBackgroundResource(R.drawable.bg_no_but_fillet);
                    }
                }
            });

        } else if (typeFlag.equals("添加")) {
            CommonUtlis.setTitleBar(this, "添加收货地址");
        }
    }

    private void TextWatcher() {
        mEtShouhuorenName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Str_Name.equals(s.toString()))
                    Bool_Name = false;
                else
                    Bool_Name = true;

                if (Bool_Name || Bool_Phone || Bool_Dizhi || Bool_Xiangxi || (bool_moren != Bool_MoRen)){
                    mBtnSave.setEnabled(true);
//                    mBtnSave.setBackgroundResource(R.drawable.bg_btn_fillet_violet);
                }else {
                    mBtnSave.setEnabled(false);
//                    mBtnSave.setBackgroundResource(R.drawable.bg_no_but_fillet);
                }
            }
        });

        mEtShouhuorenPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Str_Phone.equals(s.toString()))
                    Bool_Phone = false;
                else
                    Bool_Phone = true;

                if (Bool_Name || Bool_Phone || Bool_Dizhi || Bool_Xiangxi || (bool_moren != Bool_MoRen)) {
                    mBtnSave.setEnabled(true);
//                    mBtnSave.setBackgroundResource(R.drawable.bg_btn_fillet_violet);
                }else {
                    mBtnSave.setEnabled(false);
//                    mBtnSave.setBackgroundResource(R.drawable.bg_no_but_fillet);
                }
            }
        });

        mTvShouhuorenArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Str_Dizhi.equals(s.toString()))
                    Bool_Dizhi = false;
                else
                    Bool_Dizhi = true;

                if (Bool_Name || Bool_Phone || Bool_Dizhi || Bool_Xiangxi || (bool_moren != Bool_MoRen)){
                    mBtnSave.setEnabled(true);
//                    mBtnSave.setBackgroundResource(R.drawable.bg_btn_fillet_violet);
                }else{
                    mBtnSave.setEnabled(false);
//                    mBtnSave.setBackgroundResource(R.drawable.bg_no_but_fillet);
                }
            }
        });

        mEtAddressDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Str_Xiangxi.equals(s.toString()))
                    Bool_Xiangxi = false;
                else
                    Bool_Xiangxi = true;

                if (Bool_Name || Bool_Phone || Bool_Dizhi || Bool_Xiangxi || (bool_moren != Bool_MoRen)) {
                    mBtnSave.setEnabled(true);
//                    mBtnSave.setBackgroundResource(R.drawable.bg_btn_fillet_violet);
                }else {
                    mBtnSave.setEnabled(false);
//                    mBtnSave.setBackgroundResource(R.drawable.bg_no_but_fillet);
                }
            }
        });
    }

    @OnClick({R.id.tv_shouhuoren_area, R.id.rl_set_default_address, R.id.btn_save})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_set_default_address:
                if (mCbSetDefault.isChecked()) {
                    mCbSetDefault.setChecked(false);
                    bool_moren = false;
                } else {
                    mCbSetDefault.setChecked(true);
                    bool_moren = true;
                }

                break;
            case R.id.tv_shouhuoren_area:
//                DialogUtlis.towBtnPC(mContext, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mShouhuorenProvince = DialogUtlis.myDialogBuilder.getProvince();
//                        mShuohuorenCity = DialogUtlis.myDialogBuilder.getCity();
//                        mTvShouhuorenArea.setText(DialogUtlis.myDialogBuilder.getProvince() + " " + DialogUtlis.myDialogBuilder.getCity());
//                        DialogUtlis.dismissDialog();
//                    }
//                }, mShouhuorenProvince, mShuohuorenCity);

                hideInput(mContext, mTvShouhuorenArea);//隐藏软键盘

                CityPicker cityPicker = new CityPicker.Builder(BianjiShouhuoDizhiActivity.this)
                        .textSize(20)
                        .title("收货地区")
                        .backgroundPop(0xa0000000)
                        .titleBackgroundColor("#ffffff")
                        .titleTextColor("#000000")
                        .confirTextColor("#000000")
                        .cancelTextColor("#000000")
                        .province(mShouhuorenProvince)
                        .city(mShuohuorenCity)
                        .district(mShohuorenDistrict)
                        .textColor(Color.parseColor("#000000"))
                        .provinceCyclic(false)
                        .cityCyclic(false)
                        .districtCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .onlyShowProvinceAndCity(false)
                        .build();
                cityPicker.show();

                //监听方法，获取选择结果
                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        //省份
                        mShouhuorenProvince = citySelected[0];
                        //城市
                        mShuohuorenCity = citySelected[1];
                        //区县（如果设定了两级联动，那么该项返回空）
                        mShohuorenDistrict = citySelected[2];
                        //邮编
                        String code = citySelected[3];

                        mTvShouhuorenArea.setText(mShouhuorenProvince + mShuohuorenCity + mShohuorenDistrict);
                    }

                    @Override
                    public void onCancel() {
//                        Toast.makeText(BianjiShouhuoDizhiActivity.this, "已取消", Toast.LENGTH_LONG).show();
                    }
                });

                break;
            case R.id.btn_save:
                if (verifyData()) {
                    if (mCbSetDefault.isChecked()) {
                        mAddressState = "是";
                    } else {
                        mAddressState = "否";
                    }
                    if (typeFlag.equals("编辑")) {
                        addressInfo = new ShouhuoAddress(addressInfo.getId(),
                                mShouhuorenName,
                                mShouhuorenPhoneNum,
                                mShouhuorenProvince,
                                mShuohuorenCity,
                                mShohuorenDistrict,
                                mShouhuorenAddressDetail,
                                mAddressState,
                                mShouhuorenArea);
                        requestHandleArrayList.add(requestAction.ShouhuoAddress(this, addressInfo, "edit"));
                    } else {//添加新的收货地址
                        addressInfo = new ShouhuoAddress("",
                                mShouhuorenName,
                                mShouhuorenPhoneNum,
                                mShouhuorenProvince,
                                mShuohuorenCity,
                                mShohuorenDistrict,
                                mShouhuorenAddressDetail,
                                mAddressState,
                                mShouhuorenArea);
                        requestHandleArrayList.add(requestAction.ShouhuoAddress(this, addressInfo, "add"));
                    }

                }
                break;
        }
    }

    /**
     * 校验数据是否符合规则
     *
     * @return
     */
    private boolean verifyData() {
        mShouhuorenName = mEtShouhuorenName.getText().toString();
        mShouhuorenPhoneNum = mEtShouhuorenPhoneNum.getText().toString();
        mShouhuorenArea = mTvShouhuorenArea.getText().toString();
        mShouhuorenAddressDetail = mEtAddressDetail.getText().toString();
        if (TextUtils.isEmpty(mShouhuorenName)) {
            MyToast.getInstance().showToast(mContext, "请输入收货人姓名");
            return false;
        } else if (TextUtils.isEmpty(mShouhuorenPhoneNum)) {
            MyToast.getInstance().showToast(mContext, "请输入手机号");
            return false;
        } else if (mShouhuorenPhoneNum.length() != 11) {
            MyToast.getInstance().showToast(mContext, "请输入有效手机号");
            return false;
        } else if (TextUtils.isEmpty(mShouhuorenArea)) {
            MyToast.getInstance().showToast(mContext, "请选择收货地区");
            return false;
        } else if (TextUtils.isEmpty(mShouhuorenAddressDetail)) {
            MyToast.getInstance().showToast(mContext, "请填写详细地址");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        switch (requestTag) {
            case RequestAction.TAG_SHOUHUOADDRESS://添加/修改
                if (typeFlag.equals("编辑")) {
                    MyToast.getInstance().showToast(mContext, "保存成功");
                } else {
                    MyToast.getInstance().showToast(mContext, "添加成功");
                    addressInfo.setId(response.getString("id"));
                }
                Intent intent = new Intent();
                intent.putExtra("addressInfo", addressInfo);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    /**
     * 强制隐藏输入法键盘
     */
    public void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
