package com.gloiot.hygo.ui.activity.my;

import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * 我的人脉
 */
public class WoDeRenMaiActivity extends BaseActivity {

    @Bind(R.id.tv_contacts_name)
    TextView mTvContactsName;
    @Bind(R.id.tv_contacts_phone)
    TextView mTvContactsPhone;
    @Bind(R.id.tv_contacts_state)
    TextView mTvContactsState;

    @Override
    public int initResource() {
        return R.layout.activity_my_woderenmai;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "我的人脉");
        requestHandleArrayList.add(requestAction.GetMyContacts(WoDeRenMaiActivity.this));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_GETMYCONTACTS:
                mTvContactsName.setText(response.getString("真实名"));
                mTvContactsPhone.setText(response.getString("手机号"));
                mTvContactsState.setText(response.getString("类别"));
                break;
        }
    }
}
