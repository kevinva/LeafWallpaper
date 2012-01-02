package cn.kevin.wallpaper;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
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
		this.myEngine = new WallpaperEngine();
		return myEngine;
	}
	
	public void onDestroy(){
		this.myEngine = null;
		super.onDestroy();
	}
	
	private class WallpaperEngine extends Engine implements OnGestureListener{
		
		private ArrayList<Leaf> leafList;
		private Bitmap bitmap1;
		private Bitmap bitmap2;
		private Bitmap bitmap3;
		private Bitmap backgroundBitmap;
		private Paint paint;
		private int count;
		private int heightOfCanvas;
		private Random rand;
		private GestureDetector detector;
		private float touchX;
		private float touchY;
		
		private static final int DRAW_MSG = 0;
		private static final int MAX_SIZE = 100;
		
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
			this.backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
			this.paint = new Paint();
			this.paint.setAntiAlias(true);
			this.count = -1;
			this.rand = new Random();
			this.detector = new GestureDetector(this);
			this.touchX = -1.0f;
			this.touchY = -1.0f;
			this.setTouchEventsEnabled(true);
		}		
		

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			System.out.println("Engine: onDestroy");
			this.mHandler.removeMessages(DRAW_MSG);
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
			holder.unlockCanvasAndPost(canvas);
			
			this.mHandler.sendEmptyMessage(DRAW_MSG);
		}		
		
		
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub		
			System.out.println("Engine: onSurfaceDestroyed");
			this.mHandler.removeMessages(DRAW_MSG);
			super.onSurfaceDestroyed(holder);
		}

		private void drawPaper(){			
			count++;
			if(count >= 10000){
				count = 0;
			}
			if(count % 10 == 0){
				if(this.leafList.size() < MAX_SIZE){
					Leaf l = null;
					int index = rand.nextInt(3) + 1;
					switch(index){
					case 1:
						l = new Leaf(bitmap1, this.heightOfCanvas);
						break;
					case 2:
						l = new Leaf(bitmap2, this.heightOfCanvas);
						break;
					case 3:
						l = new Leaf(bitmap3, this.heightOfCanvas);
						break;
					default:
						l = new Leaf(bitmap1, this.heightOfCanvas);
						break;					
					}
					this.leafList.add(l);				
				}
			}
			//System.out.println("List size: " + this.leafList.size());			
			
			SurfaceHolder holder = this.getSurfaceHolder();
			Canvas canvas = holder.lockCanvas();	
			canvas.drawBitmap(backgroundBitmap, 0, 0, paint);
			for(int i = 0; i < this.leafList.size(); i++){
				Leaf l = this.leafList.get(i);
				if(l.isTouched()){
					l.handleTouched(touchX, touchY);
				}else{
					l.handleFalling();
				}		
				l.drawLeaf(canvas, paint);
				
			}	
			holder.unlockCanvasAndPost(canvas);
			this.mHandler.sendEmptyMessageDelayed(DRAW_MSG, 15L);			
			
		}		


		@Override
		public void onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			super.onTouchEvent(event);
			this.detector.onTouchEvent(event);
		}


		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			System.out.println("onDown");
			
			touchX = e.getX();
			touchY = e.getY();	
			for(int i = 0; i < this.leafList.size(); i++){
				Leaf l = this.leafList.get(i);
				float centerX = l.getX() + l.getBitmap().getWidth() / 2.0f;
				float centerY = l.getY() + l.getBitmap().getHeight() / 2.0f;
				
				if(Math.abs(centerX - touchX) <= 80 && Math.abs(centerY - touchY) <= 80 && centerX != touchX){
					l.setTouched(true);					
				}				
			}
			
			return true;
		}


		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}


		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			
			
			return false;
		}


		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			System.out.println("onFling");
			
			return false;
		}				
		
	}

}
