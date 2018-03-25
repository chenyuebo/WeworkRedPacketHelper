package com.cyb.wework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cyb.wework.R;
import com.cyb.wework.utils.SystemInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_app_name;
    private TextView tv_app_version;

    private String appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initStatus();
    }

    private void initView() {
        tv_app_name = findViewById(R.id.tv_app_name);
        tv_app_version = findViewById(R.id.tv_app_version);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_start_watch_notification).setOnClickListener(this);
        findViewById(R.id.btn_setting).setOnClickListener(this);
    }

    private void initStatus() {
        tv_app_version.setText("v" + SystemInfo.getAppVersion(this));
        appName = SystemInfo.getAppName(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                final String text = "请在系统设置->无障碍服务中开启" + appName;
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_start_watch_notification:
                startActivity( new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                Toast.makeText(this, "请在通知使用权中启用" + appName, Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }
}
