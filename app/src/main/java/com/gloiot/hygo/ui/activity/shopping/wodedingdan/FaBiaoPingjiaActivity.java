package com.gloiot.hygo.ui.activity.shopping.wodedingdan;

import android.Manifest;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.PingjiaFaBiaoBean;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.server.AliOss.MPhoto;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.PictureUtlis;
import com.zyd.wlwsdk.utils.imagepicker.ImagePreviewSeeActivity;
import com.zyd.wlwsdk.widge.MyRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class FaBiaoPingjiaActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.shangpin_pingjia_fabiaopingjia)
    Button shangpin_pingjia_fabiaopingjia;
    @Bind(R.id.shangpin_pingjia_listview)
    ListView shangpin_pingjia_listview;

    private List<PingjiaFaBiaoBean> listPingJia = new ArrayList<>();
    private CommonAdapter<PingjiaFaBiaoBean> commonAdapter;
    private MyGVAdapter myGVAdapter;

    private String type = "";

    @Override
    public int initResource() {
        return R.layout.activity_shangpin_pingjia;
    }

    @Override
    public void initData() {
        type = getIntent().getStringExtra("类型");

        listPingJia = (List<PingjiaFaBiaoBean>) getIntent().getSerializableExtra("商品信息");

        initComponent();

        setListViewHeightBasedOnChildren(shangpin_pingjia_listview);
        commonAdapter.notifyDataSetChanged();
    }

    public void initComponent() {
        CommonUtlis.setTitleBar(this, "发表评价");

        commonAdapter = new CommonAdapter<PingjiaFaBiaoBean>(this, R.layout.item_list_fabiaopingjia_pingjia, listPingJia) {
            @Override
            public void convert(final ViewHolder holder, final PingjiaFaBiaoBean Bean) {
                PictureUtlis.loadImageViewHolder(mContext, Bean.getShangpin_img_url(), R.drawable.default_image, (ImageView) holder.getView(R.id.fabiaopingjia_img));

                MyRatingBar fabiaopingjia_ratingBar = holder.getView(R.id.fabiaopingjia_ratingBar);
                final TextView fabiaopingjia_star_text = holder.getView(R.id.fabiaopingjia_star_text);

                CommonUtlis.setImageTitle(mContext, Bean.getShangpin_leixing(), Bean.getShangpin_title(), (TextView) holder.getView(R.id.fabiaopingjia_title));

                holder.setText(R.id.fabiaopingjia_guigeleixing, Bean.getShangpin_guigefenlei());
                holder.getView(R.id.fabiaopingjia_neirong).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        holder.getView(R.id.fabiaopingjia_neirong).getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });

                ((EditText) holder.getView(R.id.fabiaopingjia_neirong)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Bean.setShangpin_pingjianeirong(s.toString());
                    }
                });

                GridView fabiaopingjia_pictures = holder.getView(R.id.fabiaopingjia_pictures);

                myGVAdapter = new MyGVAdapter(Bean.getShangpin_imgList()) {
                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shangpin_shanghcuan_grid, null);

                        final ImageView mImageView = (ImageView) convertView.findViewById(R.id.id_item_image);//自定义的imageView，为了更好的放大动画效果
                        final ImageView mSelect = (ImageView) convertView.findViewById(R.id.id_item_select);//选中状态标识
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (Bean.getShangpin_imgList().size() == 0) {  //没有图片，显示添加按钮
                                    mSelect.setVisibility(View.GONE);
                                    mImageView.setBackgroundResource(R.mipmap.icon_thk_camera);
                                } else if (position == Bean.getShangpin_imgList().size() && Bean.getShangpin_imgList().size() < MAXPICTURES) {
                                    mSelect.setVisibility(View.GONE);
                                    mImageView.setBackgroundResource(R.mipmap.icon_thk_camera);
                                } else {
                                    mSelect.setVisibility(View.VISIBLE);
                                    PictureUtlis.loadImageViewHolder(mContext, Bean.getShangpin_imgList().get(position), R.mipmap.bg_spxq_shibaitu, mImageView);
                                }
                            }
                        });
                        if (Bean.getShangpin_imgList().size() > 0) {
                            mSelect.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bean.getShangpin_imgList().remove(position);
                                    notifyDataSetChanged();
                                }
                            });

                            Intent intentPreview = new Intent(mContext, ImagePreviewSeeActivity.class);
                            intentPreview.putExtra("imageList", (ArrayList<String>) Bean.getShangpin_imgList());
                            intentPreview.putExtra("position", position);
                            startActivity(intentPreview);
                        }
                        if (!isMax) {
                            if (position == Bean.getShangpin_imgList().size()) {
                                mImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        checkPermission(new CheckPermListener() {
                                            @Override
                                            public void superPermission() {
                                                MPhoto.Builder builder = new MPhoto.Builder()
                                                        .init(mContext)
                                                        .setTitle("图片选择")
                                                        .setResultCallback(new MPhoto.OnResultCallback() {
                                                            @Override
                                                            public void onSuccess(final String data) {
                                                                if (Bean.getShangpin_imgList().size() < MAXPICTURES)
                                                                    Bean.getShangpin_imgList().add(data);

                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Bean.getMyGVAdapter().notifyDataSetChanged();
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
                                        }, R.string.perm_camera, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE);
                                    }
                                });
                            }
                        }
                        return convertView;
                    }
                };
                Bean.setMyGVAdapter(myGVAdapter);
                fabiaopingjia_pictures.setAdapter(myGVAdapter);

                fabiaopingjia_ratingBar.setOnRatingChangeListener(new MyRatingBar.OnRatingChangeListener() {
                    @Override
                    public void onChange(int countSelected) {
                        Bean.setShangpin_star(countSelected + "");
                        switch (countSelected) {
                            case 1:
                                fabiaopingjia_star_text.setText("非常差");
                                break;
                            case 2:
                                fabiaopingjia_star_text.setText("差");
                                break;
                            case 3:
                                fabiaopingjia_star_text.setText("一般");
                                break;
                            case 4:
                                fabiaopingjia_star_text.setText("好");
                                break;
                            case 5:
                                fabiaopingjia_star_text.setText("非常好");
                                break;
                            default:
                                fabiaopingjia_star_text.setText("");
                                break;
                        }
                    }
                });
            }
        };
        shangpin_pingjia_listview.setAdapter(commonAdapter);
    }

    @OnClick({R.id.shangpin_pingjia_fabiaopingjia})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shangpin_pingjia_fabiaopingjia:
                boolean flag = true;
                for (int i = 0; i < listPingJia.size(); i++) {
                    if (listPingJia.get(i).getShangpin_pingjianeirong().trim().length() == 0 &&
                            listPingJia.get(i).getShangpin_imgList().size() == 0)
                        flag = false;
                }
                if (!flag)
                    MyToast.getInstance().showToast(this, "请填写评论内容");
                else {
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject;
                    for (int i = 0; i < listPingJia.size(); i++) {
                        jsonObject = new JSONObject();
                        try {
                            jsonObject.put("id", listPingJia.get(i).getId());
                            jsonObject.put("商品id", listPingJia.get(i).getShangpin_id());
                            jsonObject.put("comment", listPingJia.get(i).getShangpin_pingjianeirong());
                            jsonObject.put("star", listPingJia.get(i).getShangpin_star());

                            JSONArray imgArray = new JSONArray();
                            for (String str : listPingJia.get(i).getShangpin_imgList()) {
                                JSONObject imgObj = new JSONObject();
                                imgObj.put("imgUrl", str);
                                imgArray.put(imgObj);
                            }
                            jsonObject.put("img", imgArray.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArray.put(jsonObject);
                    }
                    requestHandleArrayList.add(requestAction.FabuPingjia(this, jsonArray, type));
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        Log.e("requestSuccess" + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_SHOP_C_COMMENT:
                if ("成功".equals(response.getString("状态"))) {
                    MyToast.getInstance().showToast(this, "发表评价" + response.getString("状态"));
                    Intent intent = new Intent();
                    intent.putExtra("type", type);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    //固定ListView高度
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
