package su.levenetc.brush;

import android.content.Context;
import android.graphics.Color;

/**
 * Created by Eugene Levenetc on 19/02/2016.
 */
public class UnitBrush extends Brush {

	public UnitBrush(Context context) {
		init(context);
	}

	private void init(Context context) {
		final float max = Utils.dpToPx(15, context);
		final float baseRadius = Utils.dpToPx(0.5f, context);
		final float sizeFactor = 1;

		PathPlot[] plots = new PathPlot[60];
//		plots[0] = new PathPlot(sizeFactor, 0, 0, baseRadius);
//		plots[1] = new PathPlot(sizeFactor, baseRadius, 0, baseRadius);
//		plots[2] = new PathPlot(sizeFactor, baseRadius * 2, 0, baseRadius);

		for (int i = 0; i < plots.length; i++) {
			plots[i] = new PathPlot(sizeFactor, baseRadius * i, baseRadius * i, baseRadius);
			PathPlot plot = plots[i];
			plot.setColor(Color.RED);
			if (i > 35 && i < 45) plot.setIsWeak(false);
			else plot.setIsWeak(i % 2 == 0);
		}

		plots[1] = null;
		plots[2] = null;
		plots[4] = null;
		plots[7] = null;
		plots[9] = null;
		plots[10] = null;
		plots[12] = null;
		plots[13] = null;
		plots[15] = null;

		plots[49] = null;
		plots[52] = null;
		plots[53] = null;
		plots[54] = null;
		plots[55] = null;
		plots[48] = null;
		plots[50] = null;
		plots[57] = null;
		plots[59] = null;

		setPlots(plots);
	}
}
