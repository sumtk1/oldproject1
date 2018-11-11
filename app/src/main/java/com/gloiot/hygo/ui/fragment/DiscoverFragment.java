package com.gloiot.hygo.ui.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseFragment;
import com.gloiot.hygo.ui.activity.web.WebActivity;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.PictureUtlis;
import com.zyd.wlwsdk.widge.refresh.RLoadListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class DiscoverFragment extends BaseFragment implements RLoadListener.RefreshLoadListener, View.OnClickListener {

    @Bind(R.id.view_status_bar)
    View viewStatusBar;
    @Bind(R.id.rv_fragment_discover)
    RecyclerView discoverRecyclerView;
    @Bind(R.id.sf_fragment_discover)
    SwipeRefreshLayout discoverSwipeRefresh;

    private LinearLayoutManager manager;
    private DiscoverAdapter discoverAdapter;
    private RLoadListener<DiscoverAdapter> mListener;

    private List<DisconverBean> listData = new ArrayList<>();
    private int XiayiyeYeshu = 0;

    //表示点赞是都还未响应
    private boolean Requesting = false;
    private String message_id = "";

    // 是否是刷新
    private boolean isRefresh = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        ButterKnife.bind(this, view);

        initView(view);

        return view;
    }

    private void initView(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            viewStatusBar.setVisibility(View.VISIBLE);
        }

        // 创建布局管理
        manager = new LinearLayoutManager(mContext);
        discoverRecyclerView.setLayoutManager(manager);

        // 创建适配器
        discoverAdapter = new DiscoverAdapter();
        discoverAdapter.openLoadAnimation();

        // 给RecyclerView设置适配器
        discoverRecyclerView.setAdapter(discoverAdapter);

        // 设置下拉控件的位置、样式
        discoverSwipeRefresh.setProgressViewEndTarget(true, 300);
        discoverSwipeRefresh.setDistanceToTriggerSync(150);
        discoverSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.swipeRefresh_color));

        // 设置刷新加载
        mListener = new RLoadListener.Builder<DiscoverAdapter>()
                .setSwipeRefreshLayout(discoverSwipeRefresh)
                .setRecyclerView(discoverRecyclerView)
                .setAdapter(discoverAdapter)
                .setRefreshLoadListener(this)
                .create();

        refresh();
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        switch (requestTag) {
            case RequestAction.TAG_P_SEL_FIND:
                L.e("-TAG_P_SEL_FIND-" + requestTag, response.toString());
                XiayiyeYeshu = response.getInt("页数");
                int loadlength = response.getInt("条数");

                //页数==1，说明是刷新或者第一次请求，清空list数据集合
                if (XiayiyeYeshu == 1) {
                    listData.clear();
                }

                int preEndIndex = listData.size(); // 用于确定加载更多的起始位置

                JSONArray infoArr = response.getJSONArray("数据");
                JSONObject obj;
                DisconverBean disconverBean;
                for (int i = 0; i < infoArr.length(); i++) {
                    obj = infoArr.getJSONObject(i);
                    disconverBean = new DisconverBean();

                    disconverBean.DisconverId = JSONUtlis.getString(obj, "id");
                    disconverBean.DisconverAuthor = "发稿人：" + JSONUtlis.getString(obj, "发布人昵称");
                    disconverBean.DisconverStyle = JSONUtlis.getString(obj, "消息类型");
                    disconverBean.DisconverIdentityNum = JSONUtlis.getString(obj, "点赞数");
                    disconverBean.DisconverTitle = JSONUtlis.getString(obj, "标题");
                    disconverBean.DisconverXiaoxiId = JSONUtlis.getString(obj, "消息id");
                    disconverBean.DisconverIdentityState = JSONUtlis.getString(obj, "是否点赞");
                    //清除掉旧的数据，防止图片
                    disconverBean.DisconverInfoData.clear();
                    switch (JSONUtlis.getString(obj, "消息类型")) {
                        case "视频":
                            disconverBean.DisconverVideoImgUrl = JSONUtlis.getString(obj, "视频图片");
                            disconverBean.DisconverInfoData.add(JSONUtlis.getString(obj, "视频地址"));
                            break;
                        case "商品":
                            JSONArray TuPianArray = obj.getJSONArray("商品图片");
                            if (!TextUtils.isEmpty(JSONUtlis.getString(obj, "缩略图"))) {
                                disconverBean.DisconverInfoData.add(JSONUtlis.getString(obj, "缩略图"));
                            } else {
                                JSONObject TuPianObj;
                                for (int imgIndex = 0; imgIndex < TuPianArray.length(); imgIndex++) {
                                    TuPianObj = TuPianArray.getJSONObject(imgIndex);
                                    disconverBean.DisconverInfoData.add(TuPianObj.getString("商品图片"));
                                }
                            }
                            break;
                        case "图文":
                            disconverBean.DisconverInfoData.add(JSONUtlis.getString(obj, "缩略图"));
                            break;
                    }
                    listData.add(disconverBean);
                }

                if (XiayiyeYeshu == 1) {
                    L.e("-discoverAdapter-", "notifyDataSetChanged-------preEndIndex ==" + preEndIndex + "---" + loadlength);
                    discoverAdapter.notifyDataSetChanged();
                } else {
                    L.e("-discoverAdapter-", "notifyItemRangeInserted---------preEndIndex ==" + preEndIndex + "---" + loadlength);
                    discoverAdapter.notifyItemRangeInserted(preEndIndex + 1, loadlength);
                }

                if (isRefresh) {
                    // 刷新完成
                    mListener.setRefreshComplete();
                } else {
                    if (loadlength == 10) {
                        discoverAdapter.loadMoreComplete(); // 本次加载完成
                    }
                }
                if (loadlength != 10) {
                    discoverAdapter.loadMoreEnd(); // 没有更多
                }
                mListener.setRefreshLayoutTrue(); // 加载完成后，开启刷新
                break;

            case RequestAction.TAG_P_CLK_PRAISE:
                L.e("-TAG_P_CLK_PRAISE-" + requestTag, response.toString());
                //{"状态":"成功","点赞":"否","当前点赞数":1}
                for (int i = 0; i < listData.size(); i++) {
                    if (message_id.equals(listData.get(i).DisconverXiaoxiId)) {
                        listData.get(i).DisconverIdentityState = JSONUtlis.getString(response, "点赞");
                        listData.get(i).DisconverIdentityNum = JSONUtlis.getString(response, "当前点赞数");
                        break;
                    }
                }
                discoverAdapter.notifyDataSetChanged();
                Requesting = false;
                break;
        }
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        super.onFailure(requestTag, errorResponse, showLoad);
        //点赞接口响应状态修改
        if (RequestAction.TAG_P_CLK_PRAISE == requestTag) {
            Requesting = false;
        } else if (RequestAction.TAG_P_SEL_FIND == requestTag) {
            if (isRefresh) {
                mListener.setRefreshComplete();
            } else {
                discoverAdapter.loadMoreFail();
                mListener.setRefreshLayoutTrue(); // 加载完失败，开启刷新
            }
        }
    }

    @Override
    public void onSuccess(int requestTag, JSONObject response, int showLoad) {
        super.onSuccess(requestTag, response, showLoad);
        //点赞接口响应状态修改
        if (RequestAction.TAG_P_CLK_PRAISE == requestTag)
            Requesting = false;
    }

    @Override
    public void onCancel(int requestTag, int showLoad) {
        super.onCancel(requestTag, showLoad);
        //点赞接口响应状态修改
        if (RequestAction.TAG_P_CLK_PRAISE == requestTag)
            Requesting = false;
    }

    @Override
    public void onClick(View v) {

    }

    //点赞/取消点赞
    private void dianZanOrQuXiao(String message_id) {
        requestHandleArrayList.add(requestAction.p_clk_praise(this,
                preferences.getString(ConstantUtlis.SP_ONLYID, ""), message_id));
    }

    /**
     * 点击发现刷新页面
     */
    public void refresh() {
        requestHandleArrayList.add(requestAction.p_sel_find(this, preferences.getString(ConstantUtlis.SP_ONLYID, ""), String.valueOf(0), null, -1));
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        isRefresh = true;
        refresh();
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoading() {
        isRefresh = false;
        requestHandleArrayList.add(requestAction.p_sel_find(this,
                preferences.getString(ConstantUtlis.SP_ONLYID, ""), String.valueOf(XiayiyeYeshu), null, -1));
    }


    class DisconverBean {
        private String DisconverId;  //Id
        private String DisconverXiaoxiId;  //消息Id
        private String DisconverTitle;  //标题
        private String DisconverAuthor;  //作者
        private String DisconverIdentityNum;  //点赞数
        private String DisconverIdentityState = "否";  //自己是否点赞
        private List<String> DisconverInfoData = new ArrayList<>(3); //图片（视频地址）
        private String DisconverVideoImgUrl = "";  //视频模式下展示的图片地址
        private String DisconverStyle = "";   //模块类型
    }

    private class DiscoverAdapter extends BaseQuickAdapter<DisconverBean, BaseViewHolder> {

        DiscoverAdapter() {
            super(R.layout.item_discover_fregment, listData);
        }

        @Override
        protected void convert(BaseViewHolder helper, final DisconverBean disconverBean) {
            helper.setText(R.id.tv_item_discover_fragment_title, disconverBean.DisconverTitle);
            helper.setText(R.id.tv_item_discover_fragment_author, disconverBean.DisconverAuthor);
            helper.setText(R.id.tv_item_discover_fragment_dianzan, disconverBean.DisconverIdentityNum);
            if ("否".equals(disconverBean.DisconverIdentityState)) {
                helper.setImageResource(R.id.iv_item_discover_fragment_dianzan, R.mipmap.ic_dianzan_no);
            } else {
                helper.setImageResource(R.id.iv_item_discover_fragment_dianzan, R.mipmap.ic_dianzan);
            }

            helper.getView(R.id.iv_item_discover_fragment_content_1).setVisibility(View.GONE);
            helper.getView(R.id.ll_item_discover_fragment_content_2).setVisibility(View.GONE);
            helper.getView(R.id.jcvp_item_discover_fragment_content_3).setVisibility(View.GONE);
            JCVideoPlayerStandard jcVideoPlayerStandard = helper.getView(R.id.jcvp_item_discover_fragment_content_3);

            //商品状态下都不显示三张图片布局
            helper.getView(R.id.iv_item_discover_fragment_content_2_1).setVisibility(View.GONE);
            helper.getView(R.id.iv_item_discover_fragment_content_2_2).setVisibility(View.GONE);
            helper.getView(R.id.iv_item_discover_fragment_content_2_3).setVisibility(View.GONE);


            for (int i = 0; i < disconverBean.DisconverInfoData.size(); i++) {
                L.e("img URL", "------    " + disconverBean.DisconverInfoData.get(i));
            }
            //多样式
            switch (disconverBean.DisconverStyle) {
                case "图文"://一张图片
                    helper.getView(R.id.iv_item_discover_fragment_content_1).setVisibility(View.VISIBLE);
                    try {
                        //这个异常捕获防止集合中没有数据时出现闪退
                        String str = disconverBean.DisconverInfoData.get(0);
                        PictureUtlis.loadImageViewHolder(mContext, str, R.drawable.default_image, (ImageView) helper.getView(R.id.iv_item_discover_fragment_content_1));
                    } catch (Exception e) {
                        L.e("ArrayList中没有数据");
                    }
                    break;
                case "商品"://三张图片
                    try {//这个异常捕获防止集合中没有数据时出现闪退

                        if (disconverBean.DisconverInfoData.size() == 1){
                            helper.getView(R.id.iv_item_discover_fragment_content_1).setVisibility(View.VISIBLE);
                            //这个异常捕获防止集合中没有数据时出现闪退
                            String str = disconverBean.DisconverInfoData.get(0);
                            PictureUtlis.loadImageViewHolder(mContext, str, R.drawable.default_image, (ImageView) helper.getView(R.id.iv_item_discover_fragment_content_1));
                        }  else {
                            helper.getView(R.id.ll_item_discover_fragment_content_2).setVisibility(View.VISIBLE);

                            //三张图片里面的ImageView
                            String str1 = disconverBean.DisconverInfoData.get(0);
                            //设置显示图片布局
                            helper.getView(R.id.iv_item_discover_fragment_content_2_1).setVisibility(View.VISIBLE);
                            PictureUtlis.loadImageViewHolder(mContext, str1, R.drawable.default_image, (ImageView) helper.getView(R.id.iv_item_discover_fragment_content_2_1));

                            String str2 = disconverBean.DisconverInfoData.get(1);
                            //设置显示图片布局
                            helper.getView(R.id.iv_item_discover_fragment_content_2_2).setVisibility(View.VISIBLE);
                            PictureUtlis.loadImageViewHolder(mContext, str2, R.drawable.default_image, (ImageView) helper.getView(R.id.iv_item_discover_fragment_content_2_2));

                            String str3 = disconverBean.DisconverInfoData.get(2);
                            //设置显示图片布局
                            helper.getView(R.id.iv_item_discover_fragment_content_2_3).setVisibility(View.VISIBLE);
                            PictureUtlis.loadImageViewHolder(mContext, str3, R.drawable.default_image, (ImageView) helper.getView(R.id.iv_item_discover_fragment_content_2_3));

                        }
                    } catch (Exception e) {
                        L.e("ArrayList中没有数据");
                    }
                    break;
                case "视频"://视频
                    helper.getView(R.id.jcvp_item_discover_fragment_content_3).setVisibility(View.VISIBLE);
                    try {
                        //这个异常捕获防止集合中没有数据时出现闪退
                        String str = "";
                        str = disconverBean.DisconverInfoData.get(0);
                        jcVideoPlayerStandard.setUp(str, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");
                        PictureUtlis.loadImageViewHolder(mContext, disconverBean.DisconverVideoImgUrl,
                                R.drawable.default_image, jcVideoPlayerStandard.thumbImageView);

                    } catch (Exception e) {
                        L.e("ArrayList中没有数据");
                    }
                    break;
                default:
                    break;
            }

            helper.getView(R.id.ll_item_discover_fragment_dianzan).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (check_login_tiaozhuang()) {
                        if (!Requesting) {
                            Requesting = true;
                            message_id = disconverBean.DisconverXiaoxiId;
                            dianZanOrQuXiao(message_id);
                        }
                    }
                }
            });

            helper.getView(R.id.ll_item_discover_fragment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "";
                    switch (disconverBean.DisconverStyle) {
                        case "图文":
                            url = ConstantUtlis.SHOUYE_FAXIAN_NEXT_URL + "huanshop.xhtml?onlyID=" + preferences.getString(ConstantUtlis.SP_ONLYID, "")
                                    + "&list_id=" + disconverBean.DisconverId + "&message_id=" + disconverBean.DisconverXiaoxiId;
                            break;
                        case "商品":
                            if (disconverBean.DisconverInfoData.size() == 1) {
                                url = ConstantUtlis.SHOUYE_FAXIAN_NEXT_URL + "huanshop.xhtml?onlyID=" + preferences.getString(ConstantUtlis.SP_ONLYID, "")
                                        + "&list_id=" + disconverBean.DisconverId + "&message_id=" + disconverBean.DisconverXiaoxiId;
                            } else {
                                url = ConstantUtlis.SHOUYE_FAXIAN_NEXT_URL + "shopdetail.xhtml?onlyID=" + preferences.getString(ConstantUtlis.SP_ONLYID, "")
                                        + "&list_id=" + disconverBean.DisconverId + "&message_id=" + disconverBean.DisconverXiaoxiId;
                            }
                            break;
                        case "视频":
                            url = ConstantUtlis.SHOUYE_FAXIAN_NEXT_URL + "videodetail.xhtml?onlyID=" + preferences.getString(ConstantUtlis.SP_ONLYID, "")
                                    + "&list_id=" + disconverBean.DisconverId + "&message_id=" + disconverBean.DisconverXiaoxiId;
                            break;
                    }

                    if (!"".equals(url)) {
                        Intent intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    } else {
                        MyToast.getInstance().showToast(mContext, "网页地址异常");
                    }

                }
            });

            if (manager.findFirstVisibleItemPosition() > helper.getLayoutPosition()
                    || manager.findLastVisibleItemPosition() < helper.getLayoutPosition()) {
                JCVideoPlayer.releaseAllVideos();
            }
        }
    }
}
