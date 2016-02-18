package su.levenetc.brush.utils;

/**
 * Created by eleven on 12/11/2015.
 */
public class Last3 {

	private float[] x = new float[3];
	private float[] y = new float[3];
	private byte fillCount;
	private AverageValueFilter averageValueFilter = new AverageValueFilter(100, false);

	public void put(float xValue, float yValue) {

		if (isNotFilled()) fillCount++;

		x[0] = x[1];
		y[0] = y[1];

		x[1] = x[2];
		y[1] = y[2];

		x[2] = xValue;
		y[2] = yValue;
	}

	public double getAngle() {

		if (isNotFilled()) return 0;

		float xOffset1 = x[1];
		float yOffset1 = y[1];

		float diffX01 = x[1] - x[0];
		float diffY01 = y[1] - y[0];

		float verticalX = x[1] + diffX01;
		float verticalY = y[1] + diffY01;

		double v = degreeBetweenPoints(x[2] - xOffset1, y[2] - yOffset1, verticalX - xOffset1, verticalY - yOffset1);
		return averageValueFilter.handle((float) v);
	}

	private boolean isNotFilled() {
		return fillCount <= 3;
	}

	private double degreeBetweenPoints(float xA, float yA, float xB, float yB) {
		double rad = Math.atan2(yB, xB) - Math.atan2(yA, xA);
		return (float) Math.toDegrees(rad);
	}
}


