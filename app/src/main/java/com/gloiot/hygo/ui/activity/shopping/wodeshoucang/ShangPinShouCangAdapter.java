package com.gloiot.hygo.ui.activity.shopping.wodeshoucang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.ui.activity.shopping.Bean.ShangPinShouCangInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 *
 * @zhan time 2016/10/14.
 */

public class ShangPinShouCangAdapter extends BaseAdapter {
    private static final String TAG = "ShangPinShouCangAdapter";

    private List<ShangPinShouCangInfo> listData;
    private List<String> deleteList;
    private LayoutInflater mInflater = null;
    private Context context;
    public String type = "编辑";  //使用Adapter的activity

    protected List<Integer> xuanzhong = new ArrayList<>();

    static class ViewHolder {
        protected ImageView tupian;
        protected TextView title;
        protected TextView pirce;
        protected RelativeLayout shanchu;
        protected RelativeLayout dagou;
        protected RelativeLayout item_touxiang;
        protected RelativeLayout buju;

        protected ImageView xuanzhong;


        protected LinearLayout ll_item_shoucang_tubiao;
        protected ImageView iv_item_shoucang_ziyin;
        protected ImageView iv_item_shoucang_quanqiugou;

    }

    /**
     * 商品收藏Adapter
     *
     * @param context
     * @param list    收藏商品List
     * @param dellist 删除商品List
     */
    public ShangPinShouCangAdapter(Context context, List<ShangPinShouCangInfo> list,
                                   List<String> dellist) {
        this.context = context;
        this.listData = list;
        this.mInflater = LayoutInflater.from(context);
        this.deleteList = dellist;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

//    @Override
//    public View getView(final int position, View convertView, ViewGroup viewGroup) {
//        Log.e("getView", "getView");
//
//        ViewHolder viewHolder;
//        if (convertView == null) {
//            viewHolder = new ViewHolder();
//            convertView = mInflater.inflate(R.layout.item_list_shangpinshoucang, null);
//            viewHolder.tupian = (ImageView) convertView.findViewById(R.id.iv_item_shoucang_tupian);
//            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_item_shoucang_title);
//            viewHolder.pirce = (TextView) convertView.findViewById(R.id.tv_item_shoucang_jiage);
//            viewHolder.shanchu = (RelativeLayout) convertView.findViewById(R.id.rl_item_shoucang_shanchu);
//            viewHolder.dagou = (RelativeLayout) convertView.findViewById(R.id.rl_item_shoucang_dagou);
//
//            viewHolder.xuanzhong = (ImageView) convertView.findViewById(R.id.iv_item_shoucang_dagou);
//
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        CommonUtlis.setDisplayImageOptions(viewHolder.tupian, listData.get(position).getIv_item_shangpinshoucang_tupian_url(), 0);
//        viewHolder.title.setText(listData.get(position).getTv_item_shangpinshoucang_title());
//        viewHolder.pirce.setText("¥" + listData.get(position).getTv_item_shangpinshoucang_pirce());
//
//        if ("全部".equals(type)) {
//            viewHolder.shanchu.setVisibility(View.GONE);
//            viewHolder.dagou.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.shanchu.setVisibility(View.VISIBLE);
//            viewHolder.dagou.setVisibility(View.GONE);
//        }
//
//        viewHolder.shanchu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                delList.add(listData.get(position).getIv_item_shangpinshoucang_id());
//                DeleteShangPin(delList);
//                delShangpin.add(listData.get(position).getIv_item_shangpinshoucang_id());
////                deletePostion = position;
//            }
//        });
//
//        for (int i = 0; i < listData.size(); i++)
//            xuanzhong.add(0);
//
//        if (xuanzhong.get(position) == 0) {
//            viewHolder.xuanzhong.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_xuanzhong_no));
//        } else if (xuanzhong.get(position) == 1) {
//            viewHolder.xuanzhong.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_xuanzhong));
//        }
//
//        return convertView;
//    }


    protected void deleteMySelect() {
    }

    protected void changeSelect(int positon) {
        if (xuanzhong.get(positon) == (int) 0) {
            xuanzhong.set(positon, 1);
        } else {
            xuanzhong.set(positon, 0);
        }

        notifyDataSetChanged();
    }

    protected void changeSelectAll() {
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
}
