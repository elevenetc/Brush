package su.levenetc.brush;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Eugene Levenetc on 19/02/2016.
 */
public abstract class Plot {

	protected Paint paint = new Paint();
	protected final float dx;
	protected final float dy;
	protected final float baseRadius;
	protected final float sizeFactor;
	protected float prevX;
	protected float prevY;
	protected float alphaFactor = 1;
	public RectF dirtyRect = new RectF();

	public Plot(float sizeFactor, float dx, float dy, float baseRadius) {
		this.dx = dx;
		this.dy = dy;
		this.baseRadius = baseRadius;
		this.sizeFactor = sizeFactor;

		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		paint.setDither(true);
	}

	public void setColor(int color) {
		paint.setColor(color);
	}

	public void setAlphaFactor(float alphaFactor) {
		this.alphaFactor = alphaFactor;
	}

	protected void configDirtyRegion(float pointX, float pointY) {
		dirtyRect.set(
				Math.min(prevX, pointX),
				Math.min(prevY, pointY),
				Math.max(prevX, pointX),
				Math.max(prevY, pointY)
		);
	}

	public void onDraw(Canvas canvas, float pressure, float x, float y, float angle, float velocity) {
		paint.setStrokeWidth(sizeFactor * baseRadius * pressure);

		float angleFactor = 2f;
		float angleDiffX = angleFactor * pressure;
		float angleDiffY = angleFactor * pressure;

		float pointX = x + dx * angleDiffX;
		float pointY = y + dy * angleDiffY;

		if (prevX != 0) {
			drawImplementation(canvas, pressure, pointX, pointY);
		} else {
			firstDraw(canvas, pressure, pointX, pointY);
		}

		configDirtyRegion(pointX, pointY);

		prevX = pointX;
		prevY = pointY;
	}

	protected abstract void drawImplementation(Canvas canvas, float pressure, float pointX, float pointY);

	protected abstract void firstDraw(Canvas canvas, float pressure, float pointX, float pointY);

}
