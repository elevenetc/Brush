package su.levenetc.brush;

import android.content.Context;

/**
 * Created by Eugene Levenetc on 20/02/2016.
 */
public interface IBrushController {

	Brush getBrush();

	void restart();

	void setBrushCanvas(BrushCanvas brushCanvas);

	void update();

	boolean isStarted();

	void start();
}
