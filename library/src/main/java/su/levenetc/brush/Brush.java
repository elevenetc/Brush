package su.levenetc.brush;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by eleven on 10/11/2015.
 */
public class Brush {

	private Plot[] plots;
	private float pressure = 1f;
	private float x;
	private float y;
	private float rotation;
	private float velocity;
	private float angle;
	RectF dirtyRect = new RectF();

	public Brush(Context context) {
//		makeBrushA(context);
		makeBrushB(context);
	}

	private void makeBrushB(Context context) {

		final float max = Utils.dpToPx(15, context);
		final float radius = Utils.dpToPx(25, context);
		final float sizeFactor = 1;

		plots = new Plot[3];
		plots[0] = new Plot(sizeFactor, 0, 0, radius);
		plots[1] = new Plot(sizeFactor / 2f, -max, 0, radius);
		plots[2] = new Plot(sizeFactor / 2f, max, 0, radius);
	}

	private void makeBrushA(Context context) {
		plots = new Plot[17];
		final float max = Utils.dpToPx(15, context);
		final float mid = Utils.dpToPx(10, context);
		final float min = Utils.dpToPx(5, context);
		final float radius = Utils.dpToPx(25, context);

		plots[0] = new Plot(1, 0, 0, radius);

		plots[1] = new Plot(0.5f, -max / 2, -max / 2, radius);
		plots[2] = new Plot(0.5f, max / 2, max / 2, radius);
		plots[3] = new Plot(0.5f, -max / 2, max / 2, radius);
		plots[4] = new Plot(0.5f, max / 2, -max / 2, radius);

		plots[5] = new Plot(0.6f, 0, -mid, radius);
		plots[6] = new Plot(0.6f, 0, mid, radius);
		plots[7] = new Plot(0.6f, mid, 0, radius);
		plots[8] = new Plot(0.6f, -mid, 0, radius);

		plots[9] = new Plot(0.7f, 0, -min, radius);
		plots[10] = new Plot(0.7f, 0, min, radius);
		plots[11] = new Plot(0.7f, min, 0, radius);
		plots[12] = new Plot(0.7f, -min, 0, radius);

		plots[13] = new Plot(0.5f, 0, -max, radius);
		plots[14] = new Plot(0.5f, 0, max, radius);
		plots[15] = new Plot(0.5f, max, 0, radius);
		plots[16] = new Plot(0.5f, -max, 0, radius);
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
}
