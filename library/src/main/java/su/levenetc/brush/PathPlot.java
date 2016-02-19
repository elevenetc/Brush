package su.levenetc.brush;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Created by eleven on 10/11/2015.
 */
public class PathPlot extends Plot {

	private Path path = new Path();
	private boolean singlePath = true;

	public PathPlot(float sizeFactor, float dx, float dy, float baseRadius) {
		super(sizeFactor, dx, dy, baseRadius);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(baseRadius * sizeFactor);
	}

	@Override protected void handleSize(float pressure) {

	}

	@Override protected float distortY(float y, float pressure) {
		return y + dy;
	}

	@Override protected float distortX(float x, float pressure) {
		return x + dx;
	}

	@Override protected void handleAlpha(float pressure) {

	}

	@Override protected void drawImplementation(Canvas canvas, float pressure, float pointX, float pointY) {


		if (singlePath) {
			path.lineTo(pointX, pointY);
		} else {
			path.reset();
			path.moveTo(prevX, prevY);
			path.lineTo(pointX, pointY);
		}

		canvas.drawPath(path, paint);
	}

	@Override protected void firstDraw(Canvas canvas, float pressure, float pointX, float pointY) {
		if (singlePath) path.moveTo(pointX, pointY);
	}
}