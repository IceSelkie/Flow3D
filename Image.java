import java.awt.Color;

/**
 * class Image
 *
 * An image format that I can easily draw to the screen.
 * Data is stored in blocks of 4 bytes in an array. (Bytes not stored as byte primitive, due to byte storing -128 to 127, and not 0 to 255.)
 *
 * This format supports alpha channel as transparency.
 *
 * Implements Cloneable.
 * This object can be cloned using public method {@link #clone()}.
 *
 * @author Stanley S.
 * @version 0.1 (mostly done (I think))
 */
public class Image implements Cloneable
{
  /**
   * The count of the number of integer elements that together represent one pixel in {@code data}.
   */
  public static final int DATABLOCKLENGTH = 4; // RED, GREEN, BLUE, ALPHA*   -Alpha being 1 is palette color
  protected final SplittableImmutableIntArray data; // array of {@code DATABLOCKLENGTH} integers, one set for each pixel.
  protected final int width;
  protected final int height;
  protected double cropU, cropD, cropL, cropR; // The amount the image is cropped by, for each: Up, Down, Left, and Right
  protected double translateX, translateY; // Translation (sliding) of the image on the X and Y axes.
  protected double scaleX; // Scale is the multiplier on each axis that the image is stretched. Negative values invert the image.
  protected double scaleY;
  protected double rotation; // The overall rotation of the image.

  /**
   * Constructor for Image.
   * Creates an image given a two dimensional matrix of colors, one for each pixel.
   * <p>
   * 4 channels supported: Red, Green, Blue, and Alpha (transparency).
   *
   * @param imageColorMatrix The 2D color matrix to be converted to a new Image object.
   */
  public Image(Color[][] imageColorMatrix)
  {
    this(toSIIA(imageColorMatrix), imageColorMatrix[0].length, imageColorMatrix.length);
  }

  /**
   * Constructor for Image.
   * Creates an image given a one dimensional array of data, with 3 or 4 ints per pixel (with or without alpha)
   *
   * @param img    The image data. If no alpha, alpha defaults to 255 (fully visible).
   * @param width  The size of the image (width).
   * @param height The size of the image (height).
   */
  public Image(SplittableImmutableIntArray img, int width, int height)
  {
    if (img.length() == width * height * (DATABLOCKLENGTH - 1))
      img = alphaPad(img);
    if (img.length() != width * height * DATABLOCKLENGTH)
      throw new IllegalArgumentException("Data array length invalid.");

    data = img;
    this.width = width;
    this.height = height;
    clearCrop();
    clearTranslation();
    clearScale();
    clearRotation();
  }

  /**
   * Gets the array of color information at the given coordinates.
   * Note: x is horizontal, increasing from LEFT to RIGHT.
   * Note: y is vertical, increasing from TOP to BOTTOM.
   *
   * @param x The horizontal position within the image.
   * @param y The vertical position within the image.
   * @return An integer array of length {@code DATABLOCKLENGTH} containing the specified pixel's color information.
   */
  public int[] getA(int x, int y)
  {
    int index = (y * width + x) * DATABLOCKLENGTH;
    return data.split(index, index + 4).toArray();
  }

  /**
   * Gets the Color of the pixel at the given coordinates.
   * Note: x is horizontal, increasing from LEFT to RIGHT.
   * Note: y is vertical, increasing from TOP to BOTTOM.
   *
   * @param x The horizontal position within the image.
   * @param y The vertical position within the image.
   * @return A Color object containing the specified pixel's color.
   */
  public Color getC(int x, int y)
  {
    int index = (y * width + x) * DATABLOCKLENGTH;
    SplittableImmutableIntArray small = data.split(index, index + 4);
    return new Color(small.get(0), small.get(1), small.get(2), small.get(3));
  }

  /**
   * <a href="http://google.com/search?q=rasterize">Rasterizes</a> any modifications made to the image (Translations, Scales, Rotations, etc.) into a new Image object. Uses average color in the given region for output color.
   *
   * @return A new Image object of the modified image, rasterized to have no remaining modifications, but look as close to the original as possible.
   */
  public Image rasterize()
  {
    double[] pt1 = applyModificationsToPosition(cropL, cropU);
    double[] pt2 = applyModificationsToPosition(cropL, height - cropD);
    double[] pt3 = applyModificationsToPosition(width - cropR, cropU);
    double[] pt4 = applyModificationsToPosition(width - cropR, height - cropD);
    double top = max(pt1[1], pt2[1], pt3[1], pt4[1]);
    double bottom = min(pt1[1], pt2[1], pt3[1], pt4[1]);
    double left = min(pt1[0], pt2[0], pt3[0], pt4[0]);
    double right = max(pt1[0], pt2[0], pt3[0], pt4[0]);

    int width = (int) (Math.ceil(right) - Math.floor(left));
    int height = (int) (Math.ceil(bottom) - Math.floor(top));

    int[] data = new int[width * height * DATABLOCKLENGTH];

    int index = 0;
    for (int r = 0; r < height; r++)
      for (int c = 0; c < width; c++)
      {
        int[] color = getRasterizedPixel(c, r);
        //*DEBUG v
        if (index != c + r * width)
          System.err.println("index counter doesn't match position!");
        //*DEBUG ^
        for (int i = 0; i < DATABLOCKLENGTH; i++)
          data[index++] = color[i];
      }

    Image ret = new Image(new SplittableImmutableIntArray(data), width, height);
    int[] origin = getRelativeOriginInRasterizedImage();
    ret.translate(-origin[0], -origin[1]);
    return ret;
  }

  /**
   * Finds the color information in the form on an int[] of the average weighted colors within that pixel in the rasterized image.
   * <p>
   * Used to create the rasterized image.
   *
   * @param x The x coordinate in the rasterized image.
   * @param y The y coordinate in the rasterized image.
   * @return An integer array of length {@code DATABLOCKLENGTH} containing the color information of the specified pixel of the rasterized image.
   */
  public int[] getRasterizedPixel(int x, int y)
  {
    //TODO
    return null;
  }

  /**
   * Gets the coordinates of this image's origin, within the rasterized image.
   * Note: Will be a whole number, because bother have their color values snapped to the same coordinate grid.
   * <p>
   * After an image has been rasterized, call this to find how it has been translated (if needed).
   *
   * @return An integer array of length 2, with the x and y coordinates of the non-rasterized image's coordinates.
   */
  public int[] getRelativeOriginInRasterizedImage()
  {
    double[] pt1 = applyModificationsToPosition(cropL, cropU);
    double[] pt2 = applyModificationsToPosition(cropL, height - cropD);
    double[] pt3 = applyModificationsToPosition(width - cropR, cropU);
    double[] pt4 = applyModificationsToPosition(width - cropR, height - cropD);

    // The opposite of the upper left most corner of what was transformed.
    return new int[]{-roundDown(min(pt1[0], pt2[0], pt3[0], pt4[0])), -roundDown(min(pt1[1], pt2[1], pt3[1], pt4[1]))};
  }

  /**
   * Scales (stretches) the whole image from its center outward, with the given magnitude.
   * <p>
   * A magnitude of 1 does nothing.
   * Negative magnitudes are equivalent to rotating the image 180 degrees about its center, and scaling it by the positive magnitude.
   * A magnitude of -1 inverts the image, and is equivalent to just rotating the image 180 degrees about its center.
   *
   * @param magnitude Magnitude of the stretch to apply.
   */
  public void scale(double magnitude)
  {
    scale(magnitude, magnitude);
  }

  /**
   * Scales (stretches) the image by the given magnitudes on each of its axes separately.
   * <p>
   * A magnitude of 1 causes no change on that axis.
   * Negative magnitudes are equivalent to flipping the image on that axis, then scaling it by the positive magnitude.
   * A magnitude of -1 causes the image to flip over its center line.
   *
   * @param xMag The magnitude of the stretch to apply on the x axis.
   * @param yMag The magnitude of the stretch to apply on the y axis.
   */
  public void scale(double xMag, double yMag)
  {
    scaleAround(width / 2D, height / 2D, xMag, yMag);
  }

  /**
   * Scales (stretches) the image by the given magnitudes on each of its axes separately around a given point.
   * <p>
   * The position of that point doesn't change. All other points are stretched away an amount proportionate to their distance from this point.
   * <p>
   * A magnitude of 1 causes no change on that axis.
   * Negative magnitudes are equivalent to flipping the image on that axis (about the specified point), then scaling it by the positive magnitude.
   * A magnitude of -1 causes the image to flip that axis over the specified point.
   *
   * @param xMag The magnitude of the stretch to apply on the x axis.
   * @param yMag The magnitude of the stretch to apply on the y axis.
   */
  public void scaleAround(double xPos, double yPos, double xMag, double yMag)
  {
    // Otherwise we are shifting the unscaled amount.
    //TODO: actually that might be wrong. I wrote this kinda late at night.
    xPos *= scaleX;
    yPos *= scaleY;

    translateX -= xPos * (xMag * scaleX) - xPos * scaleX;
    scaleX *= xMag;
    translateY -= yPos * (yMag * scaleY) - yPos * scaleY;
    scaleY *= yMag;
  }

  /**
   * Translates the image over some amount.
   *
   * @param x The distance in pixels to translate the image to the right. (Positive X direction)
   * @param y The distance in pixels to translate the image downward. (Positive Y direction)
   */
  public void translate(double x, double y)
  {
    translateX += x;
    translateY += y;
  }

  /**
   * Rotates the image about it's center the specified amount.
   * <p>
   * 360 degrees is a full circle and does nothing.
   *
   * @param degrees The amount to rotate the image about it's center.
   */
  public void rotate(double degrees)
  {
    rotateAround(width / 2D, height / 2D, degrees);
  }

  /**
   * Rotates the image the specified amount about the specified point.
   * <p>
   * 360 degrees is a full circle and does nothing.
   * 180 degrees inverts the image about that point.
   *
   * @param xPos    The x position to rotate the image about.
   * @param yPos    The y position to rotate the image about.
   * @param degrees The amount to rotate the image about the specified point.
   */
  public void rotateAround(double xPos, double yPos, double degrees)
  {
    double dist = Math.sqrt(xPos * xPos + yPos * yPos);
    double angle = Math.atan2(yPos, xPos) + (rotation * 2 * Math.PI / 180);
    angle += angle >= 0 ? -Math.PI : Math.PI;

    translateX += dist * (Math.cos(angle) - Math.cos(angle + degrees));
    translateY += dist * (Math.cos(angle) - Math.cos(angle + degrees));

    rotation += degrees;
    rotation %= 360;
  }

  /**
   * Crops the image a given number of pixels from each of the sides.
   * <p>
   * Values can be negative to negate prior cropping.
   * <p>
   * If the crop crops more than the whole image, the crop distance is proportionate to the crop amount so that the crops on that axis meet at a point.
   *
   * @param top    The amount to crop downward from the top of the image.
   * @param bottom The amount to crop upward from the bottom of the image.
   * @param left   The amount to crop to the right from the left of the image.
   * @param right  The amount to crop to the left from the right of the image.
   */
  public void crop(double top, double bottom, double left, double right)
  {
    cropU += top;
    cropD += bottom;
    cropL += left;
    cropR += right;
    if (cropU < 0) cropU = 0;
    if (cropD < 0) cropD = 0;
    if (cropL < 0) cropL = 0;
    if (cropR < 0) cropR = 0;
    if (cropU + cropD > height)
    {
      double offset = (cropU + cropD - height) / 2;
      cropU -= offset * (top / (top + bottom));
      cropD -= offset * (bottom / (top + bottom));
    }
    if (cropL + cropR > width)
    {
      double offset = (cropL + cropR - width) / 2;
      cropL -= offset * (left / (left + right));
      cropR -= offset * (right / (left + right));
    }
  }

  /**
   * Returns whether or not the image has been modified with a crop, translation, scale, or rotation.
   *
   * @return Returns {@code true} if this image has been cropped, translated, scaled, or rotated, and {@code false} otherwise.
   */
  public boolean isLocallyModified()
  {
    return isLocallyCropped() || isLocallyTranslated() || isLocallyScaled() || isLocallyRotated();
  }

  /**
   * Returns whether or not the image has been cropped.
   *
   * @return Returns {@code true} if this image has been cropped, and {@code false} otherwise.
   */
  public boolean isLocallyCropped()
  {
    return cropU != 0 || cropD != 0 || cropL != 0 || cropR != 0;
  }

  /**
   * Returns whether or not the image has been translated.
   *
   * @return Returns {@code true} if this image has been translated, and {@code false} otherwise.
   */
  public boolean isLocallyTranslated()
  {
    return translateX != 0 || translateY != 0;
  }

  /**
   * Returns whether or not the image has been scaled.
   *
   * @return Returns {@code true} if this image has been scaled, and {@code false} otherwise.
   */
  public boolean isLocallyScaled()
  {
    return scaleX != 1 || scaleY != 1;
  }

  /**
   * Returns whether or not the image has been rotated.
   *
   * @return Returns {@code true} if this image has been rotated, and {@code false} otherwise.
   */
  public boolean isLocallyRotated()
  {
    return rotation != 0;
  }

  /**
   * Clears the image's current modifications: any crop, translation, scale, and rotation.
   */
  public void clearModifications()
  {
    clearCrop();
    clearTranslation();
    clearScale();
    clearRotation();
  }

  /**
   * Clears the image's of any current crop.
   */
  public void clearCrop()
  {
    cropU = cropD = cropL = cropR = 0;
  }

  /**
   * Clears the image's of any current translation.
   */
  public void clearTranslation()
  {
    translateX = translateY = 0;
  }

  /**
   * Clears the image's of any current scale.
   */
  public void clearScale()
  {
    scaleX = scaleY = 1;
  }

  /**
   * Clears the image's of any current rotation.
   */
  public void clearRotation()
  {
    rotation = 0;
  }

  /**
   * Takes a coordinate on the current image, and finds the location of that point after the image's modifications have been applied.
   *
   * @param xPosOrig The x position on the current image.
   * @param yPosOrig The y position on the current image.
   * @return An double array of length 2, with the x and y coordinates of the point after the image's modifications have been applied.
   */
  public double[] applyModificationsToPosition(double xPosOrig, double yPosOrig)
  {
    xPosOrig *= scaleX;
    yPosOrig *= scaleY;

    double dist = Math.sqrt(xPosOrig * xPosOrig + yPosOrig * yPosOrig);
    double angleRad = Math.atan2(yPosOrig, xPosOrig);
    angleRad += rotationRadian();

    double xPosModi = dist * Math.cos(angleRad) + translateX;
    double yPosModi = dist * Math.sin(angleRad) + translateY;

    return new double[]{xPosModi, yPosModi};
  }

  /**
   * Takes a coordinate on the modified image, and finds the location of that point prior to the image's modifications.
   *
   * @param xPosModi The x position on the modified image.
   * @param yPosModi The y position on the modified image.
   * @return An double array of length 2, with the x and y coordinates of the point prior to the image's modifications.
   */
  public double[] reverseModification(double xPosModi, double yPosModi)
  {
    xPosModi -= translateX;
    yPosModi -= translateY;

    double dist = Math.sqrt(xPosModi * xPosModi + yPosModi * yPosModi);
    double angleRad = Math.atan2(yPosModi, xPosModi);
    angleRad -= rotationRadian();

    double xPosOrig = dist * Math.cos(angleRad);
    double yPosOrig = dist * Math.sin(angleRad);
    xPosOrig /= scaleX;
    yPosOrig /= scaleY;

    //*DEBUG v
    double[] modifCheck = applyModificationsToPosition(xPosOrig, yPosOrig);
    if (!(within(modifCheck[0], xPosModi, .01) && within(modifCheck[1], yPosModi, .01)))
      System.err.println("Reversing position from a point then reapplying doesn't give the original value!!");
    //*DEBUG ^
    return new double[]{xPosOrig, yPosOrig};
  }

  /**
   * The rotation of the image in radians rather than degrees.
   *
   * @return The rotation of the image in radians.
   */
  private double rotationRadian()
  {
    return (rotation % 360) * 2 * Math.PI / 180;
  }

  /**
   * Takes a 2 dimensional matrix of colors, and converts them into a SplittableImmutableIntArray, that is used to store the color information.
   *
   * @param img The 2D matrix of colors.
   * @return A new SplittableImmutableIntArray that contains the same information (minus the width and height).
   */
  private static SplittableImmutableIntArray toSIIA(Color[][] img)
  {
    int[] data = new int[img[0].length * img.length * DATABLOCKLENGTH];
    int index = 0;
    for (Color[] ca : img)
      for (Color c : ca)
      {
        data[index++] = c.getRed();
        data[index++] = c.getGreen();
        data[index++] = c.getBlue();
        data[index++] = c.getAlpha();
        if (data[index - 1] == 1)
          data[index - 1]++;
      }
    return new SplittableImmutableIntArray(data);
  }

  /**
   * Takes a SplittableImmutableIntArray representing the image without alpha, and adds the data for the alpha channel in.
   * <p>
   * Every alpha value defaults to 255 (which is fully opaque given that alpha is transparency).
   *
   * @param img The original image as a SplittableImmutableIntArray, that has no alpha channel.
   * @return A new SplittableImmutableIntArray that has the alpha channel.
   */
  private static SplittableImmutableIntArray alphaPad(SplittableImmutableIntArray img)
  {
    int[] ret = new int[img.length() / (DATABLOCKLENGTH - 1) * DATABLOCKLENGTH];

    int index = 0;
    for (int i = 0; i < ret.length; i++)
      ret[i] = (i % 4 == 0) ? 255 : img.get(index++);
    return new SplittableImmutableIntArray(ret);
  }

  public Image clone()
  {
    return new Image(data, width, height, new double[]{cropU, cropD, cropL, cropR}, scaleX, scaleY, rotation);
  }

  /**
   * Used for cloning.
   * <p>
   * All data is immutable or primitives, so only fields need to be copied.
   */
  private Image(SplittableImmutableIntArray data, int width, int height, double[] crop, double scaleX, double scaleY, double rotation)
  {
    this.width = width;
    this.height = height;
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    this.rotation = rotation;
    cropU = crop[0];
    cropD = crop[1];
    cropL = crop[2];
    cropR = crop[3];
    this.data = data;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
      return false;
    if (!(obj instanceof Image))
      return false;
    Image other = (Image) obj;

    return (this.rasterize().data.equals(other.rasterize().data));
  }

  /**
   * Returns the minimum value in a set of doubles.
   *
   * @param num1      The first value
   * @param otherNums All other values.
   * @return The smallest value in the set.
   */
  private static double min(double num1, double... otherNums)
  {
    double minimum = num1;
    for (double num : otherNums)
      if (num < minimum)
        minimum = num;
    return minimum;
  }

  /**
   * Returns the maximum value in a set of doubles.
   *
   * @param num1      The first value
   * @param otherNums All other values.
   * @return The largest value in the set.
   */
  private static double max(double num1, double... otherNums)
  {
    double maximum = num1;
    for (double num : otherNums)
      if (num > maximum)
        maximum = num;
    return maximum;
  }

  /**
   * Takes a double, and rounds down to the nearest whole number.
   * <p>
   * Equivalent to {@code (int)Math.floor(val)}
   *
   * @param val Value that is being rounded.
   * @return An integer of the value that is rounded down.
   */
  private static int roundDown(double val)
  {
    return (int) Math.floor(val);
  }

  /**
   * Takes a double, and rounds up to the nearest whole number.
   * <p>
   * Equivalent to {@code (int)Math.ceil(val)}
   *
   * @param val Value that is being rounded.
   * @return An integer of the value that is rounded up.
   */
  private static int roundUp(double val)
  {
    return (int) Math.ceil(val);
  }

  /**
   * Asserts that the difference of the two values is less than or equal to a given allowed amount of error.
   *
   * @param valueA   The first value
   * @param valueB   The second value
   * @param maxError The maximum allowed difference between the two values
   * @return {@code true} if the two values are within {@code maxError} of each other.
   */
  private static boolean within(double valueA, double valueB, double maxError)
  {
    return within(valueB - valueA, maxError);
  }

  /**
   * Asserts that the value's magnitude is less than or equal to a given allowed amount of error.
   *
   * @param difference The value being tested that must be less than the error.
   * @param maxError   The maximum allowed error (or value).
   * @return {@code true} if the value's magnitude is less than or equal to the {@code maxError}.
   */
  private static boolean within(double difference, double maxError)
  {
    return Math.abs(difference) <= maxError;
  }
}
