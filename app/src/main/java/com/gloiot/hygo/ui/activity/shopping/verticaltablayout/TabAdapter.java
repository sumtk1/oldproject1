package com.gloiot.hygo.ui.activity.shopping.verticaltablayout;


import com.gloiot.hygo.ui.activity.shopping.verticaltablayout.widget.QTabView;

/**
 * @author chqiu
 *         Email:qstumn@163.com
 */
public interface TabAdapter {
    int getCount();

    int getBadge(int position);

    QTabView.TabIcon getIcon(int position);

    QTabView.TabTitle getTitle(int position);

    int getBackground(int position);
}
