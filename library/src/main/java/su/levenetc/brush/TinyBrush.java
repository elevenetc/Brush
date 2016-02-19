package su.levenetc.brush;

import android.content.Context;
import android.graphics.Color;

/**
 * Created by Eugene Levenetc on 19/02/2016.
 */
public class TinyBrush extends Brush {

	public TinyBrush(Context context) {
		init(context);
	}

	private void init(Context context) {
		final float max = Utils.dpToPx(15, context);
		final float baseRadius = Utils.dpToPx(25, context);
		final float sizeFactor = 1;

		Plot[] plots = new Plot[3];
		plots[0] = new PathPlot(sizeFactor, 0, 0, baseRadius);
		plots[1] = new PathPlot(sizeFactor / 2f, -max, 0, baseRadius);
		plots[2] = new PathPlot(sizeFactor / 2f, max, 0, baseRadius);
//		plots[0] = new LinePlot(sizeFactor, 0, 0, baseRadius);
//		plots[1] = new LinePlot(sizeFactor / 2f, -max, 0, baseRadius);
//		plots[2] = new LinePlot(sizeFactor / 2f, max, 0, baseRadius);

		plots[0].setColor(Color.RED);
		plots[1].setColor(Color.BLUE);
		plots[2].setColor(Color.GREEN);
		setPlots(plots);
	}
}
