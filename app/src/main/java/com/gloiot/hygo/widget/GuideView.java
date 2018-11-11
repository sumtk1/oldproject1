package com.gloiot.hygo.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gloiot.hygo.R;

/**
 * 时间：on 2017/12/11 14:16.
 * 作者：by hygo04
 * 功能：首页指引
 */

public class GuideView extends RelativeLayout {

    // 关闭按钮，指引view，知道了
    private ImageView ivGuideClose, ivGuideView, ivGuideKnow;
    private RelativeLayout rlGuide;
    // 记录指引view的指针
    private int point = 1;
    // 指引view的回调
    private OnGuideChangedListener listener;
    // 指引view的父View
    private FrameLayout mParentView;
    private View view;

    public GuideView(Context context) {
        super(context);
        init(context);
    }

    public GuideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.view_custom_guide, this);
        // 设置透明
        rlGuide = (RelativeLayout) findViewById(R.id.rl_guide);
        rlGuide.setBackgroundColor(Color.BLACK);
        rlGuide.setAlpha(0.8f);

        ivGuideClose = (ImageView) findViewById(R.id.iv_guide_close);
        ivGuideView = (ImageView) findViewById(R.id.iv_guide_view);
        ivGuideKnow = (ImageView) findViewById(R.id.iv_guide_know);

        ivGuideClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });

        ivGuideKnow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                point++;
                switch(point) {
                    case 2:
                        ivGuideView.setImageResource(R.mipmap.ic_guide_view02);
                        break;
                    case 3:
                        ivGuideView.setImageResource(R.mipmap.ic_guide_view03);
                        break;
                    case 4:
                        ivGuideView.setImageResource(R.mipmap.ic_guide_view04);
                        break;
                    case 5:
                        ivGuideView.setImageResource(R.mipmap.ic_guide_view05);
                        break;
                    default:
                        remove();
                        break;
                }
            }
        });

    }

    public void show(Activity parentActivity) {
        if (listener == null) {
            throw new RuntimeException("请设置GuideRemoveListener!");
        }
        mParentView = (FrameLayout) parentActivity.getWindow().getDecorView();
        mParentView.addView(view, new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        listener.onShow();
    }

    public void remove() {
        if (listener == null) {
            throw new RuntimeException("请设置GuideRemoveListener!");
        }
        point = 1;

        if(mParentView != null) {
            mParentView.removeView(view);
        }
        listener.onRemove();
    }

    public GuideView setGuideRemoveListener(OnGuideChangedListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnGuideChangedListener {
        void onShow();

        void onRemove();
    }
}
