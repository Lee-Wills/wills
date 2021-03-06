package com.wills.help.base;

import android.support.multidex.MultiDexApplication;

import com.wills.help.db.bean.UserInfo;
import com.wills.help.db.manager.UserInfoHelper;
import com.wills.help.message.controller.EaseUI;
import com.wills.help.utils.AppConfig;
import com.wills.help.utils.SharedPreferencesUtils;
import com.wills.help.utils.StringUtils;

/**
 * com.wills.help
 * Created by lizhaoyong
 * 2016/11/7.
 */

public class App extends MultiDexApplication {
    private static App app;
    private static UserInfo user;
    private static boolean isLogin = false;

    @Override
    public void onCreate() {
        super.onCreate();
        app = (App) getApplicationContext();
        EaseUI.getInstance().init(app,null);
        user = getUser();
    }

    public static App getApp(){
        return app;
    }

    public UserInfo getUser() {
        if (user == null){
            String userId = (String) SharedPreferencesUtils.getInstance().get(AppConfig.SP_USER,"");
            if (!StringUtils.isNullOrEmpty(userId)){
                return UserInfoHelper.getInstance().queryById(userId);
            }
        }
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
        UserInfoHelper.getInstance().insertData(user).subscribe();
    }

    public void removeUser(){
        UserInfoHelper.getInstance().deleteData(user).subscribe();
    }
    public void setIsLogin(boolean isLogin){
        App.isLogin = isLogin;
    }

    public boolean getIsLogin(){
        if (isLogin == false){
            if (StringUtils.isNullOrEmpty((String) SharedPreferencesUtils.getInstance().get(AppConfig.SP_USER,"")))
                setIsLogin(false);
            else
                setIsLogin(true);
        }
        return isLogin;
    }

    public void exitApp(){
        SharedPreferencesUtils.getInstance().remove(AppConfig.SP_USER);
        removeUser();
        setIsLogin(false);
    }

}
