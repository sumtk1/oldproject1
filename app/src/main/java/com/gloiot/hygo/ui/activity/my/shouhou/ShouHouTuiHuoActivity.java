package com.gloiot.hygo.ui.activity.my.shouhou;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class ShouHouTuiHuoActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_shouhou_tuihuo_shouhoren)
    TextView tvShouhouTuihuoShouhoren;
    @Bind(R.id.tv_shouhou_tuihuo_lianxidianhua)
    TextView tvShouhouTuihuoLianxidianhua;
    @Bind(R.id.tv_shouhou_tuihuo_shouhuodizhi)
    TextView tvShouhouTuihuoShouhuodizhi;

    @Bind(R.id.tv_shouhou_tuihuo_kuaidigongsi)
    TextView tvShouhouTuihuoKuaidigongsi;
    @Bind(R.id.tv_shouhou_tuihuo_kuaididanhao)
    EditText tvShouhouTuihuoKuaididanhao;

    private String myDingdanId;
    private String myShangpinId;
    private String myId;
    private ArrayList<String> kuaiDiData  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_shouhou_tuihuo;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "申请成功");

        myDingdanId = getIntent().getStringExtra("订单id");
        myShangpinId = getIntent().getStringExtra("商品id");
        myId = getIntent().getStringExtra("id");

        //1 为查询买家收货信息
        requestHandleArrayList.add(requestAction.shop_result_success(this, myDingdanId, myShangpinId, myId, null, null, "1"));

        initComponent();
    }

    private void initComponent() {

    }

    @OnClick({R.id.tv_shouhou_tuihuo_kuaidigongsi, R.id.tv_shouhou_tuihuo_tijiao})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_shouhou_tuihuo_kuaidigongsi:
                if (kuaiDiData.size() <= 0){
                    requestHandleArrayList.add(requestAction.kuaidi(this));
                } else {
                    DialogUtlis.customListView(getmDialog(), "选择快递公司", kuaiDiData, new MDialogInterface.ListViewOnClickInter() {
                        @Override
                        public void onItemClick(String data, int position) {
                            tvShouhouTuihuoKuaidigongsi.setText(data);
                        }
                    });
                }
                break;
            case R.id.tv_shouhou_tuihuo_tijiao:
                String myKuaiDiGongSi = tvShouhouTuihuoKuaidigongsi.getText().toString().trim();
                String myKuaiDiDanHao = tvShouhouTuihuoKuaididanhao.getText().toString().trim();
                if (myKuaiDiGongSi.isEmpty() || myKuaiDiDanHao.isEmpty()) {
                    Toast.makeText(this, "快递公司与快递单号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    requestHandleArrayList.add(requestAction.shop_result_success(this, myDingdanId, myShangpinId, myId, myKuaiDiGongSi, myKuaiDiDanHao, "2"));
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess : " + requestTag, response.toString());
        switch (requestTag) {
            //查询卖家收货信息
            case RequestAction.TAG_RFRESULTSUCESS_INFO:
                tvShouhouTuihuoShouhoren.setText(response.getString("收货人"));
                tvShouhouTuihuoLianxidianhua.setText(response.getString("联系电话"));
                tvShouhouTuihuoShouhuodizhi.setText(response.getString("收货地址"));
                break;

            //
            case RequestAction.TAG_RFRESULTSUCESS_TIJIAO:
                Intent intent = new Intent(ShouHouTuiHuoActivity.this, TuiKuanXiangQingActivity.class);
                intent.putExtra("订单id", myDingdanId);
                intent.putExtra("商品id", myShangpinId);
                intent.putExtra("id", myId);
                intent.putExtra("详情类型", "等待商家确认收货");
                startActivity(intent);
                finish();
                break;
            case RequestAction.TAG_KUAIDI:
                kuaiDiData.clear();
                JSONArray j = new JSONArray(JSONUtlis.getString(response, "列表"));
                for (int i = 0; i < j.length(); i++) {
                    kuaiDiData.add(new JSONObject(j.getString(i)).getString("名称"));
                }
                DialogUtlis.customListView(getmDialog(), "选择记录类型", kuaiDiData, new MDialogInterface.ListViewOnClickInter() {
                    @Override
                    public void onItemClick(String data, int position) {
                        tvShouhouTuihuoKuaidigongsi.setText(data);
                    }
                });
                break;
        }
    }
}
