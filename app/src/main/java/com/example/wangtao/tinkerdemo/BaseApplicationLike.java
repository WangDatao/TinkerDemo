package com.example.wangtao.tinkerdemo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * @author wangtao
 * @Description:
 * @date 2017/12/7 16:09
 */
@DefaultLifeCycle(application = "com.example.wangtao.tinkerdemo.BaseApplication",
flags = ShareConstants.TINKER_ENABLE_ALL)
public class BaseApplicationLike extends DefaultApplicationLike {
    public BaseApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);

        DefaultLoadReporter loadReporter = new DefaultLoadReporter(getApplication());
        DefaultPatchReporter patchReporter = new DefaultPatchReporter(getApplication());
        DefaultPatchListener patchListener = new DefaultPatchListener(getApplication());
        AbstractPatch upgradePatchProcessor = new UpgradePatch();

        TinkerInstaller.install(this , loadReporter,patchReporter,patchListener ,
                CusResultService.class, upgradePatchProcessor);
    }
}
