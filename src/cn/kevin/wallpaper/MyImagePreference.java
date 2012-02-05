package cn.kevin.wallpaper;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyImagePreference extends Preference {
	
	private PreferenceActivity parent;
	private ImageView imgV;
	private TextView textV;
	
	public int mImage;
	public String title;

	public MyImagePreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public MyImagePreference(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public MyImagePreference(Context context){
		super(context);
	}
	
	void setActivity(PreferenceActivity activity){
		this.parent = activity;
	}
	
	public boolean isPersistent(){
		return false;
	}
	
	protected void onBindView(View view){
		super.onBindView(view);
		
		this.imgV = (ImageView)view.findViewById(R.id.recommend_image);
		this.textV = (TextView)view.findViewById(R.id.recommend_text);
		this.imgV.setImageResource(this.mImage);
		this.textV.setText(this.title);
	}

}
