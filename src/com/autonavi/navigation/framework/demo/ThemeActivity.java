
package com.autonavi.navigation.framework.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.autonavi.navigation.framework.R;
import com.autonavi.navigation.framework.theme.activity.SkinActivity;
import com.autonavi.navigation.framework.theme.model.Skin;

public class ThemeActivity extends SkinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
    }

    public void onClick(View view) {
        startActivity(new Intent(this, SkinManagerActivity.class));
    }

    @Override
    public void onInitSkin(Context context, Skin skin) {
        Log.d("MainActivity", "onInitSkin");
    }

    @Override
    public boolean isChangeSkin(Skin skin) {
        return true;
    }

    @Override
    public void onChangeSkin(Context context, Skin skin) {
        this.recreate();
    }
}
