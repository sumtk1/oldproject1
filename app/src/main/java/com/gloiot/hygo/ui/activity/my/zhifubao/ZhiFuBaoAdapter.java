package com.gloiot.hygo.ui.activity.my.zhifubao;


import android.content.Context;

import com.gloiot.hygo.R;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.MultiItemCommonAdapter;
import com.zyd.wlwsdk.adapter.abslistview.MultiItemTypeSupport;

import java.util.List;

/**
 * Created by hygo02 on 2018/1/15.
 */

public class ZhiFuBaoAdapter extends MultiItemCommonAdapter<ZhiFuBaoBean> {
    public ZhiFuBaoAdapter(Context context, List<ZhiFuBaoBean> datas) {
        super(context, datas, new MultiItemTypeSupport<ZhiFuBaoBean>() {
            @Override
            public int getLayoutId(int position, ZhiFuBaoBean zhiFuBaoBean) {
                switch (zhiFuBaoBean.zhifubao_wuxiaoka) {
                    case "正常":
                        return R.layout.item_my_zhifubao_youxiao;
                    case "无效卡":
                        return R.layout.item_my_zhifubao_wuxiao;
                    default:
                        return 0;
                }
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public int getItemViewType(int position, ZhiFuBaoBean zhiFuBaoBean) {
                if ("正常".equals(zhiFuBaoBean.zhifubao_wuxiaoka)) {
                    return ZhiFuBaoBean.SHOWTYPE_YOUXIAO;
                } else {
                    return ZhiFuBaoBean.SHOWTYPE_WUXIAO;
                }
            }
        });
    }

    @Override
    public void convert(ViewHolder holder, final ZhiFuBaoBean zhiFuBaoBean) {
        switch (holder.getLayoutId()) {
            case R.layout.item_my_zhifubao_youxiao:
                holder.setText(R.id.tv_alipay_zhanghao, zhiFuBaoBean.zhifubao_zhanghao);
                holder.setText(R.id.tv_alipay_xingming, zhiFuBaoBean.zhifubao_xingming);

                holder.setImageResource(R.id.img_alipay, R.mipmap.ic_wode_zhifubao_youxiao);
                holder.getView(R.id.rl_alipay).setBackgroundColor(mContext.getResources().getColor(R.color.cl_3faaf0));
                break;
            case R.layout.item_my_zhifubao_wuxiao:
                holder.setText(R.id.tv_alipay_wuxiao_title, "支付宝信息有误");
                holder.setText(R.id.tv_alipay_wuxiao_shuoming, "需删除后重新添加");
                holder.setText(R.id.tv_alipay_wuxiao_zhanghao, zhiFuBaoBean.zhifubao_zhanghao);

                holder.setImageResource(R.id.img_alipay, R.mipmap.ic_wode_zhifubao_wuxiao);
                holder.getView(R.id.rl_alipay).setBackgroundColor(mContext.getResources().getColor(R.color.cl_cccccc));
                break;
        }
    }

    //刷新数据UI界面
    public void shuaXinShuJu() {
        this.notifyDataSetChanged();
    }
}
