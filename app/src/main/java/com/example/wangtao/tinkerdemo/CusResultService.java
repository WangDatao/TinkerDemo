package com.example.wangtao.tinkerdemo;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.TinkerServiceInternals;

import java.io.File;

/**
 * @author wangtao
 * @Description:
 * @date 2017/12/11 15:01
 */
public class CusResultService extends DefaultTinkerResultService {
    private static final String TAG = "CusResultService";

    @Override
    public void onPatchResult(final PatchResult result) {
        if (result == null) {
            Log.e(TAG, "SampleResultService received null result!!!!");
            return;
        }
        Log.i(TAG, String.format("SampleResultService receive result: %s", result.toString()));

        //first, we want to kill the recover process
        TinkerServiceInternals.killTinkerPatchServiceProcess(getApplicationContext());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (result.isSuccess) {
                    Toast.makeText(getApplicationContext(), "patch success, please restart process", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "patch fail, please check reason", Toast.LENGTH_LONG).show();
                }
            }
        });
        // is success and newPatch, it is nice to delete the raw file, and restart at once
        // for old patch, you can't delete the patch file
        if (result.isSuccess) {
            deleteRawPatchFile(new File(result.rawPatchFilePath));

            //not like TinkerResultService, I want to restart just when I am at background!
            //if you have not install tinker this moment, you can use TinkerApplicationHelper api
            if (checkIfNeedKill(result)) {
                if (Utils.isBackground()) {
                    Log.i(TAG, "it is in background, just restart process");
                    restartProcess();
                } else {
                    //we can wait process at background, such as onAppBackground
                    //or we can restart when the screen off
                    Log.i(TAG, "tinker wait screen to restart process");
                    new Utils.ScreenState(getApplicationContext(), new Utils.ScreenState.IOnScreenOff() {
                        @Override
                        public void onScreenOff() {
                            restartProcess();
                        }
                    });
                }
            } else {
                Log.i(TAG, "I have already install the newly patch version!");
            }
        }
    }
    /**
     * you can restart your process through service or broadcast
     */
    private void restartProcess() {
        TinkerLog.i(TAG, "app is background now, i can kill quietly");
        //you can send service or broadcast intent to restart your process
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
