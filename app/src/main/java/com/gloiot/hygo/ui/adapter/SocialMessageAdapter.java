package com.gloiot.hygo.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.chatsdk.DataBase.DataBaseCallBack;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.MessageManager;
import com.gloiot.chatsdk.bean.ConversationBean;
import com.gloiot.chatsdk.bean.ImMsgBean;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.chatsdk.chatui.keyboard.emoji.SimpleCommonUtils;
import com.gloiot.chatsdk.utlis.TimeUtils;
import com.gloiot.hygo.R;
import com.gloiot.hygo.im.UserInfoManager;
import com.gloiot.hygo.ui.fragment.SocialFragment;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zyd.wlwsdk.adapter.recyclerview.OnItemClickListener1;
import com.zyd.wlwsdk.utils.PictureUtlis;

import java.util.List;

import butterknife.Bind;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * SocialFragment消息列表的适配器
 * Created by hygo05 on 2017/4/12 15:27
 */
public class SocialMessageAdapter extends SwipeMenuAdapter<SocialMessageAdapter.ViewHolder> {


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout rlImageScope;
        ImageView ivMessagePortrait;
        TextView tvMessageTitle;
        TextView tvMessageContent;
        TextView tvMessageTime;
        ImageView ivMessageNoDisturb;
        Badge badge;
        View view_1, view_20;

        OnItemClickListener1 mOnItemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            rlImageScope = (RelativeLayout) itemView.findViewById(R.id.rl_image_scope);
            ivMessagePortrait = (ImageView) itemView.findViewById(R.id.iv_message_portrait);
            tvMessageTitle = (TextView) itemView.findViewById(R.id.tv_message_title);
            tvMessageContent = (TextView) itemView.findViewById(R.id.tv_message_content);
            tvMessageTime = (TextView) itemView.findViewById(R.id.tv_message_time);
            ivMessageNoDisturb = (ImageView) itemView.findViewById(R.id.iv_message_no_disturb);
            view_1 = itemView.findViewById(R.id.view_1);
            view_20 = itemView.findViewById(R.id.view_20);
            badge = new QBadgeView(mContext).bindTarget(rlImageScope);
            badge.setBadgeGravity(Gravity.TOP | Gravity.END);
//            badge.setGravityOffset(-10, -6, false);
            badge.setBadgeBackgroundColor(Color.parseColor("#FF6D63"));
            badge.setBadgeTextSize(10, true);
            badge.setBadgePadding(6, true);
            badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                @Override
                public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                    if (dragState == STATE_SUCCEED) {
                        IMDBManager.getInstance(mContext, sendOutid).NoReadNumClean(mSocialMessageList.get(getAdapterPosition()).getSendid(), new DataBaseCallBack() {
                            @Override
                            public void operationState(boolean flag) {
                                BroadcastManager.getInstance(mContext).sendBroadcast(MessageManager.NEW_MESSAGE,
                                        new ImMsgBean(-10, "", "", "", "", "", "", "", "", -10, ""));
                            }
                        });
                    }
                }
            });
        }

        public void setOnItemClickListener(OnItemClickListener1 onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setData(ConversationBean conversationBean) {
            if ("单聊".equals(conversationBean.getSessiontype())) {//单聊
                UserInfo userInfo = UserInfoCache.getInstance(mContext).getUserInfo(conversationBean.getSendid(), sendOutid);
                if (null == userInfo) {//缓存没有数据，从数据库获取信息
                    userInfo = IMDBManager.getInstance(mContext, sendOutid).queryUserInfo(conversationBean.getSendid());
                }
                if (null != userInfo) { //缓存或数据库中有数据
                    UserInfoCache.getInstance(mContext).putData(conversationBean.getSendid(), userInfo);//再存储一次（缓存为空，向其中存数据库数据）
                    this.tvMessageTitle.setText(userInfo.getName());
                    PictureUtlis.loadImageViewHolder(mContext, userInfo.getUrl(), R.mipmap.ic_default, ivMessagePortrait);
                } else {//本地不存在，需要联网更新（异步）
//                    userInfo(bean.getSendid());//开启联网，返回值为null
                    UserInfoManager.getInstance(mContext).getUserInfo(conversationBean.getSendid());
                }

            } else if ("系统消息".equals(conversationBean.getSessiontype())) {//系统消息
                this.tvMessageTitle.setText("通知消息");
                PictureUtlis.loadImageViewHolder(mContext, "", R.mipmap.message_ic_system_notice, ivMessagePortrait);
            } else if ("物流消息".equals(conversationBean.getSessiontype())) {//物流消息
                this.tvMessageTitle.setText("物流助手");
                PictureUtlis.loadImageViewHolder(mContext, "", R.mipmap.message_ic_wuliu_notice, ivMessagePortrait);
            } else if ("账单通知".equals(conversationBean.getSessiontype())) {
                this.tvMessageTitle.setText("账单通知");
                PictureUtlis.loadImageViewHolder(mContext, "", R.mipmap.message_ic_zhangdan_notice, ivMessagePortrait);
            }

            SimpleCommonUtils.spannableEmoticonFilter(this.tvMessageContent, TextUtils.isEmpty(conversationBean.getPushdata()) ? "" : conversationBean.getPushdata());

            this.tvMessageTime.setText(TimeUtils.setMessageListTime(conversationBean.getSendTime()));

            if (conversationBean.getIsNoDiaturb() == 0) {
                this.ivMessageNoDisturb.setVisibility(View.GONE);
            } else {
                this.ivMessageNoDisturb.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(conversationBean.getNoReadNum() + "")) {
                this.badge.setBadgeNumber(conversationBean.getNoReadNum());
            }

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public Context mContext;
    public List<ConversationBean> mSocialMessageList;
    private OnItemClickListener1 mOnItemClickListener;
    private String sendOutid = "";

    public SocialMessageAdapter(Context context, List<ConversationBean> list, String sendOutid) {
        this.mContext = context;
        this.mSocialMessageList = list;
        this.sendOutid = sendOutid;
    }

    public void setOnItemClickListener(OnItemClickListener1 onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mSocialMessageList == null ? 0 : mSocialMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if ("系统消息".equals(mSocialMessageList.get(position).getSessiontype()))
            return ConversationBean.VIEW_TYPE_SYSTEM;
        else if ("物流消息".equals(mSocialMessageList.get(position).getSessiontype()))
            return ConversationBean.VIEW_TYPE_WULIU;
        else if ("账单通知".equals(mSocialMessageList.get(position).getSessiontype()))
            return ConversationBean.VIEW_TYPE_ZHANGDAN;
        else if ("群聊".equals(mSocialMessageList.get(position).getSessiontype()))
            return ConversationBean.VIEW_TYPE_GROUP;
        else
            return ConversationBean.VIEW_TYPE_NORMAL;   //单聊
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_social_message, parent, false);
    }

    @Override
    public ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.view_1.setVisibility(View.VISIBLE);
        holder.view_20.setVisibility(View.GONE);
        if (position == 2) {
            holder.view_1.setVisibility(View.GONE);
            holder.view_20.setVisibility(View.VISIBLE);
        }
        holder.setData(mSocialMessageList.get(position));
        holder.setOnItemClickListener(mOnItemClickListener);
    }

}
