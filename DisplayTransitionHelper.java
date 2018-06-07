/**
 * class DisplayTransitionHelper
 * <p>
 * Helps {@link DisplayableWindow}s calculate variations and gradients.
 *
 * @author Stanley Sisson
 * @version 1.0
 */
public class DisplayTransitionHelper
{
  /**
   * The start and end values for the curve.
   */
  protected double a, b;
  /**
   * The time it takes to get from value {@code a} to {@code b}, or visa-versa.
   */
  protected int time;

  /**
   * The current time, or "location" on the timeline.
   */
  protected int location;
  /**
   * whether values are increasing if time is increasing or not.
   */
  protected boolean upward;
  /**
   * The type of interpolation to use to find the intermediate values.
   */
  protected InterpolationType interpolation;

  /**
   * Constructor for DisplayTransitionHelper
   * <p>
   * Creates a new DisplayTransitionHelper to create transitions between the values of {@code start} and {@code end} over {@code time} frames.
   *
   * @param start         The {@code a} or starting value.
   * @param end           The {@code b} or ending value.
   * @param time          The amount of time it should take to vary between {@code a} and {@code b}.
   * @param startTime     The value in the timeline to start at. If this value is negative, it will start at that magnitude decreasing. If it is larger than {@code time}, then it well be set to the maximum value of {@code time}.
   * @param interpolation The type of interpolation between values {@code a} and {@code b}.
   */
  public DisplayTransitionHelper(double start, double end, int time, int startTime, InterpolationType interpolation)
  {
    if (start <= end)
    {
      a = start;
      b = end;
    }
    else
    {
      b = start;
      a = end;
    }
    this.time = time;
    this.interpolation = interpolation;

    if (startTime < 0)
    {
      upward = false;
      location = -startTime;
    }
    else
    {
      upward = true;
      location = startTime;
    }

    location = Display.normalizeInt(location, 0, time);
  }

  /**
   * Processes one tick of time. Call this every frame ({@link Display#FPS} times per second).
   * <p>
   * The {@code location} will increase or decrease based on if it is moving in the {@code upward} direction or not.
   */
  public void tick()
  {
    location += upward ? 1 : -1;
    location = Display.normalizeInt(location, 0, time);
  }

  /**
   * Gets the intermediate value at this {@code location}, using the specified interpolation ({@link InterpolationType}).
   *
   * @return the intermediate value for this time.
   */
  public double get()
  {
    location = Display.normalizeInt(location, 0, time);

    return a + (b - a) * interpolation.get((double) location / time);
  }

  public void setIncreasing()
  {
    upward = true;
  }

  public void setDecreasing()
  {
    upward = false;
  }

  public void reverseDirection()
  {
    upward = !upward;
  }

  public boolean isIncreasing()
  {
    return upward;
  }

  public boolean isDecreasing()
  {
    return !upward;
  }

  public int getTime()
  {
    return location;
  }

  public InterpolationType getInterpolation()
  {
    return interpolation;
  }
}