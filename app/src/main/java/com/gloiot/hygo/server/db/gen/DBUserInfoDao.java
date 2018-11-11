package com.gloiot.hygo.server.db.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.gloiot.hygo.server.db.entity.DBUserInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DBUSER_INFO".
*/
public class DBUserInfoDao extends AbstractDao<DBUserInfo, Long> {

    public static final String TABLENAME = "DBUSER_INFO";

    /**
     * Properties of entity DBUserInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Zhanghao = new Property(1, String.class, "zhanghao", false, "ZHANGHAO");
        public final static Property Nicheng = new Property(2, String.class, "nicheng", false, "NICHENG");
    };


    public DBUserInfoDao(DaoConfig config) {
        super(config);
    }
    
    public DBUserInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DBUSER_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ZHANGHAO\" TEXT NOT NULL UNIQUE ," + // 1: zhanghao
                "\"NICHENG\" TEXT);"); // 2: nicheng
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DBUSER_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DBUserInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getZhanghao());
 
        String nicheng = entity.getNicheng();
        if (nicheng != null) {
            stmt.bindString(3, nicheng);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DBUserInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getZhanghao());
 
        String nicheng = entity.getNicheng();
        if (nicheng != null) {
            stmt.bindString(3, nicheng);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public DBUserInfo readEntity(Cursor cursor, int offset) {
        DBUserInfo entity = new DBUserInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // zhanghao
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // nicheng
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DBUserInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setZhanghao(cursor.getString(offset + 1));
        entity.setNicheng(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DBUserInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DBUserInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
