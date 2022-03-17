package uk.co.dooapp.hae_launcher.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import uk.co.dooapp.hae_launcher.R;
import uk.co.dooapp.hae_launcher.models.BatteryInfo;

public class BatteryReceiver {
    private Context context;
    public BatteryReceiver(Context context){
        this.context = context;
    }


    public void registerBatteryReceiver(final MutableLiveData<BatteryInfo> batteryLevel){
        BatteryInfo batteryInfo = new BatteryInfo();
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int rawlevel = intent.getIntExtra("level", -1);
                System.out.println("Raw level: " + rawlevel);
                int scale = intent.getIntExtra("scale", -1);
                int level = -1;
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                System.out.println(status + " status");
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                batteryInfo.setLevel(level);
                batteryInfo.setStatus(status);
                batteryLevel.setValue(batteryInfo);

            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }
}
