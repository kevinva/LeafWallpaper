package cn.kevin.wallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.airpush.android.Airpush;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		new Airpush(arg0, "31944", "1313749488383825972", false, true, true);
    }

}
