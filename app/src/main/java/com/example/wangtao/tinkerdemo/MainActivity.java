package com.example.wangtao.tinkerdemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestAppPermissions();
        repair();
        TextView tv = (TextView) findViewById(R.id.tv);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);
//        tv.setText("修复前");
//        tv.setText("修复后");
        tv.setText("Release包修复后");
    }

    private void requestAppPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    private void repair() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                installPatch();
            }else{
                Toast.makeText(MainActivity.this , "please allow permission" , Toast.LENGTH_SHORT).show();
            }
        }else{
            installPatch();
        }
    }

    private void installPatch() {
        String patchPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "tinker" + File.separator + "patch_signed_7zip.apk";
        File file = new File(patchPath);
        if (file.exists()) {
            Toast.makeText(MainActivity.this , "patch exist , start repair" , Toast.LENGTH_SHORT).show();
            TinkerInstaller.onReceiveUpgradePatch(this.getApplication(), patchPath);
            Toast.makeText(MainActivity.this , "successfully repair ,please restart application" , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this , "patch does not exist" , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                repair();
                break;
            default:
                break;
        }
    }
}
