package com.gloiot.hygo.ui.activity.my.ziliao;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.utils.L;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 只有一行输入栏的Activity
 */
public class SingleLineInputActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_input)
    EditText mEtInput;
    @Bind(R.id.iv_delete)
    ImageView mIvDelete;
    @Bind(R.id.toptitle_more)
    TextView mTvRight;
    @Bind(R.id.tv_sum)
    TextView mTvSum;
    @Bind(R.id.tv_num)
    TextView mTvNum;
    @Bind(R.id.tv_explain)
    TextView mTvExplain;
    private int sum;//限制可输入的最大字数
    private int num = 0;//已输入的字数,默认为0
    private String title, originalString;

    @Override
    public int initResource() {
        return R.layout.activity_single_line_input;
    }

    @Override
    public void initData() {
        title = getIntent().getStringExtra("title");
        originalString = getIntent().getStringExtra("currentString");
        CommonUtlis.setTitleBar(this, title, "完成");
        mTvRight.setEnabled(false);
        mTvRight.setTextColor(getResources().getColor(R.color.b4b4b4));

        if (title.equals("昵称")) {
            mEtInput.setSingleLine();//昵称时，设置单行
            sum = 20;
        } else if (title.equals("个性签名")) {
            sum = 30;
        }
        mTvSum.setText(sum + "");

        if (!TextUtils.isEmpty(originalString)) {
            mEtInput.setText(originalString);
            mEtInput.setSelection(originalString.length());
            mIvDelete.setVisibility(View.VISIBLE);
        } else {
            mEtInput.setHint("请设置" + title + "(" + sum + "字以内)");
        }
        mTvNum.setText(originalString.length() + "");

        mEtInput.addTextChangedListener(new TextWatcher() {
            int selectionStart;
            int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                L.e("onTextChanged", "s= " + s + ",start=" + start + ",before = " + before + ",count = " + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                L.e("afterTextChanged", "Editable = " + s);
                mTvNum.setText(s.length() + "");
                selectionStart = mEtInput.getSelectionStart();
                selectionEnd = mEtInput.getSelectionEnd();
                if (s.length() > sum) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    mEtInput.setText(s);
                    mEtInput.setSelection(tempSelection);//设置光标在最后
                }
                if (s.length() > 0) {
                    mIvDelete.setVisibility(View.VISIBLE);
                    if ((s + "").equals(originalString)) {//和原来的内容相同，即没有更改
                        mTvExplain.setVisibility(View.GONE);
                        mTvRight.setEnabled(false);
                        mTvRight.setTextColor(getResources().getColor(R.color.b4b4b4));
                    } else {//内容和原来相比有更改
                        if (TextUtils.isEmpty(s.toString().trim())) {//全是空格
                            mTvExplain.setVisibility(View.VISIBLE);
                            mTvRight.setEnabled(false);
                            mTvRight.setTextColor(getResources().getColor(R.color.b4b4b4));
                        } else {
                            mTvExplain.setVisibility(View.GONE);
                            mTvRight.setEnabled(true);
                            mTvRight.setTextColor(getResources().getColor(R.color.main_color));
                        }
                    }
                } else {//输入框为空
                    mTvExplain.setVisibility(View.GONE);
                    mIvDelete.setVisibility(View.GONE);
                    if ((s + "").equals(originalString)) {//原来的内容为空
                        mTvRight.setEnabled(false);
                        mTvRight.setTextColor(getResources().getColor(R.color.b4b4b4));
                    } else {//原来的内容不为空
                        mEtInput.setHint("请设置" + title + "(" + sum + "字以内)");
                        if (title.equals("昵称")) {//昵称不能为空，必须要有内容
                            mTvRight.setEnabled(false);
                            mTvRight.setTextColor(getResources().getColor(R.color.b4b4b4));
                        } else {
                            mTvRight.setEnabled(true);
                            mTvRight.setTextColor(getResources().getColor(R.color.white));
                        }
                    }
                }
            }
        });
    }

    @OnClick({R.id.iv_delete, R.id.toptitle_more})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_delete:
                mEtInput.setText("");
                break;
            case R.id.toptitle_more:
                Intent intent = new Intent();
                intent.putExtra("update", mEtInput.getText().toString().trim());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
