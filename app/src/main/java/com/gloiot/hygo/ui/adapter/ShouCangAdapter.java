package com.gloiot.hygo.ui.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.activity.shopping.Bean.FenLeiSonBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShangPinShouCangInfo;

import com.gloiot.hygo.ui.activity.shopping.ShangPinXiangQingActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zyd.wlwsdk.adapter.recyclerview.OnItemClickListener1;
import com.zyd.wlwsdk.utils.PictureUtlis;


import java.util.ArrayList;
import java.util.List;


/**
 * SocialFragment消息列表的适配器
 * Created by hygo05 on 2017/4/12 15:27
 */
public class ShouCangAdapter extends SwipeMenuAdapter<ShouCangAdapter.ViewHolder> {

    private List<ShangPinShouCangInfo> listData;
    private List<String> deleteList;
    private OnItemClickListener1 mOnItemClickListener;
    private Context mContext;
    public String type = "编辑";
    public List<Integer> xuanzhong = new ArrayList<>();


    public ShouCangAdapter(Context context,List<ShangPinShouCangInfo> list, List<String> deleteList) {
        this.mContext = context;
        this.listData = list;
        this.deleteList = deleteList;
    }

    public void setOnItemClickListener(OnItemClickListener1 onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        Log.e("viewType",viewType+"");
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_shangpinshoucang_new, parent, false);
    }


    @Override
    public ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.e("收藏适配器数据",listData.get(position).toString());
        PictureUtlis.loadImageViewHolder(mContext, listData.get(position).getIv_item_shangpinshoucang_tupian_url(), R.drawable.default_image, holder.tupian);

        CommonUtlis.setImageTitle(mContext, listData.get(position).getTv_item_shangpinshoucang_leixing(), listData.get(position).getTv_item_shangpinshoucang_title(), holder.tv_title);
        switch (listData.get(position).getTv_item_shangpinshoucang_zhuantai()){
            case "已下架":
                holder.iv_item_shoucang_zhuantai.setVisibility(View.VISIBLE);
                holder.iv_item_shoucang_zhuantai.setBackgroundResource(R.mipmap.icon_xiajia);
                break;
            case "已售罄":
                holder.iv_item_shoucang_zhuantai.setVisibility(View.VISIBLE);
                holder.iv_item_shoucang_zhuantai.setBackgroundResource(R.mipmap.icon_shouqing);
                break;
            default:
                holder.iv_item_shoucang_zhuantai.setVisibility(View.GONE);
                break;
        }

        holder.tv_pirce.setText("￥" + listData.get(position).getTv_item_shangpinshoucang_pirce());

        if ("全部".equals(type)) {
            holder.dagou.setVisibility(View.VISIBLE);
        } else {
            holder.dagou.setVisibility(View.GONE);
        }


        for (int i = 0; i < listData.size(); i++)
            xuanzhong.add(0);

        if (xuanzhong.get(position) == 0) {
            holder.xuanzhong.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_xuanzhong_no));
        } else if (xuanzhong.get(position) == 1) {
            holder.xuanzhong.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_xuanzhong));
        }

        holder.rl_item_shoucang_xingxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiaoZhuan(position);
            }
        });

        holder.item_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiaoZhuan(position);
            }
        });

        holder.setOnItemClickListener(mOnItemClickListener);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name, tv_title, tv_pirce;
        ImageView tupian, xuanzhong,iv_item_shoucang_zhuantai;
        RelativeLayout dagou, buju, item_touxiang,rl_item_shoucang_xingxi;



        OnItemClickListener1 mOnItemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_name = (TextView) itemView.findViewById(R.id.tv_item_shoucang_title);
            tupian = (ImageView) itemView.findViewById(R.id.iv_item_shoucang_tupian);
            tv_title = (TextView) itemView.findViewById(R.id.tv_item_shoucang_title);
            tv_pirce = (TextView) itemView.findViewById(R.id.tv_item_shoucang_jiage);
            dagou = (RelativeLayout) itemView.findViewById(R.id.rl_item_shoucang_dagou);
            buju = (RelativeLayout) itemView.findViewById(R.id.rl_item_shoucang_youbian);
            item_touxiang = (RelativeLayout) itemView.findViewById(R.id.rl_item_shoucang_tupian);
            rl_item_shoucang_xingxi = (RelativeLayout) itemView.findViewById(R.id.rl_item_shoucang_xingxi);
            iv_item_shoucang_zhuantai = (ImageView) itemView.findViewById(R.id.iv_item_shoucang_zhuantai);

            xuanzhong = (ImageView) itemView.findViewById(R.id.iv_item_shoucang_dagou);
        }

        public void setOnItemClickListener(OnItemClickListener1 onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
//            Log.e("adpter","适配器00");
//            switch (v.getId()){
//                case R.id.rl_item_shoucang_dagou:
//                    Log.e("adpter","适配器00"+getItemCount() +    getItemId()  + getAdapterPosition()+  getPosition());
//                if(listData.get(getItemCount()).getTv_item_shangpinshoucang_xuanzhong()){
//                    xuanzhong.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_xuanzhong));
//                    listData.get(getItemCount()).setTv_item_shangpinshoucang_xuanzhong(true);
//                }else {
//                    xuanzhong.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_xuanzhong_no));
//                    listData.get(getItemCount()).setTv_item_shangpinshoucang_xuanzhong(false);
//                }
//                    break;
//            }
        }
    }

    public void changeSelectAll() {
        boolean quanxuan = true;
        for (int i = 0; i < listData.size(); i++) {
            if (xuanzhong.get(i) == 0) {
                quanxuan = false;
                break;
            }
        }
        if (quanxuan) {
            for (int i = 0; i < listData.size(); i++)
                xuanzhong.set(i, 0);
        } else
            for (int i = 0; i < listData.size(); i++)
                xuanzhong.set(i, 1);

       notifyDataSetChanged();
    }

    public void changeSelect(int positon) {
        if (xuanzhong.get(positon) == (int) 0) {
            xuanzhong.set(positon, 1);
        } else {
            xuanzhong.set(positon, 0);
        }

        notifyDataSetChanged();
    }

    public void deleteMySelect(){

    }
    /**
     * item——跳转商品详情
     */
    private void tiaoZhuan(int position) {
        if(!"已下架".equals(listData.get(position).getTv_item_shangpinshoucang_zhuantai())) {
            FenLeiSonBean bean = new FenLeiSonBean();
            bean.setFenLeiSon_dizhi("");
            bean.setFenLeiSon_jiage(listData.get(position).getTv_item_shangpinshoucang_pirce());
            bean.setFenLeiSon_img_url(listData.get(position).getIv_item_shangpinshoucang_tupian_url());
            bean.setFenLeiSon_title(listData.get(position).getTv_item_shangpinshoucang_title());

            Intent intent = new Intent(mContext, ShangPinXiangQingActivity.class);
            intent.putExtra("id", listData.get(position).getIv_item_shangpinshoucang_id());
            intent.putExtra("info", bean);
            mContext.startActivity(intent);
        }else {
            MyToast.getInstance().showToast(mContext,"该商品已下架");
        }
    }
}
