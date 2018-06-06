/**
 * enum InterpolationType
 * <p>
 * Represents a type of interpolation
 */
public enum InterpolationType
{
  /**
   * Linear is a straight line. Draws a line from A to B.
   */
  LINEAR,
  /**
   * Sinusoid uses a cosine to smoothly bring the line from A to B. Second Derivative jumps at endpoints.
   */
  SINUSOID,
  /**
   * The velocity is linear in nature. Smooth curve interpolates between A and B. Second derivative is a constant, and changes sign at the half way point.
   */
  LINEAR_VELOCITY;

  /**
   * Finds the modifier at a position for Linear interpolation.
   * <p>
   * Linear is linear and will return the passed value.
   *
   * @param val Distance between {@code a} and {@code b}, as a value between {@code 0} and {@code 1}.
   * @return The multiplier to the output for Linear interpolation.
   */
  public static double getLinear(double val)
  {
    return val;
  }

  /**
   * Finds the modifier at a position for Cosine interpolation.
   * <p>
   * Creates a cosine curve between {@code a} and {@code b}, and returns its value for that location.
   *
   * @param val Distance between {@code a} and {@code b}, as a value between {@code 0} and {@code 1}.
   * @return The multiplier to the output for Linear interpolation.
   */
  public static double getSinusoid(double val)
  {
    return -(Math.cos(val * Math.PI) + 1) / 2;
  }

  /**
   * Finds the modifier at a position for Cosine interpolation.
   * <p>
   * Acceleration is a positive constant between {@code 0} and {@code .5}, then is the opposite of that constant between {@code .5} and {@code 1}.
   * Derivative/speed will be {@code 0} at both ends, and the maximum at {@code .5}.
   *
   * @param val Distance between {@code a} and {@code b}, as a value between {@code 0} and {@code 1}.
   * @return The multiplier to the output for Linear interpolation.
   */
  public static double getLinearVelocity(double val)
  {
    if (val > .5D)
      return 1 - getLinearVelocity(1 - val);
    return 2 * val * val;
  }

  /**
   * Finds the modifier at a position for this {@code InterpolationType}.
   *
   * @param val Distance between {@code a} and {@code b}, as a value between {@code 0} and {@code 1}.
   * @return The multiplier to the output for this {@code InterpolationType}.
   */
  public double get(double val)
  {
    if (val <= 0)
      return 0;
    if (val >= 1)
      return 1;

    switch (this)
    {
      case LINEAR:
        return getLinear(val);
      case SINUSOID:
        return getSinusoid(val);
      case LINEAR_VELOCITY:
        return getLinearVelocity(val);
      default:
        return getLinear(val);
    }
  }
}