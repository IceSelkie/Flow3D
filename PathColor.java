import java.awt.Color;

/**
 * Enumeration PathColor
 *
 * The different colors the paths can be
 *
 * @author Kevin C.
 * @version 1.0
 */
public enum PathColor
{
    RED, GREEN, BLUE, YELLOW, ORANGE, MAGENTA, AQUA;

    /**
     * Gets the color of the enumerated value.
     *
     * @return The color equivalent to the value.
     */
    public Color getColor()
    {
        switch (this)
        {
            case RED:
                return new Color(255,63,63,255);
            case GREEN:
                return new Color(0,191,0,255);
            case BLUE:
                return new Color(0,127,255,255);
            case YELLOW:
                return new Color(221,221,0,255);
            case ORANGE:
                return new Color(255,127,0,255);
            case MAGENTA:
                return new Color(221,0,221,255);
            case AQUA:
                return new Color(0,221,221,255);
            default:
                return null;
        }
    }
}
