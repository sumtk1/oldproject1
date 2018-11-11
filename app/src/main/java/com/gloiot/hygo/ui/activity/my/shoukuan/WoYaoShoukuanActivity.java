package com.gloiot.hygo.ui.activity.my.shoukuan;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.zyd.wlwsdk.utils.PictureUtlis;
import com.zyd.wlwsdk.utils.StatusBarUtil;
import com.zyd.wlwsdk.zxing.activity.CodeUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class WoYaoShoukuanActivity extends BaseActivity implements View.OnClickListener  {

    @Bind(R.id.iv_shoukuan_icon)
    ImageView ivShoukuanIcon;
    @Bind(R.id.tv_shoukuan_nick)
    TextView tvShoukuanNick;
    @Bind(R.id.iv_shoukuan_qrcode)
    ImageView ivShoukuanQrcode;
    @Bind(R.id.rl_shoukuan_jilu)
    RelativeLayout rlShoukuanJilu;

    @Override
    public int initResource() {
        return R.layout.activity_wo_yao_shoukuan;
    }

    @Override
    public void initData() {
        StatusBarUtil.setColor(this, Color.parseColor("#87A4ED"), 0);
        CommonUtlis.setTitleBar(this, "我要收款");
        // 头像
        PictureUtlis.loadRoundImageViewHolder(mContext, preferences.getString(ConstantUtlis.SP_USERIMG, ""), R.mipmap.ic_head, ivShoukuanIcon, 10);
        // 昵称
        tvShoukuanNick.setText(preferences.getString(ConstantUtlis.SP_NICKNAME, ""));
        // 拼接onlyID与原生交互标识
        ivShoukuanQrcode.setImageBitmap(CodeUtils.createImage(ConstantUtlis.SUPERMERCHANT_URL + "?onlyID=" + preferences.getString(ConstantUtlis.SP_ONLYID, "") + "&AppType=Self", 400, 400, null));
    }

    @OnClick({R.id.rl_shoukuan_jilu})
    @Override    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_shoukuan_jilu:
                Intent intent = new Intent(mContext, ShoukuanJiLuActivity.class);
                startActivity(intent);
                break;
        }

    }
}
