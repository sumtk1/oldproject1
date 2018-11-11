package com.gloiot.hygo.ui.activity.shopping.gouwuche;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.GroupInfoBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.QuerendingdanBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShangpinBean;
import com.gloiot.hygo.ui.activity.shopping.ShangPinXiangQingActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.zyd.wlwsdk.utils.MyToast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hygo02 on 2017/4/11.
 */

public class GouWuCheActivity extends BaseActivity implements ShopcartExpandableListViewAdapter.CheckInterface, ShopcartExpandableListViewAdapter.ModifyCountInterface, View.OnClickListener {
    @Bind(R.id.btn_fg2_jiesuan)
    Button btn_fg2_jiesuan;
    @Bind(R.id.rl_gouwuche_foot)
    RelativeLayout rl_gouwuche_foot;
    @Bind(R.id.rl_gouwuche_kong)
    RelativeLayout rl_gouwuche_kong;
    @Bind(R.id.tv_fg2_guangguangba)
    TextView tv_fg2_guangguangba;
    @Bind(R.id.tv_fg2_privce)
    TextView tv_fg2_privce;
    @Bind(R.id.exListView)
    ExpandableListView exListView;
    @Bind(R.id.cb_fg2_check_all)
    CheckBox cb_fg2_check_all;

    //截取小数点后两位以前的值
    private DecimalFormat df;

    private List<ShangpinBean> toBeZhifuDingdan;//待支付的子元素列表
    private double totalPrice = 0.00;
    private List<GroupInfoBean> groups = new ArrayList<>();//组元素数据列表
    private Map<String, List<ShangpinBean>> children = new HashMap<>();//子元素数据列表
    private boolean gouwuche_jia, mycheckall, ischeckall_to_checkgroup = false;
    private int totalCount = 0;//购买的商品总数量
    private ShopcartExpandableListViewAdapter selva;
    private int shuliang_groupPosition, shuliang_childPosition; //购物车修改数据
    private View shuliang_showCountView; //购物车修改数据的视图
    private int mygroupPosition, mychildPosition;
    private AlertDialog alert;

    @Override
    public int initResource() {
        return R.layout.activity_gouwuche;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "购物车");

        //截取小数点后两位以前的值
        df = new DecimalFormat("######0.00");

        if ("成功".equals(preferences.getString(ConstantUtlis.SP_LOGINTYPE, ""))) {
            Log.e("TAG", "已登录");
            requestHandleArrayList.add(requestAction.shop_c_clists(this));
        } else {
            Log.e("TAG", "未登录");
            exListView.setVisibility(View.GONE);
            rl_gouwuche_kong.setVisibility(View.VISIBLE);
            rl_gouwuche_foot.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_fg2_guangguangba, R.id.cb_fg2_check_all, R.id.btn_fg2_jiesuan})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fg2_guangguangba:
                setResult(0x667, new Intent(GouWuCheActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.cb_fg2_check_all:
                doCheckAll();
                break;
            case R.id.btn_fg2_jiesuan:
                if (totalPrice == 0.00) {
                    DialogUtlis.oneBtnNormal(getmDialog(), "请选择要购买的商品");
                } else {
                    queren_dingdan();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ConstantUtlis.CHECK_ADD_GOUWUCHE) {
            groups.clear();
            children.clear();
            if (selva != null)
                selva.notifyDataSetChanged();
            rl_gouwuche_foot.setVisibility(View.GONE);
            rl_gouwuche_kong.setVisibility(View.VISIBLE);
        }
        calculate();
        pd_change();
    }

    private void pd_change() {
        if (ConstantUtlis.CHECK_ADD_GOUWUCHE) {
//            tv_fg2_privce.setText("0.0");
            if ("成功".equals(preferences.getString(ConstantUtlis.SP_LOGINTYPE, ""))) {
                requestHandleArrayList.add(requestAction.shop_c_clists(this));
            } else {
                exListView.setVisibility(View.GONE);
                rl_gouwuche_kong.setVisibility(View.VISIBLE);
                rl_gouwuche_foot.setVisibility(View.GONE);
            }
            ConstantUtlis.CHECK_ADD_GOUWUCHE = false;
        }
    }

    private void queren_dingdan() {
        GroupInfoBean groupInfoBean = new GroupInfoBean();
        QuerendingdanBean querendingdanBean = null;
        List<QuerendingdanBean> list = new ArrayList<>();
        List<ShangpinBean> shangPinList = new ArrayList<>();
        //选中的对象放入集合中
        for (int i = 0; i < groups.size(); i++) {
            boolean flag = false;
            GroupInfoBean group = groups.get(i);

            Log.e("groups", groups.size() + "--");
            List<ShangpinBean> childs = children.get(group.getId());
            toBeZhifuDingdan = new ArrayList<>();
            querendingdanBean = new QuerendingdanBean();
            for (int j = 0; j < childs.size(); j++) {
                if (childs.get(j).isChoosed()) {
                    toBeZhifuDingdan.add(childs.get(j));
                    shangPinList.add(childs.get(j));
                    groupInfoBean = group;
                    flag = true;
                }
            }
            querendingdanBean.setGroupInfoBean(groupInfoBean);
            querendingdanBean.setToBeZhifuDingdan(toBeZhifuDingdan);

            if (flag)
                list.add(querendingdanBean);
        }

        //判断商品类型（自营，全球购）
        boolean quanqiugou_flag = false;
        boolean ziying_flag = false;
        boolean huodongshangpin = false;
        for (int k = 0; k < shangPinList.size(); k++) {
            String leixin = shangPinList.get(k).getShangpin_leixing();
            Log.e("leixin=", leixin);
            switch (leixin) {
                case "全球购-自营":
                case "全球购":
                    quanqiugou_flag = true;
                    break;
                case "自营":
                case "":
                    ziying_flag = true;
                    break;
            }

            if ("是".equals(shangPinList.get(k).getIsHuoDongShangPin())) {
                huodongshangpin = true;
            }
        }

        Log.e("00-000-", quanqiugou_flag + "   " + ziying_flag);
        if (quanqiugou_flag && ziying_flag) {
            DialogUtlis.oneBtnNormal(getmDialog(), "选中商品包含“全球购”商品，请分别下单！");
        } else if (huodongshangpin && shangPinList.size() > 1) {
            DialogUtlis.oneBtnNormal(getmDialog(), "选中商品包含活动商品，请分开下单！");
        } else if (quanqiugou_flag) {
            Intent intent = new Intent(mContext, QQGQueRenDingDanActivity.class);
            intent.putExtra("类型", "多个");
            intent.putExtra("querendingdanList", (Serializable) list);
            intent.putExtra("totalPrice", String.valueOf(totalPrice));
            startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, QuerenDingdanActivity.class);
            intent.putExtra("类型", "多个");
            intent.putExtra("querendingdanList", (Serializable) list);
            intent.putExtra("totalPrice", String.valueOf(totalPrice));
            startActivity(intent);
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        switch (requestTag) {
            case RequestAction.TAG_SHOPLISTS:
                JSONArray jsonArray;
                Log.e("查询购物车", response.toString());
                if (response.getString("状态").equals("成功")) {
                    ConstantUtlis.CHECK_ADD_GOUWUCHE = false;
                    if (response.getInt("classifyCount") > 0) {
                        //添加购物车到此已加载
                        rl_gouwuche_kong.setVisibility(View.GONE);
                        rl_gouwuche_foot.setVisibility(View.VISIBLE);
                        exListView.setVisibility(View.VISIBLE);
                        groups.clear();
                        children.clear();
                        jsonArray = response.getJSONArray("classify");
                        Log.e("jsonArray", jsonArray.length() + "----");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            response = (JSONObject) jsonArray.get(i);

                            GroupInfoBean groupInfoBean = new GroupInfoBean();
                            groupInfoBean.setDianpuming(response.getString("store"));
                            groupInfoBean.setId(i + "");
                            groups.add(groupInfoBean);

                            JSONArray shangpin_jsonArray = response.getJSONArray("lists");
                            List<ShangpinBean> shangpinlist = new ArrayList<>();
                            for (int j = 0; j < shangpin_jsonArray.length(); j++) {
                                response = (JSONObject) shangpin_jsonArray.get(j);
                                ShangpinBean shangpin = new ShangpinBean(
                                        response.getString("id"),
                                        response.getString("商品id"),
                                        response.getString("商品名称"),
                                        response.getString("商品规格"),
                                        Integer.parseInt(response.getString("商品数量")),
                                        Double.parseDouble(response.getString("单价")),
                                        response.getString("缩略图"),
                                        Double.parseDouble(response.getString("快递费")),
                                        response.getString("库存"),
                                        j,
                                        i,
                                        response.getString("类型"),
                                        response.getString("是否参与活动"),
                                        response.getString("活动抵扣率"));
                                Log.e("shangpin", shangpin.toString());
                                shangpinlist.add(shangpin);
                                children.put(i + "", shangpinlist);
                            }
                        }
                        cb_fg2_check_all.setChecked(false);
                        selva = new ShopcartExpandableListViewAdapter(groups, children, mContext);
                        selva.setCheckInterface(this);//关键步骤1,设置复选框接口
                        selva.setModifyCountInterface(this);//关键步骤2,设置数量增减接口
                        exListView.setAdapter(selva);
                        for (int i = 0; i < selva.getGroupCount(); i++) {
                            exListView.expandGroup(i);//关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
                        }
                    } else {
                        rl_gouwuche_foot.setVisibility(View.GONE);
                        rl_gouwuche_kong.setVisibility(View.VISIBLE);
                    }
                }
                calculate();
                break;
            case RequestAction.TAG_SHOP_C_DELETE:
                if (response.getString("状态").equals("成功")) {
                    fg2_doDelete();
                } else {
                    DialogUtlis.oneBtnNormal(getmDialog(), response.getString("状态"));
                }
                break;
            case RequestAction.TAG_SHOP_C_CARTNUM:
                if (response.getString("状态").equals("成功")) {
                    change_shuliang();
                } else {
                    DialogUtlis.oneBtnNormal(getmDialog(), response.getString("状态"));
                }
        }
    }

    //请求成功后修改商品数量
    private void change_shuliang() {
        ShangpinBean shangpin = (ShangpinBean) selva.getChild(shuliang_groupPosition, shuliang_childPosition);
        int currentCount = shangpin.getShangpin_shuliang();
        if (gouwuche_jia) {
            currentCount++;
        } else {
            currentCount--;
        }
        shangpin.setShangpin_shuliang(currentCount);
        ((TextView) shuliang_showCountView).setText(currentCount + "");
        selva.notifyDataSetChanged();
        calculate();
    }

    /**
     * 删除操作<br>
     * 1.不要边遍历边删除，容易出现数组越界的情况<br>
     * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
     */
    protected void fg2_doDelete() {
        List<GroupInfoBean> toBeDeleteGroups = new ArrayList<>();//待删除的组元素列表
        for (int i = 0; i < groups.size(); i++) {
            GroupInfoBean groupInfoBean = groups.get(i);
            List<ShangpinBean> toBeDeletechildrens = new ArrayList<>();//待删除的子元素列表
            List<ShangpinBean> childs = children.get(groupInfoBean.getId());
            for (int j = 0; j < childs.size(); j++) {
                if (childs.size() == 1 && i == mygroupPosition && j == mychildPosition) {
                    toBeDeleteGroups.add(groupInfoBean);
                    exListView.collapseGroup(i);
                    Log.e("---", i + "");
                }
                if (i == mygroupPosition && j == mychildPosition) {
                    toBeDeletechildrens.add(childs.get(j));
                }
            }
            //删除父节点下需要删除的子元素
            childs.removeAll(toBeDeletechildrens);
            //如果子节点长度为0 删除map的值
            if (childs.size() == 0) {
                children.remove(groupInfoBean.getId());
            } else {
                children.put(groupInfoBean.getId(), childs);
            }
        }
        groups.removeAll(toBeDeleteGroups);
        Log.e("groups", groups.size() + "---");
        if (groups.size() == 0) {
            exListView.setVisibility(View.GONE);
            rl_gouwuche_foot.setVisibility(View.GONE);
            rl_gouwuche_kong.setVisibility(View.VISIBLE);
        }
        if (mygroupPosition < groups.size()) {
            List<ShangpinBean> mychildrens = children.get(groups.get(mygroupPosition).getId());
            boolean childrens_checkall = true;
            for (int i = 0; i < mychildrens.size(); i++) {
                if (!mychildrens.get(i).isChoosed()) {
                    childrens_checkall = false;
                    break;
                }
            }
            if (childrens_checkall) {
                checkGroup(mygroupPosition, childrens_checkall);
            }
        }
        mycheckall = true;
        selva.notifyDataSetChanged();
        for (int i = 0; i < selva.getGroupCount(); i++) {
            exListView.collapseGroup(i);
            exListView.expandGroup(i);
        }
        calculate();
        isCheckAll();
    }


    /**
     * 全选与反选
     */
    private void doCheckAll() {
        if (cb_fg2_check_all.isChecked()) {
            ischeckall_to_checkgroup = true;
        }
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setChoosed(cb_fg2_check_all.isChecked());
            Log.e("isChecked()", cb_fg2_check_all.isChecked() + "--");
            Log.e("groups" + i, groups.get(i).isChoosed() + "--");
            checkGroup(i, cb_fg2_check_all.isChecked());
        }
        selva.notifyDataSetChanged();
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        GroupInfoBean group = groups.get(groupPosition);
        List<ShangpinBean> childs = children.get(group.getId());
        Log.e("---groupPosition", groupPosition + "--" + isChecked);
        Log.e("---heckgroup", ischeckall_to_checkgroup + "");
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).setChoosed(isChecked);
        }
        groups.get(groupPosition).setChoosed(isChecked);


        if (ischeckall_to_checkgroup) {
            if (groupPosition == groups.size() - 1) {
                mycheckall = true;
                ischeckall_to_checkgroup = false;
            }
        } else {
            mycheckall = true;
        }
        selva.notifyDataSetChanged();
        calculate();
        isCheckAll();
    }

    @Override
    public void checkChild(int groupPosition, int childPosiTion, boolean isChecked) {
        boolean allChildSameState = true;//判断改组下面的所有子元素是否是同一种状态
        GroupInfoBean group = groups.get(groupPosition);
        List<ShangpinBean> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            if (childs.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }

        if (allChildSameState) {
            group.setChoosed(isChecked);//如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            group.setChoosed(false);//否则，组元素一律设置为未选中状态
        }
        selva.notifyDataSetChanged();
        mycheckall = true;
        isCheckAll();
        calculate();
    }

    //添加商品数量
    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        shuliang_groupPosition = groupPosition;
        shuliang_childPosition = childPosition;
        shuliang_showCountView = showCountView;
        gouwuche_jia = true;
        ShangpinBean shangpin = (ShangpinBean) selva.getChild(groupPosition, childPosition);
        int currentCount = shangpin.getShangpin_shuliang();
        int kucunCount = Integer.parseInt(shangpin.getKucun());
        String str_currentCount = String.valueOf(currentCount + 1);
        Log.e("----加", currentCount + "--" + ((TextView) shuliang_showCountView).getText().toString());
        //如果当前数量小于库存执行增加
        if (currentCount < kucunCount) {
            requestHandleArrayList.add(requestAction.shop_c_cartNum(this, shangpin.getId(), str_currentCount));
        } else {
            MyToast.getInstance().showToast(mContext, "超出商品库存添加失败!");
        }
    }

    //减少商品数量
    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        shuliang_groupPosition = groupPosition;
        shuliang_childPosition = childPosition;
        shuliang_showCountView = showCountView;
        gouwuche_jia = false;
        ShangpinBean shangpin = (ShangpinBean) selva.getChild(groupPosition, childPosition);
        int currentCount = shangpin.getShangpin_shuliang();
        Log.e("----减", currentCount + "");
        //如果当前数量大于1执行减少
        if (currentCount > 1) {
            String str_currentCount = String.valueOf(currentCount - 1);
            requestHandleArrayList.add(requestAction.shop_c_cartNum(this, shangpin.getId(), str_currentCount));
        }
//        else {
//            MyToast.getInstance().showToast(mContext, "再减就没有数量啦,亲!");
//        }
    }

    @Override
    public void doDelete(final int groupPosition, final int childPosition, View showCountView) {
        Log.e("组 子", "组" + groupPosition + "子" + childPosition);
        mygroupPosition = groupPosition;
        mychildPosition = childPosition;
        alert = new AlertDialog.Builder(mContext).create();
        alert.setTitle("操作提示");
        alert.setMessage("您确定要将这个商品从购物车中移除吗？");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //待删除的子元素列表
                ArrayList<String> ids = new ArrayList<>();
//                for (int i = 0; i < groups.size(); i++) {
//                    GroupInfo group = groups.get(i);
//                    List<ShangpinBean> childs = children.get(group.getId());
//                    for (int j = 0; j < childs.size(); j++) {
//                        if (i == mygroupPosition && j == mychildPosition) {
//                            ids.add(childs.get(j).getId());
//                        }
//                    }
//                }
                ShangpinBean shangpin = (ShangpinBean) selva.getChild(groupPosition, childPosition);
                ids.add(shangpin.getId());
                deleteShangPin(ids);
            }
        });
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alert.show();
    }

    @Override
    public void GoToShangPinXiangQing(int groupPosition, int childPosition, View showCountView) {
        Log.e("组 子", "组" + groupPosition + "子" + childPosition);
        mygroupPosition = groupPosition;
        mychildPosition = childPosition;

        Intent intent = new Intent(GouWuCheActivity.this, ShangPinXiangQingActivity.class);
        intent.putExtra("id", ((ShangpinBean) selva.getChild(groupPosition, childPosition)).getShangpin_id());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void deleteShangPin(ArrayList<String> ids) {
        requestHandleArrayList.add(requestAction.shop_c_delete(this, ids));
    }

    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < groups.size(); i++) {
            GroupInfoBean group = groups.get(i);
            List<ShangpinBean> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                ShangpinBean shangpin = childs.get(j);
                if (shangpin.isChoosed()) {
                    Log.e("calculate  isChoosed()", "i = " + i + "   j = " + j);
                    totalCount++;
                    totalPrice += shangpin.getDanjia() * shangpin.getShangpin_shuliang();
                }
            }
        }
        tv_fg2_privce.setText("￥" + df.format(totalPrice));
    }

    /*
     * 判断所有组元素有没有被选中
     */
    private void isCheckAll() {
        boolean isall = true;
        for (int i = 0; i < groups.size(); i++) {
            //判断所有组元素为选中
            if (!groups.get(i).isChoosed()) {
                isall = false;
                break;
            }
        }
        Log.e("--isall", isall + "");
        if (mycheckall) {
            if (isall) {
                cb_fg2_check_all.setChecked(true);
            } else {
                cb_fg2_check_all.setChecked(false);
            }
            mycheckall = false;
        }
    }
}
