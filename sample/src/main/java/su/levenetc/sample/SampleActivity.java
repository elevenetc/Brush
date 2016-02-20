package su.levenetc.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import su.levenetc.brush.Brush;
import su.levenetc.brush.BrushCanvas;
import su.levenetc.brush.BrushController;
import su.levenetc.brush.IBrushController;
import su.levenetc.brush.UnitBrush;

public class SampleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample);
		BrushCanvas brushCanvas = (BrushCanvas) findViewById(R.id.brush_canvas);

		final IBrushController brushController = new BrushController(initBrush());
		brushCanvas.setController(brushController);

		findViewById(R.id.btn_replay).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				brushController.restart();
			}
		});
	}

	@NonNull private Brush initBrush() {
//		return new TinyBrush(this);
//		return new LargeBrush(this);
		return new UnitBrush(this);
	}
}
