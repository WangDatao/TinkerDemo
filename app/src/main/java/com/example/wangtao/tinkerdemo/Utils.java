package com.example.wangtao.tinkerdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


/**
 * @author wangtao
 * @Description:
 * @date 2017/12/11 15:05
 */
class Utils {

    private static boolean background;

    public static boolean isBackground() {
        return background;
    }

    public static void setBackground(boolean background) {
        Utils.background = background;
    }

    public static class ScreenState {
        private static final String TAG ="ScreenState" ;

        public interface IOnScreenOff {
            void onScreenOff();
        }

        public ScreenState(final Context context, final IOnScreenOff onScreenOffInterface) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);

            context.registerReceiver(new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent in) {
                    String action = in == null ? "" : in.getAction();
                    Log.i(TAG, String.format("ScreenReceiver action [%s] ", action));
                    if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                        if (onScreenOffInterface != null) {
                            onScreenOffInterface.onScreenOff();
                        }
                    }
                    context.unregisterReceiver(this);
                }
            }, filter);
        }
    }
}
