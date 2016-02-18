package su.levenetc.brush;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by eleven on 12/11/2015.
 */
public class Debug {
	public static final boolean ENABLED = true;
	public static final Paint GREEN_STROKE = new Paint();
	static{
		GREEN_STROKE.setColor(Color.GREEN);
		GREEN_STROKE.setStyle(Paint.Style.STROKE);
	}
}
