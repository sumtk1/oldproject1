package com.gloiot.hygo.ui.activity.my.shouhou;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.activity.shopping.wodedingdan.DingdanXiangqingActivity;
import com.zyd.wlwsdk.utils.PictureUtlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ShouHouAdapter extends BaseAdapter {

    private Context mContext;
    private List<ShouHouBean> listData;
    private LayoutInflater mInflater;

    public ShouHouAdapter(Context context, List<ShouHouBean> data) {

        this.mContext = context;
        this.listData = data;
        this.mInflater = LayoutInflater.from(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_list_apply_dingdan, null);

            holder.mSuoLueTU = (ImageView) convertView.findViewById(R.id.imv_shangpin);
            holder.mShenQing = (Button) convertView.findViewById(R.id.bt_repeal);
            holder.mShangpinMingcheng = (TextView) convertView.findViewById(R.id.tv_shangpin_mingcheng);
            holder.mDingDanZhuangtai = (TextView) convertView.findViewById(R.id.tv_dingdan_statu);
            holder.mShangpinShuliang = (TextView) convertView.findViewById(R.id.tv_shangpin_shuliang);
            holder.dianpuming = (TextView) convertView.findViewById(R.id.tv_shouhou_item_dianpuming);
            holder.zhongleixiangxi = (TextView) convertView.findViewById(R.id.tv_shangpin_yansefenlei);
            holder.heji = (TextView) convertView.findViewById(R.id.iv_shouhou_item_heji);
            holder.mShuoming = (TextView) convertView.findViewById(R.id.tv_shuohou_shuoming);
            holder.rl_shenhe_statu = (RelativeLayout) convertView.findViewById(R.id.rl_shenhe_statu);
            holder.rl_01 = (RelativeLayout) convertView.findViewById(R.id.rl_01);
            holder.imv_statu = (ImageView) convertView.findViewById(R.id.imv_statu);
            holder.rl_shenqing = (RelativeLayout) convertView.findViewById(R.id.rl_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (listData != null) {
            final String zhuangtai = listData.get(position).getZhuangtai();
            holder.mShangpinMingcheng.setText(listData.get(position).getShangpinMingchen());
            holder.mShangpinShuliang.setText("数量：" + listData.get(position).getShangpinShuliang());
            holder.heji.setText("合计：¥ " + listData.get(position).getHeji());


            holder.dianpuming.setText(listData.get(position).getDianpuMing());
            holder.zhongleixiangxi.setText(listData.get(position).getZhongleixiangxi());
            PictureUtlis.loadImageViewHolder(mContext, listData.get(position).getSuoLueTu(), R.drawable.default_image, holder.mSuoLueTU);
            holder.mDingDanZhuangtai.setText(zhuangtai);

            if ("买家已付款".equals(zhuangtai)) {
                holder.mDingDanZhuangtai.setTextColor(mContext.getResources().getColor(R.color.cl_ff7f29));
                holder.mDingDanZhuangtai.setVisibility(View.VISIBLE);
            } else if ("交易完成".equals(zhuangtai)) {
                holder.mDingDanZhuangtai.setTextColor(mContext.getResources().getColor(R.color.cl_2b9ced));
                holder.mDingDanZhuangtai.setVisibility(View.VISIBLE);
            } else if ("卖家已发货".equals(zhuangtai)) {
                holder.mDingDanZhuangtai.setTextColor(mContext.getResources().getColor(R.color.cl_333333));
                holder.mDingDanZhuangtai.setVisibility(View.VISIBLE);
            } else {
                holder.mDingDanZhuangtai.setVisibility(View.GONE);
            }

            holder.rl_01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONArray idArray = new JSONArray();
                    JSONObject idObj = new JSONObject();
                    try {
                        idObj.put("订单销售id", listData.get(position).getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    idArray.put(idObj);
                    Intent intent = new Intent(mContext, DingdanXiangqingActivity.class);
                    intent.putExtra("订单状态", listData.get(position).getDingdanZhuangtai());
                    intent.putExtra("订单id", listData.get(position).getDingdanId());
                    intent.putExtra("订单销售id", idArray.toString());

                    if ("买家已付款".equals(zhuangtai) || "卖家已发货".equals(zhuangtai) || "交易完成".equals(zhuangtai))
                        intent.putExtra("订单类型", "售后订单详情_申请售后");
                    else
                        intent.putExtra("订单类型", "售后订单详情_申请记录");

                    intent.putExtra("售后状态", listData.get(position).getZhuangtai());
                    intent.putExtra("售后类别", listData.get(position).getLeibie());
                    intent.putExtra("是否撤销", listData.get(position).getIsCheXiao());
                    mContext.startActivity(intent);
                }
            });


            switch (zhuangtai) {
                case "买家已付款":
                case "卖家已发货":
                case "交易完成":
                    holder.rl_shenhe_statu.setVisibility(View.GONE);
                    holder.rl_shenqing.setVisibility(View.VISIBLE);
                    convertView.setEnabled(false);
                    holder.mShenQing.setText("申请售后");
                    holder.mShenQing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, TuikuanOrTuihuo.class);
                            intent.putExtra("订单id", listData.get(position).getDingdanId());
                            intent.putExtra("商品id", listData.get(position).getShangpinId());
                            intent.putExtra("id", listData.get(position).getId());
                            intent.putExtra("订单状态", listData.get(position).getDingdanZhuangtai());
                            mContext.startActivity(intent);
                        }
                    });
                    break;

                case "等待商家处理退款申请":
                case "等待商家处理退货申请":
                case "等待商家处理退货":
                    holder.rl_shenhe_statu.setVisibility(View.VISIBLE);
                    holder.rl_shenqing.setVisibility(View.GONE);
                    convertView.setEnabled(true);
                    holder.mShuoming.setText(listData.get(position).getShouhouShuoming());
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = null;
                            intent = new Intent(mContext, TuiKuanXiangQingActivity.class);
                            intent.putExtra("详情类型", listData.get(position).getLeibie());
                            intent.putExtra("订单id", listData.get(position).getDingdanId());
                            intent.putExtra("商品id", listData.get(position).getShangpinId());
                            intent.putExtra("id", listData.get(position).getId());
                            mContext.startActivity(intent);
                        }
                    });
                    //处理中小图标
                    holder.imv_statu.setImageResource(R.mipmap.ic_shouhou_chulizhong);
                    holder.mShuoming.setTextColor(mContext.getResources().getColor(R.color.cl_ff7f29));
                    break;

                case "商家已同意退款":
                    holder.rl_shenhe_statu.setVisibility(View.VISIBLE);
                    holder.rl_shenqing.setVisibility(View.GONE);
                    convertView.setEnabled(true);
                    holder.mShuoming.setText(listData.get(position).getShouhouShuoming());
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(mContext, ShouHouSuccessActivity.class);
                            intent.putExtra("订单id", listData.get(position).getDingdanId());
                            intent.putExtra("商品id", listData.get(position).getShangpinId());
                            intent.putExtra("id", listData.get(position).getId());
                            intent.putExtra("类别", listData.get(position).getLeibie());
                            mContext.startActivity(intent);
                        }
                    });
                    //成功小图标
                    holder.imv_statu.setImageResource(R.mipmap.ic_shouhou_chenggong);
                    holder.mShuoming.setTextColor(mContext.getResources().getColor(R.color.cl_2b9ced));
                    break;

                case "商家拒绝退款申请":
                case "商家拒绝退货申请":
                    holder.rl_shenhe_statu.setVisibility(View.VISIBLE);
                    holder.rl_shenqing.setVisibility(View.GONE);
                    convertView.setEnabled(true);
                    holder.mShuoming.setText(listData.get(position).getShouhouShuoming());
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ShouHouFailActivity.class);
                            intent.putExtra("订单id", listData.get(position).getDingdanId());
                            intent.putExtra("商品id", listData.get(position).getShangpinId());
                            intent.putExtra("id", listData.get(position).getId());
                            intent.putExtra("申请售后", listData.get(position).getLeibie());
                            intent.putExtra("订单状态", listData.get(position).getDingdanZhuangtai());
                            mContext.startActivity(intent);
                        }
                    });
                    //失败小图标
                    holder.imv_statu.setImageResource(R.mipmap.ic_shouhou_default);
                    holder.mShuoming.setTextColor(mContext.getResources().getColor(R.color.cl_ff436f));
                    break;

                case "商家已同意退货申请":
                    holder.rl_shenhe_statu.setVisibility(View.VISIBLE);
                    holder.rl_shenqing.setVisibility(View.GONE);
                    convertView.setEnabled(true);
                    holder.mShuoming.setText(listData.get(position).getShouhouShuoming());
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ShouHouTuiHuoActivity.class);
                            intent.putExtra("订单id", listData.get(position).getDingdanId());
                            intent.putExtra("商品id", listData.get(position).getShangpinId());
                            intent.putExtra("id", listData.get(position).getId());
                            mContext.startActivity(intent);
                        }
                    });
                    //处理中小图标
                    holder.imv_statu.setImageResource(R.mipmap.ic_shouhou_chulizhong);
                    holder.mShuoming.setTextColor(mContext.getResources().getColor(R.color.cl_ff7f29));
                    break;

                case "等待商家确认并退款":
                    holder.rl_shenhe_statu.setVisibility(View.VISIBLE);
                    holder.rl_shenqing.setVisibility(View.GONE);
                    convertView.setEnabled(true);
                    holder.mShuoming.setText(listData.get(position).getShouhouShuoming());
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, TuiKuanXiangQingActivity.class);
                            intent.putExtra("订单id", listData.get(position).getDingdanId());
                            intent.putExtra("商品id", listData.get(position).getShangpinId());
                            intent.putExtra("id", listData.get(position).getId());
                            intent.putExtra("详情类型", "等待商家确认收货");
                            mContext.startActivity(intent);
                        }
                    });
                    //处理中小图标
                    holder.imv_statu.setImageResource(R.mipmap.ic_shouhou_chulizhong);
                    holder.mShuoming.setTextColor(mContext.getResources().getColor(R.color.cl_ff7f29));
                    break;

                case "用户超时未处理":
                    holder.rl_shenhe_statu.setVisibility(View.VISIBLE);
                    holder.rl_shenqing.setVisibility(View.GONE);
                    convertView.setEnabled(true);
                    holder.mShuoming.setText(listData.get(position).getShouhouShuoming());
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ShouHouChexiaoActivity.class);
                            intent.putExtra("订单id", listData.get(position).getDingdanId());
                            intent.putExtra("商品id", listData.get(position).getShangpinId());
                            intent.putExtra("id", listData.get(position).getId());
                            intent.putExtra("订单状态", listData.get(position).getDingdanZhuangtai());
                            intent.putExtra("类别", "用户未处理");
                            mContext.startActivity(intent);
                        }
                    });
                    //撤销小图标
                    holder.imv_statu.setImageResource(R.mipmap.ic_shouhou_chexiao);
                    holder.mShuoming.setTextColor(mContext.getResources().getColor(R.color.cl_666666));
                    break;

                case "已撤销退款申请"://这两个就是退款关闭
                case "已撤销退货申请":
                    holder.rl_shenhe_statu.setVisibility(View.VISIBLE);
                    holder.rl_shenqing.setVisibility(View.GONE);
                    convertView.setEnabled(true);
                    holder.mShuoming.setText(listData.get(position).getShouhouShuoming());
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ShouHouChexiaoActivity.class);
                            intent.putExtra("订单id", listData.get(position).getDingdanId());
                            intent.putExtra("商品id", listData.get(position).getShangpinId());
                            intent.putExtra("id", listData.get(position).getId());
                            intent.putExtra("订单状态", listData.get(position).getDingdanZhuangtai());
                            intent.putExtra("类别", listData.get(position).getLeibie());
                            mContext.startActivity(intent);
                        }
                    });
                    //撤销小图标
                    holder.imv_statu.setImageResource(R.mipmap.ic_shouhou_chexiao);
                    holder.mShuoming.setTextColor(mContext.getResources().getColor(R.color.cl_666666));
                    break;
                case "商家未处理":
                    holder.rl_shenhe_statu.setVisibility(View.VISIBLE);
                    holder.rl_shenqing.setVisibility(View.GONE);
                    convertView.setEnabled(true);
                    holder.mShuoming.setText(listData.get(position).getShouhouShuoming());
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ShouHouFanKuiActivity.class);
                            intent.putExtra("订单id", listData.get(position).getDingdanId());
                            intent.putExtra("商品id", listData.get(position).getShangpinId());
                            intent.putExtra("id", listData.get(position).getId());
                            intent.putExtra("订单状态", listData.get(position).getDingdanZhuangtai());
                            intent.putExtra("类别", listData.get(position).getLeibie());
                            mContext.startActivity(intent);
                        }
                    });
                    //失败小图标
                    holder.imv_statu.setImageResource(R.mipmap.ic_shouhou_default);
                    holder.mShuoming.setTextColor(mContext.getResources().getColor(R.color.cl_ff436f));
                    break;

                default:
                    holder.rl_shenhe_statu.setVisibility(View.GONE);
                    holder.rl_shenqing.setVisibility(View.GONE);
                    convertView.setEnabled(false);
                    break;
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView mDingDanZhuangtai;  //订单状态
        ImageView mSuoLueTU; //缩略图
        TextView mShangpinMingcheng; //商品名称
        TextView mShangpinShuliang; // 商品数量
        Button mShenQing; //  申请|撤销按钮
        TextView mShuoming; //售后说明
        ImageView imv_statu; //订单售后对应的小图标
        RelativeLayout rl_shenhe_statu;
        RelativeLayout rl_shenqing;
        RelativeLayout rl_01; //商品信息
        TextView dianpuming;  //店铺名
        TextView zhongleixiangxi; // 种类详细
        TextView heji; // 合计
    }
}
