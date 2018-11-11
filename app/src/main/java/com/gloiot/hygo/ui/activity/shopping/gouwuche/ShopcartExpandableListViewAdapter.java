package com.gloiot.hygo.ui.activity.shopping.gouwuche;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.activity.shopping.Bean.GroupInfoBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShangpinBean;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.PictureUtlis;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * ExpandableListView的购物车适配器
 *
 * @author chenjunsen
 *         2015年8月18日下午8:53:57
 */
public class ShopcartExpandableListViewAdapter extends BaseExpandableListAdapter {
    private List<GroupInfoBean> groups;
    private Map<String, List<ShangpinBean>> children;
    private Context context;

    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private DecimalFormat format = new DecimalFormat("0.00");


    /**
     * 构造函数
     *
     * @param groups   组元素列表
     * @param children 子元素列表
     * @param context
     */
    public ShopcartExpandableListViewAdapter(List<GroupInfoBean> groups, Map<String, List<ShangpinBean>> children,
                                             Context context) {
        super();
        this.groups = groups;
        this.children = children;
        this.context = context;
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupId = groups.get(groupPosition).getId();
        return children.get(groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ShangpinBean> childs = children.get(groups.get(groupPosition).getId());
        return childs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder gholder;
        if (convertView == null) {
            gholder = new GroupHolder();
            convertView = View.inflate(context, R.layout.item_shopcart_group, null);
            gholder.cb_check = (CheckBox) convertView.findViewById(R.id.cb_check);
            gholder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
            convertView.setTag(gholder);
        } else {
            gholder = (GroupHolder) convertView.getTag();
        }
        final GroupInfoBean group = (GroupInfoBean) getGroup(groupPosition);
        if (group != null) {
            gholder.tv_group_name.setText(group.getDianpuming());
            gholder.cb_check.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    group.setChoosed(((CheckBox) v).isChecked());
                    checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());//暴露组选接口
                }
            });
            gholder.cb_check.setChecked(group.isChoosed());
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View view,
                             ViewGroup parent) {
        final ChildHolder cholder;
        final ChildHolder1 cholder1;
        View convertView = null;
        View convertView1 = null;
        if ("是".equals(((ShangpinBean) getChild(groupPosition, childPosition)).getIsHuoDongShangPin())) {
            if (convertView1 == null) {
                cholder1 = new ChildHolder1();
                convertView1 = View.inflate(context, R.layout.item_shopcart_product1, null);
                cholder1.cb_check1 = (CheckBox) convertView1.findViewById(R.id.cb_check);
                cholder1.iv_pic1 = (ImageView) convertView1.findViewById(R.id.iv_pic);
                cholder1.tv_item_shopcart_name1 = (TextView) convertView1.findViewById(R.id.tv_item_shopcart_name);
                cholder1.tv_item_shopcart_guige1 = (TextView) convertView1.findViewById(R.id.tv_item_shopcart_guige);
                cholder1.tv_price1 = (TextView) convertView1.findViewById(R.id.tv_item_shopcart_price);
                cholder1.iv_increase1 = (ImageView) convertView1.findViewById(R.id.iv_increase);
                cholder1.iv_decrease1 = (ImageView) convertView1.findViewById(R.id.iv_decrease);
                cholder1.tv_count1 = (TextView) convertView1.findViewById(R.id.tv_buy_count);
                cholder1.btn_item_delete1 = (Button) convertView1.findViewById(R.id.btn_item_delete);
                cholder1.ll_item_shopcart1 = (LinearLayout) convertView1.findViewById(R.id.ll_item_shopcart);
                cholder1.tv_shopcart_huodong_dikou = (TextView) convertView1.findViewById(R.id.tv_shopcart_huodong_dikou);

                convertView1.setTag(cholder1);
            } else {
                cholder1 = (ChildHolder1) convertView1.getTag();
            }
            final ShangpinBean product = (ShangpinBean) getChild(groupPosition, childPosition);
            if (product != null) {
                cholder1.tv_item_shopcart_name1.setText(product.getShangpin_mingcheng());
                cholder1.tv_price1.setText("￥" + format.format(product.getDanjia()));
                cholder1.tv_count1.setText(product.getShangpin_shuliang() + "");
                cholder1.cb_check1.setChecked(product.isChoosed());
                cholder1.tv_item_shopcart_guige1.setText(product.getShangpin_guige());
                cholder1.tv_shopcart_huodong_dikou.setText("可用" + product.getDiKouLv() + "%积分");
                CommonUtlis.setImageTitle(context, product.getShangpin_leixing(), product.getShangpin_mingcheng(), cholder1.tv_item_shopcart_name1);

                PictureUtlis.loadRoundImageViewHolder(context, product.getSuolvtu(), R.drawable.default_image, cholder1.iv_pic1, 8);
                cholder1.cb_check1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        product.setChoosed(((CheckBox) v).isChecked());
                        cholder1.cb_check1.setChecked(((CheckBox) v).isChecked());
                        checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());//暴露子选接口
                    }
                });
                cholder1.iv_increase1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyCountInterface.doIncrease(groupPosition, childPosition, cholder1.tv_count1,
                                cholder1.cb_check1.isChecked());//暴露增加接口
                    }
                });
                cholder1.iv_decrease1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyCountInterface.doDecrease(groupPosition, childPosition, cholder1.tv_count1,
                                cholder1.cb_check1.isChecked());//暴露删减接口
                    }
                });
                cholder1.btn_item_delete1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyCountInterface.doDelete(groupPosition, childPosition, cholder1.tv_count1);//暴露删除接口
                    }
                });
                cholder1.ll_item_shopcart1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyCountInterface.GoToShangPinXiangQing(groupPosition, childPosition, cholder1.tv_count1);
                    }
                });

            }
            return convertView1;
        } else {
            if (convertView == null) {
                cholder = new ChildHolder();
                convertView = View.inflate(context, R.layout.item_shopcart_product, null);
                cholder.cb_check = (CheckBox) convertView.findViewById(R.id.cb_check);
                cholder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
                cholder.tv_item_shopcart_name = (TextView) convertView.findViewById(R.id.tv_item_shopcart_name);
                cholder.tv_item_shopcart_guige = (TextView) convertView.findViewById(R.id.tv_item_shopcart_guige);
                cholder.tv_price = (TextView) convertView.findViewById(R.id.tv_item_shopcart_price);
                cholder.iv_increase = (ImageView) convertView.findViewById(R.id.iv_increase);
                cholder.iv_decrease = (ImageView) convertView.findViewById(R.id.iv_decrease);
                cholder.tv_count = (TextView) convertView.findViewById(R.id.tv_buy_count);
                cholder.btn_item_delete = (Button) convertView.findViewById(R.id.btn_item_delete);
                cholder.ll_item_shopcart = (LinearLayout) convertView.findViewById(R.id.ll_item_shopcart);

                convertView.setTag(cholder);
            } else {
                cholder = (ChildHolder) convertView.getTag();
            }
            final ShangpinBean product = (ShangpinBean) getChild(groupPosition, childPosition);
            if (product != null) {
                cholder.tv_item_shopcart_name.setText(product.getShangpin_mingcheng());
                cholder.tv_price.setText("￥" + format.format(product.getDanjia()));
                cholder.tv_count.setText(product.getShangpin_shuliang() + "");
                cholder.cb_check.setChecked(product.isChoosed());
                cholder.tv_item_shopcart_guige.setText(product.getShangpin_guige());

                CommonUtlis.setImageTitle(context, product.getShangpin_leixing(), product.getShangpin_mingcheng(), cholder.tv_item_shopcart_name);

                PictureUtlis.loadRoundImageViewHolder(context, product.getSuolvtu(), R.drawable.default_image, cholder.iv_pic, 8);
                cholder.cb_check.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        product.setChoosed(((CheckBox) v).isChecked());
                        cholder.cb_check.setChecked(((CheckBox) v).isChecked());
                        checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());//暴露子选接口
                    }
                });
                cholder.iv_increase.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyCountInterface.doIncrease(groupPosition, childPosition, cholder.tv_count,
                                cholder.cb_check.isChecked());//暴露增加接口
                    }
                });
                cholder.iv_decrease.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyCountInterface.doDecrease(groupPosition, childPosition, cholder.tv_count,
                                cholder.cb_check.isChecked());//暴露删减接口
                    }
                });
                cholder.btn_item_delete.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyCountInterface.doDelete(groupPosition, childPosition, cholder.tv_count);//暴露删除接口
                    }
                });
                cholder.ll_item_shopcart.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyCountInterface.GoToShangPinXiangQing(groupPosition, childPosition, cholder.tv_count);
                    }
                });

            }
            return convertView;
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * 组元素绑定器
     *
     * @author chenjunsen
     *         2015年8月18日下午8:56:15
     */
    private class GroupHolder {
        CheckBox cb_check;
        TextView tv_group_name;
    }

    /**
     * 子元素绑定器
     *
     * @author chenjunsen
     *         2015年8月18日下午8:56:25
     */
    private class ChildHolder {
        CheckBox cb_check;
        ImageView iv_pic;
        TextView tv_item_shopcart_name;
        TextView tv_item_shopcart_guige;
        TextView tv_price;
        ImageView iv_increase;
        TextView tv_count;
        ImageView iv_decrease;
        Button btn_item_delete;
        LinearLayout ll_item_shopcart;

    }

    private class ChildHolder1 {
        CheckBox cb_check1;
        ImageView iv_pic1;
        TextView tv_item_shopcart_name1;
        TextView tv_item_shopcart_guige1;
        TextView tv_price1;
        ImageView iv_increase1;
        TextView tv_count1;
        ImageView iv_decrease1;
        Button btn_item_delete1;
        LinearLayout ll_item_shopcart1;
        TextView tv_shopcart_huodong_dikou;

    }

    /**
     * 复选框接口
     *
     * @author chenjunsen
     *         2015年8月18日下午8:56:39
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素位置
         * @param isChecked     组元素选中与否
         */
        public void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        public void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    /**
     * 改变数量的接口
     *
     * @author chenjunsen
     *         2015年8月18日下午8:56:50
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删除操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         */
        public void doDelete(int groupPosition, int childPosition, View showCountView);

        /**
         * 删除操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         */
        public void GoToShangPinXiangQing(int groupPosition, int childPosition, View showCountView);

    }

}
