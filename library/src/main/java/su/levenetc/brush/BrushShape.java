package su.levenetc.brush;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.support.annotation.NonNull;

import su.levenetc.brush.utils.AverageValueFilter;
import su.levenetc.brush.utils.Last3;

/**
 * Created by Eugene Levenetc on 21/02/2016.
 */
public abstract class BrushShape {

	private AverageValueFilter velocityAvgFilter = new AverageValueFilter(100, false);
	private Last3 last3 = new Last3();
	private PathMeasure pathMeasure;
	private Matrix matrix = new Matrix();
	private Path path;
	private Brush brush;
	protected PointF startPoint;
	private float SEGMENTS_AMOUNT = 300;
	private float segment;
	private float coordinates[] = {0f, 0f};
	private float step = 0;
	private float velocity;
	private float lastDistance;
	private double rotation;
	private float pressure = 0;
	private float timeDiff;
	private float distDiff;
	private boolean isInTraverse;
	private long lastTime;
	float[] v = new float[9];

	protected abstract @NonNull Path createPath(float canvasWidth, float canvasHeight);

	public void setBrush(Brush brush) {
		this.brush = brush;
	}

	public void init(float canvasWidth, float canvasHeight) {
		isInTraverse = true;
		path = createPath(canvasWidth, canvasHeight);
		pathMeasure = new PathMeasure(path, false);
		segment = pathMeasure.getLength() / SEGMENTS_AMOUNT;
	}

	public float segmentsAmount() {
		return SEGMENTS_AMOUNT;
	}

	public boolean updateStep(int step) {
		this.step = step;

		if (step > SEGMENTS_AMOUNT) isInTraverse = false;

		pressure = computePressure();
		final float distance = segment * step;
		pathMeasure.getPosTan(distance, coordinates, null);
		pathMeasure.getMatrix(distance, matrix, PathMeasure.TANGENT_MATRIX_FLAG);

		matrix.getValues(v);

		float scaleX = v[Matrix.MSCALE_X];
		double rot = Math.atan2(v[Matrix.MSKEW_X], scaleX);
		if (rot != rotation) rotation = rot;

		if (lastTime != 0) timeDiff = System.currentTimeMillis() - lastTime;
		if (lastDistance != 0) distDiff = distance - lastDistance;

		lastDistance = distance;
		lastTime = System.currentTimeMillis();

		if (lastTime != 0 && lastDistance != 0) {
			velocity = velocityAvgFilter.handle(distDiff / timeDiff);
		}

		updateBrushParams();

		return distance != 0;
	}

	private void updateBrushParams() {
		brush.setPressure(pressure);
		brush.setX(coordinates[0]);
		brush.setY(coordinates[1]);
		brush.setRotation(rotation);
		brush.setVelocity(velocity);
	}

	private float computePressure() {
		double segments = SEGMENTS_AMOUNT;
		double s = (step / segments) * (Math.PI * 2);
		return (float) (Math.cos(s + Math.PI) + 1) / 2f;
	}

	public boolean isInTraverse() {
		return isInTraverse;
	}

	public void reset() {
		isInTraverse = false;
		step = 0;
		pressure = 0;
	}
}
