package com.gloiot.hygo.ui.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.App;
import com.gloiot.hygo.ui.BaseFragment;
import com.gloiot.hygo.ui.activity.login.WangjimimaActivity;
import com.gloiot.hygo.ui.activity.my.MyBillActivity;
import com.gloiot.hygo.ui.activity.my.kefu.KeHuZhongXinActivity;
import com.gloiot.hygo.ui.activity.my.qianbao.HongLiJiBenActivity;
import com.gloiot.hygo.ui.activity.my.qianbao.JiFenQianBaoActivity;
import com.gloiot.hygo.ui.activity.my.qianbao.JifenbaoZhanghuActivity;
import com.gloiot.hygo.ui.activity.my.qianbao.YoufeizhanghuActivity;
import com.gloiot.hygo.ui.activity.my.shezhi.SheZhiActivity;
import com.gloiot.hygo.ui.activity.my.shezhi.XiuGaiMiMaActivity;
import com.gloiot.hygo.ui.activity.my.shezhi.guanlianshouji.GenghuanShoujiHaoma01Activity;
import com.gloiot.hygo.ui.activity.my.shouhou.ShouHouActivity;
import com.gloiot.hygo.ui.activity.my.shoukuan.WoYaoShoukuanActivity;
import com.gloiot.hygo.ui.activity.my.yinhangka.WoDeYinHangKaActivity;
import com.gloiot.hygo.ui.activity.my.youhuiquan.WoDeYouHuiQuanActivity;
import com.gloiot.hygo.ui.activity.my.zhifubao.WoDeZhiFuBaoActivity;
import com.gloiot.hygo.ui.activity.my.ziliao.ZhiLiaoActivity;
import com.gloiot.hygo.ui.activity.shopping.dianpu.GuanZhuDianPuActivity;
import com.gloiot.hygo.ui.activity.shopping.dizhi.ShouhuoDizhiActivity;
import com.gloiot.hygo.ui.activity.shopping.wodedingdan.WodeDingdanActivity;
import com.gloiot.hygo.ui.activity.shopping.wodeshoucang.ShouCangActivity;
import com.gloiot.hygo.ui.activity.web.WebActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.PictureUtlis;
import com.zyd.wlwsdk.utils.SharedPreferencesUtils;
import com.zyd.wlwsdk.widge.LoadDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gloiot.hygo.R.id.ll_my_content;

public class MyFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.tv_my_nologin)
    TextView tvMyNologin;
    @Bind(R.id.rl_my_havelogin)
    RelativeLayout rlMyHavelogin;
    @Bind(R.id.im_my_head)
    ImageView imMyHead;
    @Bind(R.id.tv_my_nickname)
    TextView tvMyNickname;
    @Bind(R.id.tv_my_accounts)
    TextView tvMyAccounts;
    @Bind(R.id.rl_my_money)
    RelativeLayout rlMyMoney;
    @Bind(R.id.rl_my_bankcard)
    RelativeLayout rlMyBankcard;
    @Bind(R.id.rl_my_zhiliao)
    RelativeLayout rlMyZhiliao;
    @Bind(R.id.tv_my_shiming)
    TextView tvMyShiming;
    @Bind(ll_my_content)
    LinearLayout llMyContent;
    @Bind(R.id.rl_my_shoukuan)
    RelativeLayout rlMyShoukuan;
    @Bind(R.id.iv_my_qrcode)
    ImageView iv_my_qrcode;


    //我的订单数量
    @Bind(R.id.bt_dingdan_daifukuan)
    Button bt_dingdan_daifukuan;
    @Bind(R.id.bt_dingdan_daifahuo)
    Button bt_dingdan_daifahuo;
    @Bind(R.id.bt_dingdan_daishouhuo)
    Button bt_dingdan_daishouhuo;
    @Bind(R.id.bt_dingdan_daipingjia)
    Button bt_dingdan_daipingjia;

    //我的积分
    @Bind(R.id.tv_honglizhanghu)
    TextView tv_honglizhanghu;
    @Bind(R.id.tv_jifenzhanghu)
    TextView tv_jifenzhanghu;
    @Bind(R.id.tv_jifenbaozhanghu)
    TextView tv_jifenbaozhanghu;
    @Bind(R.id.tv_youfeizhanghu)
    TextView tv_youfeizhanghu;
    @Bind(R.id.rl_my_guanzhudianpu)
    RelativeLayout rlMyGuanzhudianpu;

    //RecyclerView集合
    @Bind(R.id.rv_my_fragment_list)
    GridView rvMyFragmentList;
    @Bind(R.id.my_scrollview)
    ScrollView my_scrollview;

    //收藏数量
    @Bind(R.id.tv_shoucang)
    TextView tv_shoucang;
    //关注店铺数量
    @Bind(R.id.tv_guanzhudianpu)
    TextView tv_guanzhudianpu;
    //优惠券数量
    @Bind(R.id.tv_coupon)
    TextView tv_coupon;
    //银行卡数量
    @Bind(R.id.tv_bankcard)
    TextView tv_bankcard;

    private CommonAdapter commonAdapter;

    private MyFragmentBean[] myFragmentBeans1 = {
            new MyFragmentBean(R.mipmap.ic_wodekefu, "我的客服"),
            new MyFragmentBean(R.mipmap.ic_my_bill, "我的账单"),
            new MyFragmentBean(R.mipmap.ic_shouhuodizhi, "收货地址"),
            new MyFragmentBean(R.mipmap.ic_mimaguanli, "密码管理"),
            new MyFragmentBean(R.mipmap.ic_guanlianzhifubao, "关联支付宝"),
            new MyFragmentBean(R.mipmap.ic_guanlianshoujihao, "关联手机号"),
            new MyFragmentBean(R.mipmap.ic_guanlianweixin, "关联微信"),
            new MyFragmentBean(R.mipmap.ic_jiaocheng, "操作指导"),
//            new MyFragmentBean(R.mipmap.ic_shezhizhifumima, "设置支付密码"),
            new MyFragmentBean(R.mipmap.ic_wodeshezhi, "设置")
    };
    private MyFragmentBean[] myFragmentBeans2 = {
            new MyFragmentBean(R.mipmap.ic_my_bill, "我的账单"),
            new MyFragmentBean(R.mipmap.ic_shouhuodizhi, "收货地址"),
            new MyFragmentBean(R.mipmap.ic_mimaguanli, "密码管理"),
            new MyFragmentBean(R.mipmap.ic_guanlianzhifubao, "关联支付宝"),
            new MyFragmentBean(R.mipmap.ic_guanlianshoujihao, "关联手机号"),
            new MyFragmentBean(R.mipmap.ic_guanlianweixin, "关联微信"),
            new MyFragmentBean(R.mipmap.ic_jiaocheng, "操作指导"),
//            new MyFragmentBean(R.mipmap.ic_shezhizhifumima, "设置支付密码"),
            new MyFragmentBean(R.mipmap.ic_wodeshezhi, "设置")
    };

    private ArrayList<MyFragmentBean> listDatas1 = new ArrayList<>(Arrays.asList(myFragmentBeans1));
    private ArrayList<MyFragmentBean> listDatas2 = new ArrayList<>(Arrays.asList(myFragmentBeans2));
    private List<MyFragmentBean> listDatas = new ArrayList<>(listDatas1);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, view);

        initLayout();

        return view;
    }

    private void initLayout() {
        rvMyFragmentList.setFocusable(false);
        rvMyFragmentList.setNumColumns(4);
        commonAdapter = new CommonAdapter<MyFragmentBean>(mContext, R.layout.item_myfragment_bean, listDatas) {
            @Override
            public void convert(ViewHolder holder, MyFragmentBean myFragmentBean) {
                holder.setText(R.id.tv_item_my_fragment_title, myFragmentBean.title);
                holder.getView(R.id.iv_item_my_fragment_img).setBackgroundResource(myFragmentBean.ImageResources);
            }
        };
        rvMyFragmentList.setAdapter(commonAdapter);
        rvMyFragmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == -1) return;
                switch (listDatas.get(position).title) {
                    case "我的客服":
                        if (check_login_tiaozhuang()) {
                            startActivity(new Intent(getActivity(), KeHuZhongXinActivity.class));
                        }
                        break;
                    case "我的账单":
                        if (check_login_tiaozhuang()) {
                            startActivity(new Intent(getActivity(), MyBillActivity.class));
                        }
                        break;
                    case "收货地址":
                        if (check_login_tiaozhuang())
                            startActivity(new Intent(getActivity(), ShouhuoDizhiActivity.class));//收货地址
                        break;
                    case "密码管理":
                        if (check_login_tiaozhuang()) {
                            startActivityForResult(new Intent(getActivity(), XiuGaiMiMaActivity.class), 100);
                        }
                        break;
                    case "关联支付宝":
                        if (check_login_tiaozhuang()) {
                            startActivity(new Intent(getActivity(), WoDeZhiFuBaoActivity.class));
                        }
                        break;
                    case "关联手机号":
                        if (check_login_tiaozhuang()) {
                            startActivity(new Intent(getActivity(), GenghuanShoujiHaoma01Activity.class));
                        }
                        break;
                    case "关联微信":
                        Log.e("wxwxwx", preferences.getString(ConstantUtlis.SP_SHIFOUBANGDINGWX, ""));
                        if ("否".equals(preferences.getString(ConstantUtlis.SP_SHIFOUBANGDINGWX, ""))) {
                            if (CommonUtlis.isWXAppInstalledAndSupported(mContext)) {
                                getWxInfo();
                            } else {
                                LoadDialog.dismiss(mContext);
                            }
                        } else {
                            showDialog();
                        }
                        break;
                    case "操作指导":
                        //客户版视频宣传网
                        Intent intent1 = new Intent(getActivity(), WebActivity.class);
                        intent1.putExtra("url", ConstantUtlis.XUANCHUAN_SHIPIN);
                        startActivity(intent1);
                        break;
                    case "设置支付密码":
                        if (check_login_tiaozhuang()) {
                            Intent intent = new Intent(mContext, WangjimimaActivity.class);
                            intent.putExtra("forgetpwd", "设置支付密码");
                            startActivity(intent);
                        }
                        break;
                    case "设置":
                        if (check_login_tiaozhuang()) {
                            startActivity(new Intent(getActivity(), SheZhiActivity.class));
                        }
                        break;
                }

            }
        });
    }

    private void init() {
        if ("成功".equals(preferences.getString(ConstantUtlis.SP_LOGINTYPE, ""))) {
            // 头像
            if (!"".equals(preferences.getString(ConstantUtlis.SP_USERIMG, ""))) {
                PictureUtlis.loadCircularImageViewHolder(mContext, preferences.getString(ConstantUtlis.SP_USERIMG, ""), R.mipmap.ic_portrait_default, imMyHead);
            } else {
                //给图片url设为空，则加载默认头像
                PictureUtlis.loadCircularImageViewHolder(mContext, "", R.mipmap.ic_portrait_default, imMyHead);
            }
            rlMyHavelogin.setVisibility(View.VISIBLE);
            tvMyNologin.setVisibility(View.GONE);
            // 昵称
            tvMyNickname.setText(preferences.getString(ConstantUtlis.SP_NICKNAME, ""));
            // 账号
            String my_phone = preferences.getString(ConstantUtlis.SP_USERPHONE, "");
            try {
                if ("是".equals(preferences.getString(ConstantUtlis.SP_HAIWAIZHUCE, ""))) {
                    if (my_phone.length() > 4) {
                        my_phone = "****" + my_phone.substring(my_phone.length() - 4, my_phone.length());
                    }
                } else {
                    my_phone = my_phone.substring(0, 3) + "****" + my_phone.substring(my_phone.length() - 4, my_phone.length());
                }
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            tvMyAccounts.setText(my_phone);
            // 实名认证
            if (preferences.getString(ConstantUtlis.SP_USERSMRZ, "").equals("未认证")) {
                tvMyShiming.setText("未认证");
            } else if (preferences.getString(ConstantUtlis.SP_USERSMRZ, "").equals("已认证")) {
                tvMyShiming.setText("已认证");
            }


            requestHandleArrayList.add(requestAction.PersonalCenterMy(MyFragment.this));  //账户积分

        } else {
            // 头像 给图片url设为空，则加载默认头像
            PictureUtlis.loadCircularImageViewHolder(mContext, "", R.mipmap.ic_portrait_default, imMyHead);
            tvMyNologin.setVisibility(View.VISIBLE);
            rlMyHavelogin.setVisibility(View.GONE);

            tv_honglizhanghu.setText("0.00");
            tv_jifenzhanghu.setText("0.00");
            tv_jifenbaozhanghu.setText("0.00");
            tv_youfeizhanghu.setText("0.00");

            bt_dingdan_daifukuan.setVisibility(View.GONE);
            bt_dingdan_daifahuo.setVisibility(View.GONE);
            bt_dingdan_daishouhuo.setVisibility(View.GONE);
            bt_dingdan_daipingjia.setVisibility(View.GONE);

        }
        if ("是".equals(SharedPreferencesUtils.getString(getActivity(), ConstantUtlis.SP_SUPERMERCHANT, ""))) { //是否是超级商家
            rlMyShoukuan.setVisibility(View.VISIBLE);
        } else {
            rlMyShoukuan.setVisibility(View.GONE);
        }
        if ("是".equals(preferences.getString(ConstantUtlis.SP_KEFUCENTER, ""))) { //是否显示客服中心
            listDatas.clear();
            listDatas.addAll(listDatas1);
        } else {
            listDatas.clear();
            listDatas.addAll(listDatas2);
        }

        if ("否".equals(preferences.getString(ConstantUtlis.SP_MYPWD, ""))) {
            listDatas.add(6, new MyFragmentBean(R.mipmap.ic_shezhizhifumima, "设置支付密码"));
        }
        commonAdapter.notifyDataSetChanged();
        CommonUtlis.reMesureGridViewHeight(rvMyFragmentList);

        // 关联微信
        String wxCode = preferences.getString(ConstantUtlis.SP_WXCODE, null);
        if (!TextUtils.isEmpty(wxCode)) {
            requestHandleArrayList.add(requestAction.WXBinding(this, wxCode));
            editor.putString(ConstantUtlis.SP_WXCODE, null);
            editor.commit();
        } else {
            LoadDialog.dismiss(mContext);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        if (check_login())
            requestHandleArrayList.add(requestAction.Shop_order_prompt(MyFragment.this));
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelRequestHandle();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.rl_my_zhiliao, R.id.iv_my_qrcode, R.id.rl_my_money, R.id.rl_my_bankcard,
            R.id.ll_my_set, R.id.ll_my_kefu, R.id.tv_wodedingdan, R.id.rl_shouhou,
            R.id.rl_daifukuan, R.id.rl_daifahuo, R.id.rl_daishouhuo, R.id.rl_daipingjia,
            R.id.rl_my_shoucang, R.id.ll_my_shouhuo, R.id.rl_my_shoukuan, R.id.rl_my_coupon,
            R.id.rl_honglizhanghu, R.id.rl_jifenzhanghu, R.id.rl_jifenbaozhanghu, R.id.rl_youfeizhanghu, R.id.ll_my_syjc, R.id.ll_my_bill, R.id.rl_my_guanzhudianpu
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_my_zhiliao:
                if (check_login_tiaozhuang()) {
                    startActivity(new Intent(getActivity(), ZhiLiaoActivity.class));
                }
                break;
            case R.id.iv_my_qrcode://二维码
                if (check_login_tiaozhuang()) {
                    if (preferences.getString(ConstantUtlis.SP_SHIFOUBANGDINGWX, "").equals("否")) {
                        DialogUtlis.twoBtnNormal(getmDialog(), "提示", "未绑定微信,请先绑定微信", true, "取消", "去绑定",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dismissDialog();
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dismissDialog();
                                        getWxInfo();
                                    }
                                });
                    } else {
                        requestHandleArrayList.add(requestAction.GetMyQRcode(MyFragment.this));
                    }
                }
                break;
            case R.id.rl_my_money:
                if (check_login_tiaozhuang()) {
                    startActivity(new Intent(getActivity(), JiFenQianBaoActivity.class));
                }
                break;
            case R.id.rl_my_bankcard:
                if (check_login_tiaozhuang()) {
                    startActivity(new Intent(getActivity(), WoDeYinHangKaActivity.class));
                }
                break;
            case R.id.rl_my_coupon:
                if (check_login_tiaozhuang()) {
                    startActivityForResult(new Intent(getActivity(), WoDeYouHuiQuanActivity.class), 666);
                }
                break;
            case R.id.ll_my_set:
                if (check_login_tiaozhuang()) {
                    startActivity(new Intent(getActivity(), SheZhiActivity.class));
                }
                break;
            case R.id.ll_my_kefu: //客服
                if (check_login_tiaozhuang()) {
                    startActivity(new Intent(getActivity(), KeHuZhongXinActivity.class));
                }
                break;
            case R.id.rl_my_shoukuan:
                if (check_login_tiaozhuang()) {
                    requestHandleArrayList.add(requestAction.p_return_address(MyFragment.this));
                }
                break;
            case R.id.ll_my_syjc://客户版视频宣传网
                Intent intent1 = new Intent(getActivity(), WebActivity.class);
                intent1.putExtra("url", ConstantUtlis.XUANCHUAN_SHIPIN);
                startActivity(intent1);
                break;
            /**
             * 我的订单页面跳转  开始
             */

            case R.id.tv_wodedingdan://我的订单
                if (check_login_tiaozhuang())
                    startWodedingdanActivity("全部");
                break;
            case R.id.rl_daifukuan://待付款
                if (check_login_tiaozhuang())
                    startWodedingdanActivity("待付款");
                break;
            case R.id.rl_daifahuo://待发货
                if (check_login_tiaozhuang())
                    startWodedingdanActivity("待发货");
                break;
            case R.id.rl_daishouhuo://待收货
                if (check_login_tiaozhuang())
                    startWodedingdanActivity("待收货");
                break;
            case R.id.rl_daipingjia://待评价
                if (check_login_tiaozhuang())
                    startWodedingdanActivity("待评价");
                break;

            case R.id.rl_shouhou: //跳转至售后
                if (check_login_tiaozhuang())
                    startActivity(new Intent(getActivity(), ShouHouActivity.class));
                break;
            /**
             * 我的订单页面跳转  结束
             */
            case R.id.rl_my_shoucang:
                if (check_login_tiaozhuang())
                    startActivity(new Intent(getActivity(), ShouCangActivity.class));   //我的收藏
                break;
            case R.id.ll_my_shouhuo:
                if (check_login_tiaozhuang())
                    startActivity(new Intent(getActivity(), ShouhuoDizhiActivity.class));//收货地址
                break;

            //  账户显示
            case R.id.rl_honglizhanghu://红利账户
                if (check_login_tiaozhuang()) {
                    Intent intent = new Intent(mContext, HongLiJiBenActivity.class);
                    intent.putExtra("type", "红利积分");
                    startActivity(intent);
                }
                break;
            //积分账户
            case R.id.rl_jifenzhanghu:
                if (check_login_tiaozhuang()) {
                    Intent intent = new Intent(mContext, HongLiJiBenActivity.class);
                    intent.putExtra("type", "基本积分");
                    startActivity(intent);
                }
                break;

            case R.id.rl_jifenbaozhanghu://积分宝账户
                if (check_login_tiaozhuang()) {
                    startActivity(new Intent(getActivity(), JifenbaoZhanghuActivity.class));
                }
                break;

            case R.id.rl_youfeizhanghu://邮费账户
                if (check_login_tiaozhuang()) {
                    startActivity(new Intent(getActivity(), YoufeizhanghuActivity.class));
                }
                break;

            case R.id.ll_my_bill://我的账单
                if (check_login_tiaozhuang()) {
                    startActivity(new Intent(getActivity(), MyBillActivity.class));
                }
                break;
            case R.id.rl_my_guanzhudianpu:
                if (check_login_tiaozhuang()) {
                    startActivity(new Intent(getActivity(), GuanZhuDianPuActivity.class));
                }
                break;
            default:

                break;
        }
    }

    private void startWodedingdanActivity(String str) {
        Intent intent = new Intent(getActivity(), WodeDingdanActivity.class);
        intent.putExtra("显示页面", str);
        startActivityForResult(intent, 666);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_GETMYQRCODE:
                final String myQRcode = response.getString("二维码地址");
                if (TextUtils.isEmpty(myQRcode)) {
                    MyToast.getInstance().showToast(mContext, "返回二维码信息错误");
                    return;
                }
                DialogUtlis.customQRCode(getmDialog(), new MDialogInterface.QrCodeInter() {
                    @Override
                    public void callback(TextView tvName, TextView tvNum, ImageView ivPic, ImageView ivQRCode, ImageView ivSex) {
                        // 头像
                        PictureUtlis.loadCircularImageViewHolder(mContext, preferences.getString(ConstantUtlis.SP_USERIMG, ""), R.mipmap.ic_head, ivPic);

                        // 昵称
                        tvName.setText(preferences.getString(ConstantUtlis.SP_NICKNAME, ""));
                        // 账号
                        String my_phone = preferences.getString(ConstantUtlis.SP_USERPHONE, "");
                        my_phone = my_phone.substring(0, 3) + "****" + my_phone.substring(my_phone.length() - 4, my_phone.length());
                        tvNum.setText(my_phone);
                        //性别
                        String gender = preferences.getString(ConstantUtlis.SP_GENDER, "");
                        if (gender.equals("男")) {
                            PictureUtlis.loadImageViewHolder(mContext, "", R.mipmap.male_ic, ivSex);
                        } else if (gender.equals("女")) {
                            PictureUtlis.loadImageViewHolder(mContext, "", R.mipmap.female_ic, ivSex);
                        } else {

                        }
                        PictureUtlis.loadImageViewHolder(mContext, myQRcode, ivQRCode);
                    }
                });
                break;

            case RequestAction.TAG_SHOP_ORDER_PROMPT:
                bt_dingdan_daifukuan.setVisibility(View.GONE);
                bt_dingdan_daifahuo.setVisibility(View.GONE);
                bt_dingdan_daishouhuo.setVisibility(View.GONE);
                bt_dingdan_daipingjia.setVisibility(View.GONE);

                bt_dingdan_daifukuan.setText(response.getInt("待付款数量") + "");
                bt_dingdan_daifahuo.setText(response.getInt("待发货数量") + "");
                bt_dingdan_daishouhuo.setText(response.getInt("待收货数量") + "");
                bt_dingdan_daipingjia.setText(response.getInt("待评价数量") + "");

                //收藏数量
                tv_shoucang.setText(JSONUtlis.getInt(response, "我的收藏", 0) + "");
                //关注店铺数量
                tv_guanzhudianpu.setText(JSONUtlis.getInt(response, "关注店铺", 0) + "");
                //优惠券数量
                tv_coupon.setText(JSONUtlis.getInt(response, "优惠券", 0) + "");
                //银行卡数量
                tv_bankcard.setText(JSONUtlis.getInt(response, "银行卡", 0) + "");

                if (response.getInt("待付款数量") != 0)
                    bt_dingdan_daifukuan.setVisibility(View.VISIBLE);
                if (response.getInt("待发货数量") != 0)
                    bt_dingdan_daifahuo.setVisibility(View.VISIBLE);
                if (response.getInt("待收货数量") != 0)
                    bt_dingdan_daishouhuo.setVisibility(View.VISIBLE);
                if (response.getInt("待评价数量") != 0)
                    bt_dingdan_daipingjia.setVisibility(View.VISIBLE);

                break;

            case RequestAction.TAG_RETURNADDRESS:
                if (response.getString("地址").equals("")) {
                    Intent intent = new Intent(getActivity(), WoYaoShoukuanActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", mUrl(response.getString("地址")));
                    startActivity(intent);
                }
                break;

            case RequestAction.TAG_PERSONALCENTER:

                tv_honglizhanghu.setText(response.getString("红利账户"));
                tv_jifenzhanghu.setText(response.getString("积分账户"));
                tv_jifenbaozhanghu.setText(response.getString("积分宝账户"));
                tv_youfeizhanghu.setText(response.getString("油费账户"));

                break;

            case RequestAction.TAG_WXBINDING:
                editor.putString(ConstantUtlis.SP_SHIFOUBANGDINGWX, "是");
                editor.commit();
                MyToast.getInstance().showToast(mContext, "关联成功");
                break;
            case RequestAction.TAG_WXUNBIND:
                editor.putString(ConstantUtlis.SP_SHIFOUBANGDINGWX, "否");
                editor.commit();
                MyToast.getInstance().showToast(mContext, "解除关联成功");
                break;
            default:

                break;
        }
    }

    private String mUrl(String url) {
        // 判断地址是否有问号
        if (url.contains("?")) {
            return url + "&onlyID=" + preferences.getString(ConstantUtlis.SP_ONLYID, "") + "&version=" + ConstantUtlis.VERSION;
        } else {
            return url + "?onlyID=" + preferences.getString(ConstantUtlis.SP_ONLYID, "") + "&version=" + ConstantUtlis.VERSION;
        }
    }

    public class MyFragmentBean {
        public MyFragmentBean(int imageResources, String title) {
            ImageResources = imageResources;
            this.title = title;
        }

        private int ImageResources;
        private String title;
    }

    /**
     * 获取微信信息
     */
    public void getWxInfo() {
        LoadDialog.show(mContext);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_authorization";
        App.getInstance().wxapi.sendReq(req);
    }

    public void showDialog() {
        DialogUtlis.twoBtnNormal(getmDialog(), "提示", "您是否要解除关联？\n解除后无法使用该微信登录环游购", true, "取消", "解除关联", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                requestHandleArrayList.add(requestAction.WXUnbind(MyFragment.this));
            }
        });
    }
}
