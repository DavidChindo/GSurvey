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
 * DAO for table "OPCIONES".
*/
public class OpcionesDao extends AbstractDao<Opciones, Long> {

    public static final String TABLENAME = "OPCIONES";

    /**
     * Properties of entity Opciones.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Pregunta_id = new Property(1, Integer.class, "pregunta_id", false, "PREGUNTA_ID");
        public final static Property Encuenta_id = new Property(2, Integer.class, "encuenta_id", false, "ENCUENTA_ID");
        public final static Property Opcion_id = new Property(3, Integer.class, "opcion_id", false, "OPCION_ID");
        public final static Property Opcion_contenido = new Property(4, String.class, "opcion_contenido", false, "OPCION_CONTENIDO");
    }


    public OpcionesDao(DaoConfig config) {
        super(config);
    }
    
    public OpcionesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"OPCIONES\" (" + //
                "\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"PREGUNTA_ID\" INTEGER," + // 1: pregunta_id
                "\"ENCUENTA_ID\" INTEGER," + // 2: encuenta_id
                "\"OPCION_ID\" INTEGER," + // 3: opcion_id
                "\"OPCION_CONTENIDO\" TEXT);"); // 4: opcion_contenido
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"OPCIONES\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Opciones entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer pregunta_id = entity.getPregunta_id();
        if (pregunta_id != null) {
            stmt.bindLong(2, pregunta_id);
        }
 
        Integer encuenta_id = entity.getEncuenta_id();
        if (encuenta_id != null) {
            stmt.bindLong(3, encuenta_id);
        }
 
        Integer opcion_id = entity.getOpcion_id();
        if (opcion_id != null) {
            stmt.bindLong(4, opcion_id);
        }
 
        String opcion_contenido = entity.getOpcion_contenido();
        if (opcion_contenido != null) {
            stmt.bindString(5, opcion_contenido);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Opciones entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer pregunta_id = entity.getPregunta_id();
        if (pregunta_id != null) {
            stmt.bindLong(2, pregunta_id);
        }
 
        Integer encuenta_id = entity.getEncuenta_id();
        if (encuenta_id != null) {
            stmt.bindLong(3, encuenta_id);
        }
 
        Integer opcion_id = entity.getOpcion_id();
        if (opcion_id != null) {
            stmt.bindLong(4, opcion_id);
        }
 
        String opcion_contenido = entity.getOpcion_contenido();
        if (opcion_contenido != null) {
            stmt.bindString(5, opcion_contenido);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Opciones readEntity(Cursor cursor, int offset) {
        Opciones entity = new Opciones( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // pregunta_id
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // encuenta_id
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // opcion_id
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // opcion_contenido
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Opciones entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPregunta_id(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setEncuenta_id(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setOpcion_id(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setOpcion_contenido(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Opciones entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Opciones entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Opciones entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
