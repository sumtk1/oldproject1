package com.gloiot.hygo.ui.activity.shopping.wodedingdan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseFragment;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.ui.activity.login.WangjimimaActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.DizhiBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.PingjiaFaBiaoBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.TreeNodeBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.WoDeDingDan_ShangPinInfo;
import com.gloiot.hygo.ui.activity.web.WebToPayManager;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.widge.SwitchView;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;
import com.zyd.wlwsdk.widge.pulltorefresh.PullableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DingdanFragment extends BaseFragment implements PullToRefreshLayout.OnRefreshListener, View.OnClickListener {
    /**
     * 没有订单时候显示的默认布局
     */
    @Bind(R.id.iv_wodedingdan_meiyoudingdan)
    ImageView iv_wodedingdan_meiyoudingdan;
    @Bind(R.id.tv_wodedingdan_meiyoudingdan)
    TextView tv_wodedingdan_meiyoudingdan;
    @Bind(R.id.tv_wodedingdan_quguangguang)
    TextView tv_wodedingdan_quguangguang;
    @Bind(R.id.rl_wodedingdan_quguangguang)
    RelativeLayout rl_wodedingdan_quguangguang;

    @Bind(R.id.pull_wode_dingdan)
    PullableListView pull_wode_dingdan;
    @Bind(R.id.ptrl_wode_dingdan)
    PullToRefreshLayout ptrl_wode_dingdan;

    private String currentFragment;

    private List<TreeNodeBean> listData = new ArrayList<>();

    private List<TreeNodeBean> listData1 = new ArrayList<>();
    private List<TreeNodeBean> listData2 = new ArrayList<>();
    private List<TreeNodeBean> listData3 = new ArrayList<>();
    private List<TreeNodeBean> listData4 = new ArrayList<>();
    private List<TreeNodeBean> listData5 = new ArrayList<>();

    private Boolean refreshOrLoad1, refreshOrLoad2, refreshOrLoad3, refreshOrLoad4, refreshOrLoad5;  //true为刷新或第一次加载状态   false加载更多状态

    private int Loadlength1, Loadlength2, Loadlength3, Loadlength4, Loadlength5;

    private int XiayiyeYeshu1 = 0, XiayiyeYeshu2 = 0, XiayiyeYeshu3 = 0, XiayiyeYeshu4 = 0, XiayiyeYeshu5 = 0;

    private DecimalFormat df;

    private WoDeDingDan_DingDanAdapter woDeDingDan_dingDanAdapter;
    private ViewHolder viewHolder;

    private boolean flag = false;

    //线下交易说明
    private String Str_XianXiaJiaoYi_ShuoMing = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dingdan, container, false);
        ButterKnife.bind(this, view);

        initComponent();
        init();

        return view;
    }

    public void init() {
        refreshOrLoad1 = true;
        refreshOrLoad2 = true;
        refreshOrLoad3 = true;
        refreshOrLoad4 = true;
        refreshOrLoad5 = true;
        df = new DecimalFormat("######0.00");
        currentFragment = getArguments().getString("text");

        ptrl_wode_dingdan.setOnRefreshListener(this);

        shuaxin();
        flag = true;
    }


    private void changeViewVis(boolean flag) {
        if (flag) {
            iv_wodedingdan_meiyoudingdan.setVisibility(View.VISIBLE);
            tv_wodedingdan_meiyoudingdan.setVisibility(View.VISIBLE);
            rl_wodedingdan_quguangguang.setVisibility(View.VISIBLE);
        } else {
            iv_wodedingdan_meiyoudingdan.setVisibility(View.GONE);
            tv_wodedingdan_meiyoudingdan.setVisibility(View.GONE);
            rl_wodedingdan_quguangguang.setVisibility(View.GONE);
        }
    }

    public void initComponent() {
        woDeDingDan_dingDanAdapter = new WoDeDingDan_DingDanAdapter(getContext()) {

            public void changeData(List<TreeNodeBean> list) {
                listData = list;
            }

            @Override
            public int getCount() {
                return listData.size();
            }

            @Override
            public Object getItem(int position) {
                return listData.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup viewGroup) {
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_wodedingdan, null);
                    viewHolder.dianpuming = (TextView) convertView.findViewById(R.id.tv_item_dingdan_xingming);
//                    viewHolder.dingdanbianhao = (TextView) convertView.findViewById(R.id.tv_item_dingdan_bianhao);
                    viewHolder.zhuangtai = (TextView) convertView.findViewById(R.id.tv_item_dingdan_zhuangtai);
                    viewHolder.zongjia = (TextView) convertView.findViewById(R.id.iv_item_dingdan_zongjia);
                    viewHolder.yunfei = (TextView) convertView.findViewById(R.id.iv_item_dingdan_yunfei);

                    viewHolder.jianshu = (TextView) convertView.findViewById(R.id.iv_item_dingdan_jianshu);

                    viewHolder.button1 = (Button) convertView.findViewById(R.id.iv_item_dingdan_button1);
                    viewHolder.button2 = (Button) convertView.findViewById(R.id.iv_item_dingdan_button2);

                    viewHolder.item_rl_list_wodedingdan_button = (RelativeLayout) convertView.findViewById(R.id.item_rl_list_wodedingdan_button);
                    viewHolder.ll_item_dingdan = (LinearLayout) convertView.findViewById(R.id.ll_item_dingdan);

                    /**
                     * 这两个是线下交易对应的布局控件声明，保证下次要改的，接盘的别谢我
                     */
                    viewHolder.rl_item_dingdan_xianxiajiaoyi = (RelativeLayout) convertView.findViewById(R.id.rl_item_dingdan_xianxiajiaoyi);
                    viewHolder.sv_item_dingdan_xianxiajiaoyi = (SwitchView) convertView.findViewById(R.id.sv_item_dingdan_xianxiajiaoyi);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                ListView listView = (ListView) convertView.findViewById(R.id.lv_fragment_dingdan_shangpin);

                viewHolder.dianpuming.setText(listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dianpuming());
//                viewHolder.dingdanbianhao.setText(listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanid());
                viewHolder.zhuangtai.setText(listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanzhuangtai());

                viewHolder.zongjia.setText(listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanzongjia());
                viewHolder.yunfei.setText("(含运费" + listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanyunfei() + ")");
                viewHolder.jianshu.setText("件数：" + listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_jianshu());
                viewHolder.comment = listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_comment();


                switch (viewHolder.zhuangtai.getText().toString()) {
                    case "等待买家付款":
                        viewHolder.item_rl_list_wodedingdan_button.setVisibility(View.VISIBLE);
                        viewHolder.button1.setVisibility(View.VISIBLE);
                        viewHolder.button2.setVisibility(View.VISIBLE);
                        viewHolder.button1.setText("取消订单");
                        viewHolder.button2.setText("付款");

                        //线下交易
                        viewHolder.rl_item_dingdan_xianxiajiaoyi.setVisibility(View.GONE);
                        break;
                    case "交易完成":
                        if ("0".equals(viewHolder.comment)) {
                            viewHolder.item_rl_list_wodedingdan_button.setVisibility(View.VISIBLE);
                            viewHolder.button1.setVisibility(View.GONE);
                            viewHolder.button2.setVisibility(View.VISIBLE);
                            viewHolder.button2.setText("评价");
                        } else if ("2".equals(viewHolder.comment)) {
                            viewHolder.item_rl_list_wodedingdan_button.setVisibility(View.VISIBLE);
                            viewHolder.button1.setVisibility(View.GONE);
                            viewHolder.button2.setVisibility(View.VISIBLE);
                            viewHolder.button2.setText("追加评价");
                        } else
                            viewHolder.item_rl_list_wodedingdan_button.setVisibility(View.GONE);

                        //线下交易
                        viewHolder.rl_item_dingdan_xianxiajiaoyi.setVisibility(View.GONE);
                        break;
                    case "":
                        viewHolder.item_rl_list_wodedingdan_button.setVisibility(View.GONE);

                        //线下交易
                        viewHolder.rl_item_dingdan_xianxiajiaoyi.setVisibility(View.GONE);
                        break;
                    case "卖家已发货":
//                        if ("全部".equals(currentFragment)) {
//                            viewHolder.item_rl_list_wodedingdan_button.setVisibility(View.GONE);
//                            viewHolder.button1.setVisibility(View.GONE);
//                            viewHolder.button2.setVisibility(View.GONE);
//                        } else{
                        viewHolder.item_rl_list_wodedingdan_button.setVisibility(View.VISIBLE);
                        viewHolder.button1.setVisibility(View.GONE);
                        viewHolder.button2.setVisibility(View.VISIBLE);
                        viewHolder.button2.setText("确认收货");

                        //线下交易
                        viewHolder.rl_item_dingdan_xianxiajiaoyi.setVisibility(View.GONE);
//                        }

                        break;
                    case "作废":
                        viewHolder.item_rl_list_wodedingdan_button.setVisibility(View.GONE);

                        //线下交易
                        viewHolder.rl_item_dingdan_xianxiajiaoyi.setVisibility(View.GONE);
                        break;
                    case "售后处理中":
                        viewHolder.item_rl_list_wodedingdan_button.setVisibility(View.GONE);

                        //线下交易
                        viewHolder.rl_item_dingdan_xianxiajiaoyi.setVisibility(View.GONE);
                        break;
                    case "买家已付款":
                        viewHolder.item_rl_list_wodedingdan_button.setVisibility(View.GONE);

                        //线下交易
                        viewHolder.rl_item_dingdan_xianxiajiaoyi.setVisibility(View.VISIBLE);
                        if ("否".equals(listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_xianxiajiaoyi())) {
                            viewHolder.sv_item_dingdan_xianxiajiaoyi.setOpened(false);
                        } else {
                            viewHolder.sv_item_dingdan_xianxiajiaoyi.setOpened(true);
                        }
                        viewHolder.sv_item_dingdan_xianxiajiaoyi.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
                            @Override
                            public void toggleToOn(SwitchView view) {
                                if ("否".equals(listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_xianxiajiaoyi())) {

                                    //线下交易说明请求,如果说明为空先请求说明
                                    if (Str_XianXiaJiaoYi_ShuoMing.length() == 0) {
                                        requestHandleArrayList.add(requestAction.shop_userExplain(DingdanFragment.this));
                                        MyToast.getInstance().showToast(mContext, "正在获取线下交易说明，请稍后再试。");
                                        return;
                                    }

                                    DialogUtlis.twoBtnNormal(getmDialog(), "通知", Str_XianXiaJiaoYi_ShuoMing.replace("/n", "\n"), Gravity.LEFT,
                                            false, "取消", "确定",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dismissDialog();
                                                    viewHolder.sv_item_dingdan_xianxiajiaoyi.setOpened(false);
                                                }
                                            }, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    requestHandleArrayList.add(requestAction.shop_lineCode(DingdanFragment.this,
                                                            listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanid()));
                                                    dismissDialog();
                                                }
                                            });
                                }
                            }

                            @Override
                            public void toggleToOff(SwitchView view) {

                            }
                        });
                        break;
                    default:
                        viewHolder.item_rl_list_wodedingdan_button.setVisibility(View.GONE);

                        //线下交易
                        viewHolder.rl_item_dingdan_xianxiajiaoyi.setVisibility(View.GONE);
                        break;
                }

                viewHolder.button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanzhuangtai()) {
                            case "等待买家付款":
                                Log.e("id", listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanid() + "-取消订单");
                                DialogUtlis.twoBtnNormal(getmDialog(), "提示", "是否取消订单？", true, "否", "是",
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dismissDialog();
                                            }
                                        }, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dismissDialog();
                                                DingdanCaozuo(listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanid(), 1);
                                            }
                                        });
                                break;
                        }
                    }
                });
                viewHolder.button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanzhuangtai()) {
                            case "":
                            case "卖家已发货":
                                if (listData.get(position).getShangpinArray().length > 1) {
                                    DialogUtlis.twoBtnNormal(getmDialog(), "提示", "是否已收到订单中所有商品（" + listData.get(position).getShangpinArray().length + "件）？", true, "否", "是",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dismissDialog();
                                                }
                                            }, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dismissDialog();
                                                    DingdanCaozuo(listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanid(), 2);
                                                }
                                            });
                                } else {
                                    DialogUtlis.twoBtnNormal(getmDialog(), "提示", "是否确认收货？", true, "否", "是",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dismissDialog();
                                                }
                                            }, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dismissDialog();
                                                    DingdanCaozuo(listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanid(), 2);
                                                }
                                            });
                                }

                                break;
                            case "等待买家付款":
                                fukuan(listData.get(position));
                                break;
                            case "交易完成":
//                                if (listData.get(position).getShangpinArray().length == 1) {
                                if ("0".equals(listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_comment())) {
                                    Pingjia("评价", listData.get(position));
                                } else if ("2".equals(listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_comment())) {
                                    Pingjia("追加评价", listData.get(position));
                                }
//                                } else {

//                                    JSONArray idArray = new JSONArray();
//                                    JSONObject idObj;
//                                    for (int i = 0; i < listData.get(position).getShangpinArray().length; i++) {
//                                        idObj = new JSONObject();
//                                        try {
//                                            idObj.put("订单销售id", listData.get(position).getShangpinArray()[i].getIv_item_dingdanxiangqing_id());
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                        idArray.put(idObj);
//                                    }
//
//                                    Intent intent = new Intent(getActivity(), DingdanXiangqingActivity.class);
//                                    intent.putExtra("订单状态", listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanzhuangtai());
//                                    intent.putExtra("订单id", listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanid());
//                                    intent.putExtra("订单类型", listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_leixing());
//                                    intent.putExtra("订单销售id", idArray.toString());
//                                    getActivity().startActivity(intent);
//                                }
                                break;
                        }
                    }
                });

                viewHolder.ll_item_dingdan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("----", listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanzhuangtai());

                        JSONArray idArray = new JSONArray();
                        JSONObject idObj;
                        for (int i = 0; i < listData.get(position).getShangpinArray().length; i++) {
                            idObj = new JSONObject();
                            try {
                                idObj.put("订单销售id", listData.get(position).getShangpinArray()[i].getIv_item_dingdanxiangqing_id());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            idArray.put(idObj);
                        }

                        Intent intent = new Intent(getActivity(), DingdanXiangqingActivity.class);
                        intent.putExtra("订单状态", listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanzhuangtai());
                        intent.putExtra("订单id", listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanid());
                        intent.putExtra("订单类型", listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_leixing());
                        intent.putExtra("订单销售id", idArray.toString());
                        getActivity().startActivity(intent);
                    }
                });


                listView.setEnabled(false);
                listView.setClickable(false);
                WoDeDingDan_ShangPinAdapter woDeDingDan_shangPinAdapter = new WoDeDingDan_ShangPinAdapter(convertView.getContext(), listData.get(position).getShangpinArray(),
                        listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_dingdanzhuangtai(),
                        listData.get(position).getDingdanInfo().getTv_item_dingdanxiangqing_leixing());
                listView.setAdapter(woDeDingDan_shangPinAdapter);
                setListViewHeightBasedOnChildren(listView);
                return convertView;
            }
        };
        woDeDingDan_dingDanAdapter.changeData(listData);
        pull_wode_dingdan.setAdapter(woDeDingDan_dingDanAdapter);
    }

    private void initNetData(String leixing, String yeshu, boolean flag) {

        //初始化界面为普通请求 —— 0
        //刷新界面——1
        //加载更多——2
        //flag:   true  进来默认请求
        //flag:   false  不是一进来的默认请求
        if (flag)
            requestHandleArrayList.add(requestAction.shop_wodedingdan(this, leixing, yeshu, ptrl_wode_dingdan, 0));
        else
            requestHandleArrayList.add(requestAction.shop_wodedingdan(this, leixing, yeshu, ptrl_wode_dingdan, 1));
    }

    //Fragment变化时刷新数据
    private void shuaxin() {
        switch (getArguments().getString("text")) {
            case "全部":
                XiayiyeYeshu1 = 0;
//                listData1.clear();
                refreshOrLoad1 = true;
                initNetData("全部", String.valueOf(XiayiyeYeshu1), true);
                break;
            case "待付款":
                XiayiyeYeshu2 = 0;
//                listData2.clear();
                refreshOrLoad2 = true;
                initNetData("待付款", String.valueOf(XiayiyeYeshu2), true);
                break;
            case "待发货":
                XiayiyeYeshu3 = 0;
//                listData3.clear();
                refreshOrLoad3 = true;
                initNetData("待发货", String.valueOf(XiayiyeYeshu3), true);

                //线下交易说明请求
                if (Str_XianXiaJiaoYi_ShuoMing.length() == 0)
                    requestHandleArrayList.add(requestAction.shop_userExplain(this));

                break;
            case "待收货":
                XiayiyeYeshu4 = 0;
//                listData4.clear();
                refreshOrLoad4 = true;
                initNetData("待收货", String.valueOf(XiayiyeYeshu4), true);
                break;
            case "待评价":
                XiayiyeYeshu5 = 0;
//                listData5.clear();
                refreshOrLoad5 = true;
                initNetData("待评价", String.valueOf(XiayiyeYeshu5), true);
                break;
            default:
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && flag) {
            L.e("------", "-----" + currentFragment);
            shuaxin();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flag)
            shuaxin();
    }

    /**
     * 将response转成json格式
     *
     * @param response
     * @return
     * @throws JSONException
     */
    private void ShujuChuli(JSONObject response, int index) throws JSONException {
        switch (index) {
            case 1:
                Loadlength1 = response.getInt("条数");
                XiayiyeYeshu1 = response.getInt("页数");
                break;
            case 2:
                Loadlength2 = response.getInt("条数");
                XiayiyeYeshu2 = response.getInt("页数");
                break;
            case 3:
                Loadlength3 = response.getInt("条数");
                XiayiyeYeshu3 = response.getInt("页数");
                break;
            case 4:
                Loadlength4 = response.getInt("条数");
                XiayiyeYeshu4 = response.getInt("页数");
                break;
            case 5:
                Loadlength5 = response.getInt("条数");
                XiayiyeYeshu5 = response.getInt("页数");
                break;
        }
        if (response.getInt("条数") > 0) {
            JSONArray arrDingdan = new JSONArray(response.getString("一级列表"));
            TreeNodeBean treeNodeBean;
            for (int i = 0; i < arrDingdan.length(); i++) {
                double kuaidifei = 0;
                double zongjia = 0;
                JSONObject jsonDingdan = arrDingdan.getJSONObject(i);
                JSONArray arrShangpin = new JSONArray(jsonDingdan.getString("商品"));
                treeNodeBean = new TreeNodeBean(arrShangpin.length());
                WoDeDingDan_ShangPinInfo woDeDingDan_shangPinInfo;

                //添加订单类型（全部、待付款、待发货、待收货、待评价）
                switch (index) {
                    case 1:
                        treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_leixing("全部");
                        break;
                    case 2:
                        treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_leixing("待付款");
                        break;
                    case 3:
                        treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_leixing("待发货");
                        break;
                    case 4:
                        treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_leixing("待收货");
                        break;
                    case 5:
                        treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_leixing("待评价");
                        break;
                }

                treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_dingdanid(jsonDingdan.getString("订单id"));
                treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_dingdanbianhao(jsonDingdan.getString("订单编号"));
                treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_dingdanzhuangtai(jsonDingdan.getString("状态"));
                treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_dianpuming(jsonDingdan.getString("店铺名"));
                treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_shouhuoren(jsonDingdan.getString("收货人"));
                treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_shoujihao(jsonDingdan.getString("收货人手机号"));
                treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_shouhuodizhi(jsonDingdan.getString("收货地址"));
                treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_comment(jsonDingdan.getString("comment"));
//                treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_shangjinbi(jsonDingdan.getString("商金币"));
                treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_dingdanzongjia(jsonDingdan.getString("合计"));
                treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_xianxiajiaoyi(jsonDingdan.getString("线下交易"));

                treeNodeBean.getDingdanInfo().setBt_button1_text("查看物流");
                treeNodeBean.getDingdanInfo().setBt_button2_text("确认收货");

                int jianshu = 0;
                for (int j = 0; j < arrShangpin.length(); j++) {
                    JSONObject jsonShangpin = arrShangpin.getJSONObject(j);
                    woDeDingDan_shangPinInfo = new WoDeDingDan_ShangPinInfo();

                    woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_id(jsonShangpin.getString("id"));
                    woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_shangpinId(jsonShangpin.getString("商品id"));
                    woDeDingDan_shangPinInfo.setTv_item_dingdanxiangqing_zhongleixiangxi(jsonShangpin.getString("种类详细"));
                    woDeDingDan_shangPinInfo.setTv_item_dingdanxiangqing_title(jsonShangpin.getString("商品名称"));
                    woDeDingDan_shangPinInfo.setTv_item_dingdanxiangqing_shuliang(jsonShangpin.getString("商品数量"));
                    woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_tupian_url(jsonShangpin.getString("缩略图"));
                    woDeDingDan_shangPinInfo.setTv_item_dingdanxiangqing_danjia("¥" + df.format(Double.parseDouble(jsonShangpin.getString("价格"))));
                    woDeDingDan_shangPinInfo.setTv_item_dingdanxiangqing_yunfei(jsonShangpin.getString("快递费"));
                    woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_shangpinZhuangTai(jsonShangpin.getString("状态"));
                    woDeDingDan_shangPinInfo.setShangPinType(jsonShangpin.getString("类型"));

                    //计算商品数量
                    jianshu = jianshu + Integer.parseInt(jsonShangpin.getString("商品数量"));

//                    if (kuaidifei < Double.parseDouble(jsonShangpin.getString("快递费")))
                    kuaidifei = kuaidifei + Double.parseDouble(jsonShangpin.getString("快递费"));

                    zongjia += Double.parseDouble(jsonShangpin.getString("价格")) * Integer.parseInt(jsonShangpin.getString("商品数量"));
                    treeNodeBean.ShangpinArray[j] = woDeDingDan_shangPinInfo;
                }
                treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_jianshu(jianshu + "");

//                double suoyoujiage = kuaidifei + zongjia - Double.parseDouble(jsonDingdan.getString("商金币"));
//
//                if (suoyoujiage < 0)
//                    suoyoujiage = 0.00;
//
//                treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_dingdanzongjia("¥" + df.format(suoyoujiage));
                treeNodeBean.getDingdanInfo().setTv_item_dingdanxiangqing_dingdanyunfei("¥" + df.format(kuaidifei));

                switch (index) {
                    case 1:
                        listData1.add(treeNodeBean);
                        break;
                    case 2:
                        listData2.add(treeNodeBean);
                        break;
                    case 3:
                        listData3.add(treeNodeBean);
                        break;
                    case 4:
                        listData4.add(treeNodeBean);
                        break;
                    case 5:
                        listData5.add(treeNodeBean);
                        break;
                }
            }
        }
    }


    /**
     * 别问我当初写代码的思路，WoDeDingDan_DingDanAdapter谨慎改动
     *
     * @param requestTag
     * @param response
     * @throws JSONException
     */
    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        Log.e("requestSuccess" + requestTag, "requestSuccess: " + response.toString());

        switch (requestTag) {
            //确认收货
            case RequestAction.TAG_SHOP_QUERENSHOUHUO:
//            {"状态":"成功"}
//                MyDialog.showDialog(getActivity(), response.getString("状态"));
                MyToast.getInstance().showToast(mContext, "确认收货" + response.getString("状态"), true);
//                midToast("确认收货" + response.getString("状态"), Toast.LENGTH_SHORT, true);
                switch (getArguments().getString("text")) {
                    case "全部":
                        XiayiyeYeshu1 = 0;
                        initNetData("全部", String.valueOf(XiayiyeYeshu1), true);
                        break;
                    case "待付款":
                        XiayiyeYeshu2 = 0;
                        initNetData("待付款", String.valueOf(XiayiyeYeshu2), true);
                        break;
                    case "待发货":
                        XiayiyeYeshu3 = 0;
                        initNetData("待发货", String.valueOf(XiayiyeYeshu3), true);
                        break;
                    case "待收货":
                        XiayiyeYeshu4 = 0;
                        initNetData("待收货", String.valueOf(XiayiyeYeshu4), true);
                        break;
                    case "待评价":
                        XiayiyeYeshu5 = 0;
                        initNetData("待评价", String.valueOf(XiayiyeYeshu5), true);
                        break;
                    default:
                        break;
                }
                break;
            //取消订单
            case RequestAction.TAG_DINGDANCAOZUO_QUXIAODINGDAN:
                MyToast.getInstance().showToast(mContext, "取消订单" + response.getString("状态"), true);
//                midToast("取消订单" + response.getString("状态"), Toast.LENGTH_SHORT, true);
                switch (getArguments().getString("text")) {
                    case "全部":
                        XiayiyeYeshu1 = 0;
                        refreshOrLoad1 = true;
                        initNetData("全部", String.valueOf(XiayiyeYeshu1), true);
                        break;
                    case "待付款":
                        XiayiyeYeshu2 = 0;
                        refreshOrLoad2 = true;
                        initNetData("待付款", String.valueOf(XiayiyeYeshu2), true);
                        break;
                    case "待发货":
                        XiayiyeYeshu3 = 0;
                        refreshOrLoad3 = true;
                        initNetData("待发货", String.valueOf(XiayiyeYeshu3), true);
                        break;
                    case "待收货":
                        XiayiyeYeshu4 = 0;
                        refreshOrLoad4 = true;
                        initNetData("待收货", String.valueOf(XiayiyeYeshu4), true);
                        break;
                    case "待评价":
                        XiayiyeYeshu5 = 0;
                        refreshOrLoad5 = true;
                        initNetData("待评价", String.valueOf(XiayiyeYeshu5), true);
                        break;
                    default:
                        break;
                }
                break;
            case RequestAction.TAG_WODEDINGDAN_QUANBU:
                if ("成功".equals(response.getString("状态"))) {
                    if (refreshOrLoad1) {
                        Log.e("清除了数据", "true");
                        listData1.clear();
                        woDeDingDan_dingDanAdapter.changeData(listData1);
                        woDeDingDan_dingDanAdapter.notifyDataSetChanged();
                    } else {
                        ptrl_wode_dingdan.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }

                    ShujuChuli(response, 1);

                    woDeDingDan_dingDanAdapter.changeData(listData1);
                    woDeDingDan_dingDanAdapter.notifyDataSetChanged();

                    Log.e("listData1.size()", listData1.size() + "");

                    if (listData1.size() > 0) {
                        changeViewVis(false);
                    } else {
                        changeViewVis(true);
                    }

                }
                break;
            case RequestAction.TAG_WODEDINGDAN_DAIFUKUAN:
                if ("成功".equals(response.getString("状态"))) {
                    if (refreshOrLoad2) {
                        Log.e("清除了数据", "true");
                        listData2.clear();
                        woDeDingDan_dingDanAdapter.changeData(listData2);
                        woDeDingDan_dingDanAdapter.notifyDataSetChanged();
                    } else {
                        ptrl_wode_dingdan.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }

                    ShujuChuli(response, 2);
                    woDeDingDan_dingDanAdapter.changeData(listData2);
                    woDeDingDan_dingDanAdapter.notifyDataSetChanged();

                    Log.e("listData2.size()", listData2.size() + "");

                    if (listData2.size() > 0) {
                        changeViewVis(false);
                    } else {
                        changeViewVis(true);
                    }
                }
                break;

            case RequestAction.TAG_WODEDINGDAN_DAIFAHUO:
                if ("成功".equals(response.getString("状态"))) {
                    if (refreshOrLoad3) {
                        Log.e("清除了数据", "true");
                        listData3.clear();
                        woDeDingDan_dingDanAdapter.changeData(listData3);
                        woDeDingDan_dingDanAdapter.notifyDataSetChanged();
                    } else {
                        ptrl_wode_dingdan.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }

                    ShujuChuli(response, 3);
                    woDeDingDan_dingDanAdapter.changeData(listData3);
                    woDeDingDan_dingDanAdapter.notifyDataSetChanged();

                    Log.e("listData3.size()", listData3.size() + "");

                    if (listData3.size() > 0) {
                        changeViewVis(false);
                    } else {
                        changeViewVis(true);
                    }

                }
                break;

            case RequestAction.TAG_WODEDINGDAN_DAISHOUHUO:
                if ("成功".equals(response.getString("状态"))) {
                    if (refreshOrLoad4) {
                        Log.e("清除了数据", "true");
                        listData4.clear();
                        woDeDingDan_dingDanAdapter.changeData(listData4);
                        woDeDingDan_dingDanAdapter.notifyDataSetChanged();
                    } else {
                        ptrl_wode_dingdan.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }

                    ShujuChuli(response, 4);
                    woDeDingDan_dingDanAdapter.changeData(listData4);
                    woDeDingDan_dingDanAdapter.notifyDataSetChanged();

                    Log.e("listData4.size()", listData4.size() + "");

                    if (listData4.size() > 0) {
                        changeViewVis(false);
                    } else {
                        changeViewVis(true);
                    }
                }
                break;
            case RequestAction.TAG_WODEDINGDAN_DAIPINGJIA:
                if ("成功".equals(response.getString("状态"))) {
                    if (refreshOrLoad5) {
                        Log.e("清除了数据", "true");
                        listData5.clear();
                        woDeDingDan_dingDanAdapter.changeData(listData5);
                        woDeDingDan_dingDanAdapter.notifyDataSetChanged();
                    } else {
                        ptrl_wode_dingdan.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }

                    ShujuChuli(response, 5);
                    woDeDingDan_dingDanAdapter.changeData(listData5);
                    woDeDingDan_dingDanAdapter.notifyDataSetChanged();

                    Log.e("listData5.size()", listData5.size() + "");

                    if (listData5.size() > 0) {
                        changeViewVis(false);
                    } else {
                        changeViewVis(true);
                    }
                }
                break;

            case RequestAction.TAG_SHOP_REPAY:
                if ("成功".equals(response.getString("状态"))) {
                    if ("是".equals(preferences.getString(ConstantUtlis.SP_MYPWD, ""))) {
                        // 跳转收银台
                        startActivity(WebToPayManager.toCashier(preferences, mContext, response));
                    } else {
                        DialogUtlis.oneBtnNormal(getmDialog(), "您还未设置支付密码！", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, WangjimimaActivity.class);
                                intent.putExtra("forgetpwd", "设置支付密码");
                                startActivity(intent);
                                dismissDialog();
                            }
                        });
                    }
                }
                break;

            //线下交易开关接口
            case RequestAction.TAG_SHOP_LINECODE:
                DialogUtlis.oneBtnNormal(getmDialog(), "该订单的线下交易收货验证码：" + response.getString("lineCode") +
                        "，也可在该订单详情页面进行查看，需要在商家确认发货的时候提供给商家，切勿外泄！！！");
                shuaxin();
                break;

            //线下交易说明接口
            case RequestAction.TAG_SHOP_USEREXPLAIN:
                Str_XianXiaJiaoYi_ShuoMing = JSONUtlis.getString(response, "explain");
                break;
        }
    }

    //评价
    private void Pingjia(String type, TreeNodeBean mTN) {
        List<PingjiaFaBiaoBean> listPingJia = new ArrayList<>();

        PingjiaFaBiaoBean pingjiaFaBiaoBean;
        for (int i = 0; i < mTN.getShangpinArray().length; i++) {
            pingjiaFaBiaoBean = new PingjiaFaBiaoBean();
            pingjiaFaBiaoBean.setId(mTN.getShangpinArray()[i].getIv_item_dingdanxiangqing_id());
            pingjiaFaBiaoBean.setShangpin_id(mTN.getShangpinArray()[i].getIv_item_dingdanxiangqing_shangpinId());
            pingjiaFaBiaoBean.setShangpin_title(mTN.getShangpinArray()[i].getTv_item_dingdanxiangqing_title());
            pingjiaFaBiaoBean.setShangpin_img_url(mTN.getShangpinArray()[i].getIv_item_dingdanxiangqing_tupian_url());
            pingjiaFaBiaoBean.setShangpin_guigefenlei(mTN.getShangpinArray()[i].getTv_item_dingdanxiangqing_zhongleixiangxi());
            pingjiaFaBiaoBean.setShangpin_leixing(mTN.getShangpinArray()[i].getShangPinType());
            pingjiaFaBiaoBean.setShangpin_imgList(new ArrayList<String>(4));

            listPingJia.add(pingjiaFaBiaoBean);
        }
        Intent intent = new Intent(getActivity(), FaBiaoPingjiaActivity.class);
//        Bundle bundle = new Bundle();
//        TreeNodeBean treeNodeBean = mTN;
//        bundle.putSerializable("商品信息", treeNodeBean);
//        intent.putExtras(bundle);
        intent.putExtra("商品信息", (Serializable) listPingJia);
        intent.putExtra("类型", type);
        getActivity().startActivity(intent);
    }

    //付款
    private void fukuan(TreeNodeBean mTN) {
        TreeNodeBean treeNodeBean = mTN;
        String id = treeNodeBean.getDingdanInfo().getTv_item_dingdanxiangqing_dingdanid();
        String dizhi = treeNodeBean.getDingdanInfo().getTv_item_dingdanxiangqing_shouhuodizhi();
        String shoujihao = treeNodeBean.getDingdanInfo().getTv_item_dingdanxiangqing_shoujihao();
        String shouhuoren = treeNodeBean.getDingdanInfo().getTv_item_dingdanxiangqing_shouhuoren();
        String zhuantai = treeNodeBean.getDingdanInfo().getTv_item_dingdanxiangqing_dingdanzhuangtai();

        DizhiBean dizhiBen = new DizhiBean("0", dizhi, shoujihao, shouhuoren, zhuantai);

        requestHandleArrayList.add(requestAction.shop_repay(this, treeNodeBean.getDingdanInfo().getTv_item_dingdanxiangqing_dingdanid()));
    }

    //订单操作
    private void DingdanCaozuo(String id, int i) {
        Log.e("id", id + "--" + i);
        switch (i) {
            case 1: //取消订单
                requestHandleArrayList.add(requestAction.shop_wodedingdan_quxiao(this, id));
                break;
            case 2: //确认收货
                requestHandleArrayList.add(requestAction.queRenShouHuo(this, id));
                break;
        }
    }

    @OnClick({R.id.tv_wodedingdan_quguangguang})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_wodedingdan_quguangguang:
                getActivity().setResult(0x666, new Intent(this.getActivity(), MainActivity.class));
                getActivity().finish();
                break;
        }
    }

//    //Toast
//    private void midToast(String str, int showTime, boolean haveImg) {
//        Toast toast = Toast.makeText(getActivity(), str, showTime);
//        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 0);  //设置显示位置
//        View view = View.inflate(getActivity(), R.layout.toast_buju, null);
//        ImageView imageView = (ImageView) view.findViewById(R.id.iv_toast_ic);
//        TextView textView = (TextView) view.findViewById(R.id.tv_toast_info);
//
//        if (haveImg) {
//            imageView.setVisibility(View.VISIBLE);
////            imageView.setImageResource(R.mipmap.ic_toast_config);
//        } else imageView.setVisibility(View.GONE);
//
//        textView.setText(str);
//        toast.setView(view);
////        LinearLayout layout = (LinearLayout) toast.getView();
////        layout.setBackgroundColor(Color.BLUE);
////        ImageView image = new ImageView(mContext);
////        image.setImageResource(R.mipmap.ic_agri_bank);
////        layout.addView(image, 0);
////        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
////        v.setTextColor(Color.YELLOW);     //设置字体颜色
//        toast.show();
//    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        if ("成功".equals(preferences.getString(ConstantUtlis.SP_LOGINTYPE, ""))) {
            switch (currentFragment) {
                case "全部":
                    refreshOrLoad1 = true;
                    XiayiyeYeshu1 = 0;
                    Log.i("onRefresh", "全部");
                    initNetData("全部", String.valueOf(XiayiyeYeshu1), false);
                    break;

                case "待付款":
                    refreshOrLoad2 = true;
                    XiayiyeYeshu2 = 0;
                    Log.i("onRefresh", "待付款");
                    Log.i("onRefresh", XiayiyeYeshu2 + "---");
                    initNetData("待付款", String.valueOf(XiayiyeYeshu2), false);
                    break;

                case "待发货":
                    refreshOrLoad3 = true;
                    XiayiyeYeshu3 = 0;
                    Log.i("onRefresh", "待发货");
                    initNetData("待发货", String.valueOf(XiayiyeYeshu3), false);

                    break;

                case "待收货":
                    refreshOrLoad4 = true;
                    XiayiyeYeshu4 = 0;

                    Log.i("onRefresh", "待收货");
                    initNetData("待收货", String.valueOf(XiayiyeYeshu4), false);
                    break;
                case "待评价":
                    refreshOrLoad5 = true;
                    XiayiyeYeshu5 = 0;

                    Log.i("onRefresh", "待评价");
                    initNetData("待评价", String.valueOf(XiayiyeYeshu5), false);
                    break;
            }
        } else {
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
            DialogUtlis.oneBtnNormal(getmDialog(), "请登录后查看!");
        }
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if ("成功".equals(preferences.getString(ConstantUtlis.SP_LOGINTYPE, ""))) {
            //确保有数据才设置上拉加载更多监听
            if (Loadlength1 > 0 || Loadlength2 > 0 || Loadlength3 > 0 || Loadlength4 > 0 || Loadlength5 > 0) {
                switch (currentFragment) {
                    case "全部":
                        if (Loadlength1 > 0) {
                            refreshOrLoad1 = false;
                            requestHandleArrayList.add(requestAction.shop_wodedingdan(this, "全部", String.valueOf(XiayiyeYeshu1), ptrl_wode_dingdan, 2));
                        }
                        break;
                    case "待付款":
                        if (Loadlength2 > 0) {
                            refreshOrLoad2 = false;
                            requestHandleArrayList.add(requestAction.shop_wodedingdan(this, "待付款", String.valueOf(XiayiyeYeshu2), ptrl_wode_dingdan, 2));
                        }
                        break;
                    case "待发货":
                        if (Loadlength3 > 0) {
                            refreshOrLoad3 = false;
                            requestHandleArrayList.add(requestAction.shop_wodedingdan(this, "待发货", String.valueOf(XiayiyeYeshu3), ptrl_wode_dingdan, 2));
                        }
                        break;
                    case "待收货":
                        if (Loadlength4 > 0) {
                            refreshOrLoad4 = false;
                            requestHandleArrayList.add(requestAction.shop_wodedingdan(this, "待收货", String.valueOf(XiayiyeYeshu4), ptrl_wode_dingdan, 2));
                        }
                        break;
                    case "待评价":
                        if (Loadlength5 > 0) {
                            refreshOrLoad5 = false;
                            requestHandleArrayList.add(requestAction.shop_wodedingdan(this, "待评价", String.valueOf(XiayiyeYeshu5), ptrl_wode_dingdan, 2));
                        }
                        break;
                }
            } else {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                MyToast.getInstance().showToast(mContext, "无更多数据！");
            }
        } else {
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
            DialogUtlis.oneBtnNormal(getmDialog(), "请登录后查看!");
        }
    }

    static class ViewHolder {
        private TextView dianpuming;
        //        private TextView dingdanbianhao;
        private TextView zhuangtai;
        private TextView zongjia;
        private TextView yunfei;
        private String comment;
        private TextView jianshu;

        private Button button1;
        private Button button2;

        private RelativeLayout item_rl_list_wodedingdan_button;

        private LinearLayout ll_item_dingdan;

        /**
         * 这两个是线下交易对应的布局控件声明，保证下次要改的，接盘的别谢我
         */
        private RelativeLayout rl_item_dingdan_xianxiajiaoyi;
        private SwitchView sv_item_dingdan_xianxiajiaoyi;
    }
}
