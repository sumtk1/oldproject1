package com.gloiot.hygo.ui.activity.shopping.gouwuche;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.my.youhuiquan.ShiYongYouHuiQuanActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.DiYongBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.DiYongYouHuiBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.GroupInfoBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.QuerenDingdanYouHuiQuanBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShangpinBean;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.PictureUtlis;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Created by lenovo on 2/23/2016.
 */
public class SectionedExpandableGridAdapter extends RecyclerView.Adapter<SectionedExpandableGridAdapter.ViewHolder> {

    //data array
    private ArrayList<Object> mDataArrayList;
    //判断是否是快递费
    private Boolean iskdf = true;

    //context
    private Context mContext;
    private DecimalFormat format = new DecimalFormat("0.00");

    private QuerenDingdanYouHuiQuanBean querenDingdanYouHuiQuanBean;

    private ArrayList<String> transferType = new ArrayList<>();//存储类型
    private  ArrayList<DiYongBean> diYongBeen_list;

    //listeners
//    private final ItemClickListener mItemClickListener;
//    private final SectionStateChangeListener mSectionStateChangeListener;

    //view type
    private static final int VIEW_TYPE_DIANPU = R.layout.item_daifukuan_dingdan_dianpu;
    private static final int VIEW_TYPE_SHANGPIN = R.layout.item_daifukuan_dingdan_shangpin; //TODO : change this
    private static final int VIEW_TYPE_KUAIDIFEI = R.layout.item_daifukuan_dingdan_kuaidifei;
    private static final int VIEW_TYPE_YOUHUIQUAN = R.layout.item_daifukuan_dingdan_youhuiquan;
    private static final int VIEW_TYPE_ZHANGHUDIYONG = R.layout.item_daifukuan_dingdan_zhanghudiyong;

    public SectionedExpandableGridAdapter(Context context, ArrayList<Object> dataArrayList,
                                          final GridLayoutManager gridLayoutManager) {
        mContext = context;
//        mItemClickListener = itemClickListener;
//        mSectionStateChangeListener = sectionStateChangeListener;
        mDataArrayList = dataArrayList;

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return isGroupInfo(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
    }

    private boolean isGroupInfo(int position) {
        return mDataArrayList.get(position) instanceof GroupInfoBean;
    }

    private boolean isShangpinBean(int position) {
        return mDataArrayList.get(position) instanceof ShangpinBean;
    }

    private boolean isYouHuiQuan(int position) {
        return mDataArrayList.get(position) instanceof QuerenDingdanYouHuiQuanBean;
    }

    private boolean isZhangHuDiYong(int position) {
        return mDataArrayList.get(position) instanceof DiYongYouHuiBean;
    }

    private boolean isKuaiDiFei(int position) {
        return mDataArrayList.get(position) instanceof Double;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mContext, LayoutInflater.from(mContext).inflate(viewType, parent, false), viewType);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.viewType) {
            case VIEW_TYPE_SHANGPIN:
                final ShangpinBean shangpinBean = (ShangpinBean) mDataArrayList.get(position);
                holder.tv_item_shopcart_name.setText(shangpinBean.getShangpin_mingcheng());
                holder.tv_item_shopcart_guige.setText("" + shangpinBean.getShangpin_guige());
                holder.tv_item_shopcart_price.setText("￥" + format.format(shangpinBean.getDanjia()));
                holder.tv_item_shopcart_shuliang.setText("X" + shangpinBean.getShangpin_shuliang());
                CommonUtlis.setImageTitle(mContext, shangpinBean.getShangpin_leixing(), shangpinBean.getShangpin_mingcheng(), holder.tv_item_shopcart_name);

                PictureUtlis.loadRoundImageViewHolder(mContext, shangpinBean.getSuolvtu(), R.drawable.default_image, holder.iv_item_shopcart_suolvtu, 8);
                break;
            case VIEW_TYPE_DIANPU:
                final GroupInfoBean groupInfoBean = (GroupInfoBean) mDataArrayList.get(position);
                holder.tv_group.setText(groupInfoBean.getDianpuming());
                break;
            case VIEW_TYPE_KUAIDIFEI:
//                Log.e("kdf--heji", mDataArrayList.get(position) + "--");
//                final double kdf = (Double) mDataArrayList.get(position);
//                holder.tv_item_dingdan_kuaidifei.setText("" + format.format(kdf));

                holder.tv_item_dingdan_kuaidifei.setText(mDataArrayList.get(position).toString());
                iskdf = false;
                break;
            case VIEW_TYPE_ZHANGHUDIYONG:
                DiYongYouHuiBean diYongYouHuiBean = (DiYongYouHuiBean) mDataArrayList.get(position);
                diYongBeen_list = diYongYouHuiBean.getDiYong_list();
                if (diYongBeen_list != null) {
                    if (diYongBeen_list.size() > 0) {
                        transferType.clear();
                        for (int i = 0; i < diYongBeen_list.size();i++) {
                            DiYongBean diYongBean = diYongBeen_list.get(i);
                            transferType.add(diYongBean.getType() + "共" + diYongBean.getAccount() + "  （可用" + diYongBean.getOffset() + ")");
                        }
                    }
                }
                break;
            case VIEW_TYPE_YOUHUIQUAN:
                //已选
                if (((QuerenDingdanYouHuiQuanBean) mDataArrayList.get(position)).getYouHuiQuanShiYongBean() != null) {
                    holder.tv_item_dingdan_youhuiquan.setText(
                            "-" + ((QuerenDingdanYouHuiQuanBean) mDataArrayList.get(position)).getYouHuiQuanShiYongBean().getJine().toString());
                } else { //未选
                    holder.tv_item_dingdan_youhuiquan.setText(
                            ((QuerenDingdanYouHuiQuanBean) mDataArrayList.get(position)).getFushuXiaoxi().toString());
                }
                querenDingdanYouHuiQuanBean = (QuerenDingdanYouHuiQuanBean) mDataArrayList.get(position);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return mDataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isGroupInfo(position)) {
            return VIEW_TYPE_DIANPU;
        } else if (isShangpinBean(position)) {
            return VIEW_TYPE_SHANGPIN;
        } else if (isYouHuiQuan(position)) {
            return VIEW_TYPE_YOUHUIQUAN;
        } else if (isZhangHuDiYong(position)) {
            return VIEW_TYPE_ZHANGHUDIYONG;
        } else {
            return VIEW_TYPE_KUAIDIFEI;
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        //common
        View view;
        int viewType;

        //组元素
        TextView tv_group;
        // ToggleButton sectionToggleButton;

        //子元素
        TextView tv_item_shopcart_name, tv_item_shopcart_guige, tv_item_shopcart_price, tv_item_shopcart_shuliang, tv_item_dingdan_kuaidifei, tv_item_dingdan_heji,
                tv_item_dingdan_youhuiquan, tv_item_dingdan_zhanghudiyong,tv_item_dingdan_dingyongjiner,tv_queren_dingdan_price;

        ImageView iv_item_shopcart_suolvtu;

        RelativeLayout rl_item_daifukuan_dingdan_yunfei, rl_item_daifukuan_dingdan_heji, rl_item_daifukuan_dingdan_youhuiquan;


        RelativeLayout rl_item_daifukuan_dingdan_zhanghudiyong;

        public ViewHolder(final Context context, final View view, int viewType) {
            super(view);
            this.viewType = viewType;
            this.view = view;
            if (viewType == VIEW_TYPE_SHANGPIN) {
                tv_item_shopcart_name = (TextView) view.findViewById(R.id.tv_item_shopcart_name);
                tv_item_shopcart_guige = (TextView) view.findViewById(R.id.tv_item_shopcart_guige);
                tv_item_shopcart_price = (TextView) view.findViewById(R.id.tv_item_shopcart_price);
                tv_item_shopcart_shuliang = (TextView) view.findViewById(R.id.tv_item_shopcart_shuliang);
                iv_item_shopcart_suolvtu = (ImageView) view.findViewById(R.id.iv_item_shopcart_suolvtu);
            } else if (viewType == VIEW_TYPE_DIANPU) {
                tv_group = (TextView) view.findViewById(R.id.tv_item_daifukuan_dianpu);
            } else if (viewType == VIEW_TYPE_KUAIDIFEI) {
                rl_item_daifukuan_dingdan_yunfei = (RelativeLayout) view.findViewById(R.id.rl_item_daifukuan_dingdan_yunfei);
                tv_item_dingdan_kuaidifei = (TextView) view.findViewById(R.id.tv_item_dingdan_kuaidifei);
            } else if (viewType == VIEW_TYPE_ZHANGHUDIYONG) {
                tv_item_dingdan_zhanghudiyong = (TextView) view.findViewById(R.id.tv_item_dingdan_zhanghudiyong);
                tv_item_dingdan_dingyongjiner = (TextView) view.findViewById(R.id.tv_item_dingdan_dingyongjiner);
                rl_item_daifukuan_dingdan_zhanghudiyong = (RelativeLayout) view.findViewById(R.id.rl_item_daifukuan_dingdan_zhanghudiyong);

                    rl_item_daifukuan_dingdan_zhanghudiyong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (QuerenDingdanActivity.getIsSuccess()) {
                                if (transferType.size() > 0) {
                                    DialogUtlis.customListView(((BaseActivity) mContext).getmDialog(), "请选择抵用账户", transferType, new MDialogInterface.ListViewOnClickInter() {
                                        @Override
                                        public void onItemClick(String data, int position) {
                                            DiYongBean diYongBean = diYongBeen_list.get(position);
                                            tv_item_dingdan_zhanghudiyong.setText(diYongBean.getType());
                                            tv_item_dingdan_dingyongjiner.setText(diYongBean.getOffset());
                                            ((QuerenDingdanActivity) context).ZhanghuDiyong(diYongBean.getType(), diYongBean.getOffset());
                                        }
                                    });
                                } else {
                                    tv_item_dingdan_zhanghudiyong.setText("无抵用账户可用");
                                    MyToast.getInstance().showToast(mContext, "无抵用账户可用");
                                }
                            }else {
                                MyToast.getInstance().showToast(mContext, "数据请求中");
                            }

                        }
                    });

            } else if (viewType == VIEW_TYPE_YOUHUIQUAN) {
                rl_item_daifukuan_dingdan_youhuiquan = (RelativeLayout) view.findViewById(R.id.rl_item_daifukuan_dingdan_youhuiquan);
                tv_item_dingdan_youhuiquan = (TextView) view.findViewById(R.id.tv_item_dingdan_youhuiquan);
                rl_item_daifukuan_dingdan_youhuiquan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (querenDingdanYouHuiQuanBean.getInfo() != null) {
                            if ("".equals(querenDingdanYouHuiQuanBean.getFushuXiaoxi())) {

                            } else {
                                Intent intent = new Intent(mContext, ShiYongYouHuiQuanActivity.class);
                                intent.putExtra("Obj", (Serializable) querenDingdanYouHuiQuanBean);
                                ((Activity) mContext).startActivityForResult(intent, 2);
                            }
                        } else {
                            MyToast.getInstance().showToast(mContext, "数据加载中~");
                        }
                    }
                });
            }
        }
    }
}
