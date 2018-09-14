package com.hics.g500;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.hics.g500.Library.Statics;
import com.hics.g500.Network.Definitions.Urls;
import com.hics.g500.Network.HicsWebService;
import com.hics.g500.Network.RetrofitEnvironments;
import com.hics.g500.db.DaoMaster;
import com.hics.g500.db.DaoSession;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;

/**
 * Created by david.barrera on 1/31/18.
 */

public class G500App extends Application {

    public static HicsWebService hicsService;
    static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"G500-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        hicsService= RetrofitEnvironments.createEnvironment(Urls.initStatics(this, Urls.STAGE_PRODUCTION));
        initDFolders();
    }

    public static HicsWebService getHicsService() {
        return hicsService;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    private void initDFolders(){
        File storagePath = new File(Environment.getExternalStorageDirectory(), Statics.NAME_FOLDER);
        try {
            if (!storagePath.exists()){
                storagePath.mkdirs();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
      //  MultiDex.install(this);
    }

}
