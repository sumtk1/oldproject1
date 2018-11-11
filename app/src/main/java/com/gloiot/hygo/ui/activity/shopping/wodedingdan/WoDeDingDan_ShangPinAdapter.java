package com.gloiot.hygo.ui.activity.shopping.wodedingdan;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.activity.shopping.Bean.WoDeDingDan_ShangPinInfo;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.utils.PictureUtlis;


/**
 * Created by hygo02 on 2016/10/14.
 */

public class WoDeDingDan_ShangPinAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private Context context;

    private WoDeDingDan_ShangPinInfo[] ShangpinArray;
    private String dingDanZhuangTai;
    private String dingDanLeiXing;

    static class ViewHolder {
        private ImageView tupian;
        private TextView title;
        private TextView danjia;
        private TextView shuliang;
        private TextView xiangxi;
        private ImageView zhuangtai;
    }

    public WoDeDingDan_ShangPinAdapter(Context context, WoDeDingDan_ShangPinInfo[] ShangpinArray, String dingDanZhuangTai, String dingDanLeiXing) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.dingDanZhuangTai = dingDanZhuangTai;
        this.dingDanLeiXing = dingDanLeiXing;

        this.ShangpinArray = ShangpinArray;
    }

    @Override
    public int getCount() {
        return ShangpinArray.length;
    }

    @Override
    public Object getItem(int position) {
        return ShangpinArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_list_wodedingdan_shangpin, null);

            viewHolder.tupian = (ImageView) convertView.findViewById(R.id.iv_item_dingdan_shangpin_tupian);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_item_dingdan_shangpin_title);
            viewHolder.danjia = (TextView) convertView.findViewById(R.id.tv_item_dingdan_shangpin_danjia);
            viewHolder.shuliang = (TextView) convertView.findViewById(R.id.tv_item_dingdan_shangpin_shuliang);
            viewHolder.xiangxi = (TextView) convertView.findViewById(R.id.tv_item_dingdan_shangpin_zhongleixiangxi);

            viewHolder.zhuangtai = (ImageView) convertView.findViewById(R.id.tv_item_dingdan_shangpin_zhuangtai);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PictureUtlis.loadImageViewHolder(context, ShangpinArray[position].getIv_item_dingdanxiangqing_tupian_url(), R.drawable.default_image, viewHolder.tupian);
//        viewHolder.title.setText(ShangpinArray[position].getTv_item_dingdanxiangqing_title());
        viewHolder.danjia.setText(ShangpinArray[position].getTv_item_dingdanxiangqing_danjia());
        viewHolder.shuliang.setText("×" + ShangpinArray[position].getTv_item_dingdanxiangqing_shuliang());
        viewHolder.xiangxi.setText(ShangpinArray[position].getTv_item_dingdanxiangqing_dingdanyanse());

        CommonUtlis.setImageTitle(context, ShangpinArray[position].getShangPinType(), ShangpinArray[position].getTv_item_dingdanxiangqing_title(), viewHolder.title);

        if ("全部".equals(dingDanLeiXing) && ShangpinArray.length > 1 &&
                ("".equals(dingDanZhuangTai) || "买家已付款".equals(dingDanZhuangTai) || "卖家已发货".equals(dingDanZhuangTai))) {

            viewHolder.zhuangtai.setVisibility(View.VISIBLE);

            if ("卖家已发货".equals(ShangpinArray[position].getIv_item_dingdanxiangqing_shangpinZhuangTai())) {
                viewHolder.zhuangtai.setImageResource(R.mipmap.ic_shangpinzhuangtai_yifahuo);
            } else if ("买家已付款".equals(ShangpinArray[position].getIv_item_dingdanxiangqing_shangpinZhuangTai())) {
                viewHolder.zhuangtai.setImageResource(R.mipmap.ic_shangpinzhuangtai_daifahuo);
            }

        } else {
            viewHolder.zhuangtai.setVisibility(View.GONE);
        }


        return convertView;
    }
}
