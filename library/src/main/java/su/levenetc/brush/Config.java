package su.levenetc.brush;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Eugene Levenetc on 19/02/2016.
 */
public class Config {

	private static int USE_DIRTY_FLAG = 2;
	private static int DRAW_DIRECTION_VECTOR_FLAG = 4;

	public static boolean USE_DIRTY;
	public static boolean DRAW_DIRECTION_VECTOR;
	public static final Paint GREEN_STROKE = new Paint();

	private static final int FLAGS = DRAW_DIRECTION_VECTOR_FLAG;

	static {
		if (!BuildConfig.DEBUG) {
			if ((FLAGS & USE_DIRTY_FLAG) == USE_DIRTY_FLAG)
				USE_DIRTY = true;
			if ((FLAGS & DRAW_DIRECTION_VECTOR_FLAG) == DRAW_DIRECTION_VECTOR_FLAG)
				DRAW_DIRECTION_VECTOR = true;

			GREEN_STROKE.setStyle(Paint.Style.STROKE);
			GREEN_STROKE.setColor(Color.GREEN);
		}
	}


}