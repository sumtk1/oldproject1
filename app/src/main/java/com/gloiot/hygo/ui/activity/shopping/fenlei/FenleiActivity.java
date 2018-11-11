package com.gloiot.hygo.ui.activity.shopping.fenlei;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.FenLeiErJiBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.FenLeiShangPinBean;
import com.gloiot.hygo.ui.activity.shopping.SousuoShangpingActivity;
import com.gloiot.hygo.ui.activity.shopping.verticaltablayout.TabAdapter;
import com.gloiot.hygo.ui.activity.shopping.verticaltablayout.VerticalTabLayout;
import com.gloiot.hygo.ui.activity.shopping.verticaltablayout.widget.QTabView;
import com.gloiot.hygo.ui.activity.shopping.verticaltablayout.widget.TabView;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.recyclerview.CommonAdapter;
import com.zyd.wlwsdk.adapter.recyclerview.OnItemClickListener;
import com.zyd.wlwsdk.utils.PictureUtlis;
import com.zyd.wlwsdk.widge.EmptyLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 莫小霞
 * 分类
 */

public class FenleiActivity extends BaseActivity {
    @Bind(R.id.toptitle_more_img)
    ImageView toptitle_more_img;
    @Bind(R.id.tablayout0)
    VerticalTabLayout tablayout;
    @Bind(R.id.rv_shouye_liebiao_zhanshi)
    RecyclerView rv_shouye_liebiao_zhanshi;
    @Bind(R.id.emptylayout_fenlei_shangpin)
    EmptyLayout emptylayout_fenlei_shangpin;
    @Bind(R.id.emptylayout_fenlei_liebiao)
    EmptyLayout emptylayout_fenlei_liebiao;

    private List<String> anniu_list = new ArrayList<>();
    private int page = 0;
    public int num = 0;
    private List<FenLeiShangPinBean> fenLei_list = new ArrayList<>();
    private List<FenLeiErJiBean> fenLeiErJiBeen_list;
    private String fenlei;

    private CommonAdapter adapter;

    private CommonAdapter adapter1;

    @Override
    public int initResource() {
        return R.layout.activity_fenlei;
    }

    @Override
    public void initData() {
        mContext = this;
        CommonUtlis.setTitleBar(this, "全部分类");

        initComponent();

//        Drawable rightDrawable = getResources().getDrawable(R.mipmap.ic_sousuo);
//        toptitle_more_img.setBackgroundDrawable(rightDrawable);
//        toptitle_more_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, SousuoShangpingActivity.class);
//                startActivity(intent);
//            }
//        });
        page = Integer.parseInt(getIntent().getStringExtra("下标"));
        request_liebiao();
        rv_shouye_liebiao_zhanshi.setLayoutManager(new GridLayoutManager(mContext, 1));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("onNewIntent", "onNewIntent: ");
    }

    public void initComponent() {
        final WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
//        int width2 = outMetrics.widthPixels;
        int height2 = outMetrics.heightPixels;

        int height = (int) ((height2 + 1) * 0.075);

        tablayout.setTabHeight(height);

        rv_shouye_liebiao_zhanshi = (RecyclerView) findViewById(R.id.rv_shouye_liebiao_zhanshi);

        adapter1 = new CommonAdapter<FenLeiShangPinBean>(mContext, R.layout.item_fenlei_zhanshi_fu, fenLei_list) {
            @Override
            public void convert(final ViewHolder holder, final FenLeiShangPinBean fenLeiShangPinBean) {
                holder.setText(R.id.tv_fenlei_yijifenlei, fenLeiShangPinBean.getFenLeiYIJI());
                RecyclerView recyclerView = holder.getView(R.id.rv_shouye_liebiao_shangpin);
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));


                adapter = new CommonAdapter<FenLeiErJiBean>(mContext, R.layout.item_fenlei_zhanshi_son, fenLeiShangPinBean.getToBeShangPin()) {
                    @Override
                    public void convert(ViewHolder holder, final FenLeiErJiBean fenLeiErJiBean) {
                        ImageView imageView = holder.getView(R.id.iv_fenlei_tu);
                        PictureUtlis.loadImageViewHolder(mContext, fenLeiErJiBean.getTupian(), R.mipmap.ic_fenlei_loading, imageView);
                        holder.setText(R.id.tv_fenlei_zhanshi, fenLeiErJiBean.getMingzi());

                        RelativeLayout rl_fenlei_item = holder.getView(R.id.rl_fenlei_item);
                        rl_fenlei_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, FenLeiSonActivity.class);
                                intent.putExtra("分类",fenlei);
                                intent.putExtra("关键字", fenLeiErJiBean.getMingzi());
                                intent.putExtra("类型", "分类");
                                startActivity(intent);
                            }
                        });
                    }
                };

                recyclerView.setAdapter(adapter);
            }
        };
        adapter1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        rv_shouye_liebiao_zhanshi.setAdapter(adapter1);


        emptylayout_fenlei_liebiao = (EmptyLayout) findViewById(R.id.emptylayout_fenlei_liebiao);
        emptylayout_fenlei_liebiao.setErrorDrawable(R.mipmap.img_error_layout);
        emptylayout_fenlei_liebiao.setErrorMessage("网络出错了");
        emptylayout_fenlei_liebiao.setErrorViewButtonId(R.id.buttonError);
        emptylayout_fenlei_liebiao.setShowErrorButton(true);
        emptylayout_fenlei_liebiao.setErrorButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_liebiao();
                emptylayout_fenlei_liebiao.hide();
            }
        });

        emptylayout_fenlei_shangpin = (EmptyLayout) findViewById(R.id.emptylayout_fenlei_shangpin);
        emptylayout_fenlei_shangpin.setErrorDrawable(R.mipmap.img_error_layout);
        emptylayout_fenlei_shangpin.setErrorMessage("网络出错了");
        emptylayout_fenlei_shangpin.setErrorViewButtonId(R.id.buttonError);
        emptylayout_fenlei_shangpin.setShowErrorButton(true);
        emptylayout_fenlei_shangpin.setErrorButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_ShangPinData(anniu_list.get(page));
                emptylayout_fenlei_shangpin.hide();
            }
        });

    }

    public void request_liebiao() {
        requestHandleArrayList.add(requestAction.getLieBiaoData(this));
    }

    public void request_ShangPinData(String data) {

        requestHandleArrayList.add(requestAction.getFenLei_ShangPinData(this, data));
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        super.onFailure(requestTag, errorResponse, showLoad);
        if (requestTag == RequestAction.TAG_SHOUYE_LIEBIAO)
            emptylayout_fenlei_liebiao.showError();
        if (requestTag == RequestAction.TAG_SHOUYE_SHNAGPIN_LIST)
            emptylayout_fenlei_shangpin.showError();
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        Log.e("requestSuccess" + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_SHOUYE_LIEBIAO:
                if (response.getString("状态").equals("成功")) {
                    num = Integer.parseInt(response.getString("主页列表条数"));
                    if (num != 0) {
                        JSONArray jsonArray = response.getJSONArray("主页列表");
                        for (int i = 0; i < num; i++) {
                            JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                            anniu_list.add(jsonObj.getString("类别名称"));
                        }
                    }
                    tablayout.setTabAdapter(new MyTabAdapter(), page);

                    for (int i = 0; i < anniu_list.size(); i++) {
//                        if (leibie.equals(anniu_list.get(i))) {
                        if (page == i) {
                            tablayout.getTabAt(i).animate();
                            tablayout.getTabAt(i).setBackgroundResource(R.drawable.bg_btn_fenlei_white);
                            request_ShangPinData(anniu_list.get(i));
                            fenlei = anniu_list.get(i);
                            page = i;
                        }

                    }
                    tablayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabView tab, final int position) {
                            if (position != page) {
                                tab.setBackgroundResource(R.drawable.bg_btn_fenlei_white);
                                tablayout.getTabAt(page).setBackgroundResource(R.drawable.bg_btn_fenlei);
                                page = position;
                                fenLei_list.clear();
                                adapter1.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(200);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                fenlei  = anniu_list.get(position);
                                                request_ShangPinData(anniu_list.get(position));
                                                emptylayout_fenlei_shangpin.hide();
                                            }
                                        });
                                    }
                                }).start();

                            }
                        }

                        @Override
                        public void onTabReselected(TabView tab, int position) {
                        }
                    });

                }
                break;
            case RequestAction.TAG_SHOUYE_SHNAGPIN_LIST:
                fenLei_list.clear();
                if (response.getString("状态").equals("成功")) {
                    int num = Integer.parseInt(response.getString("条数"));
                    if (num != 0) {
                        JSONArray jsonArray = response.getJSONArray("全部分类");
                        List<FenLeiErJiBean> fenLeiErJiBeanList;
                        FenLeiShangPinBean fenLeiShangPinBean;

                        for (int i = 0; i < num; i++) {
                            fenLeiShangPinBean = new FenLeiShangPinBean();
                            JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                            fenLeiShangPinBean.setFenLeiYIJI(jsonObj.getString("一级分类"));
                            int num_02 = Integer.parseInt(jsonObj.getString("条数"));
                            if (num_02 != 0) {
//                                fenLeiErJiBeen_list = new ArrayList<>();
                                fenLeiErJiBeanList = new ArrayList<>();
                                JSONArray jsonArray1 = jsonObj.getJSONArray("分类");
                                FenLeiErJiBean fenLeiErJiBean;
                                for (int j = 0; j < num_02; j++) {
                                    fenLeiErJiBean = new FenLeiErJiBean();
                                    JSONObject jsonObject = (JSONObject) jsonArray1.get(j);
                                    fenLeiErJiBean.setMingzi(jsonObject.getString("二级分类"));
                                    fenLeiErJiBean.setTupian(jsonObject.getString("图标"));
                                    fenLeiErJiBeanList.add(fenLeiErJiBean);
                                }
                                fenLeiShangPinBean.setToBeShangPin(fenLeiErJiBeanList);
                                fenLei_list.add(fenLeiShangPinBean);
                            }
                        }
                    }
                    adapter1.notifyDataSetChanged();
                }
                break;
        }
    }

    class MyTabAdapter implements TabAdapter {

        @Override
        public int getCount() {
            return anniu_list.size();
        }


        @Override
        public int getBadge(int position) {
            return 0;
        }

        @Override
        public QTabView.TabIcon getIcon(int position) {
            return null;
        }

        @Override
        public QTabView.TabTitle getTitle(int position) {
            return new QTabView.TabTitle.Builder(mContext)
                    .setContent(anniu_list.get(position))
                    .setTextColor(getResources().getColor(R.color.cl_2b9ced), getResources().getColor(R.color.cl_666666))
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return 0;
        }

    }
}
