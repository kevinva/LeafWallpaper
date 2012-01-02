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
	private float bounceOffset;
	private boolean touched;

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
		bounceOffset = 5.0f;
		touched = false;

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

	public boolean isTouched(){
		return touched;
	}
	
	public void setTouched(boolean flag){
		this.touched = flag;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public void drawLeaf(Canvas canvas, Paint paint){		
		canvas.drawBitmap(bitmap, x, y, paint);
	}	
	
	public void handleFalling(){
		this.y += this.speedY;		
		if(this.y >= this.heightBound){
			this.y = -this.bitmap.getHeight();
		}
	}
	
	public void handleTouched(float touchX, float touchY){			
		float centerX = this.x + this.bitmap.getWidth() / 2.0f;
		float centerY = this.y + this.bitmap.getHeight() / 2.0f;

		if(centerX <= touchX){
			this.x -= this.bounceOffset;
			if(centerY <= touchY){
				this.y -= this.bounceOffset;
			}else{
				this.y += this.bounceOffset;
			}
			
		}else{
			this.x += this.bounceOffset;
			if(centerY <= touchY){
				this.y -= this.bounceOffset;
			}else{
				this.y += this.bounceOffset;
			}
		}

		this.bounceOffset *= 0.8;
		if(this.bounceOffset <= 1.0){
			touched = false;
			this.bounceOffset = 1.0f;
		}	
		
	}
	
}
