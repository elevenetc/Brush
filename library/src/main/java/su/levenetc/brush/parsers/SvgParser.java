/*
   Copyright 2013 Paul LeBeau, Cave Rock Software Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/


package su.levenetc.brush.parsers;

import android.content.Context;
import android.graphics.Path;
import android.support.annotation.RawRes;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Eugene Levenetc on 24/02/2016.
 */
public class SvgParser {

	private static final String TAG = "tag";

	protected enum Unit {
		px,
		em,
		ex,
		in,
		cm,
		mm,
		pt,
		pc,
		percent
	}

	public static Path parsePath(@RawRes int rawRes, Context context) throws XmlPullParserException, IOException, SAXException {
		InputStream is = context.getResources().openRawResource(rawRes);
		XmlPullParser parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setInput(is, null);
		parser.nextTag();

		int next = parser.next();
		while (next != XmlPullParser.END_DOCUMENT) {
			next = parser.next();

			if (next == XmlPullParser.START_TAG) {
				if ("path".equals(parser.getName())) {
					String pathDescription = parser.getAttributeValue(null, "d");
					return parsePath(pathDescription);
				}
			}
		}
		throw new IllegalArgumentException("No path tag");
	}

	private static Path parsePath(String val) throws SAXException {
		TextScanner scan = new TextScanner(val);

		int pathCommand = '?';
		float currentX = 0f, currentY = 0f;    // The last point visited in the subpath
		float lastMoveX = 0f, lastMoveY = 0f;  // The initial point of current subpath
		float lastControlX = 0f, lastControlY = 0f;  // Last control point of the just completed bezier curve.
		Float x, y, x1, y1, x2, y2;
		Float rx, ry, xAxisRotation;
		Boolean largeArcFlag, sweepFlag;

		SvgParser.PathDefinition path = new SvgParser.PathDefinition();

		if (scan.empty())
			return path.path;

		pathCommand = scan.nextChar();

		if (pathCommand != 'M' && pathCommand != 'm')
			return path.path;  // Invalid path - doesn't start with a move

		while (true) {
			scan.skipWhitespace();

			switch (pathCommand) {
				// Move
				case 'M':
				case 'm':
					x = scan.nextFloat();
					scan.skipCommaWhitespace();
					y = scan.nextFloat();
					if (y == null) {
						Log.e(TAG, "Bad path coords for " + pathCommand + " path segment");
						return path.path;
					}
					// Relative moveto at the start of a path is treated as an absolute moveto.
					if (pathCommand == 'm' && !path.isEmpty()) {
						x += currentX;
						y += currentY;
					}
					path.moveTo(x, y);
					currentX = lastMoveX = lastControlX = x;
					currentY = lastMoveY = lastControlY = y;
					// Any subsequent coord pairs should be treated as a lineto.
					pathCommand = (pathCommand == 'm') ? 'l' : 'L';
					break;

				// Line
				case 'L':
				case 'l':
					x = scan.nextFloat();
					scan.skipCommaWhitespace();
					y = scan.nextFloat();
					if (y == null) {
						Log.e(TAG, "Bad path coords for " + pathCommand + " path segment");
						return path.path;
					}
					if (pathCommand == 'l') {
						x += currentX;
						y += currentY;
					}
					path.lineTo(x, y);
					currentX = lastControlX = x;
					currentY = lastControlY = y;
					break;

				// Cubic bezier
				case 'C':
				case 'c':
					x1 = scan.nextFloat();
					scan.skipCommaWhitespace();
					y1 = scan.nextFloat();
					scan.skipCommaWhitespace();
					x2 = scan.nextFloat();
					scan.skipCommaWhitespace();
					y2 = scan.nextFloat();
					scan.skipCommaWhitespace();
					x = scan.nextFloat();
					scan.skipCommaWhitespace();
					y = scan.nextFloat();
					if (y == null) {
						Log.e(TAG, "Bad path coords for " + pathCommand + " path segment");
						return path.path;
					}
					if (pathCommand == 'c') {
						x += currentX;
						y += currentY;
						x1 += currentX;
						y1 += currentY;
						x2 += currentX;
						y2 += currentY;
					}
					path.cubicTo(x1, y1, x2, y2, x, y);
					lastControlX = x2;
					lastControlY = y2;
					currentX = x;
					currentY = y;
					break;

				// Smooth curve (first control point calculated)
				case 'S':
				case 's':
					x1 = 2 * currentX - lastControlX;
					y1 = 2 * currentY - lastControlY;
					x2 = scan.nextFloat();
					scan.skipCommaWhitespace();
					y2 = scan.nextFloat();
					scan.skipCommaWhitespace();
					x = scan.nextFloat();
					scan.skipCommaWhitespace();
					y = scan.nextFloat();
					if (y == null) {
						Log.e(TAG, "Bad path coords for " + pathCommand + " path segment");
						return path.path;
					}
					if (pathCommand == 's') {
						x += currentX;
						y += currentY;
						x2 += currentX;
						y2 += currentY;
					}
					path.cubicTo(x1, y1, x2, y2, x, y);
					lastControlX = x2;
					lastControlY = y2;
					currentX = x;
					currentY = y;
					break;

				// Close path
				case 'Z':
				case 'z':
					path.close();
					currentX = lastControlX = lastMoveX;
					currentY = lastControlY = lastMoveY;
					break;

				// Horizontal line
				case 'H':
				case 'h':
					x = scan.nextFloat();
					if (x == null) {
						Log.e(TAG, "Bad path coords for " + pathCommand + " path segment");
						return path.path;
					}
					if (pathCommand == 'h') {
						x += currentX;
					}
					path.lineTo(x, currentY);
					currentX = lastControlX = x;
					break;

				// Vertical line
				case 'V':
				case 'v':
					y = scan.nextFloat();
					if (y == null) {
						Log.e(TAG, "Bad path coords for " + pathCommand + " path segment");
						return path.path;
					}
					if (pathCommand == 'v') {
						y += currentY;
					}
					path.lineTo(currentX, y);
					currentY = lastControlY = y;
					break;

				// Quadratic bezier
				case 'Q':
				case 'q':
					x1 = scan.nextFloat();
					scan.skipCommaWhitespace();
					y1 = scan.nextFloat();
					scan.skipCommaWhitespace();
					x = scan.nextFloat();
					scan.skipCommaWhitespace();
					y = scan.nextFloat();
					if (y == null) {
						Log.e(TAG, "Bad path coords for " + pathCommand + " path segment");
						return path.path;
					}
					if (pathCommand == 'q') {
						x += currentX;
						y += currentY;
						x1 += currentX;
						y1 += currentY;
					}
					path.quadTo(x1, y1, x, y);
					lastControlX = x1;
					lastControlY = y1;
					currentX = x;
					currentY = y;
					break;

				// Smooth quadratic bezier
				case 'T':
				case 't':
					x1 = 2 * currentX - lastControlX;
					y1 = 2 * currentY - lastControlY;
					x = scan.nextFloat();
					scan.skipCommaWhitespace();
					y = scan.nextFloat();
					if (y == null) {
						Log.e(TAG, "Bad path coords for " + pathCommand + " path segment");
						return path.path;
					}
					if (pathCommand == 't') {
						x += currentX;
						y += currentY;
					}
					path.quadTo(x1, y1, x, y);
					lastControlX = x1;
					lastControlY = y1;
					currentX = x;
					currentY = y;
					break;

				// Arc
				case 'A':
				case 'a':
					rx = scan.nextFloat();
					scan.skipCommaWhitespace();
					ry = scan.nextFloat();
					scan.skipCommaWhitespace();
					xAxisRotation = scan.nextFloat();
					scan.skipCommaWhitespace();
					largeArcFlag = scan.nextFlag();
					scan.skipCommaWhitespace();
					sweepFlag = scan.nextFlag();
					scan.skipCommaWhitespace();
					x = scan.nextFloat();
					scan.skipCommaWhitespace();
					y = scan.nextFloat();
					if (y == null || rx < 0 || ry < 0) {
						Log.e(TAG, "Bad path coords for " + pathCommand + " path segment");
						return path.path;
					}
					if (pathCommand == 'a') {
						x += currentX;
						y += currentY;
					}
					path.arcTo(rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x, y);
					currentX = lastControlX = x;
					currentY = lastControlY = y;
					break;

				default:
					return path.path;
			}

			scan.skipWhitespace();
			if (scan.empty())
				break;

			// Test to see if there is another set of coords for the current path command
			if (scan.hasLetter()) {
				// Nope, so get the new path command instead
				pathCommand = scan.nextChar();
			}
		}
		return path.path;
	}

	protected interface PathInterface {
		public void moveTo(float x, float y);

		public void lineTo(float x, float y);

		public void cubicTo(float x1, float y1, float x2, float y2, float x3, float y3);

		public void quadTo(float x1, float y1, float x2, float y2);

		public void arcTo(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x,
		                  float y);

		public void close();
	}

	protected static class PathDefinition implements PathInterface {
		private List<Byte> commands = null;
		private List<Float> coords = null;

		private static final byte MOVETO = 0;
		private static final byte LINETO = 1;
		private static final byte CUBICTO = 2;
		private static final byte QUADTO = 3;
		private static final byte ARCTO = 4;   // 4-7
		private static final byte CLOSE = 8;
		private Path path = new Path();

		public PathDefinition() {
			this.commands = new ArrayList<Byte>();
			this.coords = new ArrayList<Float>();
		}


		public boolean isEmpty() {
			return path.isEmpty();
			//return commands.isEmpty();
		}


		@Override
		public void moveTo(float x, float y) {
//			commands.add(MOVETO);
//			coords.add(x);
//			coords.add(y);
			path.moveTo(x, y);
		}


		@Override
		public void lineTo(float x, float y) {
//			commands.add(LINETO);
//			coords.add(x);
//			coords.add(y);
			path.lineTo(x, y);
		}


		@Override
		public void cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
//			commands.add(CUBICTO);
//			coords.add(x1);
//			coords.add(y1);
//			coords.add(x2);
//			coords.add(y2);
//			coords.add(x3);
//			coords.add(y3);
			path.cubicTo(x1, y1, x2, y2, x3, y3);
		}


		@Override
		public void quadTo(float x1, float y1, float x2, float y2) {
//			commands.add(QUADTO);
//			coords.add(x1);
//			coords.add(y1);
//			coords.add(x2);
//			coords.add(y2);
			path.quadTo(x1, y1, x2, y2);
		}


		@Override
		public void arcTo(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) {
			int arc = ARCTO | (largeArcFlag ? 2 : 0) | (sweepFlag ? 1 : 0);
//			commands.add((byte) arc);
//			coords.add(rx);
//			coords.add(ry);
//			coords.add(xAxisRotation);
//			coords.add(x);
//			coords.add(y);
			//path.arcTo();
		}


		@Override
		public void close() {
			path.close();
//			commands.add(CLOSE);
		}


		public void enumeratePath(PathInterface handler) {
			Iterator<Float> coordsIter = coords.iterator();

			for (byte command : commands) {
				switch (command) {
					case MOVETO:
						handler.moveTo(coordsIter.next(), coordsIter.next());
						break;
					case LINETO:
						handler.lineTo(coordsIter.next(), coordsIter.next());
						break;
					case CUBICTO:
						handler.cubicTo(coordsIter.next(), coordsIter.next(), coordsIter.next(), coordsIter.next(),
								coordsIter.next(), coordsIter.next());
						break;
					case QUADTO:
						handler.quadTo(coordsIter.next(), coordsIter.next(), coordsIter.next(), coordsIter.next());
						break;
					case CLOSE:
						handler.close();
						break;
					default:
						boolean largeArcFlag = (command & 2) != 0;
						boolean sweepFlag = (command & 1) != 0;
						handler.arcTo(coordsIter.next(), coordsIter.next(), coordsIter.next(), largeArcFlag, sweepFlag, coordsIter.next(), coordsIter.next());
				}
			}
		}

	}

	private static class TextScanner {

		protected String input;
		protected int position = 0;


		public TextScanner(String input) {
			this.input = input.trim();
		}

		/**
		 * Returns true if we have reached the end of the input.
		 */
		public boolean empty() {
			return (position == input.length());
		}

		protected boolean isWhitespace(int c) {
			return (c == ' ' || c == '\n' || c == '\r' || c == '\t');
		}

		public void skipWhitespace() {
			while (position < input.length()) {
				if (!isWhitespace(input.charAt(position)))
					break;
				position++;
			}
		}

		protected boolean isEOL(int c) {
			return (c == '\n' || c == '\r');
		}

		// Skip the sequence: <space>*(<comma><space>)?
		// Returns true if we found a comma in there.
		public boolean skipCommaWhitespace() {
			skipWhitespace();
			if (position == input.length())
				return false;
			if (!(input.charAt(position) == ','))
				return false;
			position++;
			skipWhitespace();
			return true;
		}


		public Float nextFloat() {
			int floatEnd = scanForFloat();
			if (floatEnd == position)
				return null;
			Float result = Float.parseFloat(input.substring(position, floatEnd));
			position = floatEnd;
			return result;
		}

		/*
		   * Scans for a comma-whitespace sequence with a float following it.
		   * If found, the float is returned. Otherwise null is returned and
		   * the scan position left as it was.
		   */
		public Float possibleNextFloat() {
			int start = position;
			skipCommaWhitespace();
			Float result = nextFloat();
			if (result != null)
				return result;
			position = start;
			return null;
		}

		public Integer nextInteger() {
			int intEnd = scanForInteger();
			//System.out.println("nextFloat: "+position+" "+floatEnd);
			if (intEnd == position)
				return null;
			Integer result = Integer.parseInt(input.substring(position, intEnd));
			position = intEnd;
			return result;
		}

		public Integer nextChar() {
			if (position == input.length())
				return null;
			return Integer.valueOf(input.charAt(position++));
		}

		public Length nextLength() {
			Float scalar = nextFloat();
			if (scalar == null)
				return null;
			Unit unit = nextUnit();
			if (unit == null)
				return new Length(scalar, Unit.px);
			else
				return new Length(scalar, unit);
		}

		/*
		   * Scan for a 'flag'. A flag is a '0' or '1' digit character.
		   */
		public Boolean nextFlag() {
			if (position == input.length())
				return null;
			char ch = input.charAt(position);
			if (ch == '0' || ch == '1') {
				position++;
				return Boolean.valueOf(ch == '1');
			}
			return null;
		}


		public boolean consume(char ch) {
			boolean found = (position < input.length() && input.charAt(position) == ch);
			if (found)
				position++;
			return found;
		}


		public boolean consume(String str) {
			int len = str.length();
			boolean found = (position <= (input.length() - len) && input.substring(position, position + len).equals(str));
			if (found)
				position += len;
			return found;
		}


		protected int advanceChar() {
			if (position == input.length())
				return -1;
			position++;
			if (position < input.length())
				return input.charAt(position);
			else
				return -1;
		}


		/*
		   * Scans the input starting immediately at 'position' for the next token.
		   * A token is a sequence of characters terminating at a whitespace character.
		   * Note that this routine only checks for whitespace characters.  Use nextToken(char)
		   * if token might end with another character.
		   */
		public String nextToken() {
			return nextToken(' ');
		}

		/*
		   * Scans the input starting immediately at 'position' for the next token.
		   * A token is a sequence of characters terminating at either a whitespace character
		   * or the supplied terminating character.
		   */
		public String nextToken(char terminator) {
			if (empty())
				return null;

			int ch = input.charAt(position);
			if (isWhitespace(ch) || ch == terminator)
				return null;

			int start = position;
			ch = advanceChar();
			while (ch != -1 && ch != terminator && !isWhitespace(ch)) {
				ch = advanceChar();
			}
			return input.substring(start, position);
		}

		/*
		   * Scans the input starting immediately at 'position' for the a sequence
		   * of letter characters terminated by an open bracket.  The function
		   * name is returned.
		   */
		public String nextFunction() {
			if (empty())
				return null;
			int start = position;

			int ch = input.charAt(position);
			while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
				ch = advanceChar();
			int end = position;
			while (isWhitespace(ch))
				ch = advanceChar();
			if (ch == '(') {
				position++;
				return input.substring(start, end);
			}
			position = start;
			return null;
		}

		/*
		   * Scans the input starting immediately at 'position' for a floating point number.
		   * If one is found, the end position of the float will be returned.
		   * If the returned value is the same as 'position' then no float was found.
		   */
		private int scanForFloat() {
			if (empty())
				return position;
			int lastValidPos = position;
			int start = position;

			int ch = input.charAt(position);
			// Check whole part of mantissa
			if (ch == '-' || ch == '+')
				ch = advanceChar();
			if (Character.isDigit(ch)) {
				lastValidPos = position + 1;
				ch = advanceChar();
				while (Character.isDigit(ch)) {
					lastValidPos = position + 1;
					ch = advanceChar();
				}
			}
			// Fraction or exponent starts here
			if (ch == '.') {
				lastValidPos = position + 1;
				ch = advanceChar();
				while (Character.isDigit(ch)) {
					lastValidPos = position + 1;
					ch = advanceChar();
				}
			}
			// Exponent
			if (ch == 'e' || ch == 'E') {
				ch = advanceChar();
				if (ch == '-' || ch == '+')
					ch = advanceChar();
				if (Character.isDigit(ch)) {
					lastValidPos = position + 1;
					ch = advanceChar();
					while (Character.isDigit(ch)) {
						lastValidPos = position + 1;
						ch = advanceChar();
					}
				}
			}

			position = start;
			return lastValidPos;
		}

		/*
		   * Scans the input starting immediately at 'position' for an integer number.
		   * If one is found, the end position of the float will be returned.
		   * If the returned value is the same as 'position' then no number was found.
		   */
		private int scanForInteger() {
			if (empty())
				return position;
			int lastValidPos = position;
			int start = position;

			int ch = input.charAt(position);
			// Check whole part of mantissa
			if (ch == '-' || ch == '+')
				ch = advanceChar();
			if (Character.isDigit(ch)) {
				lastValidPos = position + 1;
				ch = advanceChar();
				while (Character.isDigit(ch)) {
					lastValidPos = position + 1;
					ch = advanceChar();
				}
			}

			position = start;
			return lastValidPos;
		}

		/*
		   * Get the next few chars. Mainly used for error messages.
		   */
		public String ahead() {
			int start = position;
			while (!empty() && !isWhitespace(input.charAt(position)))
				position++;
			String str = input.substring(start, position);
			position = start;
			return str;
		}

		public Unit nextUnit() {
			if (empty())
				return null;
			int ch = input.charAt(position);
			if (ch == '%') {
				position++;
				return Unit.percent;
			}
			if (position > (input.length() - 2))
				return null;
			try {
				Unit result = Unit.valueOf(input.substring(position, position + 2).toLowerCase(Locale.US));
				position += 2;
				return result;
			} catch (IllegalArgumentException e) {
				return null;
			}
		}

		/*
		   * Check whether the next character is a letter.
		   */
		public boolean hasLetter() {
			if (position == input.length())
				return false;
			char ch = input.charAt(position);
			return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'));
		}

		/*
		   * Extract a quoted string from the input.
		   */
		public String nextQuotedString() {
			if (empty())
				return null;
			int start = position;
			int ch = input.charAt(position);
			int endQuote = ch;
			if (ch != '\'' && ch != '"')
				return null;
			ch = advanceChar();
			while (ch != -1 && ch != endQuote)
				ch = advanceChar();
			if (ch == -1) {
				position = start;
				return null;
			}
			position++;
			return input.substring(start + 1, position - 1);
		}

		/*
		   * Return the remaining input as a string.
		   */
		public String restOfText() {
			if (empty())
				return null;

			int start = position;
			position = input.length();
			return input.substring(start);
		}
	}

	private static class Length implements Cloneable {

		float value = 0;
		Unit unit = Unit.px;

		public Length(float value, Unit unit) {
			this.value = value;
			this.unit = unit;
		}

		public Length(float value) {
			this.value = value;
			this.unit = Unit.px;
		}

		public float floatValue() {
			return value;
		}

		// Convert length to user units for a horizontally-related context.
//		public float floatValueX(SVGAndroidRenderer renderer) {
//			switch (unit) {
//				case px:
//					return value;
//				case em:
//					return value * renderer.getCurrentFontSize();
//				case ex:
//					return value * renderer.getCurrentFontXHeight();
//				case in:
//					return value * renderer.getDPI();
//				case cm:
//					return value * renderer.getDPI() / 2.54f;
//				case mm:
//					return value * renderer.getDPI() / 25.4f;
//				case pt: // 1 point = 1/72 in
//					return value * renderer.getDPI() / 72f;
//				case pc: // 1 pica = 1/6 in
//					return value * renderer.getDPI() / 6f;
//				case percent:
//					Box viewPortUser = renderer.getCurrentViewPortInUserUnits();
//					if (viewPortUser == null)
//						return value;  // Undefined in this situation - so just return value to avoid an NPE
//					return value * viewPortUser.width / 100f;
//				default:
//					return value;
//			}
//		}

		// Convert length to user units for a vertically-related context.
//		public float floatValueY(SVGAndroidRenderer renderer) {
//			if (unit == Unit.percent) {
//				Box viewPortUser = renderer.getCurrentViewPortInUserUnits();
//				if (viewPortUser == null)
//					return value;  // Undefined in this situation - so just return value to avoid an NPE
//				return value * viewPortUser.height / 100f;
//			}
//			return floatValueX(renderer);
//		}

		// Convert length to user units for a context that is not orientation specific.
		// For example, stroke width.
//		public float floatValue(SVGAndroidRenderer renderer) {
//			if (unit == Unit.percent) {
//				Box viewPortUser = renderer.getCurrentViewPortInUserUnits();
//				if (viewPortUser == null)
//					return value;  // Undefined in this situation - so just return value to avoid an NPE
//				float w = viewPortUser.width;
//				float h = viewPortUser.height;
//				if (w == h)
//					return value * w / 100f;
//				float n = (float) (Math.sqrt(w * w + h * h) / SQRT2);  // see spec section 7.10
//				return value * n / 100f;
//			}
//			return floatValueX(renderer);
//		}

		// Convert length to user units for a context that is not orientation specific.
		// For percentage values, use the given 'max' parameter to represent the 100% value.
//		public float floatValue(SVGAndroidRenderer renderer, float max) {
//			if (unit == Unit.percent) {
//				return value * max / 100f;
//			}
//			return floatValueX(renderer);
//		}

		// For situations (like calculating the initial viewport) when we can only rely on
		// physical real world units.
		public float floatValue(float dpi) {
			switch (unit) {
				case px:
					return value;
				case in:
					return value * dpi;
				case cm:
					return value * dpi / 2.54f;
				case mm:
					return value * dpi / 25.4f;
				case pt: // 1 point = 1/72 in
					return value * dpi / 72f;
				case pc: // 1 pica = 1/6 in
					return value * dpi / 6f;
				case em:
				case ex:
				case percent:
				default:
					return value;
			}
		}

		public boolean isZero() {
			return value == 0f;
		}

		public boolean isNegative() {
			return value < 0f;
		}

		@Override
		public String toString() {
			return String.valueOf(value) + unit;
		}

	}


}
