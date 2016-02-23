package su.levenetc.sample;

import android.app.Activity;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;


import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import su.levenetc.brush.Brush;
import su.levenetc.brush.BrushCanvas;
import su.levenetc.brush.BrushController;
import su.levenetc.brush.BrushShape;
import su.levenetc.brush.IBrushController;
import su.levenetc.brush.TinyBrush;
import su.levenetc.brush.UnitBrush;
import su.levenetc.brush.parsers.SvgParser;
import su.levenetc.brush.shapes.LineShape;
import su.levenetc.brush.shapes.ResourceShape;
import su.levenetc.brush.shapes.SinShape;

public class SampleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample);
		BrushCanvas brushCanvas = (BrushCanvas) findViewById(R.id.brush_canvas);

		final IBrushController brushController = new BrushController();

		brushController.setShape(initShape());
		brushController.setBrush(initBrush());
		brushController.setBrushCanvas(brushCanvas);
		brushCanvas.setController(brushController);

		findViewById(R.id.btn_replay).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				brushController.restart();
			}
		});
	}

	@NonNull private BrushShape initShape() {
//		return new LineShape();
//		return new SinShape();
		return new ResourceShape(R.raw.path, this);
	}

	@NonNull private Brush initBrush() {
		return new TinyBrush(this);
//		return new LargeBrush(this);
//		return new UnitBrush(this);
	}
}
