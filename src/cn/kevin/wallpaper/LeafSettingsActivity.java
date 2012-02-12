package cn.kevin.wallpaper;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.admogo.AdMogoLayout;
import com.admogo.AdMogoManager;
import com.airpush.android.Airpush;



public class LeafSettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener, OnPreferenceClickListener{

	private ListPreference list4Amount;
	private ListPreference list4Speed;
	private ListPreference list4MovingDirection;
	private ListPreference list4Color;
	private ListPreference list4Bg;
	private MyImagePreference imagePreference1;
	private MyImagePreference imagePreference2;
	private MyImagePreference imagePreference3;

	
	//private String prevBgFile = null;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//Airpush
		new Airpush(this.getApplicationContext(), "31944", "1313749488383825972", false, true, true);
		
		this.addPreferencesFromResource(R.xml.wallpaper_setting);
		this.setContentView(R.layout.preference_main);
		
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
		this.setBackgroundSummary(pref.getString("paper_background", "0"));
		
		/*
		this.prevBgFile = pref.getString("paper_background", "0");
		if(this.prevBgFile.equals("0")){
			this.list4Bg.setSummary("当前背景：默认");
		}else{
			this.list4Bg.setSummary("当前背景：" + this.prevBgFile);
		}	
		*/	
		
		this.imagePreference1 = (MyImagePreference)this.findPreference("recommend1");
		this.imagePreference1.setOnPreferenceClickListener(this);
		this.imagePreference1.title = this.getResources().getString(R.string.recommend1_title);
		this.imagePreference1.mImage = R.drawable.recommend1;
		this.imagePreference2 = (MyImagePreference)this.findPreference("recommend2");
		this.imagePreference2.setOnPreferenceClickListener(this);
		this.imagePreference2.title = this.getResources().getString(R.string.recommend2_title);
		this.imagePreference2.mImage = R.drawable.recommend2;		
		this.imagePreference3 = (MyImagePreference)this.findPreference("recommend3");
		this.imagePreference3.setOnPreferenceClickListener(this);
		this.imagePreference3.title = this.getResources().getString(R.string.recommend3_title);
		this.imagePreference3.mImage = R.drawable.recommend3;
		
		//AdMogo
		this.addAdMogoLayout();
		
	}

	
	protected void onDestroy(){
		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
		AdMogoManager.clear();
		
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
			
			/*
			if(value.equals("1")){
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				this.startActivityForResult(intent, 1);
			}else if(value.equals("0")){
				this.list4Bg.setSummary("当前背景：默认");
				this.prevBgFile = value;
			}
			*/
			this.setBackgroundSummary(value);
		}
		
		
	}
	
	private void setLeafNumberSummary(String value){		
		String title = "";
		if(value.equals("100")){
			title = this.getString(R.string.leaf_number_many);
		}else if(value.equals("50")){
			title = this.getString(R.string.leaf_number_normal);
		}else if(value.equals("20")){
			title = this.getString(R.string.leaf_number_few);
		}			
		list4Amount.setSummary(this.getString(R.string.leaf_number_summary_prefix) + ": " + title);
	}
	
	private void setLeafFallingSpeedSummary(String value){
		String title = "";
		if(value.equals("10")){
			title = this.getString(R.string.leaf_speed_fast);
		}else if(value.equals("20")){
			title = this.getString(R.string.leaf_speed_medium);
		}else if(value.equals("50")){
			title = this.getString(R.string.leaf_speed_slow);
		}			
		list4Speed.setSummary(this.getString(R.string.leaf_falling_speed_summary_prefix) + ": " + title);
	}
	
	private void setLeafMovingDirectionSummary(String value){
		String title = "";
		if(value.equals("0")){
			title = this.getString(R.string.leaf_direction_downward);
		}else if(value.equals("1")){
			title = this.getString(R.string.leaf_direction_upward);
		}		
		list4MovingDirection.setSummary(this.getString(R.string.leaf_moving_direction_summary_prefix) + ": " + title);
	}
	
	private void setLeafColorSummary(String value){
		String title = "";
		if(value.equals("0")){
			title = this.getString(R.string.leaf_color_random);
		}else if(value.equals("1")){
			title = this.getString(R.string.leaf_color_yellow);
		}else if(value.equals("2")){
			title = this.getString(R.string.leaf_color_red);
		}else if(value.equals("3")){
			title = this.getString(R.string.leaf_color_blue);
		}
		this.list4Color.setSummary(this.getString(R.string.leaf_color_summary_prefix) + ": " + title);
	}
	
	private void setBackgroundSummary(String value){
		String title = "";
		if(value.equals("0")){
			title = this.getString(R.string.paper_bg_1);
		}else if(value.equals("1")){
			title = this.getString(R.string.paper_bg_2);
		}else if(value.equals("2")){
			title = this.getString(R.string.paper_bg_3);
		}
		this.list4Bg.setSummary(this.getString(R.string.paper_bg_summary_prefix) + ": " + title);
	}

	/*
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
	*/


	public boolean onPreferenceClick(Preference arg0) {
		// TODO Auto-generated method stub
		String urlStr = null;
		if(arg0 == this.imagePreference1){
			System.out.println("imagePreference1 clicked!");
			urlStr = this.getString(R.string.recommend1_url);
			this.openLink(urlStr);
		}else if(arg0 == this.imagePreference2){
			urlStr = this.getString(R.string.recommend2_url);
			this.openLink(urlStr);
		}else if(arg0 == this.imagePreference3){
			urlStr = this.getString(R.string.recommend3_url);
			this.openLink(urlStr);
		}
		
		return false;
	}
	
	private void openLink(String urlStr){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlStr));
		startActivity(intent);
	}
	
	//加载上部AdMogo广告
	private void addAdMogoLayout(){
		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.main_layout);
		System.out.println("MainLayout: " + mainLayout);
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    	AdMogoLayout adMogoLayoutCode = new AdMogoLayout(this, this.getResources().getString(R.string.AdMogo_USER_ID2));
    	mainLayout.addView(adMogoLayoutCode, 0, params);
	}
}
