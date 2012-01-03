package cn.kevin.wallpaper;


import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class LeafSettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	private ListPreference list4Amount;
	private ListPreference list4Speed;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		this.addPreferencesFromResource(R.xml.wallpaper_setting);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		pref.registerOnSharedPreferenceChangeListener(this);
		
		this.list4Amount = (ListPreference)this.findPreference("leaf_number");
		this.list4Speed = (ListPreference)this.findPreference("leaf_falling_speed");		
		this.setLeafFallingSpeedSummary(pref.getString("leaf_falling_speed", "20"));
		this.setLeafNumberSummary(pref.getString("leaf_number", "75"));
	}

	
	protected void onDestroy(){
		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();		
	}
	
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
		if(key.equals("leaf_number")){
			String value = list4Amount.getValue();
			this.setLeafNumberSummary(value);
			
		}else if(key.equals("leaf_falling_speed")){
			String value = list4Speed.getValue();
			this.setLeafFallingSpeedSummary(value);
		}
	}
	
	private void setLeafNumberSummary(String value){		
		String title = "";
		if(value.equals("100")){
			title = "���";
		}else if(value.equals("75")){
			title = "��ͨ";
		}else if(value.equals("60")){
			title = "����";
		}			
		list4Amount.setSummary("��ǰ������" + title);
	}
	
	private void setLeafFallingSpeedSummary(String value){
		String title = "";
		if(value.equals("10")){
			title = "�Ͽ�";
		}else if(value.equals("20")){
			title = "�е�";
		}else if(value.equals("50")){
			title = "����";
		}			
		list4Speed.setSummary("��ǰ�ٶȣ�" + title);
	}

}
