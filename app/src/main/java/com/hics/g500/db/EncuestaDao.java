package com.hics.g500.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ENCUESTA".
*/
public class EncuestaDao extends AbstractDao<Encuesta, Long> {

    public static final String TABLENAME = "ENCUESTA";

    /**
     * Properties of entity Encuesta.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Encuesta_id = new Property(0, Long.class, "encuesta_id", true, "ENCUESTA_ID");
        public final static Property Encuesta_nombre = new Property(1, String.class, "encuesta_nombre", false, "ENCUESTA_NOMBRE");
        public final static Property Encuesta_desc = new Property(2, String.class, "encuesta_desc", false, "ENCUESTA_DESC");
    }


    public EncuestaDao(DaoConfig config) {
        super(config);
    }
    
    public EncuestaDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ENCUESTA\" (" + //
                "\"ENCUESTA_ID\" INTEGER PRIMARY KEY ," + // 0: encuesta_id
                "\"ENCUESTA_NOMBRE\" TEXT," + // 1: encuesta_nombre
                "\"ENCUESTA_DESC\" TEXT);"); // 2: encuesta_desc
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ENCUESTA\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Encuesta entity) {
        stmt.clearBindings();
 
        Long encuesta_id = entity.getEncuesta_id();
        if (encuesta_id != null) {
            stmt.bindLong(1, encuesta_id);
        }
 
        String encuesta_nombre = entity.getEncuesta_nombre();
        if (encuesta_nombre != null) {
            stmt.bindString(2, encuesta_nombre);
        }
 
        String encuesta_desc = entity.getEncuesta_desc();
        if (encuesta_desc != null) {
            stmt.bindString(3, encuesta_desc);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Encuesta entity) {
        stmt.clearBindings();
 
        Long encuesta_id = entity.getEncuesta_id();
        if (encuesta_id != null) {
            stmt.bindLong(1, encuesta_id);
        }
 
        String encuesta_nombre = entity.getEncuesta_nombre();
        if (encuesta_nombre != null) {
            stmt.bindString(2, encuesta_nombre);
        }
 
        String encuesta_desc = entity.getEncuesta_desc();
        if (encuesta_desc != null) {
            stmt.bindString(3, encuesta_desc);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Encuesta readEntity(Cursor cursor, int offset) {
        Encuesta entity = new Encuesta( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // encuesta_id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // encuesta_nombre
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // encuesta_desc
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Encuesta entity, int offset) {
        entity.setEncuesta_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setEncuesta_nombre(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setEncuesta_desc(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Encuesta entity, long rowId) {
        entity.setEncuesta_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Encuesta entity) {
        if(entity != null) {
            return entity.getEncuesta_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Encuesta entity) {
        return entity.getEncuesta_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
