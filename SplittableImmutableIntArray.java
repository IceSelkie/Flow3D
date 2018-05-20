import java.util.Iterator;

public class SplittableImmutableIntArray implements Iterable
{
  protected final int[] data;
  protected final int start;
  protected final int end;
  protected final int len;

  public SplittableImmutableIntArray(int[] array)
  {
    this(array, 0, array.length);
  }

  public SplittableImmutableIntArray(int[] array, int start, int end)
  {
    this(start, end, array.clone());
  }

  private SplittableImmutableIntArray(int start, int end, int[] array)
  {
    data = array;
    this.start = start;
    this.end = end;
    this.len = end - start;

    if (start < 0)
      throw new IllegalArgumentException("The array cannot start at a negative index (" + start + ").");
    if (!(start < end))
      throw new IllegalArgumentException("The array cannot end before it starts.");
    if (end >= data.length)
      throw new IllegalArgumentException("The array isn't long enough to end at " + end + ", it is only " + len + " long.");
  }

  public int get(int i)
  {
    if (i > 0 && i < end)
      return data[start + i];
    else
      throw new ArrayIndexOutOfBoundsException();
  }

  public SplittableImmutableIntArray split(int start, int end)
  {
    if (end >= len)
      throw new IllegalArgumentException("This array isn't long enough to end at " + end + ". (It can be at most " + (len - 1) + ")");
    return new SplittableImmutableIntArray(data, this.start + start, start + end);
  }

  public int length()
  {
    return len;
  }

  public int[] toArray()
  {
    int[] array = new int[len];
    for (int i = 0; i < len; i++)
      array[i] = data[start + i];
    return array;
  }

  @Deprecated
  public SplittableImmutableIntArray trim()
  {
    return new SplittableImmutableIntArray(0, len, this.toArray());
  }

  @Override
  public Iterator iterator()
  {
    return new SIIAIterator(this);
  }

  private class SIIAIterator implements Iterator
  {
    protected SplittableImmutableIntArray siia;
    protected int index;

    private SIIAIterator(SplittableImmutableIntArray arr)
    {
      siia = arr;
      index = 0;
    }

    public boolean hasNext()
    {
      return index < siia.length();
    }

    public Object next()
    {
      return siia.get(index++);
    }
  }
}
