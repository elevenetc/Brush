package su.levenetc.brush;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by eleven on 10/11/2015.
 */
public class LinePlot extends Plot {

	public LinePlot(float sizeFactor, float dx, float dy, float baseRadius) {
		super(sizeFactor, dx, dy, baseRadius);

		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
	}

	@Override protected void drawImplementation(Canvas canvas, float pressure, float pointX, float pointY) {
		paint.setAlpha((int) (255 * pressure * alphaFactor));
		canvas.drawLine(prevX, prevY, pointX, pointY, paint);
	}

	@Override protected void firstDraw(Canvas canvas, float pressure, float pointX, float pointY) {

	}
}