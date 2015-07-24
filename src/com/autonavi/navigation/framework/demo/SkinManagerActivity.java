
package com.autonavi.navigation.framework.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.autonavi.navigation.framework.R;
import com.autonavi.navigation.framework.theme.activity.SkinActivity;
import com.autonavi.navigation.framework.theme.configuration.SkinConfiguration;
import com.autonavi.navigation.framework.theme.manager.SkinManager;
import com.autonavi.navigation.framework.theme.model.Skin;

import java.io.File;

public class SkinManagerActivity extends SkinActivity {

    private ListView listview;

    private SkinManager mSkinManager;

    private Skin[] skins;

    private TextView mCurSkinTV;

    private String mStrCurSkinFormat;

    private String mStrCurSkinVersionFormat;

    private Skin mCurSkin;

    private String mStrDefaultSkin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStrCurSkinFormat = getResources().getString(R.string.cur_skin);
        mStrCurSkinVersionFormat = getResources().getString(R.string.cur_skin_version);
        mStrDefaultSkin = getResources().getString(R.string.default_skin);
        mSkinManager = SkinManager.getInstance(this);
        skins = mSkinManager.getSkins(SkinConfiguration.URL_SDCARD + File.separator
                + getString(R.string.app_name));
        setContentView(R.layout.skin_managet);
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(new SkinAdapter(skins));
        mCurSkinTV = (TextView) findViewById(R.id.tv_skin);
        mCurSkin = mSkinManager.getCurSkin();
        mCurSkinTV.setText(String.format(mStrCurSkinFormat, mCurSkin == null ? mStrDefaultSkin
                : mCurSkin.mName));
        ((TextView) findViewById(R.id.tv_skin_version)).setText(String.format(
                mStrCurSkinVersionFormat, mSkinManager.getMinVersion(),
                mSkinManager.getTargetVersion()));
    }

    @Override
    public void onInitSkin(Context context, Skin skin) {

    }

    @Override
    public boolean isChangeSkin(Skin skin) {

        return false;
    }

    @Override
    public void onChangeSkin(Context context, Skin skin) {
        this.recreate();
    }

    class SkinAdapter extends BaseAdapter {
        Skin[] mSkins;

        public SkinAdapter(Skin[] skins) {
            mSkins = skins;
        }

        @Override
        public int getCount() {
            return (mSkins != null ? mSkins.length : 0) + 1;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return null;
            }
            return mSkins != null ? mSkins[position - 1] : null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(SkinManagerActivity.this, R.layout.skin_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mBtnUsed = (Button) convertView.findViewById(R.id.btn_used);
                viewHolder.mIvIcSkin = (ImageView) convertView.findViewById(R.id.ic_skin);
                viewHolder.mTvSkinName = (TextView) convertView.findViewById(R.id.tv_skin_name);
                viewHolder.mTvSkinVersion = (TextView) convertView
                        .findViewById(R.id.tv_skin_version);
                convertView.setTag(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Skin skin = (Skin) getItem(position);

            if (skin != null && !TextUtils.isEmpty(skin.mIcPath)) {
                Bitmap ic = BitmapFactory.decodeFile(skin.mIcPath);
                if (ic != null) {
                    Bitmap icOld = (Bitmap) viewHolder.mIvIcSkin.getTag();
                    viewHolder.mIvIcSkin.setImageBitmap(ic);
                    viewHolder.mIvIcSkin.setTag(ic);
                    if (icOld != null && !icOld.isRecycled()) {
                        icOld.recycle();
                    }
                }
            }
            viewHolder.mTvSkinName.setText(skin == null ? mStrDefaultSkin : skin.mName);
            viewHolder.mTvSkinVersion.setText("version:" + (skin == null ? "*" : skin.mVersion));
            viewHolder.mBtnUsed.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (skin == null || mSkinManager.isSkinAvailable(skin)) {
                        mSkinManager.changeSkin(skin);
                        mCurSkinTV.setText(String.format(mStrCurSkinFormat,
                                skin == null ? mStrDefaultSkin : skin.mName));
                    } else {
                        Toast.makeText(SkinManagerActivity.this,
                                "Version does not conform to the！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        ImageView mIvIcSkin;

        TextView mTvSkinVersion;

        TextView mTvSkinName;

        Button mBtnUsed;
    }
}
