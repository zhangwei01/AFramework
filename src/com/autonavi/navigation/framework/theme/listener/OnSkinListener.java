
package com.autonavi.navigation.framework.theme.listener;

import android.content.Context;

import com.autonavi.navigation.framework.theme.model.Skin;

/**
 * 皮肤加载监听接口
 * 
 * @author fei.bao
 */
public interface OnSkinListener {

    /**
     * 生成相应的Resources后回调
     * 
     * @param context
     * @param skin 皮肤包
     */
    void onInitSkin(Context context, Skin skin);

    /**
     * 调用changeSkin后回调，此时Activiy需要刷新,建议使用recreate(); 此方法主要是用于通知界面
     * 
     * @param context
     * @param skin 皮肤包
     */
    void onChangeSkin(Context context, Skin skin);

    /**
     * 验证此皮肤包是否符合此界面
     * 
     * @param skin 皮肤包
     * @return true此皮肤包可以在此界面上使用,false则不符合
     */
    boolean isChangeSkin(Skin skin);
}
