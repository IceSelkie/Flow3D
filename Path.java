/**
 * class Path
 * <p>
 * Represents a single path within a {@link Cube}.
 *
 * @author Kevin C.
 * @version 1.2
 */
public class Path
{
  /**
   * The type of path: Start or Path
   */
  private PathType type;
  /**
   * The color of the path.
   */
  private PathColor color;
  /**
   * The direction of the next Path in the flow.
   */
  private PathDirection direction;

  /**
   * Constructor for Path
   * <p>
   * Creates a new {@code Path} object with the given type, color, and direction.
   */
  public Path(PathType inputType, PathColor inputColor, PathDirection inputDirection)
  {
    type = inputType;
    color = inputColor;
    direction = inputDirection;
  }

  /**
   * Constructor for Path
   * <p>
   * Creates a new {@code Path} object with the given type and color. Direction is set to null.
   */
  public Path(PathType inputType, PathColor inputColor)
  {
    type = inputType;
    color = inputColor;
    direction = null;
  }

  /**
   * Changes this {@code Path}'s {@link PathType} to the specified {@link PathType}.
   *
   * @param newType The new type of this path.
   */
  public void setType(PathType newType)
  {
    type = newType;
  }

  /**
   * Changes this {@code Path}'s {@link PathColor} to the specified {@link PathColor}.
   *
   * @param newColor The new color of this path.
   */
  public void setColor(PathColor newColor)
  {
    color = newColor;
  }

  /**
   * Changes this {@code Path}'s flow direction to the specified {@link PathDirection}.
   *
   * @param newDirection The new flow direction of this path.
   */
  public void setDirection(PathDirection newDirection)
  {
    direction = newDirection;
  }

  /**
   * Returns the {@link PathType} of this {@code Path}.
   *
   * @return the type of this {@code Path}.
   */
  public PathType getType()
  {
    return type;
  }

  /**
   * Returns the {@link PathColor} of this {@code Path}.
   *
   * @return the color of this {@code Path}.
   */
  public PathColor getColor()
  {
    return color;
  }

  /**
   * Returns the flow direction of this {@code Path}.
   *
   * @return the flow direction of this {@code Path}.
   */
  public PathDirection getDirection()
  {
    return direction;
  }
}