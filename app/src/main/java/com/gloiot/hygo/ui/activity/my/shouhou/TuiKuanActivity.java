package com.gloiot.hygo.ui.activity.my.shouhou;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.zxy.tiny.Tiny;
import com.zyd.wlwsdk.server.AliOss.MPhoto;
import com.zyd.wlwsdk.utils.PictureUtlis;
import com.zyd.wlwsdk.utils.imagepicker.ImagePreviewSeeActivity;
import com.zyd.wlwsdk.widge.LoadDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by dwj on 2017/6/9 .
 * 申请退款、退货
 */

public class TuiKuanActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_refund_money)
    TextView tvMoney;
    @Bind(R.id.et_refund_reason)
    EditText etReason;
    @Bind(R.id.gd_shangpin_sshangchuang_pictures)
    GridView gv_shangpin_shangchuan_tupian;
    @Bind(R.id.rl_wuliu_statu)
    RelativeLayout rlWuliuStatu;
    @Bind(R.id.tv_refund_wuliu_statu)
    TextView tv_refund_wuliu_statu;
    @Bind(R.id.tv_refund_cause)
    TextView tv_refund_cause;

    @Bind(R.id.tv_cause)
    TextView tvCause;
    @Bind(R.id.tv_refund_money_1)
    TextView tvMoney1;
    @Bind(R.id.tv_shuoming)
    TextView tv_Shuoming;

    private final int MAXPICTURES = 5; //图片上传最大数量
    private List<String> picUrlList = new ArrayList<>();  //添加的图片地址集合
    private MyGVAdapter adapter;


    // 数据源，物流状态
    private ArrayList<String> arrayStatu = new ArrayList<>();
    // 数据源，退款原因
    private String[] arrayCause;
    private ArrayList<String> arrayCause2 = new ArrayList<>();

    //商品的最大金额
    private String myShngpinJine = "0.00";
    //订单id
    private String myDingDanId = "";
    //商品id
    private String mySangpinId = "";
    //id
    private String Id = "";
    //物流状态
    private String myStatu = "";
    //退款原因
    private String myCause = "";
    //退款补充说明
    private String myReason = "";
    //要上传的json格式的图片地址集合
    private String myPicPathArray = "";
    private String shouhouType;   //申请类型  退货/退款

    private int causePosition, statuPosition;
    private String strDialog;

    @Override
    public int initResource() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_refund_apply;
    }

    @Override
    public void initData() {
        arrayStatu.add("未收到货");
        arrayStatu.add("已收到货");

        myDingDanId = getIntent().getStringExtra("订单id");
        mySangpinId = getIntent().getStringExtra("商品id");
        Id = getIntent().getStringExtra("id");
        shouhouType = getIntent().getStringExtra("申请售后");

        if ("退款".equals(shouhouType)) {
            CommonUtlis.setTitleBar(this, "申请退款");
            strDialog = "选择退款原因";
            tv_refund_cause.setText("请选择退款原因");
            requestHandleArrayList.add(requestAction.shop_rf_reason(TuiKuanActivity.this, 0 + ""));
            requestHandleArrayList.add(requestAction.shop_rf_apply(TuiKuanActivity.this, myDingDanId, mySangpinId, Id, "7天无理由退货", myStatu, "", myReason, myPicPathArray, 0 + ""));
            rlWuliuStatu.setVisibility(View.VISIBLE);
        } else {
            CommonUtlis.setTitleBar(this, "申请退货");
            tvCause.setText("退货原因");
            tvMoney1.setText("退货金额");
            tv_Shuoming.setText("退货说明：");
            strDialog = "选择退货原因";
            tv_refund_cause.setText("请选择退货原因");
            requestHandleArrayList.add(requestAction.shop_rf_reason(TuiKuanActivity.this, 1 + ""));
            requestHandleArrayList.add(requestAction.shop_rf_apply_goods(TuiKuanActivity.this, myDingDanId, mySangpinId, Id, "7天无理由退货", "", myReason, myPicPathArray, 0 + ""));
        }

        adapter = new MyGVAdapter(picUrlList);
        gv_shangpin_shangchuan_tupian.setAdapter(adapter);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_RFEDIT:  //退款 重新申请成功后跳转
                Intent intent = new Intent(TuiKuanActivity.this, TuiKuanXiangQingActivity.class);
                intent.putExtra("订单id", myDingDanId);
                intent.putExtra("商品id", mySangpinId);
                intent.putExtra("id", Id);
                intent.putExtra("详情类型", "退款");
                startActivity(intent);
                finish();
                break;

            case RequestAction.TAG_RFAPPLY_MONEY:
                myShngpinJine = response.getString("退款金额");
                tvMoney.setText("¥" + myShngpinJine);
                break;
            case RequestAction.TAG_RFAPPLY_CONFIG:
                Intent intent3 = new Intent(TuiKuanActivity.this, TuiKuanXiangQingActivity.class);
                intent3.putExtra("详情类型", "退款");
                intent3.putExtra("订单id", myDingDanId);
                intent3.putExtra("商品id", mySangpinId);
                intent3.putExtra("id", Id);
                startActivity(intent3);
                finish();
                break;

            case RequestAction.TAG_RFYUANYIN:
                arrayCause2.clear();
                JSONArray array = response.getJSONArray("退款原因");
                String strCause = array.toString().substring(2, array.toString().length() - 2);
                arrayCause = strCause.split("\",\"");
                for (int i = 0; i < arrayCause.length; i++) {
                    arrayCause2.add(arrayCause[i]);
                }
                break;

            case RequestAction.TAG_RFAPPLYGOODS_MONEY:
                myShngpinJine = response.getString("退款金额");
                tvMoney.setText("¥" + myShngpinJine);
                break;

            case RequestAction.TAG_RFAPPLYGOODS_CONFIG:
                Intent intent4 = new Intent(TuiKuanActivity.this, TuiKuanXiangQingActivity.class);
                intent4.putExtra("详情类型", "退货");
                intent4.putExtra("订单id", myDingDanId);
                intent4.putExtra("商品id", mySangpinId);
                intent4.putExtra("id", Id);
                startActivity(intent4);
                finish();

                break;
        }
    }


    /**
     * gridview适配器
     */
    class MyGVAdapter extends BaseAdapter {
        private boolean isMax;
        private List<String> list = new ArrayList<>();

        public MyGVAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list.size() >= MAXPICTURES) {
                isMax = true;
                return MAXPICTURES;
            } else {
                isMax = false;
                return list.size() + 1;
            }
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            View view = View.inflate(TuiKuanActivity.this, R.layout.item_shangpin_shanghcuan_grid, null);

            final ImageView mImageView = (ImageView) view.findViewById(R.id.id_item_image);//自定义的imageView，为了更好的放大动画效果
            final ImageView mSelect = (ImageView) view.findViewById(R.id.id_item_select);//选中状态标识
            TuiKuanActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (list.size() == 0) {  //没有图片，显示添加按钮
                        mSelect.setVisibility(View.GONE);
                        mImageView.setBackgroundResource(R.mipmap.icon_thk_camera);
                    } else if (position == list.size() && list.size() < MAXPICTURES) {
                        mSelect.setVisibility(View.GONE);
                        mImageView.setBackgroundResource(R.mipmap.icon_thk_camera);
                    } else {
                        mSelect.setVisibility(View.VISIBLE);
                        LoadDialog.show(mContext);
                        PictureUtlis.loadImageViewHolder(mContext, list.get(position), mImageView, new RequestListener() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                                LoadDialog.dismiss(mContext);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                                LoadDialog.dismiss(mContext);
                                return false;
                            }
                        });
                    }
                }
            });
            if (list.size() > 0) {
                mSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.remove(position);
//                        picUrlList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<ImageItem> imageItemlist = new ArrayList<>();
                        for (String path : list) {
                            ImageItem imageItem = new ImageItem();
                            imageItem.path = path;
                            imageItemlist.add(imageItem);
                        }
                        Intent intentPreview = new Intent(mContext, ImagePreviewSeeActivity.class);
                        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, imageItemlist);
                        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                        startActivity(intentPreview);
                    }
                });

            }
            if (!isMax) {
                if (position == list.size()) {
                    mImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setShangpin_tupian();
                        }
                    });
                }
            }
            return view;
        }
    }

    //选择图片
    private void setShangpin_tupian() {

        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        //控制上传的头像小于200KB
                        Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
                        compressOptions.size = 200;
                        MPhoto.Builder builder = new MPhoto.Builder()
                                .init(mContext)
                                .setTitle("图片选择")
                                .setCompressOptions(compressOptions)
                                .setResultCallback(new MPhoto.OnResultCallback() {
                                    @Override
                                    public void onSuccess(final String data) {
                                        if (picUrlList.size() < MAXPICTURES)
                                            picUrlList.add(data);
                                        TuiKuanActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(String errorMsg) {
                                        Log.e("上传图片失败", errorMsg);
                                    }
                                });
                        MPhoto.init(builder);

                    }
                }, R.string.perm_readstorage, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }, R.string.perm_camera, Manifest.permission.CAMERA);
    }

    @OnClick({R.id.rl_refund_cause, R.id.bt_submit, R.id.rl_wuliu_statu})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_wuliu_statu:
                if (null != arrayStatu && arrayStatu.size() > 0) {
                    DialogUtlis.customLoopView(getmDialog(), "选择物流状态", arrayStatu, statuPosition, new MDialogInterface.LoopViewInter() {
                        @Override
                        public void getPostition(int postition) {
                            statuPosition = postition;
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                myStatu = arrayStatu.get(statuPosition);
                                tv_refund_wuliu_statu.setText(myStatu);
                                dismissDialog();
                            } catch (Exception e) {
                                dismissDialog();
                            }
                        }
                    });
                }
                break;

            case R.id.rl_refund_cause:
                if (arrayCause2.size() > 0) {
                    DialogUtlis.customLoopView(getmDialog(), strDialog, arrayCause2, causePosition, new MDialogInterface.LoopViewInter() {
                        @Override
                        public void getPostition(int postition) {
                            causePosition = postition;
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                myCause = arrayCause2.get(causePosition);
                                tv_refund_cause.setText(myCause);
                                dismissDialog();
                            } catch (Exception e) {
                                dismissDialog();
                            }
                        }
                    });
                }
                break;

            case R.id.bt_submit:
                //提交按钮
                myReason = etReason.getText().toString().trim();
                JSONArray jsonArrayPictures = new JSONArray();
                myPicPathArray = null;
                if (!picUrlList.isEmpty()) {
                    //图片重组
                    for (String url : picUrlList) {
                        JSONObject object = new JSONObject();
                        try {
                            object.put("imgUrl", url);
                            jsonArrayPictures.put(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                myPicPathArray = jsonArrayPictures.toString();


                if ("退款".equals(shouhouType)) {
                    requestHandleArrayList.add(requestAction.shop_rf_apply(TuiKuanActivity.this, myDingDanId, mySangpinId, Id, myCause, myStatu, myShngpinJine, myReason, myPicPathArray, 1 + ""));
                } else {  //退货
                    requestHandleArrayList.add(requestAction.shop_rf_apply_goods(TuiKuanActivity.this, myDingDanId, mySangpinId, Id, myCause, myShngpinJine, myReason, myPicPathArray, 1 + ""));
                }
                break;
        }
    }
}
