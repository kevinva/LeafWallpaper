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
	private ListPreference list4MovingDirection;
	private ListPreference list4Color;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		this.addPreferencesFromResource(R.xml.wallpaper_setting);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		pref.registerOnSharedPreferenceChangeListener(this);
		
		this.list4Amount = (ListPreference)this.findPreference("leaf_number");
		this.list4Speed = (ListPreference)this.findPreference("leaf_falling_speed");
		this.list4MovingDirection = (ListPreference)this.findPreference("leaf_moving_direction");
		this.list4Color = (ListPreference)this.findPreference("leaf_color");
		this.setLeafFallingSpeedSummary(pref.getString("leaf_falling_speed", "20"));
		this.setLeafNumberSummary(pref.getString("leaf_number", "50"));
		this.setLeafMovingDirectionSummary(pref.getString("leaf_moving_direction", "0"));
		this.setLeafColorSummary(pref.getString("leaf_color", "0"));
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
			
		}
	}
	
	private void setLeafNumberSummary(String value){		
		String title = "";
		if(value.equals("100")){
			title = "���";
		}else if(value.equals("50")){
			title = "��ͨ";
		}else if(value.equals("20")){
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
	
	private void setLeafMovingDirectionSummary(String value){
		String title = "";
		if(value.equals("0")){
			title = "����";
		}else if(value.equals("1")){
			title = "����";
		}		
		list4MovingDirection.setSummary("��ǰ����" + title);
	}
	
	private void setLeafColorSummary(String value){
		String title = "";
		if(value.equals("0")){
			title = "���";
		}else if(value.equals("1")){
			title = "��";
		}else if(value.equals("2")){
			title = "��";
		}else if(value.equals("3")){
			title = "��";
		}
		this.list4Color.setSummary("��ǰ��ɫ��" + title);
	}

}
