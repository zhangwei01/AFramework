
package com.autonavi.navigation.framework.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.autonavi.navigation.framework.R;
import com.autonavi.navigation.framework.base.activity.AnActivity;

/**
 * Demo
 * 
 * @author hao.zhong
 */
public class DemoActivity extends AnActivity {

    private ListView mLvDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mLvDemo = (ListView) findViewById(R.id.lv_demo);
        String[] demos = new String[2];
        demos[0] = "plugin";
        demos[1] = "theme";
        mLvDemo.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                demos));
        mLvDemo.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                switch (arg2) {
                    case 0:
                        Toast.makeText(DemoActivity.this, "plugin", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        startActivity(new Intent(DemoActivity.this, ThemeActivity.class));
                        break;

                    default:
                        break;
                }
            }
        });
    }
}
