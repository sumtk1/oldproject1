package com.gloiot.hygo.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gloiot.chatsdk.chatui.ChatUiIM;
import com.gloiot.chatsdk.chatui.keyboard.XhsEmoticonsKeyBoard;
import com.gloiot.hygo.R;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.zyd.wlwsdk.utils.MyToast;
import com.gloiot.hygo.ui.activity.social.ConversationActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

import static com.gloiot.chatsdk.chatui.ChatUiIM.REQUEST_CODE_CAMERA;
import static com.gloiot.chatsdk.chatui.ChatUiIM.REQUEST_CODE_PIC;

/**
 * Created by hygo01 on 17/5/23.
 */

public class ConversationFragment extends Fragment implements XhsEmoticonsKeyBoard.RequestVoicePermission {

    private XhsEmoticonsKeyBoard ekBar; // 表情输入键盘
    private ChatUiIM chatUiIM;
    private ArrayList<ImageItem> images = null; // 选择的图片集合

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_simple_chat_translucent, container, false);
        ButterKnife.bind(this, view);

        HashMap usreMap = new HashMap();
        usreMap.put("receiveId", getArguments().getString("receiveid"));    // 对方的用户id
        usreMap.put("sendId", getArguments().getString("userid"));   // 当前的用户id
        usreMap.put("random", getArguments().getString("random"));    // 随机码

        chatUiIM = ChatUiIM.getInstance();
        chatUiIM.setView(view, getActivity(), usreMap, getArguments().getBundle("data"));
        ekBar = chatUiIM.getEkBar();
        ekBar.setRequestVoicePermission(this);
        if (getArguments().getString("receiveid").contains("service")){
            ekBar.setVoice(false);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        chatUiIM.destroyBroadcast();
    }

    @Override
    public void onPause() {
        super.onPause();
        chatUiIM.cleanNoReadNum();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选取图片
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_PIC) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                chatUiIM.sendImage(images);
            } else if (data != null && requestCode == REQUEST_CODE_CAMERA){
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                chatUiIM.sendImage(images);
            }else {
                MyToast.getInstance().showToast(getActivity(), "没有数据");
            }
        }
    }

    @Override
    public boolean voiceResult() {
        return ((ConversationActivity)getActivity()).requestPermission();
    }
}
