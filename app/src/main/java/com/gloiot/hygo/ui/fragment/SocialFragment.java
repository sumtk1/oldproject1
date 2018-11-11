package com.gloiot.hygo.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gloiot.chatsdk.DataBase.DataBaseCallBack;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.MessageManager;
import com.gloiot.chatsdk.bean.ConversationBean;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseFragment;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.ui.activity.social.ConversationActivity;
import com.gloiot.hygo.ui.activity.social.SystemMessageActivity;
import com.gloiot.hygo.ui.activity.social.WuLiuMessageActivity;
import com.gloiot.hygo.ui.activity.social.ZhangDanMessageActivity;
import com.gloiot.hygo.ui.adapter.SocialMessageAdapter;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zyd.wlwsdk.adapter.recyclerview.OnItemClickListener1;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 社交Fragment
 */
public class SocialFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    SwipeMenuRecyclerView mSwipeMenuRecyclerView;
    @Bind(R.id.ll_nodata)
    LinearLayout llNodata;
    @Bind(R.id.view_status_bar)
    View viewStatusBar;
    @Bind(R.id.tv_network_error)
    TextView tvNetworkError;

    private List<ConversationBean> list = new ArrayList<>(2);
    private SocialMessageAdapter mMessageAdapter;
    private boolean xitong_flag = false;
    private boolean wuliu_flag = false;
    private boolean zhangdan_flag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social, container, false);
        ButterKnife.bind(this, view);
        initData();
        BroadcastManager.getInstance(mContext).addAction(MessageManager.LINK_CHANGED, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (check_login()) {
                    try {
                        switch (intent.getStringExtra("data")) {
                            case MessageManager.LINK_CHANGED_SUCCEED:
                                tvNetworkError.setVisibility(View.GONE);
                                break;
                            case MessageManager.LINK_CHANGED_FAULT:
                                tvNetworkError.setVisibility(View.VISIBLE);
                                break;
                            case MessageManager.LINK_CHANGED_DONW:
                                tvNetworkError.setVisibility(View.VISIBLE);
                                break;
                            default:
                        }
                    } catch (Exception e) {

                    }
                }
            }
        });
        BroadcastManager.getInstance(mContext).addAction(MessageManager.NEW_MESSAGE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getData();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("---onResume111", "onResume");
        if (check_login()) {
            getData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mSwipeMenuRecyclerView.smoothCloseMenu();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && mSwipeMenuRecyclerView != null)
            mSwipeMenuRecyclerView.smoothCloseMenu();
    }

    private void initData() {
        processData();
        getData();
        if (Build.VERSION.SDK_INT >= 21) {
            viewStatusBar.setVisibility(View.VISIBLE);
        }
    }

    private void getData() {
        if (check_login()) {
            try {
                List<ConversationBean> conversationBeanList = IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).queryConversationList();
                llNodata.setVisibility(View.GONE);
                mSwipeMenuRecyclerView.setVisibility(View.VISIBLE);
                list.clear();
                ConversationBean conversationBean1 = new ConversationBean();
                conversationBean1.setExtra("");
                conversationBean1.setMessage("");
                conversationBean1.setSendTime("");
                conversationBean1.setSessiontype("系统消息");
                conversationBean1.setPushdata("暂无消息");
                conversationBean1.setSendid("");
                conversationBean1.setReceiveid("");
                list.add(0, conversationBean1);
                ConversationBean conversationBean2 = new ConversationBean();
                conversationBean2.setExtra("");
                conversationBean2.setMessage("");
                conversationBean2.setSendTime("");
                conversationBean2.setSessiontype("物流消息");
                conversationBean2.setPushdata("暂无消息");
                conversationBean2.setSendid("");
                conversationBean2.setReceiveid("");
                list.add(1, conversationBean2);

                ConversationBean conversationBean3 = new ConversationBean();
                conversationBean3.setExtra("");
                conversationBean3.setMessage("");
                conversationBean3.setSendTime("");
                conversationBean3.setSessiontype("账单通知");
                conversationBean3.setPushdata("暂无消息");
                conversationBean3.setSendid("");
                conversationBean3.setReceiveid("");
                list.add(2, conversationBean3);

                Log.e("00000000", conversationBeanList.toString());
                for (final ConversationBean conversationBean : conversationBeanList) {
                    switch (conversationBean.getSessiontype()) {
                        case "系统消息":
                            list.remove(0);
                            list.add(0, conversationBean);
                            xitong_flag = true;
                            continue;
                        case "物流消息":
                            list.remove(1);
                            list.add(1, conversationBean);
                            wuliu_flag = true;
                            continue;
                        case "账单通知":
                            list.remove(2);
                            list.add(2, conversationBean);
                            zhangdan_flag = true;
                            continue;
                        case "单聊":
                            list.add(conversationBean);
                            break;
                    }
                }
            } catch (Exception e) {
                Log.e("Exception", "" + e);
            }
        } else {
            llNodata.setVisibility(View.VISIBLE);
            mSwipeMenuRecyclerView.setVisibility(View.GONE);
        }
        mMessageAdapter.notifyDataSetChanged();
    }

    private void processData() {
        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));// 布局管理器。
        mSwipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mSwipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
//      mSwipeMenuRecyclerView.addItemDecoration(new SimplePaddingDecoration(mContext));// 添加分割线。

        // 为SwipeRecyclerView的Item创建菜单
        // 设置菜单创建器。
        mSwipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);

        mMessageAdapter = new SocialMessageAdapter(getActivity(), list, preferences.getString(ConstantUtlis.SP_USERPHONE, ""));
        mMessageAdapter.setOnItemClickListener(onItemClickListener);
        mSwipeMenuRecyclerView.setAdapter(mMessageAdapter);
    }

    /**
     * 条目点击监听
     */
    private OnItemClickListener1 onItemClickListener = new OnItemClickListener1() {
        @Override
        public void onItemClick(final int position) {

            //清除未读数
            IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).NoReadNumClean(list.get(position).getSendid(), new DataBaseCallBack() {
                @Override
                public void operationState(boolean flag) {
                    getData();
                    mMessageAdapter.notifyItemChanged(position);
                }
            });
            switch (position) {
                case 0:
                    if (xitong_flag) {
                        startActivity(new Intent(mContext, SystemMessageActivity.class).putExtra("data", "yes"));
                    } else {
                        startActivity(new Intent(mContext, SystemMessageActivity.class).putExtra("data", "no"));
                    }
                    ((MainActivity) getActivity()).getBadge();
                    break;
                case 1:
                    if (wuliu_flag) {
                        startActivity(new Intent(mContext, WuLiuMessageActivity.class).putExtra("data", "yes"));
                    } else {
                        startActivity(new Intent(mContext, WuLiuMessageActivity.class).putExtra("data", "no"));
                    }
                    ((MainActivity) getActivity()).getBadge();
                    break;
                case 2:
                    if (zhangdan_flag) {
                        startActivity(new Intent(mContext, ZhangDanMessageActivity.class).putExtra("data", "yes"));
                    } else {
                        startActivity(new Intent(mContext, ZhangDanMessageActivity.class).putExtra("data", "no"));
                    }
                    ((MainActivity) getActivity()).getBadge();
                    break;
                default:
                    Intent intent = new Intent(new Intent(mContext, ConversationActivity.class));
                    intent.putExtra("receiveId", list.get(position).getSendid());
                    startActivity(intent);
                    ((MainActivity) getActivity()).getBadge();
                    break;
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

            int width = 200;//单位为px,没有适配性

            //根据viewType设置不同的侧滑菜单
            if (viewType == ConversationBean.VIEW_TYPE_NORMAL) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF6D63")))
                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
            }
        }
    };
    /**
     * 菜单点击监听
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView#RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

            // TODO 如果是删除：推荐调用Adapter.notifyItemRemoved(position)，不推荐Adapter.notifyDataSetChanged();
            if ("单聊".equals(list.get(adapterPosition).getSessiontype())) {
                if (menuPosition == 0) {// 置顶按钮被点击。
                    Log.e("删除按钮被点击", adapterPosition + "");
                    final int position = adapterPosition;
                    IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).deleteConversationList(list.get(adapterPosition).getSendid(), new DataBaseCallBack() {
                        @Override
                        public void operationState(boolean flag) {
                            try {
                                ((MainActivity) getActivity()).getBadge();
                                list.remove(position);
                                mMessageAdapter.notifyItemChanged(position);
                                getData();
                            } catch (Exception e) {
                                getData();
                            }
                        }
                    });
                }
            }
        }

    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BroadcastManager.getInstance(mContext).destroy(MessageManager.NEW_MESSAGE);
        ButterKnife.unbind(this);
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        if (check_login()) {
            try {
                getData();
            } catch (Exception e) {

            }
        }
    }

}
