package com.gloiot.hygo.ui.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.bean.Life;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseFragment;
import com.gloiot.hygo.ui.activity.SaoMiaoActivity;
import com.gloiot.hygo.ui.activity.payment.SettlementActivity;
import com.gloiot.hygo.ui.activity.web.WebActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.adapter.abslistview.MultiItemCommonAdapter;
import com.zyd.wlwsdk.adapter.abslistview.MultiItemTypeSupport;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.PictureUtlis;
import com.zyd.wlwsdk.widge.EmptyLayout;
import com.zyd.wlwsdk.zxing.activity.CodeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LifeFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.iv_life_scan)
    ImageView ivLifeScan;
    @Bind(R.id.life_emptyLayout)
    EmptyLayout life_emptyLayout;
    @Bind(R.id.life_swipeRefreshLayout)
    SwipeRefreshLayout lifeSwipeRefresh;
    @Bind(R.id.gv_life)
    GridView gvLife;
    @Bind(R.id.lv_life)
    ListView lvLife;
    @Bind(R.id.wv_tianqi)
    WebView wv_tianqi;
    @Bind(R.id.view_status_bar)
    View viewStatusBar;

    private List<Life> list = new ArrayList<>();
    private List<String[]> lists = new ArrayList<>();
    private LifeAdapter lifeAdapter;
    private CommonAdapter gridAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_life, container, false);
        ButterKnife.bind(this, view);

        initView();
        initData();
        return view;
    }

    private void initData() {
        if (null != preferences.getString(ConstantUtlis.CACHE_LIFE, null)) {
            try {
                JSONObject jsonObject = new JSONObject(preferences.getString(ConstantUtlis.CACHE_LIFE, null));
                ShuJuChuLi(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                life_emptyLayout.showError();
            }
        }

        // 设置下拉控件的位置、样式
        lifeSwipeRefresh.setProgressViewEndTarget(true, 300);
        lifeSwipeRefresh.setDistanceToTriggerSync(150);
        lifeSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.swipeRefresh_color));
        lifeSwipeRefresh.setOnRefreshListener(this);

        lvLife.setFocusable(false); // 去掉listview的焦点, 解决scrollview嵌套listview运行后最先显示出来的位置不在顶部而是中间问题
        lvLife.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == -1) return;
                if (check_login_tiaozhuang()) {
                    if (checkIsSetPwd()) {
                        if (list.get(position).getShowType() == 0) {
                            Intent intent = new Intent(mContext, WebActivity.class);
                            intent.putExtra("url", mUrl(list.get(position).getBtnLists().get(0)[1]));
                            startActivity(intent);
                        }
                    }
                }

            }
        });
//        getTianQi(); // 显示天气
        requestData();
    }

    /**
     * 显示天气
     */
    boolean tianQiError = false;

    private void getTianQi() {
        wv_tianqi.loadUrl(ConstantUtlis.TIANQI_URL);
        WebSettings ws = wv_tianqi.getSettings();
        ws.setDefaultTextEncodingName("utf-8");
        ws.setJavaScriptEnabled(true);  //支持js
        wv_tianqi.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                tianQiError = false; // 开始加载改变状态
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                tianQiError = true; // 加载失败改变状态
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (tianQiError) {
                    view.setVisibility(View.GONE); // 加载失败隐藏webview
                } else {
                    view.setVisibility(View.VISIBLE); // 加载完成显示webview
                }
            }

        });

    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= 21) {
            viewStatusBar.setVisibility(View.VISIBLE);
        }
        life_emptyLayout.setErrorDrawable(R.mipmap.img_error_layout);
        life_emptyLayout.setErrorMessage("网络出错了");
        life_emptyLayout.setErrorViewButtonId(R.id.buttonError);
        life_emptyLayout.setShowErrorButton(true);
        life_emptyLayout.setErrorButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
                life_emptyLayout.hide();
            }
        });

        lifeAdapter = new LifeAdapter(mContext, list);
        lvLife.setAdapter(lifeAdapter);
        gridAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_life_top, lists) {
            @Override
            public void convert(ViewHolder holder, final String[] strings) {
                holder.setText(R.id.tv_life_name, strings[1]);
                PictureUtlis.loadImageViewHolder(mContext, strings[0], R.drawable.default_image, (ImageView) holder.getView(R.id.iv_life_img));
                holder.setOnClickListener(R.id.rl_life_top, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (check_login_tiaozhuang()) {
                            if (checkIsSetPwd()) {
                                if (lists == null || lists.isEmpty()) {
                                    return;
                                }
                                Intent intent = new Intent(mContext, WebActivity.class);
                                intent.putExtra("url", mUrl(strings[2]));
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        };
        gvLife.setAdapter(gridAdapter);
    }

    private void requestData() {
        requestHandleArrayList.add(requestAction.getLifeData(LifeFragment.this, null, -1));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_GETLIFEDATA:
                lifeSwipeRefresh.setRefreshing(false);

                lists.clear();
                list.clear();
                ShuJuChuLi(response);
                break;
        }
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        super.onFailure(requestTag, errorResponse, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_GETLIFEDATA:
                lifeSwipeRefresh.setRefreshing(false);
                break;
        }
    }

    private void ShuJuChuLi(JSONObject response) throws JSONException {
        L.e("TAG_LIFE_DATA", response + "");
        editor.putString(ConstantUtlis.CACHE_LIFE, response.toString());
        editor.commit();
        /**
         * 存储顶部数据
         */
        JSONArray jsonArray = response.getJSONArray("top");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = (JSONObject) jsonArray.get(i);
            String[] details = new String[3];
            details[0] = jsonObj.getString("ico");
            details[1] = jsonObj.getString("title");
            details[2] = jsonObj.getString("url");
            lists.add(details);
        }
        setData();


        /**
         * 存储模板1、2,3数据
         */
        JSONArray jAMB = response.getJSONArray("list");
        for (int i = 0; i < jAMB.length(); i++) {
            JSONObject jOMB = (JSONObject) jAMB.get(i);
            Log.e("json数据" + i, jOMB.toString());
            Life life = new Life();
            life.setIcon(jOMB.getString("ico"));
            life.setTitle(jOMB.getString("title"));
            life.setExplain(jOMB.getString("slogan"));

            JSONArray ivLists = jOMB.getJSONArray("images");
            List<String[]> btnList = new ArrayList<>();
            for (int j = 0; j < ivLists.length(); j++) {
                JSONObject jOs = (JSONObject) ivLists.get(j);
                String[] s = new String[4];
                s[0] = jOs.getString("picture");
                s[1] = jOs.getString("url");
                if (jOMB.getString("event").equals("5")) {
                    s[2] = jOs.getString("标题");
                    s[3] = jOs.getString("图片");

                }
                btnList.add(s);
            }
            life.setBtnLists(btnList);
            if (jOMB.getString("event").equals("1")) {
                life.setShowType(Life.SHOWTYPE_0);
                list.add(life);
            } else if (jOMB.getString("event").equals("3")) {
                life.setShowType(Life.SHOWTYPE_1);
                list.add(life);
            } else if (jOMB.getString("event").equals("5")) {
                life.setShowType(Life.SHOWTYPE_2);
                list.add(life);
            }
        }
        lifeAdapter.notifyDataSetChanged();
        CommonUtlis.setListViewHeightBasedOnChildren(lvLife);
    }

    /**
     * 设置顶部模块数据
     */
    private void setData() {
        if (lists.size() > 3) {
            gvLife.setNumColumns(4);
        } else {
            gvLife.setNumColumns(3);
        }
        gridAdapter.notifyDataSetChanged();
        CommonUtlis.reMesureGridViewHeight(gvLife);
    }

    @OnClick({R.id.iv_life_scan})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_life_scan:
                if (check_login_tiaozhuang()) {
                    if (checkIsSetPwd()) {
                        if (lists == null || lists.isEmpty()) {
                            return;
                        }
                        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                Intent intent = new Intent(mContext, SaoMiaoActivity.class);
                                startActivityForResult(intent, 100);

                            }
                        }, R.string.perm_camera, Manifest.permission.CAMERA, Manifest.permission.VIBRATE);
                    }
                }
                break;
        }
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        wv_tianqi.loadUrl(ConstantUtlis.TIANQI_URL); // 刷新天气
        requestData();
    }


    public class LifeAdapter extends MultiItemCommonAdapter<Life> {

        public LifeAdapter(Context context, List<Life> datas) {
            super(context, datas, new MultiItemTypeSupport<Life>() {
                @Override
                public int getLayoutId(int position, Life life) {
                    switch (life.getShowType()) {
                        case Life.SHOWTYPE_0:
                            return R.layout.item_life01;
                        case Life.SHOWTYPE_1:
                            return R.layout.item_life02;
                        case Life.SHOWTYPE_2:
                            return R.layout.item_life03;
                        default:
                            return 0;
                    }
                }

                @Override
                public int getViewTypeCount() {
                    return 3;
                }

                @Override
                public int getItemViewType(int position, Life life) {
                    return life.getShowType();
                }
            });
        }

        @Override
        public void convert(ViewHolder holder, final Life life) {
            switch (holder.getLayoutId()) {
                case R.layout.item_life01:
                    PictureUtlis.loadRoundImageViewHolder(mContext, life.getIcon(), R.drawable.default_image, (ImageView) holder.getView(R.id.iv_icon), 5);
                    holder.setText(R.id.tv_title, life.getTitle());
                    holder.setText(R.id.tv_explain, life.getExplain());
                    PictureUtlis.loadImageViewHolder(mContext, life.getBtnLists().get(0)[0], R.drawable.default_image, (ImageView) holder.getView(R.id.iv_guanggao));
                    break;
                case R.layout.item_life02:
                    PictureUtlis.loadRoundImageViewHolder(mContext, life.getIcon(), R.drawable.default_image, (ImageView) holder.getView(R.id.iv_icon), 5);
                    holder.setText(R.id.tv_title, life.getTitle());
                    holder.setText(R.id.tv_explain, life.getExplain());
                    L.e("-lists-", life.getTitle() + "==" + life.getBtnLists().size());
                    if (life.getBtnLists().size() > 0) {
                        String[] guanggaos = life.getBtnLists().get(0);
                        ImageView iv_guanggao = holder.getView(R.id.iv_guanggao);
                        PictureUtlis.loadImageViewHolder(mContext, guanggaos[0], R.drawable.default_image, iv_guanggao);
                        setClick(iv_guanggao, guanggaos[1]);
                    }
                    if (life.getBtnLists().size() > 1) {
                        String[] firsts = life.getBtnLists().get(1);
                        ImageView iv_first = holder.getView(R.id.iv_first);
                        PictureUtlis.loadImageViewHolder(mContext, firsts[0], R.drawable.default_image, iv_first);
                        setClick(iv_first, firsts[1]);
                    }
                    if (life.getBtnLists().size() > 2) {
                        String[] seconds = life.getBtnLists().get(2);
                        ImageView iv_second = holder.getView(R.id.iv_second);
                        PictureUtlis.loadImageViewHolder(mContext, seconds[0], R.drawable.default_image, iv_second);
                        setClick(iv_second, seconds[1]);
                    }
                    break;
                case R.layout.item_life03:
                    if (life.getBtnLists().size() > 0) {
                        String[] titles = life.getBtnLists().get(0);
                        RelativeLayout rl_title = holder.getView(R.id.rl_title);
                        PictureUtlis.loadImageViewHolder(mContext, titles[0], R.drawable.default_image, (ImageView) holder.getView(R.id.iv_guanggao));
                        holder.setText(R.id.tv_title, titles[2]);
                        setClick(rl_title, titles[1]);
                    }
                    if (life.getBtnLists().size() > 1) {
                        LinearLayout container = holder.getView(R.id.ll_life_03);
                        container.removeAllViews();
                        for (int i = 1; i < life.getBtnLists().size(); i++) {
                            String[] titles = life.getBtnLists().get(i);
                            RelativeLayout item = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.item_life03_son, container, false);
                            TextView tv_son_title2 = (TextView) item.findViewById(R.id.tv_son_title2);
                            RelativeLayout rl_title = (RelativeLayout) item.findViewById(R.id.rl_son2);
                            View view = item.findViewById(R.id.view);
                            View view_01 = item.findViewById(R.id.view_01);
                            if (i == 1) {
                                view_01.setVisibility(View.VISIBLE);
                            } else {
                                view_01.setVisibility(View.GONE);
                            }
                            if (i == life.getBtnLists().size() - 1) {
                                view.setVisibility(View.GONE);
                            }
                            tv_son_title2.setText(titles[2]);
                            PictureUtlis.loadRoundImageViewHolder(mContext, titles[3], R.drawable.default_image, (ImageView) item.findViewById(R.id.iv_son_icon2), 5);
                            setClick(rl_title, titles[1]);
                            container.addView(item);
                        }
                    }

                    break;
            }
        }
    }

    /**
     * 设置item里面的控件的事件
     */
    private void setClick(ImageView iv, final String url) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.e("点击事件了：是否跳转");
                if (check_login_tiaozhuang()) {
                    if (checkIsSetPwd()) {
                        Intent intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra("url", mUrl(url));
                        startActivity(intent);
                    }
                }

            }
        });
    }

    /**
     * 设置item里面的控件的事件
     */
    private void setClick(RelativeLayout rl, final String url) {
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.e("点击事件了：是否跳转");
                if (check_login_tiaozhuang()) {
                    if (checkIsSetPwd()) {
                        Intent intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra("url", mUrl(url));
                        startActivity(intent);
                    }
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 100) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.e("扫描结果", result);
                    if (!result.startsWith("http")) {
                        // 判断是否ip地址开头
                        if (result.startsWith("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$")) {
                            Intent intent = new Intent(mContext, WebActivity.class);
                            intent.putExtra("url", mUrl(result));
                            startActivity(intent);
                        } else if (result.startsWith("www.")) {
                            Intent intent = new Intent(mContext, WebActivity.class);
                            intent.putExtra("url", mUrl(result));
                            startActivity(intent);
                        } else {
                            DialogUtlis.oneBtnNormal(getmDialog(), result);
                        }
                    } else {
                        if (result.contains("&AppType=Self")) {
                            String onlyid = "";
                            try {
                                onlyid = result.split("onlyID=")[1].split("&AppType")[0];
                                Log.e("扫描结果333", onlyid);
                            } catch (Exception e) {

                            }
                            //收款的二维码扫描回调，暂时写加在末尾的AppType=Self标记，等接口一出再重新拼接所需参数写过完整的标识
                            Intent it = new Intent(getActivity(), SettlementActivity.class);
                            it.putExtra("onlyID", onlyid);
                            startActivity(it);
                        } else {
                            Intent intent = new Intent(mContext, WebActivity.class);
                            intent.putExtra("url", mUrl(result));
                            startActivity(intent);
                        }
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    MyToast.getInstance().showToast(mContext, "解析二维码失败");
                }
            }
        }
    }

    // 判断地址是否有问号
    private String mUrl(String url) {
        if (url.contains("?")) {
            return url + "&onlyID=" + preferences.getString(ConstantUtlis.SP_ONLYID, "") + "&version=" + ConstantUtlis.VERSION;
        } else {
            return url + "?onlyID=" + preferences.getString(ConstantUtlis.SP_ONLYID, "") + "&version=" + ConstantUtlis.VERSION;
        }
    }

    /**
     * 点击发现刷新页面
     */
    public void refresh() {
        try {
            lifeSwipeRefresh.setRefreshing(true);
            requestData();
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
