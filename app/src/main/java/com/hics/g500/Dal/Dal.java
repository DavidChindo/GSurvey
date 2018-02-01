package com.hics.g500.Dal;

import com.hics.g500.G500App;
import com.hics.g500.db.User;

import java.util.List;

/**
 * Created by david.barrera on 1/31/18.
 */

public class Dal {

    public static User user(){
        try{
            List<User> user = G500App.getDaoSession().getUserDao().queryBuilder().list();
            return user != null && user.size() > 0 ? user.get(0) : null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
