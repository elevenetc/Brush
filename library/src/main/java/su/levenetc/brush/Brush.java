package su.levenetc.brush;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by eleven on 10/11/2015.
 */
public abstract class Brush {

	private Plot[] plots;
	private float pressure = 1f;
	private float x;
	private float y;
	private double rotation;
	private float velocity;
	RectF dirtyRect = new RectF();
	private float maxRadius;

	public Brush() {

	}

	protected void setMaxRadius(float maxRadius) {
		this.maxRadius = maxRadius;
	}

	float getMaxRadius() {
		return maxRadius;
	}

	protected void setPlots(Plot[] plots) {
		this.plots = plots;
	}

	public void onDraw(Canvas canvas) {

		resetDirtyRect();

		for (Plot plot : plots) {
			if (plot == null) continue;
			plot.onDraw(canvas, this, pressure, x, y, velocity);
			dirtyRect.union(plot.dirtyRect);
		}

		updateDirtyRegion(canvas);

		if (Debug.ENABLED) {
			//canvas.drawLine(x, y, x, y - 100 * velocity, Debug.GREEN_STROKE);
		}

		if (Config.DRAW_DIRECTION_VECTOR) {
			double r = rotation + Math.PI / 2;
			int vecLength = 200;
			float xD = (float) (vecLength * Math.sin(r)) + x;
			float yD = (float) (vecLength * Math.cos(r)) + y;
			canvas.drawLine(x, y, xD, yD, Config.GREEN_STROKE);
		}

	}

	private void resetDirtyRect() {
		if (Config.USE_DIRTY) dirtyRect.set(0, 0, 0, 0);
	}

	private void updateDirtyRegion(Canvas canvas) {
		if (Config.USE_DIRTY) {
			dirtyRect.union(
					dirtyRect.left - 50,
					dirtyRect.top - 50,
					dirtyRect.right + 50,
					dirtyRect.bottom + 50
			);
			canvas.drawRect(dirtyRect, Debug.GREEN_STROKE);
		}
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setPressure(float value) {
		pressure = value;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public void reset() {
		x = 0;
		y = 0;
		pressure = 1f;
		velocity = 0;
		for (Plot plot : plots) {
			if (plot == null) continue;
			plot.reset();
		}
	}
}
