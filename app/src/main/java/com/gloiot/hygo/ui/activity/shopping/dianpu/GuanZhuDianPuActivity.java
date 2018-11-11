package com.gloiot.hygo.ui.activity.shopping.dianpu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.GuanZhuDianPuBean;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zyd.wlwsdk.adapter.recyclerview.OnItemClickListener1;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.PictureUtlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hygo03 on 2017/8/23.
 */

public class GuanZhuDianPuActivity extends BaseActivity {

    @Bind(R.id.toptitle_more)
    TextView toptitleMore;
    @Bind(R.id.swipeMenuRecyclerView)
    SwipeMenuRecyclerView swipeMenuRecyclerView;
    @Bind(R.id.srl_swipeRefreshLayout)
    SwipeRefreshLayout srlSwipeRefreshLayout;

    private int tiaoshu = 0, yeshu = 0;
    private List<GuanZhuDianPuBean> listData = new ArrayList<>();
    private GuanZhuDianPuAdapter guanZhuDianPuAdapter;
    private boolean cehua_flag = false;
    private int delete_position = 0;
    private boolean isLoading = false;
    private int dataNum = 0;
//    private boolean first_flag = false;

    @Override
    public int initResource() {
        return R.layout.activity_guanzhudianpu;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "关注店铺", "", true);
        initSwipeMenuRecyclerView();
//        requestData("", "", "", "");

        guanZhuDianPuAdapter.setOnItemClickListener(new OnItemClickListener1() {
            @Override
            public void onItemClick(int position) {
                if (check_login_tiaozhuang()) {
                    Intent intent = new Intent(GuanZhuDianPuActivity.this, ShangJiaDianPuActivity.class);
                    intent.putExtra("店铺id", listData.get(position).getDianpu_id());
                    startActivity(intent);
                }
            }
        });

        //刷新
        srlSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                yeshu = 0;
                listData.clear();
                dataNum = 0;
                guanZhuDianPuAdapter.notifyDataSetChanged();
                requestData("", "", "", "");
            }
        });

        //加载
        swipeMenuRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

//                if (newState == SwipeMenuRecyclerView.SCROLL_STATE_IDLE
//                        && lastVisibleItem + 1 == shouCangAdapter.getItemCount()) {
//                    if(Loadlength<10&&XiayiyeYeshu>0){
//                        ToastUtils.shopToast(mContext,"已无数据",false);
//                    }
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) swipeMenuRecyclerView.getLayoutManager();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (lastVisibleItem + 1 == guanZhuDianPuAdapter.getItemCount()) {
                    boolean isRefreshing = srlSwipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
                        guanZhuDianPuAdapter.notifyItemRemoved(guanZhuDianPuAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        if (tiaoshu > 0) {
                            requestData("","","",yeshu+"");
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(first_flag) {
            listData.clear();
            requestData("", "", "", "");
//        }
    }

    /**
     * 初始化SwipeMenuRecyclerView
     */
    public void initSwipeMenuRecyclerView() {
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        swipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        swipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        swipeMenuRecyclerView.addItemDecoration(new SimplePaddingDecoration(mContext));// 添加分割线。
        // 设置菜单创建器。
        swipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        guanZhuDianPuAdapter = new GuanZhuDianPuAdapter(mContext, listData);
        swipeMenuRecyclerView.setAdapter(guanZhuDianPuAdapter);
        swipeMenuRecyclerView.setSwipeMenuItemClickListener(swipeMenuItemClickListener);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_HP_FOLLOW:
//                first_flag = true;
                if (cehua_flag) {
                    listData.remove(delete_position);
                    guanZhuDianPuAdapter.notifyItemRemoved(delete_position);
                    cehua_flag = false;
                } else {
                    tiaoshu = response.getInt("条数");
                    yeshu = Integer.parseInt(response.getString("页数"));
                    isLoading = false;
                    srlSwipeRefreshLayout.setRefreshing(false);
                    if (tiaoshu > 0) {
                        JSONArray jsonArray = response.getJSONArray("列表");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            GuanZhuDianPuBean guanZhuDianPuBean = new GuanZhuDianPuBean();
                            guanZhuDianPuBean.setDianpu_id(jsonObject.getString("店铺id"));
                            guanZhuDianPuBean.setDianpu_name(jsonObject.getString("店铺名称"));
                            guanZhuDianPuBean.setDianpu_touxiang(jsonObject.getString("店铺图标"));
                            listData.add(guanZhuDianPuBean);
                        }
                        guanZhuDianPuAdapter.notifyDataSetChanged();
                    }
                }
                dataNum = listData.size();
                CommonUtlis.setTitleBar(this, "关注店铺(" + dataNum + ")", "", true);
                break;
        }
    }

    /**
     * 请求数据
     */
    private void requestData(String id, String is, String no, String yeshu) {
        requestHandleArrayList.add(requestAction.shop_hp_follow(GuanZhuDianPuActivity.this, id, is, no, yeshu));
    }

    private OnSwipeMenuItemClickListener swipeMenuItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            // TODO 如果是删除：推荐调用Adapter.notifyItemRemoved(position)，不推荐Adapter.notifyDataSetChanged();
            if (menuPosition == 0) {// 删除按钮被点击。
                cehua_flag = true;
                delete_position = adapterPosition;
                requestData(listData.get(adapterPosition).getDianpu_id(), "", "是", "");
            }

        }
    };
    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

//            int width = getResources().getDimensionPixelSize(R.dimen.item_height);
            int width = 200;//单位为px,没有适配性

            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                    .setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF6D63")))
                    .setText("取消关注") // 文字，还可以设置文字颜色，大小等。。
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
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


    /**
     * 适配器
     */
    public class GuanZhuDianPuAdapter extends SwipeMenuAdapter<GuanZhuDianPuAdapter.ViewHolder> {
        private Context mContext;
        private List<GuanZhuDianPuBean> listData;
        private OnItemClickListener1 mOnItemClickListener;

        public GuanZhuDianPuAdapter(Context context, List<GuanZhuDianPuBean> list) {
            this.mContext = context;
            this.listData = list;
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
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guanzhudianpu, parent, false);
        }


        @Override
        public ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
            return new ViewHolder(realContentView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.tv_name.setText(listData.get(position).getDianpu_name());
            PictureUtlis.loadImageViewHolder(mContext, listData.get(position).getDianpu_touxiang(), R.drawable.default_image, holder.tupian);
            holder.setOnItemClickListener(mOnItemClickListener);

        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tv_name;
            ImageView tupian;
            OnItemClickListener1 mOnItemClickListener;


            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                tv_name = (TextView) itemView.findViewById(R.id.tv_dianpu_name);
                tupian = (ImageView) itemView.findViewById(R.id.iv_dianpu_touxiang);
            }

            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(getAdapterPosition());
                }
            }

            public void setOnItemClickListener(OnItemClickListener1 onItemClickListener) {
                this.mOnItemClickListener = onItemClickListener;
            }
        }
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        super.onFailure(requestTag, errorResponse, showLoad);
        isLoading = false;
        srlSwipeRefreshLayout.setRefreshing(false);
    }
}
