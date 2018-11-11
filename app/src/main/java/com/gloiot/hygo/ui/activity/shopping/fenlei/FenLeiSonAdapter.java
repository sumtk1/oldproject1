package com.gloiot.hygo.ui.activity.shopping.fenlei;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.activity.shopping.Bean.FenLeiSonBean;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.utils.PictureUtlis;

import java.util.List;

/**
 * 时间：on 2017/12/29 10:34.
 * 作者：by hygo04
 * 功能：FenLeiSonActivity的适配器
 */
public class FenLeiSonAdapter extends BaseMultiItemQuickAdapter<FenLeiSonBean, BaseViewHolder> {

    public FenLeiSonAdapter(List<FenLeiSonBean> data) {
        super(data);
        addItemType(FenLeiSonBean.OneLine, R.layout.item_shangpin_fenlei_son_01);
        addItemType(FenLeiSonBean.TwoLine, R.layout.item_shangpin_fenlei_son_02);
    }

    @Override
    protected void convert(BaseViewHolder helper, FenLeiSonBean item) {
        switch(helper.getItemViewType()) {
            case FenLeiSonBean.OneLine:
                PictureUtlis.loadImageViewHolder(mContext, item.getFenLeiSon_img_url(), R.drawable.default_image,
                        (ImageView) helper.getView(R.id.iv_item_shangpin_fenlei_tupian));
                helper.setText(R.id.tv_item_shangpin_fenlei_jiage, item.getFenLeiSon_jiage());
                helper.setText(R.id.tv_item_shangpin_fenlei_dizhi, item.getFenLeiSon_dizhi());
                CommonUtlis.setImageTitle(mContext, item.getFenLeiSon_leixin(), item.getFenLeiSon_title(),
                        (TextView) helper.getView(R.id.tv_item_shangpin_fenlei_title));
                break;
            case FenLeiSonBean.TwoLine:
                PictureUtlis.loadImageViewHolder(mContext, item.getFenLeiSon_img_url(), R.drawable.default_image,
                        (ImageView) helper.getView(R.id.iv_item_shangpin_fenlei_tupian));
                helper.setText(R.id.tv_item_shangpin_fenlei_title, item.getFenLeiSon_title());
                helper.setText(R.id.tv_item_shangpin_fenlei_jiage, item.getFenLeiSon_jiage());
                helper.setText(R.id.tv_item_shangpin_fenlei_dizhi, item.getFenLeiSon_dizhi());
                CommonUtlis.setImageTitle(mContext, item.getFenLeiSon_leixin(), item.getFenLeiSon_title(),
                        (TextView) helper.getView(R.id.tv_item_shangpin_fenlei_title));
                break;
        }
    }
}
