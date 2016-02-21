package su.levenetc.brush.shapes;

import android.graphics.Path;
import android.graphics.PointF;

import su.levenetc.brush.BrushShape;

/**
 * Created by Eugene Levenetc on 21/02/2016.
 */
public class CircleShape extends BrushShape {

	@Override protected Path createPath(float canvasWidth, float canvasHeight) {
		Path path = new Path();

		startPoint = new PointF(canvasWidth / 2, canvasHeight / 2);

//		path.moveTo(startPoint.x, startPoint.y);
		path.addCircle(startPoint.x, startPoint.y, 100, Path.Direction.CW);

		return path;
	}
}