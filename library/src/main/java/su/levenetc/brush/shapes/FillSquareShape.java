package su.levenetc.brush.shapes;

import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.NonNull;

import su.levenetc.brush.BrushShape;

/**
 * Created by Eugene Levenetc on 21/02/2016.
 */
public class FillSquareShape extends BrushShape {
	@NonNull @Override protected Path createPath(float canvasWidth, float canvasHeight) {

		Path path = new Path();
		float centerX = canvasWidth / 2;
		float centerY = canvasHeight / 2;
		startPoint = new PointF(centerX, centerY);
		float squareSize = 200;
		path.moveTo(centerX - squareSize / 2, centerY - squareSize / 2);
		path.rLineTo(squareSize, 0);
		path.rLineTo(0, squareSize);
		path.rLineTo(-squareSize, 0);
		path.rLineTo(0, -squareSize);

		return path;
	}
}
