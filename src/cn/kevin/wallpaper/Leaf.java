package cn.kevin.wallpaper;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Leaf {
	
	private float x;
	private float y;
	private float speedY;
	private int heightBound;
	private Bitmap bitmap;	


	public Leaf(Bitmap source, int h){		
		Random rand = new Random();		
		speedY = 1 + rand.nextFloat() * 3;
		float scaleRate = rand.nextFloat() * 0.5f;
		if(scaleRate <= 0.01){
			scaleRate = 0.1f;
		}
		int width = (int)(source.getWidth() * scaleRate);
		int height = (int)(source.getHeight() * scaleRate);
		bitmap = Bitmap.createScaledBitmap(source, width, height, true);
		x = rand.nextFloat() * 400;
		y = -bitmap.getHeight();
		heightBound = h + bitmap.getHeight();
		
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}
	
	public void updateLeaf(Canvas canvas, Paint paint){
		this.y += this.speedY;		
		if(this.y >= this.heightBound){
			this.y = -this.bitmap.getHeight();
		}
		
		canvas.drawBitmap(bitmap, x, y, paint);
	}	
	
}
