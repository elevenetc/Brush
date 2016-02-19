package su.levenetc.brush;

import android.content.Context;

/**
 * Created by Eugene Levenetc on 19/02/2016.
 */
public class LargeBrush extends Brush {

	public LargeBrush(Context context) {
		init(context);
	}

	private void init(Context context) {
		Plot[] plots = new Plot[17];
		final float max = Utils.dpToPx(15, context);
		final float mid = Utils.dpToPx(10, context);
		final float min = Utils.dpToPx(5, context);
		final float radius = Utils.dpToPx(25, context);

		genPath(plots, max, mid, min, radius);
//		genLine(plots, max, mid, min, radius);

		setPlots(plots);
	}

	private void genLine(Plot[] plots, float max, float mid, float min, float radius) {
		plots[0] = new LinePlot(1, 0, 0, radius);

		plots[1] = new LinePlot(0.5f, -max / 2, -max / 2, radius);
		plots[2] = new LinePlot(0.5f, max / 2, max / 2, radius);
		plots[3] = new LinePlot(0.5f, -max / 2, max / 2, radius);
		plots[4] = new LinePlot(0.5f, max / 2, -max / 2, radius);

		plots[5] = new LinePlot(0.6f, 0, -mid, radius);
		plots[6] = new LinePlot(0.6f, 0, mid, radius);
		plots[7] = new LinePlot(0.6f, mid, 0, radius);
		plots[8] = new LinePlot(0.6f, -mid, 0, radius);

		plots[9] = new LinePlot(0.7f, 0, -min, radius);
		plots[10] = new LinePlot(0.7f, 0, min, radius);
		plots[11] = new LinePlot(0.7f, min, 0, radius);
		plots[12] = new LinePlot(0.7f, -min, 0, radius);

		plots[13] = new LinePlot(0.5f, 0, -max, radius);
		plots[14] = new LinePlot(0.5f, 0, max, radius);
		plots[15] = new LinePlot(0.5f, max, 0, radius);
		plots[16] = new LinePlot(0.5f, -max, 0, radius);
	}


	private void genPath(Plot[] plots, float max, float mid, float min, float radius) {
		plots[0] = new PathPlot(1, 0, 0, radius);

		plots[1] = new PathPlot(0.5f, -max / 2, -max / 2, radius);
		plots[2] = new PathPlot(0.5f, max / 2, max / 2, radius);
		plots[3] = new PathPlot(0.5f, -max / 2, max / 2, radius);
		plots[4] = new PathPlot(0.5f, max / 2, -max / 2, radius);

		plots[5] = new PathPlot(0.6f, 0, -mid, radius);
		plots[6] = new PathPlot(0.6f, 0, mid, radius);
		plots[7] = new PathPlot(0.6f, mid, 0, radius);
		plots[8] = new PathPlot(0.6f, -mid, 0, radius);

		plots[9] = new PathPlot(0.7f, 0, -min, radius);
		plots[10] = new PathPlot(0.7f, 0, min, radius);
		plots[11] = new PathPlot(0.7f, min, 0, radius);
		plots[12] = new PathPlot(0.7f, -min, 0, radius);

		plots[13] = new PathPlot(0.5f, 0, -max, radius);
		plots[14] = new PathPlot(0.5f, 0, max, radius);
		plots[15] = new PathPlot(0.5f, max, 0, radius);
		plots[16] = new PathPlot(0.5f, -max, 0, radius);
	}
}
