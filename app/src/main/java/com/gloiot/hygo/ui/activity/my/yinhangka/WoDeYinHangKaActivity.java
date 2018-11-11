package com.gloiot.hygo.ui.activity.my.yinhangka;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.my.ziliao.shimingrenzheng.RenZheng01Activity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.widge.swipe.MySwipe;
import com.zyd.wlwsdk.widge.swipe.SwipeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class WoDeYinHangKaActivity extends BaseActivity implements BaseActivity.RequestErrorCallback {

    @Bind(R.id.lv_my_yinhangka)
    ListView lvMyyinhangka;


    private View view;
    private List<String[]> list = new ArrayList<String[]>();
    private CommonAdapter myBankCardAdapter;
    private int deletePosition = -1;//记录删除的位置，初始化为-1

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        getBank();
    }

    @Override
    public int initResource() {
        return R.layout.activity_my_wodeyinhangka;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "我的银行卡");
        view = View.inflate(mContext, R.layout.item_tianjiayinhangka, null);
        lvMyyinhangka.addFooterView(view);
        lvMyyinhangka.setDividerHeight(0);
        lvMyyinhangka.setAdapter(myBankCardAdapter);
        //添加银行卡
        view.findViewById(R.id.item_listcard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!preferences.getString(ConstantUtlis.SP_USERSMRZ, "").equals("已认证")) {
                    DialogUtlis.twoBtnNormal(getmDialog(), "提示", "请先进行实名认证,一经认证通过，以后只能添加该认证人银行卡", true, "取消", "去认证",
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
                    Intent intent = new Intent(mContext, BangDingYinHangKaActivity.class);
                    startActivity(intent);
                }

            }
        });

        setRequestErrorCallback(this);
    }

    public void getBank() {
        requestHandleArrayList.add(requestAction.HuoQvYinHangKa(WoDeYinHangKaActivity.this));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_HUOQVYINHANGKA:
                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("银行列表");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        String[] a = new String[5];
                        JSONObject jsonObject = (JSONObject) jsonArray.get(j);
                        a[0] = jsonObject.getString("银行名");
                        a[1] = jsonObject.getString("类别");
                        a[2] = jsonObject.getString("银行卡号");
                        a[3] = jsonObject.getString("id");
                        a[4] = jsonObject.getString("状态");
                        list.add(a);
                    }
                    adapter(false);
                }
                break;
            case RequestAction.TAG_JIEBANGYINHANGKA:
                MyToast.getInstance().showToast(mContext, response.getString("状态"));
                if (deletePosition != -1) {//为了保险，其实只要删除成功，这里肯定不等于-1
                    list.remove(deletePosition);
                    adapter(true);
                }
                deletePosition = -1;
                break;
            default:
                break;
        }
    }

    private void adapter(Boolean isChanged) {
        if (isChanged) {
            myBankCardAdapter.notifyDataSetChanged();
        } else {
            myBankCardAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_my_yinhangka, list) {
                @Override
                public void convert(ViewHolder holder, String[] strings) {

                    if (strings[0].contains("中国银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_china);
                    } else if (strings[0].contains("建设银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_jianshe);
                    } else if (strings[0].contains("交通银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_jiaotong);
                    } else if (strings[0].contains("工商银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_gongshang);
                    } else if (strings[0].contains("民生银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_mingsheng);
                    } else if (strings[0].contains("农业银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_nongye);
                    } else if (strings[0].contains("中信银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_zhongxin);
                    } else if (strings[0].contains("招商银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_zhaoshang);
                    } else if (strings[0].contains("华夏银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_huaxia);
                    } else if (strings[0].contains("光大银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_guangda);
                    } else if (strings[0].contains("浦发银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_pufa);
                    } else if (strings[0].contains("兴业银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_xinye);
                    } else if (strings[0].contains("北京银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_beijing);
                    } else if (strings[0].contains("广发银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_guangfa);
                    } else if (strings[0].contains("平安银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_pingan);
                    } else if (strings[0].contains("上海银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_shanghai);
                    } else if (strings[0].contains("邮储银行")) {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_youzheng);
                    } else {
                        holder.setBackgroundRes(R.id.img_bank, R.mipmap.ic_bank_moren);
                    }

                    holder.setText(R.id.tv_bank, strings[0]);
                    holder.setText(R.id.tv_type, strings[1]);
                    try {
                        holder.setText(R.id.tv_cardnum, strings[2].substring(strings[2].length() - 4, strings[2].length()));
                    } catch (StringIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    // 是否是无效卡
                    boolean isInvalid = "无效卡".equals(strings[4]);
                    switch (holder.getmPosition() % 3) {
                        case 0:
                            //不要用setBackgroundColor,会出错
                            holder.setBackgroundRes(R.id.rl_yinhangka,
                                    isInvalid ? R.mipmap.bg_bank_color_gray : R.mipmap.bg_bank_color_blue);
                            holder.setBackgroundRes(R.id.iv_invalid_card, isInvalid ? R.mipmap.bg_bank_invalid : 0);
                            break;
                        case 1:
                            holder.setBackgroundRes(R.id.rl_yinhangka,
                                    isInvalid ? R.mipmap.bg_bank_color_gray : R.mipmap.bg_bank_color_green);
                            holder.setBackgroundRes(R.id.iv_invalid_card, isInvalid ? R.mipmap.bg_bank_invalid : 0);
                            break;
                        case 2:
                            holder.setBackgroundRes(R.id.rl_yinhangka,
                                    isInvalid ? R.mipmap.bg_bank_color_gray : R.mipmap.bg_bank_color_yellow);
                            holder.setBackgroundRes(R.id.iv_invalid_card, isInvalid ? R.mipmap.bg_bank_invalid : 0);
                            break;
                        default:
                            break;
                    }

                    SwipeLayout sl = (SwipeLayout) holder.getConvertView();
                    sl.close(false, false);
                    sl.getFrontView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    int p = holder.getmPosition();//选中的位置
                    Button b2 = holder.getView(R.id.bt_bank_delete);
                    sl.setSwipeListener(MySwipe.mSwipeListener);
                    b2.setTag(p);
                    b2.setOnClickListener(onClick);
                }
            };
            lvMyyinhangka.setAdapter(myBankCardAdapter);
        }
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int p = (int) view.getTag();
            int id = view.getId();
            if (id == R.id.bt_bank_delete) {
                if (preferences.getString(ConstantUtlis.SP_MYPWD, "否").equals("否")) {
                    CommonUtlis.toSetPwd(mContext, getmDialog());
                } else {
                    DialogUtlis.customPwd(getmDialog(), null, false, new MDialogInterface.PasswordInter() {
                        @Override
                        public void callback(String data) {
                            deletePosition = p;
                            requestHandleArrayList.add(requestAction.JieBangYinHangKa(WoDeYinHangKaActivity.this, list.get(p)[3], data));
                        }
                    });
                }
            }
        }
    };

    @Override
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        switch (requestTag) {
            case RequestAction.TAG_JIEBANGYINHANGKA:
                if (response.getString("状态").equals("支付密码输入错误")) {
                    DialogUtlis.twoBtnNormal(getmDialog(), "提示", "支付密码输入错误", true, "取消", "重试",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                }
                            },
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                    DialogUtlis.customPwd(getmDialog(), "身份验证", false, new MDialogInterface.PasswordInter() {
                                        @Override
                                        public void callback(String data) {
                                            requestHandleArrayList.add(requestAction.JieBangYinHangKa(WoDeYinHangKaActivity.this, list.get(deletePosition)[3], data));
                                        }
                                    });
                                }
                            });
                } else {
                    MyToast.getInstance().showToast(mContext, response.getString("状态"));
                }
                break;
            default:
                MyToast.getInstance().showToast(mContext, response.getString("状态"));
                break;
        }
    }
}
