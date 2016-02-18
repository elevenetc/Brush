package su.levenetc.brush.utils;

import java.util.Arrays;

/**
 * Created by Eugene Levenetc.
 */
public class AverageValueFilter {

	private final float[] lastValues;
	private final boolean waitForReadiness;
	private boolean ready;
	private int index;

	public AverageValueFilter(int size, boolean waitForReadiness) {
		lastValues = new float[size];
		this.waitForReadiness = waitForReadiness;
	}

	public boolean isReady() {
		return ready;
	}

	public float handle(float value) {

		if (waitForReadiness && !ready) {
			fillLastValues(value);
			value = 0;
		} else {
			lastValues[index++] = value;
			value = getAverage();
			if (index == lastValues.length) index = 0;
		}

		return value;
	}

	private void fillLastValues(float value) {
		lastValues[index++] = value;
		if (index == lastValues.length) {
			ready = true;
			index = 0;
		}
	}

	private float getAverage() {
		float result = 0;
		for (float lastValue : lastValues) result += lastValue;
		return result / lastValues.length;
	}

	@Override public String toString() {
		return "isReady:" + isReady() + " avg:" + getAverage() + ":" + Arrays.toString(lastValues);
	}
}
