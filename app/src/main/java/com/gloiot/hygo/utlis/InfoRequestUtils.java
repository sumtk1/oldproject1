package com.gloiot.hygo.utlis;

import android.content.SharedPreferences;

import com.gloiot.hygo.server.db.gen.DBPictureListDao;
import com.gloiot.hygo.server.db.DBHelper;
import com.gloiot.hygo.server.db.entity.DBPictureList;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.App;
import com.zyd.wlwsdk.server.network.OnDataListener;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * （无用类 可删除）
 * Created by hygo01 on 17/3/28.
 */

public class InfoRequestUtils {


    public static InfoRequestUtils instance;
    private RequestAction requestAction;
    protected SharedPreferences preferences;

    protected SharedPreferences.Editor editor;

    public static InfoRequestUtils getInstance() {
        if (instance == null) {
            instance = new InfoRequestUtils();
        }
        return instance;
    }

    private InfoRequestUtils() {
        requestAction = new RequestAction();
        preferences = SharedPreferencesUtils.getInstance().getSharedPreferences();
        editor = preferences.edit();
    }

    public void getPictureList() {
        requestAction.PictureList(new OnDataListener() {
            @Override
            public void onStart(int requestTag, int showLoad) {

            }

            @Override
            public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {

            }

            @Override
            public void onSuccess(int requestTag, JSONObject response, int showLoad) {
                L.e(response.toString());
                try {
                    if (response.getString("状态").equals("成功")) {
                        DBPictureListDao dbPictureListDao = DBHelper.getDaoSession(App.getInstance()).getDBPictureListDao();
                        DBPictureList dbPictureList;
                        if (Integer.parseInt(response.getString("条数")) > 0) {
                            JSONArray jsonArray = response.getJSONArray("列表");
                            dbPictureListDao.deleteAll();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                dbPictureList = new DBPictureList(null
                                        , JSONUtlis.getString(jsonObject, "代码")
                                        , JSONUtlis.getString(jsonObject, "名称")
                                        , JSONUtlis.getString(jsonObject, "简介")
                                        , JSONUtlis.getString(jsonObject, "地址")
                                        , JSONUtlis.getString(jsonObject, "类别"));

                                dbPictureListDao.insert(dbPictureList);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {

            }

            @Override
            public void onCancel(int requestTag, int showLoad) {

            }
        });
    }
}
