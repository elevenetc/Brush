package su.levenetc.brush.shapes;

import android.content.Context;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import su.levenetc.brush.BrushShape;
import su.levenetc.brush.parsers.SvgParser;

/**
 * Created by Eugene Levenetc on 24/02/2016.
 */
public class ResourceShape extends BrushShape {

	private int pathResId;
	private Context context;

	public ResourceShape(@RawRes int pathResId, Context context) {
		this.pathResId = pathResId;
		this.context = context;
	}

	@NonNull @Override protected Path createPath(float canvasWidth, float canvasHeight) {

		startPoint = new PointF();
		try {
			Path path = SvgParser.parsePath(pathResId, context);
			if (path != null) {

			}
			return path;
		} catch (XmlPullParserException | IOException | SAXException e) {
			e.printStackTrace();
		}
		return new Path();
	}
}
