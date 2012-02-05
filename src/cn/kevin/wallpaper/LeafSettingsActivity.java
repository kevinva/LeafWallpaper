package cn.kevin.wallpaper;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


public class LeafSettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	private ListPreference list4Amount;
	private ListPreference list4Speed;
	private ListPreference list4MovingDirection;
	private ListPreference list4Color;
	private ListPreference list4Bg;
	private MyImagePreference imagePreference1;
	private MyImagePreference imagePreference2;
	private MyImagePreference imagePreference3;
	private MyImagePreference imagePreference4;
	
	private String prevBgFile = null;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		this.addPreferencesFromResource(R.xml.wallpaper_setting);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		pref.registerOnSharedPreferenceChangeListener(this);
		
		this.list4Amount = (ListPreference)this.findPreference("leaf_number");
		this.list4Speed = (ListPreference)this.findPreference("leaf_falling_speed");
		this.list4MovingDirection = (ListPreference)this.findPreference("leaf_moving_direction");
		this.list4Color = (ListPreference)this.findPreference("leaf_color");
		this.list4Bg = (ListPreference)this.findPreference("paper_background");
		this.setLeafFallingSpeedSummary(pref.getString("leaf_falling_speed", "20"));
		this.setLeafNumberSummary(pref.getString("leaf_number", "50"));
		this.setLeafMovingDirectionSummary(pref.getString("leaf_moving_direction", "0"));
		this.setLeafColorSummary(pref.getString("leaf_color", "0"));
		
		this.prevBgFile = pref.getString("paper_background", "0");
		if(this.prevBgFile.equals("0")){
			this.list4Bg.setSummary("当前背景：默认");
		}else{
			this.list4Bg.setSummary("当前背景：" + this.prevBgFile);
		}		
		
		//this.imagePreference1 = (MyImagePreference)this.findPreference("recommend1");
		//this.imagePreference1.title = this.getResources().getString(R.string.recommend1_title);
		//this.imagePreference1.mImage = R.drawable.ic_launcher;
		this.imagePreference2 = (MyImagePreference)this.findPreference("recommend2");
		this.imagePreference2.title = this.getResources().getString(R.string.recommend2_title);
		this.imagePreference2.mImage = R.drawable.ic_launcher;		
		this.imagePreference3 = (MyImagePreference)this.findPreference("recommend3");
		this.imagePreference3.title = this.getResources().getString(R.string.recommend3_title);
		this.imagePreference3.mImage = R.drawable.ic_launcher;
		this.imagePreference4 = (MyImagePreference)this.findPreference("recommend4");
		this.imagePreference4.title = this.getResources().getString(R.string.recommend4_title);
		this.imagePreference4.mImage = R.drawable.ic_launcher;
		
		
	}

	
	protected void onDestroy(){
		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();		
	}	
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
		if(key.equals("leaf_number")){
			String value = list4Amount.getValue();
			this.setLeafNumberSummary(value);
			
		}else if(key.equals("leaf_falling_speed")){
			String value = list4Speed.getValue();
			this.setLeafFallingSpeedSummary(value);
			
		}else if(key.equals("leaf_moving_direction")){
			this.setLeafMovingDirectionSummary(this.list4MovingDirection.getValue());
		
		}else if(key.equals("leaf_color")){
			this.setLeafColorSummary(this.list4Color.getValue());
			
		}else if(key.equals("paper_background")){
			String value = this.list4Bg.getValue();
			
			if(value.equals("1")){
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				this.startActivityForResult(intent, 1);
			}else if(value.equals("0")){
				this.list4Bg.setSummary("当前背景：默认");
				this.prevBgFile = value;
			}
		}
	}
	
	private void setLeafNumberSummary(String value){		
		String title = "";
		if(value.equals("100")){
			title = "许多";
		}else if(value.equals("50")){
			title = "普通";
		}else if(value.equals("20")){
			title = "少量";
		}			
		list4Amount.setSummary("当前数量：" + title);
	}
	
	private void setLeafFallingSpeedSummary(String value){
		String title = "";
		if(value.equals("10")){
			title = "较快";
		}else if(value.equals("20")){
			title = "中等";
		}else if(value.equals("50")){
			title = "较慢";
		}			
		list4Speed.setSummary("当前速度：" + title);
	}
	
	private void setLeafMovingDirectionSummary(String value){
		String title = "";
		if(value.equals("0")){
			title = "向下";
		}else if(value.equals("1")){
			title = "向上";
		}		
		list4MovingDirection.setSummary("当前方向：" + title);
	}
	
	private void setLeafColorSummary(String value){
		String title = "";
		if(value.equals("0")){
			title = "随机";
		}else if(value.equals("1")){
			title = "黄";
		}else if(value.equals("2")){
			title = "红";
		}else if(value.equals("3")){
			title = "蓝";
		}
		this.list4Color.setSummary("当前颜色：" + title);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//System.out.println("resultCode: " + resultCode + ", requestCode: " + requestCode);
		
		if(resultCode == Activity.RESULT_OK){
			Uri uri = data.getData();
			System.out.println("Uri: " + uri);
			ContentResolver cr = this.getContentResolver();
			Cursor cursor = cr.query(uri, null, null, null, null);
			while(cursor.moveToNext()){			
				System.out.println("_id:" + cursor.getString(0) + ", path:" + cursor.getString(1) + 
						", size:" + cursor.getString(2) + ", name:" + cursor.getString(3));
				
				this.prevBgFile = uri.toString();
				this.list4Bg.setSummary("当前背景：" + cursor.getString(3));
				this.list4Bg.setValue(this.prevBgFile); 
			}			
		}else{
			this.list4Bg.setValue(this.prevBgFile);
		}
		
	}
}
