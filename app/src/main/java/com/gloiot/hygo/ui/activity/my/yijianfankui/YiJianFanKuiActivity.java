package com.gloiot.hygo.ui.activity.my.yijianfankui;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.zxy.tiny.Tiny;
import com.zyd.wlwsdk.server.AliOss.MPhoto;
import com.zyd.wlwsdk.utils.MyToast;
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

import static com.gloiot.hygo.R.id.et_opinion;

public class YiJianFanKuiActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @Bind(et_opinion)
    EditText mEtOpinion;
    @Bind(R.id.btn_commit)
    Button mBtnCommit;
    @Bind(R.id.rg_btn)
    RadioGroup mRgBtn;
    @Bind(R.id.tv_suggestion_num)
    TextView mTvSuggestionNum;
    @Bind(R.id.tv_suggestion_sum)
    TextView mTvSuggestionSum;
    @Bind(R.id.gd_yijianfankui_pictures)
    GridView gd_yijianfankui_pictures;
    private String type;
    private CharSequence tt;

    private int selectionStart;
    private int selectionEnd;
    private int num = 50;

    private final int MAXPICTURES = 4; //图片上传最大数量
    private List<String> picUrlList = new ArrayList<>();  //添加的图片地址集合
    private MyGVAdapter adapter;

    //要上传的json格式的图片地址集合
    private String myPicPathArray = "";

    @Override
    public int initResource() {
        return R.layout.activity_my_yijianfankui;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "意见反馈", "历史反馈");
        mRgBtn.setOnCheckedChangeListener(this);
        mEtOpinion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                tt = s;

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTvSuggestionNum.setText(editable.length() + "");
                selectionStart = mEtOpinion.getSelectionStart();
                selectionEnd = mEtOpinion.getSelectionEnd();
                if (tt.length() > num) {
                    editable.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    mEtOpinion.setText(editable);
                    mEtOpinion.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

        adapter = new MyGVAdapter(picUrlList);
        gd_yijianfankui_pictures.setAdapter(adapter);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_01:
                type = "功能异常";
                break;
            case R.id.rb_02:
                type = "其他问题";
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.btn_commit, R.id.toptitle_more})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                final String opinion = mEtOpinion.getText().toString();
                if (TextUtils.isEmpty(type)) {
                    MyToast.getInstance().showToast(mContext, "请选择反馈类型");
                } else if (TextUtils.isEmpty(opinion)) {
                    MyToast.getInstance().showToast(mContext, "请输入你的问题或者意见");
                } else {
                    DialogUtlis.twoBtnNormal(getmDialog(), "是否提交反馈?", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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
                            requestHandleArrayList.add(requestAction.YiJianFanKui(YiJianFanKuiActivity.this, type, opinion, myPicPathArray));
                            dismissDialog();
                        }
                    });
                }
                break;
            case R.id.toptitle_more:
                startActivity(new Intent(YiJianFanKuiActivity.this, LiShiFanKuiActivity.class));
                break;
            default:
                break;
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_YIJIANFANKUI:
                MyToast.getInstance().showToast(mContext, response.getString("状态"));
                finish();
                break;
            default:
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

            View view = View.inflate(YiJianFanKuiActivity.this, R.layout.item_shangpin_shanghcuan_grid, null);

            final ImageView mImageView = (ImageView) view.findViewById(R.id.id_item_image);//自定义的imageView，为了更好的放大动画效果
            final ImageView mSelect = (ImageView) view.findViewById(R.id.id_item_select);//选中状态标识
            YiJianFanKuiActivity.this.runOnUiThread(new Runnable() {
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
                        ArrayList<ImageItem> listImageItem = new ArrayList<>();
                        for (String path : list) {
                            ImageItem imageItem = new ImageItem();
                            imageItem.path = path;
                            listImageItem.add(imageItem);
                        }
                        Intent intentPreview = new Intent(mContext, ImagePreviewSeeActivity.class);
                        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, listImageItem);
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
                                        YiJianFanKuiActivity.this.runOnUiThread(new Runnable() {
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

}
