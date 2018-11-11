package com.gloiot.hygo.ui.activity.shopping.dianpu;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.activity.shopping.Bean.FenLeiSonBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.YouHuiQuanBean;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.recyclerview.CommonAdapter;
import com.zyd.wlwsdk.adapter.recyclerview.OnItemClickListener;
import com.zyd.wlwsdk.utils.PictureUtlis;

import java.util.List;

/**
 * Created by mo on 2017/11/3.
 */

//http://blog.csdn.net/leejizhou/article/details/50708349

public class DianPuRecyclerViewAdapter extends RecyclerView.Adapter {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<FenLeiSonBean> listDatas;
    private List<YouHuiQuanBean> youHuiQuanBeanList;
    private MyItemClickListener mItemClickListener;
    private GetYouHuiQuan getYouHuiQuan;
    private View header;

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM1 = 1;
    private static final int ITEM_VIEW_TYPE_ITEM2 = 2;

    public DianPuRecyclerViewAdapter(Context context, List<FenLeiSonBean> data, List<YouHuiQuanBean> youHuiQuanBeanList) {
        this.listDatas = data;
        this.context = context;
        this.youHuiQuanBeanList = youHuiQuanBeanList;
        mLayoutInflater = LayoutInflater.from(context);
    }


    public void setHeaderView(View headerView) {
        this.header = headerView;
        notifyItemInserted(0);
    }

    //获取position
    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return youHuiQuanBeanList.size() <= 0 ? position : position - 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载Item View的时候根据不同TYPE加载不同的布局
        if (viewType == ITEM_VIEW_TYPE_ITEM1) {
            return new Item1ViewHolder(mLayoutInflater.inflate(R.layout.item_shangpin_fenlei_son_01, parent, false), mItemClickListener);
        } else if (viewType == ITEM_VIEW_TYPE_ITEM2) {
            return new Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_shangpin_fenlei_son_02, parent, false), mItemClickListener);
        } else if (viewType == ITEM_VIEW_TYPE_HEADER) {
            return new Item3ViewHolder(header);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final int pos = getRealPosition(holder);

        if (header != null && holder instanceof Item3ViewHolder) {
            LinearLayoutManager ms = new LinearLayoutManager(context);
            ms.setOrientation(LinearLayoutManager.HORIZONTAL);
            ((Item3ViewHolder) holder).recyclerView.setLayoutManager(ms);
            CommonAdapter commonAdapter = new CommonAdapter<YouHuiQuanBean>(context, R.layout.item_dianpu_youhuiquan, youHuiQuanBeanList) {
                @Override
                public void convert(ViewHolder holder, YouHuiQuanBean youHuiQuanBean) {
                    holder.setText(R.id.tv_jine, youHuiQuanBean.getPrice());
                    holder.setText(R.id.tv_youhui_type, youHuiQuanBean.getName());
                }
            };
            ((Item3ViewHolder) holder).recyclerView.setAdapter(commonAdapter);

            commonAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                    Log.e("优惠券", youHuiQuanBeanList.get(position).toString());
                    getYouHuiQuan.getyouhuiquan(youHuiQuanBeanList.get(position).getId());
                }

                @Override
                public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                    return false;
                }
            });

        } else if (holder instanceof Item1ViewHolder) {
            PictureUtlis.loadImageViewHolder(context, listDatas.get(pos).getFenLeiSon_img_url(), R.drawable.default_image, ((Item1ViewHolder) holder).iv_item_shangpin_fenlei_tupian);
            ((Item1ViewHolder) holder).tv_item_shangpin_fenlei_jiage.setText(listDatas.get(pos).getFenLeiSon_jiage());
//            ((Item1ViewHolder) holder).tv_item_shangpin_fenlei_dizhi.setText("销量 " + listDatas.get(pos).getFenLeiSon_xiaoshouliang());
            CommonUtlis.setImageTitle(context, listDatas.get(position).getFenLeiSon_leixin(), listDatas.get(pos).getFenLeiSon_title(), ((Item1ViewHolder) holder).tv_item_shangpin_fenlei_title);

        } else if (holder instanceof Item2ViewHolder) {
            PictureUtlis.loadImageViewHolder(context, listDatas.get(pos).getFenLeiSon_img_url(), R.drawable.default_image, ((Item2ViewHolder) holder).iv_item_shangpin_fenlei_tupian);
            ((Item2ViewHolder) holder).tv_item_shangpin_fenlei_title.setText(listDatas.get(pos).getFenLeiSon_title());
            ((Item2ViewHolder) holder).tv_item_shangpin_fenlei_jiage.setText(listDatas.get(pos).getFenLeiSon_jiage());
//            ((Item2ViewHolder) holder).tv_item_shangpin_fenlei_dizhi.setText("销量 " + listDatas.get(pos).getFenLeiSon_xiaoshouliang());
            CommonUtlis.setImageTitle(context, listDatas.get(position).getFenLeiSon_leixin(), listDatas.get(pos).getFenLeiSon_title(), ((Item2ViewHolder) holder).tv_item_shangpin_fenlei_title);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (youHuiQuanBeanList.size() > 0) {
            if (position == 0) {
                return ITEM_VIEW_TYPE_HEADER;
            } else {
                if (flag) {
                    return ITEM_VIEW_TYPE_ITEM1;
                } else {
                    return ITEM_VIEW_TYPE_ITEM2;
                }
            }
        } else {
            if (flag) {
                return ITEM_VIEW_TYPE_ITEM1;
            } else {
                return ITEM_VIEW_TYPE_ITEM2;
            }
        }

    }

    private boolean flag = false;

    public void changeItemView(boolean f) {
        this.flag = f;
    }

    @Override
    public int getItemCount() {
        return youHuiQuanBeanList.size() <= 0 ? listDatas.size() : listDatas.size() + 1;
    }

    //item1 的ViewHolder
    public static class Item1ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_item_shangpin_fenlei_tupian;
        TextView tv_item_shangpin_fenlei_title;
        TextView tv_item_shangpin_fenlei_jiage;
        TextView tv_item_shangpin_fenlei_dizhi;
        private MyItemClickListener mListener;

        public Item1ViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            iv_item_shangpin_fenlei_tupian = (ImageView) itemView.findViewById(R.id.iv_item_shangpin_fenlei_tupian);
            tv_item_shangpin_fenlei_title = (TextView) itemView.findViewById(R.id.tv_item_shangpin_fenlei_title);
            tv_item_shangpin_fenlei_jiage = (TextView) itemView.findViewById(R.id.tv_item_shangpin_fenlei_jiage);
            tv_item_shangpin_fenlei_dizhi = (TextView) itemView.findViewById(R.id.tv_item_shangpin_fenlei_dizhi);

            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }

    //item2 的ViewHolder
    public static class Item2ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_item_shangpin_fenlei_tupian;
        TextView tv_item_shangpin_fenlei_title;
        TextView tv_item_shangpin_fenlei_jiage;
        TextView tv_item_shangpin_fenlei_dizhi;
        private MyItemClickListener mListener;

        public Item2ViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            iv_item_shangpin_fenlei_tupian = (ImageView) itemView.findViewById(R.id.iv_item_shangpin_fenlei_tupian);
            tv_item_shangpin_fenlei_title = (TextView) itemView.findViewById(R.id.tv_item_shangpin_fenlei_title);
            tv_item_shangpin_fenlei_jiage = (TextView) itemView.findViewById(R.id.tv_item_shangpin_fenlei_jiage);
            tv_item_shangpin_fenlei_dizhi = (TextView) itemView.findViewById(R.id.tv_item_shangpin_fenlei_dizhi);
            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }

    public static class Item3ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;

        public Item3ViewHolder(View itemView) {
            super(itemView);
            if (itemView != null)
                recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_youhuiquan_header);
        }
    }

    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 创建一个优惠券回调接口
     */
    public interface GetYouHuiQuan {
        void getyouhuiquan(String id);
    }

    public void setGetYouHuiQuan(GetYouHuiQuan getYouHuiQuan) {
        this.getYouHuiQuan = getYouHuiQuan;
    }


    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == ITEM_VIEW_TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }


    //StaggeredGridLayoutManager并没有setSpanSizeLookup这样的方法，处理方式
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }
}
