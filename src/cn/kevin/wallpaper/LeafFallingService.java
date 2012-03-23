package cn.kevin.wallpaper;

import java.util.ArrayList;
import java.util.Random;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class LeafFallingService extends WallpaperService {

	private WallpaperEngine myEngine;
	
	public void onCreate(){
		super.onCreate();
	}
	
	@Override
	public Engine onCreateEngine() {
		// TODO Auto-generated method stub
		System.out.println("Service: onCreateEngine");
		this.myEngine = new WallpaperEngine();
		return myEngine;
	}
	
	public void onDestroy(){
		this.myEngine = null;
		super.onDestroy();
	}
	
	private class WallpaperEngine extends Engine implements OnGestureListener, OnSharedPreferenceChangeListener{
		
		private ArrayList<Leaf> leafList;
		private Bitmap bitmap1;
		private Bitmap bitmap2;
		private Bitmap bitmap3;
		//private Bitmap backgroundBitmap1;
		//private Bitmap backgroundBitmap2;
		//private Bitmap backgroundBitmap3;
		private Bitmap currentBackgroundBitmap;
		private Paint paint;
		private int count;
		private int heightOfCanvas;
		private int widthOfCanvas;
		private float touchX;
		private float touchY;
		private int interval; //涉及叶子下落速度
		private int amount; //涉及叶子数量
		private boolean fallingDown; //叶子运动方向
		private float bgX = 0;  //绘制背景的x坐标
		//private float bgY = 0;	//绘制背景的y坐标
		private String colorFlag;
		private String backgroundFlag;		
		private Random rand;
		private GestureDetector detector;
		
		private static final int DRAW_MSG = 0;
		private static final int MAX_SIZE = 101;
		
		private Handler mHandler = new Handler(){
			
			public void handleMessage(Message msg){
				super.handleMessage(msg);	
				switch(msg.what){
				case DRAW_MSG:
					drawPaper();
					break;
				}
				
			}
			
		};

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			// TODO Auto-generated method stub
			super.onCreate(surfaceHolder);
			System.out.println("Engine: onCreate");	
			
			this.leafList = new ArrayList<Leaf>();	
			this.bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.flower1);
			this.bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.flower2);
			this.bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.flower3);
			this.paint = new Paint();
			this.paint.setAntiAlias(true);
			this.count = -1;
			this.rand = new Random();
			this.detector = new GestureDetector(this);
			this.touchX = -1.0f;
			this.touchY = -1.0f;
			
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(LeafFallingService.this);
			pref.registerOnSharedPreferenceChangeListener(this);
			String speedStr = pref.getString("leaf_falling_speed", "20");
			String amountStr = pref.getString("leaf_number", "50");				
			this.interval = Integer.parseInt(speedStr);
			this.amount = Integer.parseInt(amountStr);
			this.colorFlag = pref.getString("leaf_color", "0");
			this.backgroundFlag = pref.getString("paper_background", "0");
			String directionFlag = pref.getString("leaf_moving_direction", "0");
			if(directionFlag.equals("0")){
				this.fallingDown = true;
			}else{
				this.fallingDown = false;
			}
			
			this.setTouchEventsEnabled(true);
			
		}		
		

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			System.out.println("Engine: onDestroy");
			this.mHandler.removeMessages(DRAW_MSG);
			PreferenceManager.getDefaultSharedPreferences(LeafFallingService.this).unregisterOnSharedPreferenceChangeListener(this);
			
			super.onDestroy();
		}


		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			// TODO Auto-generated method stub
			super.onSurfaceChanged(holder, format, width, height);
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			super.onSurfaceCreated(holder);
			System.out.println("Engine: onSurfaceCreate");
			
			Canvas canvas = holder.lockCanvas();
			this.heightOfCanvas = canvas.getHeight();
			this.widthOfCanvas = canvas.getWidth();
			System.out.println("Width = " + widthOfCanvas + ", Height = " + heightOfCanvas);
			
			/*
			BitmapFactory.Options bitmapOpt = new BitmapFactory.Options();
			bitmapOpt.inSampleSize = 2;			
			Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.background1, bitmapOpt);
			this.backgroundBitmap1 = Bitmap.createScaledBitmap(temp, temp.getWidth() * 2, heightOfCanvas, true);				
			temp = BitmapFactory.decodeResource(getResources(), R.drawable.background2, bitmapOpt);
			this.backgroundBitmap2 = Bitmap.createScaledBitmap(temp, temp.getWidth() * 2, heightOfCanvas, true);			
			temp = BitmapFactory.decodeResource(getResources(), R.drawable.background3, bitmapOpt);
			this.backgroundBitmap3 = Bitmap.createScaledBitmap(temp, temp.getWidth() * 2, heightOfCanvas, true);				
			*/
			
			this.updateBackgroundForIndex(this.backgroundFlag);
			
			holder.unlockCanvasAndPost(canvas);			

			this.mHandler.sendEmptyMessage(DRAW_MSG);
		}		
		
		
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub		
			System.out.println("Engine: onSurfaceDestroyed");
			this.mHandler.removeMessages(DRAW_MSG);
			
			if(this.currentBackgroundBitmap != null){
				this.currentBackgroundBitmap.recycle();
				this.currentBackgroundBitmap = null;
			}
			if(this.bitmap1 != null){
				this.bitmap1.recycle();
				this.bitmap1 = null;
			}
			if(this.bitmap2 != null){
				this.bitmap2.recycle();
				this.bitmap2 = null;
			}
			if(this.bitmap3 != null){
				this.bitmap3.recycle();
				this.bitmap3 = null;
			}
			
			super.onSurfaceDestroyed(holder);
		}		

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset,
				float xOffsetStep, float yOffsetStep, int xPixelOffset,
				int yPixelOffset) {
			// TODO Auto-generated method stub
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep,
					xPixelOffset, yPixelOffset);
			
			//System.out.println("xOffset: " + xOffset + ", yOffset: " + yOffset);
			//System.out.println("xOffsetStep: " + xOffsetStep + ", yOffsetStep: " + yOffsetStep);
			System.out.println("xPixelOffset: " + xPixelOffset + ", yPixelOffset: " + yPixelOffset);			

			this.bgX = xPixelOffset;			
			
		}


		private void drawPaper(){			
			count++;
			if(count >= 10000){
				count = 0;
			}
			if(count % 10 == 0){
				if(this.leafList.size() < MAX_SIZE){
					Leaf l = null;
					Bitmap temp = bitmap1;
					if(colorFlag.equals("0")){
						int index = rand.nextInt(3) + 1;
						switch(index){
						case 1:
							temp = bitmap1;
							break;
						case 2:
							temp = bitmap2;
							break;
						case 3:
							temp = bitmap3;
							break;
						default:
							temp = bitmap1;
							break;					
						}
					}else if(colorFlag.equals("1")){
						temp = bitmap1;
					}else if(colorFlag.equals("2")){
						temp = bitmap2;
					}else if(colorFlag.equals("3")){
						temp = bitmap3;
					}
					l = new Leaf(temp, this.heightOfCanvas, this.widthOfCanvas);
					this.leafList.add(l);				
				}
			}
			//System.out.println("List size: " + this.leafList.size());			
			
			SurfaceHolder holder = this.getSurfaceHolder();
			Canvas canvas = holder.lockCanvas();	
			this.drawBackground(canvas);
			int size = Math.min(this.amount, this.leafList.size());			
			for(int i = 0; i < size; i++){
				Leaf l = this.leafList.get(i);
				if(l.isTouched()){
					l.handleTouched(touchX, touchY);
				}else{
					l.handleFalling(this.fallingDown);
				}		
				l.drawLeaf(canvas, paint);
				
			}	
			holder.unlockCanvasAndPost(canvas);
			this.mHandler.sendEmptyMessageDelayed(DRAW_MSG, this.interval);			
			//System.out.println("interval = " + interval + ", amount = " + amount);
		}		

		private void drawBackground(Canvas c){
			/*
			Bitmap currentBitmap = null;
			if(this.backgroundFlag.equals("0")){
				currentBitmap = this.backgroundBitmap1;				
			}else if(this.backgroundFlag.equals("1")){
				currentBitmap = this.backgroundBitmap2;
			}else if(this.backgroundFlag.equals("2")){
				currentBitmap = this.backgroundBitmap3;
			}
	*/
			c.drawBitmap(this.currentBackgroundBitmap, this.bgX, 0, paint);

		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			super.onTouchEvent(event);
			this.detector.onTouchEvent(event);
		}


		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			//System.out.println("onDown");
			
			touchX = e.getX();
			touchY = e.getY();
			int size  = Math.min(this.amount, this.leafList.size());
			for(int i = 0; i < size; i++){
				Leaf l = this.leafList.get(i);
				float centerX = l.getX() + l.getBitmap().getWidth() / 2.0f;
				float centerY = l.getY() + l.getBitmap().getHeight() / 2.0f;
				
				if(!l.isTouched()){
					if(Math.abs(centerX - touchX) <= 80 && Math.abs(centerY - touchY) <= 80 && centerX != touchX){
						l.setTouched(true);					
					}
				}				
			}
			
			return true;
		}


		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}


		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}


		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			
			
			return false;
		}


		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}


		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			//System.out.println("onFling");
			
			
			return false;
		}


		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			// TODO Auto-generated method stub
			
			if(key.equals("leaf_falling_speed")){
				String speedStr = sharedPreferences.getString(key, "20");
				this.interval = Integer.parseInt(speedStr);
				
			}else if(key.equals("leaf_number")){
				String amountStr = sharedPreferences.getString(key, "50");
				this.amount = Integer.parseInt(amountStr);
				
			}else if(key.equals("leaf_moving_direction")){
				String directionFlag = sharedPreferences.getString(key, "0");
				if(directionFlag.equals("0")){
					this.fallingDown = true;
				}else{
					this.fallingDown = false;
				}
			
			}else if(key.equals("leaf_color")){
				this.colorFlag = sharedPreferences.getString(key, "0");
				this.leafList.removeAll(leafList);				

			}else if(key.equals("paper_background")){
				this.backgroundFlag = sharedPreferences.getString(key, "0");				
				updateBackgroundForIndex(backgroundFlag);
				
			}
		}	
		
		
		private void updateBackgroundForIndex(String index){
			
			if(this.currentBackgroundBitmap != null && !this.currentBackgroundBitmap.isRecycled()){
				System.out.println("Bitmap will be recycled!");
				
				this.currentBackgroundBitmap.recycle();				
				this.currentBackgroundBitmap = null;
			}					
			
			//BitmapFactory.Options bitmapOpt = new BitmapFactory.Options();
			//bitmapOpt.inTempStorage = new byte[1024 * 1024 * 3];
			Bitmap temp = null;
			if(index.equals("1")){
				temp = BitmapFactory.decodeResource(getResources(), R.drawable.background2);
				//temp = BitmapFactory.decodeResource(getResources(), R.drawable.background2);
			}else if(index.equals("2")){
				temp = BitmapFactory.decodeResource(getResources(), R.drawable.background3);
				//temp = BitmapFactory.decodeResource(getResources(), R.drawable.background3);
			}else{
				temp = BitmapFactory.decodeResource(getResources(), R.drawable.background1);
				//temp = BitmapFactory.decodeResource(getResources(), R.drawable.background1);
			}
			
			this.currentBackgroundBitmap = Bitmap.createScaledBitmap(temp, temp.getWidth(), heightOfCanvas, true);
						
			
		}
		
		/*
		public int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) { 
			int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels); 
			int roundedSize; 
			if (initialSize <= 8 ) { 
				roundedSize = 1;
				while (roundedSize < initialSize) { 
					roundedSize <<= 1; 
				} 
			}else{ 
				roundedSize = (initialSize + 7) / 8 * 8; 
			}
		return roundedSize; 
		
		} 

		private int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) { 
		
			double w = options.outWidth; 
			double h = options.outHeight; 

			int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels)); 
			int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength)); 

			if (upperBound < lowerBound) { 
				// return the larger one when there is no overlapping zone. 
				return lowerBound; 
			} 

			if ((maxNumOfPixels == -1) && (minSideLength == -1)){ 
				return 1; 
			}else if(minSideLength == -1) { 
				return lowerBound; 
			} else { 
				return upperBound;
			} 
		}
		*/
		
	}
	
	

}
