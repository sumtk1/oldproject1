package com.gloiot.hygo.server.db.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.gloiot.hygo.server.db.entity.DBPictureList;
import com.gloiot.hygo.server.db.entity.DBUserInfo;

import com.gloiot.hygo.server.db.gen.DBPictureListDao;
import com.gloiot.hygo.server.db.gen.DBUserInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig dBPictureListDaoConfig;
    private final DaoConfig dBUserInfoDaoConfig;

    private final DBPictureListDao dBPictureListDao;
    private final DBUserInfoDao dBUserInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        dBPictureListDaoConfig = daoConfigMap.get(DBPictureListDao.class).clone();
        dBPictureListDaoConfig.initIdentityScope(type);

        dBUserInfoDaoConfig = daoConfigMap.get(DBUserInfoDao.class).clone();
        dBUserInfoDaoConfig.initIdentityScope(type);

        dBPictureListDao = new DBPictureListDao(dBPictureListDaoConfig, this);
        dBUserInfoDao = new DBUserInfoDao(dBUserInfoDaoConfig, this);

        registerDao(DBPictureList.class, dBPictureListDao);
        registerDao(DBUserInfo.class, dBUserInfoDao);
    }
    
    public void clear() {
        dBPictureListDaoConfig.getIdentityScope().clear();
        dBUserInfoDaoConfig.getIdentityScope().clear();
    }

    public DBPictureListDao getDBPictureListDao() {
        return dBPictureListDao;
    }

    public DBUserInfoDao getDBUserInfoDao() {
        return dBUserInfoDao;
    }

}
