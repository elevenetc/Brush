package su.levenetc.brush;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by eleven on 10/11/2015.
 */
public class PathPlot extends Plot {

	private Path path = new Path();

	public PathPlot(float sizeFactor, float dx, float dy, float baseRadius) {
		super(sizeFactor, dx, dy, baseRadius);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setPathEffect(new CornerPathEffect(10));
//		paint.setPathEffect(new );
	}

	@Override protected void drawImplementation(Canvas canvas, float pressure, float pointX, float pointY) {
		path.lineTo(pointX, pointY);
		canvas.drawPath(path, paint);
	}

	@Override protected void firstDraw(Canvas canvas, float pressure, float pointX, float pointY) {
		path.moveTo(pointX, pointY);
	}
}