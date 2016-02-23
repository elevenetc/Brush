package su.levenetc.brush;

import android.content.Context;

/**
 * Created by Eugene Levenetc on 20/02/2016.
 */
public interface IBrushController {

	void setBrush(Brush brush);

	Brush getBrush();

	void restart();

	void setBrushCanvas(BrushCanvas brushCanvas);

	void setShape(BrushShape shape);

	boolean isStarted();

	void start();
}
