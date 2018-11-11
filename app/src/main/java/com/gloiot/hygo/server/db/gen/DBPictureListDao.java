package com.gloiot.hygo.server.db.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.gloiot.hygo.server.db.entity.DBPictureList;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DBPICTURE_LIST".
*/
public class DBPictureListDao extends AbstractDao<DBPictureList, Long> {

    public static final String TABLENAME = "DBPICTURE_LIST";

    /**
     * Properties of entity DBPictureList.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Code = new Property(1, String.class, "code", false, "CODE");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Info = new Property(3, String.class, "info", false, "INFO");
        public final static Property Path = new Property(4, String.class, "path", false, "PATH");
        public final static Property Type = new Property(5, String.class, "type", false, "TYPE");
    };


    public DBPictureListDao(DaoConfig config) {
        super(config);
    }
    
    public DBPictureListDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DBPICTURE_LIST\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CODE\" TEXT NOT NULL UNIQUE ," + // 1: code
                "\"NAME\" TEXT," + // 2: name
                "\"INFO\" TEXT," + // 3: info
                "\"PATH\" TEXT," + // 4: path
                "\"TYPE\" TEXT);"); // 5: type
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DBPICTURE_LIST\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DBPictureList entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getCode());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String info = entity.getInfo();
        if (info != null) {
            stmt.bindString(4, info);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(5, path);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(6, type);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DBPictureList entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getCode());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String info = entity.getInfo();
        if (info != null) {
            stmt.bindString(4, info);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(5, path);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(6, type);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public DBPictureList readEntity(Cursor cursor, int offset) {
        DBPictureList entity = new DBPictureList( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // code
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // info
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // path
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // type
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DBPictureList entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCode(cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setInfo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPath(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setType(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DBPictureList entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DBPictureList entity) {
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