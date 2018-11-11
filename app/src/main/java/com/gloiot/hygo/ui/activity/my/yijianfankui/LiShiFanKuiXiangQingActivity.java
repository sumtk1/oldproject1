package com.gloiot.hygo.ui.activity.my.yijianfankui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.PictureUtlis;
import com.zyd.wlwsdk.utils.imagepicker.ImagePreviewSeeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class LiShiFanKuiXiangQingActivity extends BaseActivity {

    @Bind(R.id.tv_lishi_fankui_xiangqing_time1)
    TextView tvLishiFankuiXiangqingTime1;
    @Bind(R.id.tv_lishi_fankui_xiangqing_leixing)
    TextView tvLishiFankuiXiangqingLeixing;
    @Bind(R.id.tv_lishi_fankui_xiangqing_question)
    TextView tvLishiFankuiXiangqingQuestion;
    @Bind(R.id.tv_lishi_fankui_xiangqing_time2)
    TextView tvLishiFankuiXiangqingTime2;
    @Bind(R.id.tv_lishi_fankui_xiangqing_answer)
    TextView tvLishiFankuiXiangqingAnswer;
    @Bind(R.id.tv_lishi_fankui_xiangqing_tupian)
    GridView tv_lishi_fankui_xiangqing_tupian;

    private List<String> listDatas = new ArrayList<>(4);
    private CommonAdapter<String> commonAdapter;

    @Override
    public int initResource() {
        return R.layout.activity_lishi_fankui_xiangqing;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "历史反馈");

        commonAdapter = new CommonAdapter<String>(mContext, R.layout.item_img, listDatas) {
            @Override
            public void convert(ViewHolder holder, String s) {
                PictureUtlis.loadImageViewHolder(mContext, s, R.drawable.default_image, (ImageView) holder.getView(R.id.iv_img));
            }
        };
        tv_lishi_fankui_xiangqing_tupian.setAdapter(commonAdapter);
        //点击图片
        tv_lishi_fankui_xiangqing_tupian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0 || position > 4)
                    return;

                ArrayList<ImageItem> listImageItem = new ArrayList<>();
                for (String path : listDatas) {
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

        String fankui_id = getIntent().getStringExtra("反馈id");
        requestHandleArrayList.add(requestAction.history_feedback(this, fankui_id));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess" + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_HISTORY_FEEDBACK:
                JSONObject obj = response.getJSONArray("list").getJSONObject(0);
                tvLishiFankuiXiangqingTime1.setText(JSONUtlis.getString(obj, "录入时间"));
                tvLishiFankuiXiangqingLeixing.setText("类型：" + JSONUtlis.getString(obj, "反馈类别"));
                tvLishiFankuiXiangqingQuestion.setText(JSONUtlis.getString(obj, "问题描述"));
                tvLishiFankuiXiangqingTime2.setText(JSONUtlis.getString(obj, "处理完成时间"));
                tvLishiFankuiXiangqingAnswer.setText(JSONUtlis.getString(obj, "处理结果"));

                listDatas.clear();
                String str = obj.getString("图片");
                JSONArray jsonArray = new JSONArray(str);
                for (int i = 0; i < jsonArray.length(); i++) {
                    listDatas.add(jsonArray.getJSONObject(i).getString("imgUrl"));
                }
                if(listDatas.size() > 0){
                    tv_lishi_fankui_xiangqing_tupian.setVisibility(View.VISIBLE);
                }else{
                    tv_lishi_fankui_xiangqing_tupian.setVisibility(View.GONE);
                }
                commonAdapter.notifyDataSetChanged();
                break;
        }
    }
}
