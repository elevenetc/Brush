package su.levenetc.brush;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by eleven on 10/11/2015.
 */
public class BrushController implements IBrushController {

	private Brush brush;
	private BrushCanvas brushCanvas;
	private BrushShape shape;
	private int duration = 2000;

	public BrushController() {

	}

	@Override public void setBrush(Brush brush) {
		this.brush = brush;
	}

	@Override public Brush getBrush() {
		return brush;
	}

	@Override public void restart() {
		shape.reset();
		brush.reset();
		brushCanvas.reset();
		brushCanvas.invalidate();
		start();
	}

	public void setBrushCanvas(BrushCanvas brushCanvas) {
		this.brushCanvas = brushCanvas;
	}

	@Override public void setShape(BrushShape shape) {
		this.shape = shape;
	}

	public boolean isStarted() {
		return shape.isInTraverse();
	}

	public void start() {
		shape.setBrush(brush);
		shape.init(brushCanvas.getWidth(), brushCanvas.getHeight());

		brush.setX(shape.startPoint.x);
		brush.setY(shape.startPoint.y);

		initAnimatorAndStart();
	}

	private void initAnimatorAndStart() {
		ValueAnimator animator = ValueAnimator.ofInt(0, (int) shape.segmentsAmount());
		animator.setDuration(duration);
		animator.setInterpolator(new AccelerateDecelerateInterpolator());
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override public void onAnimationUpdate(ValueAnimator animation) {
				if (shape.updateStep((int) animation.getAnimatedValue()))
					brushCanvas.invalidate();
			}
		});
		animator.start();
	}

}
