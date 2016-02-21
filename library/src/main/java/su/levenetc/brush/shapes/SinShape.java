package su.levenetc.brush.shapes;

import android.graphics.Path;
import android.graphics.PointF;

import su.levenetc.brush.BrushShape;

/**
 * Created by Eugene Levenetc on 21/02/2016.
 */
public class SinShape extends BrushShape {

	@Override protected Path createPath(float canvasWidth, float canvasHeight) {
		Path path = new Path();

		startPoint = new PointF(0, canvasHeight / 2);

		path.moveTo(startPoint.x, startPoint.y);

		for (int i = 0; i < 1000; i++) {
			float x = i / 100f;
			float y = (float) Math.sin(x);
			path.rLineTo(x / 4, y);
		}

		return path;
	}
}
