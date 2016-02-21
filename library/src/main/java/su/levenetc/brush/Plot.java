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
	protected float angleFactor = 2;
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

	public void reset() {
		prevX = 0;
		prevY = 0;
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

	protected void handleSize(float pressure) {
		paint.setStrokeWidth(sizeFactor * baseRadius * pressure);
	}

	protected float distortY(float y, float pressure) {
		float angleDiffY = angleFactor * pressure;
		return y + dy * angleDiffY;
	}

	protected float distortX(float x, float pressure) {
		float angleDiffX = angleFactor * pressure;
		return x + dx * angleDiffX;
	}

	protected void handleAlpha(float pressure) {
		paint.setAlpha((int) (255 * pressure * alphaFactor));
	}

	public void onDraw(Canvas canvas, float pressure, float x, float y, float velocity) {

		handleAlpha(pressure);
		handleSize(pressure);

		float pointX = distortX(x, pressure);
		float pointY = distortY(y, pressure);

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
