import java.awt.Color;

/**
 * Enumeration PathColor
 * <p>
 * The different colors the paths can be
 *
 * @author Kevin C. & Stanley S.
 * @version 1.1
 */
public enum PathColor
{
  RED, GREEN, BLUE, YELLOW, ORANGE, MAGENTA, AQUA;

  /**
   * An array of all the different {@code PathColor} types.
   * <p>
   * Used in {@link PathColor#get(int)}.
   */
  private static final PathColor[] VALUES = values();

  /**
   * The number of different colors that are supported.
   */
  public static final int COUNT = VALUES.length;

  /**
   * Gets the color of the enumerated value.
   *
   * @return The color equivalent to the value.
   */
  public Color toColor()
  {
    switch (this)
    {
      case RED:
        return new Color(255, 63, 63, 255);
      case GREEN:
        return new Color(0, 191, 0, 255);
      case BLUE:
        return new Color(0, 127, 255, 255);
      case YELLOW:
        return new Color(221, 221, 0, 255);
      case ORANGE:
        return new Color(255, 127, 0, 255);
      case MAGENTA:
        return new Color(221, 0, 221, 255);
      case AQUA:
        return new Color(0, 221, 221, 255);
      default:
        return null;
    }
  }

  /**
   * Gets a Path color from an enumerated integer value.
   *
   * @param i the integer value of the {@code PathColor}
   * @return the corresponding {@code PathColor} of the given integer.
   */
  public static PathColor get(int i)
  {
    return VALUES[Math.abs(i) % COUNT];
    //throw new IndexOutOfBoundsException("(positive number) % "+COUNT+" isn't between 0 and "+COUNT+".");
  }
}
