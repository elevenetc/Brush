package su.levenetc.brush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by eleven on 10/11/2015.
 */
public class BrushCanvas extends View {

	private IBrushController brushController;
	private Bitmap bufferBitmap;
	private Canvas bufferCanvas;
	private Paint paint = new Paint();
	private Rect dirtyRect = new Rect(0, 0, 0, 0);

	public BrushCanvas(Context context) {
		super(context);
	}

	public BrushCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setController(IBrushController brushController) {

		this.brushController = brushController;
		brushController.setBrushCanvas(this);

		getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override public void onGlobalLayout() {
				getViewTreeObserver().removeOnGlobalLayoutListener(this);

				postDelayed(new Runnable() {
					@Override public void run() {
						bufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
						bufferCanvas = new Canvas(bufferBitmap);
						BrushCanvas.this.brushController.start();
					}
				}, 500);
			}
		});
	}

	public void reset() {
		bufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		bufferCanvas = new Canvas(bufferBitmap);
	}

	@Override protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (brushController != null && brushController.isStarted()) {
			brushController.update();
			Brush brush = brushController.getBrush();
			brush.onDraw(bufferCanvas);

			if (Config.USE_DIRTY) {
				dirtyRect.set(
						(int) brush.dirtyRect.left,
						(int) brush.dirtyRect.right,
						(int) brush.dirtyRect.top,
						(int) brush.dirtyRect.bottom
				);
				canvas.drawBitmap(bufferBitmap, dirtyRect, brush.dirtyRect, paint);
			} else {
				canvas.drawBitmap(bufferBitmap, 0, 0, paint);
			}

		} else {
			if (bufferBitmap != null) canvas.drawBitmap(bufferBitmap, 0, 0, paint);
		}
	}
}
