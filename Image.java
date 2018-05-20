import java.awt.Color;

/**
 * class Image
 *
 * An image format that I can easily draw to the screen.
 * Data is stored in blocks of 4 bytes. (Bytes not stored as byte primitive, due to byte storing -128 to 127, and not 0 to 255.)
 *
 *
 * @author Stanley S.
 * @version 0.0 (not done)
 */
public class Image implements Cloneable
{
  protected static final int DATABLOCKLENGTH = 4; // RED, GREEN, BLUE, ALPHA*   -Alpha being 1 is palette color
  protected final SplittableImmutableIntArray data; // width*height num of colors, stored as ints
  protected final int width;
  protected final int height;
  protected double cropU,cropD,cropL,cropR; // Up, Down, Left, Right
  protected double translateX,translateY;
  protected double scaleX;
  protected double scaleY;
  protected double rotation;

  public Image(Color[][] img)
  {
    this(toSIIA(img), img[0].length,img.length);
  }

  public Image(SplittableImmutableIntArray img, int width, int height)
  {
    if (img.length() != width * height * DATABLOCKLENGTH)
      throw new IllegalArgumentException("Data array length invalid.");

    data = img;
    this.width = width;
    this.height = height;
    cropU = 0;
    cropD = 0;
    cropL = 0;
    cropR = 0;
    translateX = 0;
    translateY = 0;
    scaleX = 1;
    scaleY = 1;
    rotation = 0;
  }

  public int[] getA(int x, int y)
  {
    int index = (y*width+x)*DATABLOCKLENGTH;
    return data.split(index,index+4).toArray();
  }
  public Color getC(int x, int y)
  {
    int index = (y*width+x)*DATABLOCKLENGTH;
    SplittableImmutableIntArray small = data.split(index,index+4);
    return new Color(small.get(0),small.get(1),small.get(2),small.get(3));
  }

  public Image rasterize()
  {
    double[] pt1 = applyModificationsToPosition(cropL,cropU);
    double[] pt2 = applyModificationsToPosition(cropL,height-cropD);
    double[] pt3 = applyModificationsToPosition(width-cropR,cropU);
    double[] pt4 = applyModificationsToPosition(width-cropR,height-cropD);
    double top = max(pt1[1],pt2[1],pt3[1],pt4[1]);
    double bottom = min(pt1[1],pt2[1],pt3[1],pt4[1]);
    double left = min(pt1[0],pt2[0],pt3[0],pt4[0]);
    double right = max(pt1[0],pt2[0],pt3[0],pt4[0]);

    int width = (int)(Math.ceil(right)-Math.floor(left));
    int height = (int)(Math.ceil(bottom)-Math.floor(top));

    int[] data = new int[width*height*DATABLOCKLENGTH];

    int index = 0;
    for (int r=0; r<height; r++)
      for (int c=0; c<width; c++)
      {
        int[] color = getRasterizedPixel(c,r);
        //*DEBUG v
        if (index!=c+r*width)
          System.err.println("index counter doesn't match position!");
        //*DEBUG ^
        for (int i = 0; i<DATABLOCKLENGTH; i++)
          data[index++] = color[i];
      }
      return new Image(new SplittableImmutableIntArray(data), width,height);
  }
  public int[] getRasterizedPixel(int x, int y)
  {
    //TODO
  }

  public int[] getRelativeOriginInRasterizedImage()
  {
    double[] pt1 = applyModificationsToPosition(cropL,cropU);
    double[] pt2 = applyModificationsToPosition(cropL,height-cropD);
    double[] pt3 = applyModificationsToPosition(width-cropR,cropU);
    double[] pt4 = applyModificationsToPosition(width-cropR,height-cropD);

    // The opposite of the upper left most corner that was transformed.
    return new int[]{-roundDown(min(pt1[0],pt2[0],pt3[0],pt4[0])),-roundDown(min(pt1[1],pt2[1],pt3[1],pt4[1]))};
  }
  private static double min(double num1, double... otherNums)
  {
    double minimum = num1;
    for (double num : otherNums)
      if (num<minimum)
        minimum = num;
    return minimum;
  }
  private static double max(double num1, double... otherNums)
  {
    double maximum = num1;
    for (double num : otherNums)
      if (num>maximum)
        maximum = num;
    return maximum;
  }
  private static int roundDown(double val)
  {
    return (int) Math.floor(val);
  }

  public void scale(double magnitude)
  {
    scale(magnitude,magnitude);
  }
  public void scale(double xMag, double yMag)
  {
    scaleAround(width/2D,height/2D,xMag,yMag);
  }
  public void scaleAround(double xPos, double yPos, double xMag, double yMag)
  {
    // Otherwise we are shifting the unscaled amount.
    //TODO: actually that might be wrong. I wrote this kinda late at night.
    xPos*=scaleX;
    yPos*=scaleY;

    translateX-=xPos*(xMag*scaleX)-xPos*scaleX;
    scaleX*=xMag;
    translateY-=yPos*(yMag*scaleY)-yPos*scaleY;
    scaleY*=yMag;
  }

  public void translate(double x, double y)
  {
    translateX+=x;
    translateY+=y;
  }

  public void rotate(double degrees)
  {
    rotateAround(width/2D,height/2D,degrees);
  }
  public void rotateAround(double xPos, double yPos, double degrees)
  {
    double dist = Math.sqrt(xPos * xPos + yPos * yPos);
    double angle = Math.atan2(yPos, xPos) + (rotation * 2 * Math.PI / 180);
    angle += angle >= 0 ? -Math.PI : Math.PI;

    translateX += dist * (Math.cos(angle) - Math.cos(angle+degrees));
    translateY += dist * (Math.cos(angle) - Math.cos(angle+degrees));

    rotation += degrees;
    rotation %= 360;
  }

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
  }

  public boolean isLocallyModified()
  {
    return isLocallyScaled() || isLocallyRotated() || isLocallyCropped();
  }
  private boolean isLocallyCropped()
  {
    return cropU!=0 || cropD!=0 || cropL!=0 || cropR!=0;
  }
  private boolean isLocallyScaled()
  {
    return scaleX!=0 || scaleY!=0;
  }
  private boolean isLocallyRotated()
  {
    return rotation!=0;
  }

  public void clearModifications()
  {
    cropU = 0;
    cropD = 0;
    cropL = 0;
    cropR = 0;
    translateX = 0;
    translateY = 0;
    scaleX = 1;
    scaleY = 1;
    rotation = 0;
  }

  public double[] applyModificationsToPosition(double x, double y)
  {
    x*=scaleX;
    y*=scaleY;

    double dist = Math.sqrt(x*x+y*y);
    double angleRad = Math.atan2(y,x);

    return new double[]{dist*Math.cos(angleRad+rotationRadian())+translateX,dist*Math.sin(angleRad+rotationRadian())+translateY};
  }
  public double[] reverseModi

  private double rotationRadian()
  {
    return (rotation % 360) * 2 * Math.PI / 180;
  }

  private static SplittableImmutableIntArray toSIIA(Color[][] img)
  {
    int[] data = new int[img[0].length*img.length*DATABLOCKLENGTH];
    int index = 0;
    for (Color[] ca : img)
      for (Color c : ca)
      {
        data[index++] = c.getRed();
        data[index++] = c.getGreen();
        data[index++] = c.getBlue();
        data[index++] = c.getAlpha();
        if (data[index-1] == 1)
          data[index-1]++;
      }
    return new SplittableImmutableIntArray(data);
  }

  public Image clone()
  {
    return new Image(data,width,height,new double[]{cropU,cropD,cropL,cropR},scaleX,scaleY,rotation);
  }
  private Image(SplittableImmutableIntArray data, int width, int height, double crop[], double scaleX, double scaleY, double rotation)
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
}
