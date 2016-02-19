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
	private float rotation;
	private float velocity;
	private float angle;
	RectF dirtyRect = new RectF();

	public Brush() {

	}

	protected void setPlots(Plot[] plots) {
		this.plots = plots;
	}

	public void onDraw(Canvas canvas) {

		dirtyRect.set(0, 0, 0, 0);
		for (Plot plot : plots) {
			plot.onDraw(canvas, pressure, x, y, angle, velocity);
			dirtyRect.union(plot.dirtyRect);
		}

		configDirtyRegion(canvas);

		if (Debug.ENABLED) {
			//canvas.drawLine(x, y, x, y - 100 * velocity, Debug.GREEN_STROKE);
		}

	}

	private void configDirtyRegion(Canvas canvas) {
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

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public void reset() {
		x = 0;
		y = 0;
		pressure = 1f;
		velocity = 0;
		angle = 0;
		for (Plot plot : plots) plot.reset();
	}
}
