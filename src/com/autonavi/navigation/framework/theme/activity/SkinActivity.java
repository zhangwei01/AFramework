
package com.autonavi.navigation.framework.theme.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import com.autonavi.navigation.framework.theme.context.SkinContextWrapper;
import com.autonavi.navigation.framework.theme.listener.OnSkinListener;
import com.autonavi.navigation.framework.theme.manager.SkinManager;
import com.autonavi.navigation.framework.theme.model.Skin;

public abstract class SkinActivity extends Activity implements OnSkinListener {

    private Context context;

    //皮肤包管理器
    private SkinManager mSkinManager;

    /**
     * 设置自己的Context
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        if (context == null) {
            context = onCreateContext(newBase);
            if (context != null) {
                mSkinManager = SkinManager.getInstance(context);
                mSkinManager.addOnSkinListener(this);
                onCreateResources(context, mSkinManager.getCurSkin());
            }
        }
        super.attachBaseContext(context == null ? newBase : context);
    }

    /**
     * 返回应用原有的Context
     */
    @Override
    public Context getBaseContext() {
        Context context = super.getBaseContext();
        if (context instanceof SkinContextWrapper) {
            context = ((SkinContextWrapper) context).getBaseContext();
        }
        return context;
    }

    @Override
    public Resources getResources() {
        return context != null ? context.getResources() : super.getResources();
    }

    @Override
    protected void onDestroy() {
        if (mSkinManager != null) {
            mSkinManager.removeOnSkinListener(this);
        }
        super.onDestroy();
    }

    /**
     * 创建自己的Context
     * 
     * @param newBase
     * @return
     */
    public Context onCreateContext(Context newBase) {
        return new SkinContextWrapper(newBase);
    }

    /**
     * 创建Resources
     * 
     * @param context
     * @param skinName
     */
    public void onCreateResources(Context context, Skin skin) {
        if (context instanceof SkinContextWrapper && isChangeSkin(skin)) {
            ((SkinContextWrapper) context).setSkinName(skin);
            ((SkinContextWrapper) context).reset();
            onInitSkin(context, skin);
        }
    }

    /**
     * 验证此皮肤包是否符合此界面，每个需要换肤的界面都需覆盖
     * 
     * @param skin 皮肤包
     * @return true此皮肤包可以在此界面上使用,false则不符合
     */
    @Override
    public boolean isChangeSkin(Skin skin) {
        if (skin == null) {
            return true;
        }
        return false;
    }

}
