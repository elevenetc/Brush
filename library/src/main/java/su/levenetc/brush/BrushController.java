package su.levenetc.brush;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import su.levenetc.brush.utils.AverageValueFilter;
import su.levenetc.brush.utils.Last3;

/**
 * Created by eleven on 10/11/2015.
 */
public class BrushController {

	private Brush brush;
	private BrushCanvas brushCanvas;
	private boolean isStarted;
	private int halfHeight;
	private float path;
	private PathMeasure pathMeasure;
	private float SEGMENTS_AMOUNT = 300;
	private float segment;
	private AverageValueFilter velocityAvgFilter = new AverageValueFilter(100, false);
	private double angle;
	private int duration = 5000;

	public BrushController(Brush brush) {
		this.brush = brush;
	}

	public void setBrushCanvas(BrushCanvas brushCanvas) {
		this.brushCanvas = brushCanvas;
	}

	public boolean isStarted() {
		return isStarted;
	}

	private RectF oval = new RectF();

	public void start(Context context) {


		int centerX = brushCanvas.getWidth() / 2;
		int centerY = brushCanvas.getHeight() / 2;
		brush.setX(centerX);
		brush.setY(centerY);


		halfHeight = centerY;
		path = halfHeight * 0.7f;


		Path p = getCircle(centerX, centerY, context);
//		Path p = getOval(centerX, centerY);
//		Path p = getLine(centerX, centerY);
//		Path p = getSin(0, centerY);

		pathMeasure = new PathMeasure(p, false);
		segment = pathMeasure.getLength() / SEGMENTS_AMOUNT;

		initAnimator();

		isStarted = true;
//		brushCanvas.invalidate();
	}

	private Path getSin(float centerX, float centerY) {
		Path p = new Path();

		p.moveTo(centerX, centerY);

		for (int i = 0; i < 600; i++) {
			float x = i / 100f;
			float y = (float) Math.sin(x);
			p.rLineTo(x / 4, y);
		}


		return p;
	}

	private Path getOval(float centerX, float centerY) {
		Path p = new Path();
		oval.set(centerX, centerY, centerX + 950, centerY + 150);
		p.addOval(oval, Path.Direction.CW);
		return p;
	}

	private Path getCircle(float centerX, float centerY, Context context) {
		Path p = new Path();
		p.addCircle(centerX, centerY, Utils.dpToPx(100, context), Path.Direction.CW);
		return p;
	}

	private Path getLine(float centerX, float centerY) {
		Path p = new Path();
		p.moveTo(centerX, centerY);
		p.lineTo(centerX, centerY + 200);
		return p;
	}

	private void initAnimator() {
		ValueAnimator animator = ValueAnimator.ofInt(0, (int) SEGMENTS_AMOUNT);
		animator.setDuration(duration);
		animator.setInterpolator(new AccelerateDecelerateInterpolator());
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override public void onAnimationUpdate(ValueAnimator animation) {
				step = (int) animation.getAnimatedValue();
				if (updateStep()) brushCanvas.invalidate();

			}
		});
		animator.start();
	}

	private float computePressure() {
		double segments = SEGMENTS_AMOUNT / 3;
		double s = (step / segments) * (Math.PI * 2);
		return (float) (Math.cos(s + Math.PI) + 1) / 2f;
	}


	private boolean updateStep() {
		pressure = computePressure();
//		pressure = step / SEGMENTS_AMOUNT;
		final float distance = segment * step;
		pathMeasure.getPosTan(distance, coordinates, null);
		pathMeasure.getMatrix(distance, matrix, PathMeasure.TANGENT_MATRIX_FLAG);
		brush.setPressure(pressure);
		brush.setX(coordinates[0]);
		brush.setY(coordinates[1]);

		matrix.getValues(v);

		calcAngle(distance);

		float scaleX = v[Matrix.MSCALE_X];
		float rotation = 0;
//		float rotation = Math.round(Math.atan2(v[Matrix.MSKEW_X], scaleX) * (180 / Math.PI));
		brush.setRotation(rotation);


		if (lastTime != 0) timeDiff = System.currentTimeMillis() - lastTime;
		if (lastDistance != 0) distDiff = distance - lastDistance;

		lastDistance = distance;
		lastTime = System.currentTimeMillis();

		if (lastTime != 0 && lastDistance != 0) {
			velocity = velocityAvgFilter.handle(distDiff / timeDiff);
		}
//		if (timeDiff != 0)


		brush.setVelocity(velocity);
		brush.setAngle((float) angle);


		return distance != 0;
	}

	private void calcAngle(float distance) {
		if (lastDistance != distance) {
			last3.put(coordinates[0], coordinates[1]);
			angle = last3.getAngle();
			Log.i("angle", "" + angle + " vel:" + velocity + " step:" + step + " pressure:" + pressure);
		}
	}

	private Last3 last3 = new Last3();

	float[] v = new float[9];

	private float velocity;
	private float lastDistance;
	private long lastTime;
	private float timeDiff;
	private float distDiff;
	private float pressure = 0;
	private float coordinates[] = {0f, 0f};
	private float tan[] = {0f, 0f};
	private float step = 0;
	private Matrix matrix = new Matrix();

	public void update() {

		if (step <= SEGMENTS_AMOUNT) {
			//updateStep();
//			step++;
		} else {
			isStarted = false;
		}
	}

	public Matrix getMatrix() {
		return matrix;
	}
}
