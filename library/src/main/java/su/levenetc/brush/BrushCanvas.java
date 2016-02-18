package su.levenetc.brush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by eleven on 10/11/2015.
 */
public class BrushCanvas extends View {

	private Brush brush = new Brush(getContext());
	BrushController brushController = new BrushController(brush);
	private Bitmap bufferBitmap;
	private Canvas bufferCanvas;
	private Paint paint = new Paint();

	public BrushCanvas(Context context) {
		super(context);
		config();
	}

	public BrushCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		config();
	}

	private void config() {

		brushController.setBrushCanvas(BrushCanvas.this);

		getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override public void onGlobalLayout() {
				getViewTreeObserver().removeOnGlobalLayoutListener(this);

				postDelayed(new Runnable() {
					@Override public void run() {
						bufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
						bufferCanvas = new Canvas(bufferBitmap);
						brushController.start(getContext());
					}
				}, 500);
			}
		});
	}

	@Override protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (brushController.isStarted()) {
			brushController.update();
			brush.onDraw(bufferCanvas);
			canvas.drawBitmap(bufferBitmap, 0, 0, paint);
//			postInvalidateDelayed(1000 / 60);
		} else {
			if (bufferBitmap != null) canvas.drawBitmap(bufferBitmap, 0, 0, paint);
		}
	}
}
