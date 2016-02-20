package su.levenetc.brush;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import java.util.Random;

/**
 * Created by eleven on 10/11/2015.
 */
public class PathPlot extends Plot {

	private Path path = new Path();
	private Path clearPath = new Path();
	private boolean singlePath = true;
	private Paint clearPaint = new Paint();
	private Random rnd = new Random();
	private boolean isWeak;

	public PathPlot(float sizeFactor, float dx, float dy, float baseRadius) {
		super(sizeFactor, dx, dy, baseRadius);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.RED);
//		paint.setAntiAlias(true);
		paint.setDither(true);
//		paint.setStrokeJoin(Paint.Join.ROUND);
//		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(baseRadius * sizeFactor);

		clearPaint.setStyle(Paint.Style.STROKE);
		clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		clearPaint.setStrokeWidth(baseRadius * sizeFactor * 1.1f);
	}

	public void setIsWeak(boolean isWeak) {
		this.isWeak = isWeak;
	}

	@Override public void reset() {
		super.reset();
		path.reset();
		clearPath.reset();
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

		if (isWeak && rnd.nextBoolean()) addClearStroke(pointX, pointY);

		canvas.drawPath(path, paint);
		canvas.drawPath(clearPath, clearPaint);
	}

	private void addClearStroke(float pointX, float pointY) {
		clearPath.moveTo(prevX, prevY);
		clearPath.lineTo(pointX, pointY);
	}

	@Override protected void firstDraw(Canvas canvas, float pressure, float pointX, float pointY) {
		if (singlePath) path.moveTo(pointX, pointY);
	}

	private float deviateValue(float in, float max) {
		int dir = rnd.nextBoolean() ? 1 : -1;
		return in + dir * (max * rnd.nextFloat());
	}
}