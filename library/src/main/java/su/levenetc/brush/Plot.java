package su.levenetc.brush;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by eleven on 10/11/2015.
 */
public class Plot {

	private final Paint paint = new Paint();
	private final float dx;
	private final float dy;
	private final float radius;
	private final float sizeFactor;
	private float prevX;
	private float prevY;
	public RectF dirtyRect = new RectF();

	public Plot(float sizeFactor, float dx, float dy, float radius) {
		this.dx = dx;
		this.dy = dy;
		this.radius = radius;
		this.sizeFactor = sizeFactor;
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		paint.setStrokeJoin(Paint.Join.BEVEL);
	}

	public void onDraw(Canvas canvas, float pressure, float x, float y, float angle, float velocity) {

		paint.setStrokeWidth(sizeFactor * radius * pressure);

		float angleFactor = 2f;
		float angleDiffX = angleFactor * pressure;
		float angleDiffY = angleFactor * pressure;

		float pointX = x + dx * angleDiffX;
		float pointY = y + dy * angleDiffY;

		if (prevX != 0) {
			paint.setAlpha((int) (255 * pressure));
			canvas.drawLine(prevX, prevY, pointX, pointY, paint);
		}

		configDirtyRegion(pointX, pointY);

		prevX = pointX;
		prevY = pointY;
	}

	private void configDirtyRegion(float pointX, float pointY) {
		dirtyRect.set(
				Math.min(prevX, pointX),
				Math.min(prevY, pointY),
				Math.max(prevX, pointX),
				Math.max(prevY, pointY)
		);
	}
}