package su.levenetc.brush;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by eleven on 10/11/2015.
 */
public class Plot {

	private final Paint paint = new Paint();
	private final float dx;
	private final float dy;
	private final float radius;
	private final float sizeFactor;
	private float prevX;
	private float prevY;
	private static Random rnd = new Random();

	public Plot(float sizeFactor, float dx, float dy, float radius) {
		this.dx = dx;
		this.dy = dy;
		this.radius = radius;
		this.sizeFactor = sizeFactor;
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.RED);
		paint.setAlpha(100);
//		paint.setStrokeWidth(r);
//		paint.setARGB(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
		paint.setAntiAlias(true);
//		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStrokeMiter(100);
		paint.setStrokeJoin(Paint.Join.MITER);


//		paint.setAlpha(50);
//		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
	}

	public void onDraw(Canvas canvas, float pressure, float x, float y, float angle, float velocity) {

		paint.setStrokeWidth(sizeFactor * radius * pressure);

		float angleFactor = 5f;
		float angleDiffX = angleFactor * pressure;
		float angleDiffY = angleFactor * pressure;

		float pointX = x + dx * angleDiffX;
		float pointY = y + dy * angleDiffY;

		if (prevX != 0) {
			paint.setAlpha((int) (255 * pressure) / 2);
			canvas.drawLine(prevX, prevY, pointX, pointY, paint);
		}

		prevX = pointX;
		prevY = pointY;
//		canvas.drawCircle(x + dx, y + dy, sizeFactor * radius * pressure, paint);
	}
}