package com.gloiot.hygo.ui.activity.shopping.wodeshoucang;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShangPinShouCangInfo;
import com.gloiot.hygo.ui.adapter.ShouCangAdapter;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zyd.wlwsdk.adapter.recyclerview.OnItemClickListener1;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShouCangActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_choucang_meishangpin)
    TextView tv_choucang_meishangpin;
    @Bind(R.id.iv_choucang_meishangpin)
    ImageView iv_choucang_meishangpin;

    //点击编辑页面需要的控件
    @Bind(R.id.bt_shoucang_bianji_shanchu)
    Button bt_shoucang_bianji_shanchu;
    @Bind(R.id.toptitle_more)
    TextView toptitle_more;
    @Bind(R.id.swipeMenuRecyclerView)
    SwipeMenuRecyclerView swipeMenuRecyclerView;
    @Bind(R.id.srl_swipeRefreshLayout)
    SwipeRefreshLayout srlSwipeRefreshLayout;

    private List<ShangPinShouCangInfo> listData = new ArrayList<>();
    private List<String> deleteList = new ArrayList<>(5);
    private ShouCangAdapter shouCangAdapter;

    private int Loadlength, XiayiyeYeshu;
    private boolean isLoading;
    private int dataNum = 0;

    //辨别当前是否在执行删除操作中...
    //true 为正在删除操作中。
    //false 为没有在执行删除操作。
    private boolean Flag = false;
    private int deletePosition = -1;
    private boolean cehua_Flag = false;

    @Override
    public int initResource() {
        return R.layout.shoucan_activity;
    }

    @Override
    public void initData() {
        initComponent();
        initShouCangData();
        initSwipeMenuRecyclerView();
        changeViewVisibility();

        srlSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        swipeMenuRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) swipeMenuRecyclerView.getLayoutManager();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (lastVisibleItem + 1 == shouCangAdapter.getItemCount()) {
                    boolean isRefreshing = srlSwipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
                        shouCangAdapter.notifyItemRemoved(shouCangAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        if (Loadlength > 0) {
                            requestHandleArrayList.add(requestAction.shop_wodeshoucang(ShouCangActivity.this, XiayiyeYeshu + ""));
                        }
                    }
                }
            }
        });


    }

    public void initShouCangData() {
        if ("成功".equals(preferences.getString(ConstantUtlis.SP_LOGINTYPE, "")))
            requestHandleArrayList.add(requestAction.shop_wodeshoucang(this, "0"));
    }

    public void initSwipeMenuRecyclerView() {
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        swipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        swipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        swipeMenuRecyclerView.addItemDecoration(new SimplePaddingDecoration(mContext));// 添加分割线。
        // 设置菜单创建器。
        swipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        shouCangAdapter = new ShouCangAdapter(mContext, listData, deleteList) {
            @Override
            public void deleteMySelect() {
                super.deleteMySelect();
                deleteList.clear();
                if (listData != null && listData.size() > 0) {
                    for (int i = listData.size() - 1; i >= 0; i--) {
                        if (xuanzhong.get(i) == 1) {
                            deleteList.add(listData.get(i).getIv_item_shangpinshoucang_id());
                        }
                    }
                }
                DeleteShangPin(deleteList);

            }
        };
        swipeMenuRecyclerView.setAdapter(shouCangAdapter);
        swipeMenuRecyclerView.setSwipeMenuItemClickListener(swipeMenuItemClickListener);
    }

    public void initComponent() {
        CommonUtlis.setTitleBar(this, "收藏夹", "编辑", true);
        tv_choucang_meishangpin.setVisibility(View.GONE);
        iv_choucang_meishangpin.setVisibility(View.GONE);
    }

    /**
     * 刷新
     */
    public SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            XiayiyeYeshu = 0;
            listData.clear();
            dataNum = 0;
            shouCangAdapter.notifyDataSetChanged();
            initShouCangData();
        }
    };

    /**
     * 空白页的显示
     */
    private void changeViewVisibility() {
        if (listData.size() != 0) {
            tv_choucang_meishangpin.setVisibility(View.GONE);
            iv_choucang_meishangpin.setVisibility(View.GONE);
            toptitle_more.setVisibility(View.VISIBLE);
        } else {
            tv_choucang_meishangpin.setVisibility(View.VISIBLE);
            iv_choucang_meishangpin.setVisibility(View.VISIBLE);
            toptitle_more.setVisibility(View.GONE);
        }
    }

    //删除收藏的商品
    private void DeleteShangPin(List<String> list) {
        if (list.size() == 0) {
            //删除操作结束标示
            Flag = false;
            return;
        } else
            requestHandleArrayList.add(requestAction.shop_wodeshoucang_delete(this, list));
    }

    /**
     * 当数据为空时
     */
    public void ListDataEmpty() {
        tv_choucang_meishangpin.setVisibility(View.VISIBLE);
        iv_choucang_meishangpin.setVisibility(View.VISIBLE);
        bt_shoucang_bianji_shanchu.setVisibility(View.GONE);
        toptitle_more.setVisibility(View.GONE);
    }


    /**
     * 点击编辑，页面动态修改
     */
    private void BianJiView() {
        CommonUtlis.setTitleBar(this, "收藏夹" + "(" + dataNum + ")", "全部", true);

        shouCangAdapter.type = "全部";
        shouCangAdapter.notifyDataSetChanged();

        shouCangAdapter.setOnItemClickListener(new OnItemClickListener1() {
            @Override
            public void onItemClick(int position) {
                shouCangAdapter.changeSelect(position);
            }
        });
        bt_shoucang_bianji_shanchu.setVisibility(View.VISIBLE);
    }

    /**
     * 点击编辑，页面动态修改
     */
    private void BianJiViewFnhui() {
        CommonUtlis.setTitleBar(this, "收藏夹", "编辑", true);

        shouCangAdapter.type = "编辑";
        shouCangAdapter.notifyDataSetChanged();
        XiayiyeYeshu = 0;
        bt_shoucang_bianji_shanchu.setVisibility(View.GONE);
    }

    @OnClick({R.id.bt_shoucang_bianji_shanchu, R.id.toptitle_more})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toptitle_more:
                if ("全部".equals(toptitle_more.getText())) {
                    shouCangAdapter.changeSelectAll();
                } else {
                    BianJiView();
                }
                break;

            case R.id.bt_shoucang_bianji_shanchu:
                if (!Flag) {
                    Flag = true;
                    shouCangAdapter.deleteMySelect();
                    BianJiViewFnhui();
                } else {
                    L.e("------", "正在执行删除操作    2");
                    return;
                }
                break;
        }
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        switch (requestTag) {
            case RequestAction.TAG_WODESHOUCANG_SHOUCANGLIEBIAO:
                ConstantUtlis.CHECK_CHANGE_SHOUCANG = true;
                isLoading = false;
                if (!isLoading) {
//                    if(response.getInt("条数")<10&&response.getInt("页数")>0){
//                        ToastUtils.shopToast(mContext,"已无数据",false);
//                    }
//                    ToastUtils.shopToast(mContext,"加载成功",false);
                } else {
                    listData.clear();
                    shouCangAdapter.notifyDataSetChanged();
                }
                ShangPinShouCangInfo shangPinShouCangInfo;

                srlSwipeRefreshLayout.setRefreshing(false);

                if ("成功".equals(response.getString("状态"))) {
                    Loadlength = response.getInt("条数");

                    if (response.getInt("条数") > 0) {
                        XiayiyeYeshu = response.getInt("页数");
                        JSONArray jsonArray = new JSONArray(response.getString("列表"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject info = jsonArray.getJSONObject(i);
                            shangPinShouCangInfo = new ShangPinShouCangInfo();
                            shangPinShouCangInfo.setIv_item_shangpinshoucang_id(info.getString("商品id"));
                            shangPinShouCangInfo.setTv_item_shangpinshoucang_pirce(info.getString("价格"));
                            shangPinShouCangInfo.setTv_item_shangpinshoucang_title(info.getString("商品名称"));
                            shangPinShouCangInfo.setIv_item_shangpinshoucang_tupian_url(info.getString("缩略图"));
                            shangPinShouCangInfo.setTv_item_shangpinshoucang_leixing(info.getString("类型"));
                            if(!info.isNull("商品状态")){
                                shangPinShouCangInfo.setTv_item_shangpinshoucang_zhuantai(JSONUtlis.getString(info,"商品状态"));
                            }else {
                                shangPinShouCangInfo.setTv_item_shangpinshoucang_zhuantai("");
                            }
                            listData.add(shangPinShouCangInfo);
                        }
                    }

                    changeViewVisibility();
                    shouCangAdapter.type = toptitle_more.getText().toString();
                    shouCangAdapter.notifyDataSetChanged();

                    dataNum =listData.size();
                    if (toptitle_more.getText().equals("编辑")){
                        CommonUtlis.setTitleBar(this, "收藏夹" + "(" + dataNum + ")", "编辑", true);
                    }else {
                        CommonUtlis.setTitleBar(this, "收藏夹" + "(" + dataNum + ")", "全部", true);
                    }
                }
                break;

            case RequestAction.TAG_WODESHOUCANG_SHANCHU:
                ConstantUtlis.CHECK_CHANGE_SHOUCANG = true;

                if (cehua_Flag) {
                    listData.remove(deletePosition);
                    shouCangAdapter.notifyItemRemoved(deletePosition);
                    cehua_Flag = false;
                } else {
                    for (int i = 0; i < deleteList.size(); i++) {
                        for (int j = listData.size() - 1; j >= 0; j--) {
                            if (deleteList.get(i).equals(listData.get(j).getIv_item_shangpinshoucang_id()))
                                listData.remove(j);
                        }
                    }
                    shouCangAdapter.notifyDataSetChanged();
                }

                dataNum = listData.size();
                if (toptitle_more.getText().equals("编辑")){
                    CommonUtlis.setTitleBar(this, "收藏夹" + "(" + dataNum + ")", "编辑", true);
                }else {
                    CommonUtlis.setTitleBar(this, "收藏夹" + "(" + dataNum + ")", "全部", true);
                }

                if (listData.isEmpty()) {
                    ListDataEmpty();
                }

                Flag = false;
                //清空删除集合
                deleteList.clear();
                //清空选中状态
                for (int i = 0; i < listData.size() - 1; i++) {
                    shouCangAdapter.xuanzhong.set(i, 0);
                }

                break;
        }
    }


    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

//            int width = getResources().getDimensionPixelSize(R.dimen.divider_height);
            int width = 200;//单位为px,没有适配性

            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                    .setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF6D63")))
                    .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
        }

    };

    private OnSwipeMenuItemClickListener swipeMenuItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            // TODO 如果是删除：推荐调用Adapter.notifyItemRemoved(position)，不推荐Adapter.notifyDataSetChanged();
            if (menuPosition == 0) {// 删除按钮被点击。
                deletePosition = adapterPosition;
                deleteList = new ArrayList<>();
                deleteList.add(listData.get(adapterPosition).getIv_item_shangpinshoucang_id());
                cehua_Flag = true;
                DeleteShangPin(deleteList);
            }

        }
    };

    /**
     * 分割线
     */
    public class SimplePaddingDecoration extends RecyclerView.ItemDecoration {
        private int dividerHeight;

        public SimplePaddingDecoration(Context context) {
            dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.divider_height);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.top = dividerHeight;//类似加了一个bottom padding }
        }
    }


}
