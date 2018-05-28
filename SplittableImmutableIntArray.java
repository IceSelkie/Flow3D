import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * class SplittableImmutableIntArray (Better Name Needed)
 *
 * Its an array. That is immutable. And can be split without copying the whole array.
 * Can define a sub-region to treat as its own immutable array.
 *
 * I forgot why I thought I needed this. Ill put that reason here when I remember it.
 *
 * @author Stanley S.
 * @version 1.0
 */
public final class SplittableImmutableIntArray implements Iterable
{
  protected final int[] data; // The array of data. NOTE: is final, and is therefor immutable.
  protected final int start;
  protected final int end;
  protected final int len;

  /**
   * Constructor for SplittableImmutableIntArray.
   * Input array is copied.
   *
   * @param array The array to be used. (Note: a copy of the array will be made, so old references of the array cannot be used to modify the data within this immutable object.)
   */
  public SplittableImmutableIntArray(int[] array)
  {
    this(array, 0, array.length);
  }

  /**
   * Constructor for SplittableImmutableIntArray
   * Input array is copied.
   *
   * @param array The array to be used. (Note: a copy of the array will be made, so old references of the array cannot be used to modify the data within this immutable object.)
   * @param start Starting index (smallest index that can be used). Must be zero or larger, and smaller than the end index.
   * @param end   Ending index (smallest index that canNOT be used). Must be smaller than or equal to the array's length, and larger than the start index.
   */
  public SplittableImmutableIntArray(int[] array, int start, int end)
  {
    this(array, start, end, false);
  }

  /**
   * Constructor for SplittableImmutableIntArray
   * Creates a new SplittableImmutableIntArray for a subsection of that array.
   * Does not copy the array.
   *
   * @param old Existing SplittableImmutableIntArray to build this object off of.
   * @param start Starting index within that SplittableImmutableIntArray.
   * @param end Ending index within that SplittableImmutableIntArray.
   */
  public SplittableImmutableIntArray(SplittableImmutableIntArray old, int start, int end)
  {
    this(old.toArray(), start, end, true);
  }

  /**
   * a PACKAGE-PRIVATE Constructor for SplittableImmutableIntArray
   * Input array is optionally copied.
   *
   * @param array The array to be used. Will be copied if arrayIsImmutable is <code>true</code>.
   * @param start Starting index (smallest index that can be used). Must be zero or larger, and smaller than the end index.
   * @param end   Ending index (smallest index that canNOT be used). Must be smaller than or equal to the array's length, and larger than the start index.
   * @param arrayIsImmutable If the input array is immutable. If this is <code>true</code>, then the input array will not be copied. If <code>false</code>, it will.
   */
  SplittableImmutableIntArray(int[] array, int start, int end, boolean arrayIsImmutable)
  {
    if (start < 0)
      throw new IllegalArgumentException("The array cannot start at a negative index (" + start + ").");
    if (!(start < end))
      throw new IllegalArgumentException("The array cannot end before it starts.");
    if (end > array.length)
      throw new IllegalArgumentException("The array isn't long enough to end at " + end + ", it is only " + array.length + " long.");

    data = arrayIsImmutable?array:array.clone();
    this.start = start;
    this.end = end;
    this.len = end - start;
  }

  /**
   * Gets the value at the specified index within the bounded array.
   *
   * @param index The index to get information from. (Starting from the start location, not the beginning of the internal array.)
   * @return Value at that index.
   */
  public int get(int index)
  {
    if (index >= 0 && index < end)
      return data[start + index];
    else
      throw new ArrayIndexOutOfBoundsException("Tried to get element at index: "+index+". The index must be " + (index<0?"greater than or equal to 0.":"at most "+length()+"."));
  }

  /**
   * Creates a new SplittableImmutableIntArray object that is bounded within the specified indexes.
   *
   * @param start Starting index (smallest index that can be used). Must be zero or larger, and smaller than the end index.
   * @param end   Ending index (smallest index that canNOT be used). Must be smaller than or equal to the array's length, and larger than the start index.
   * @return The newly created SplittableImmutableIntArray object with this array that is now bounded.
   */
  public SplittableImmutableIntArray split(int start, int end)
  {
    if (end > len)
      throw new IllegalArgumentException("This array isn't long enough to end at " + end + ". (It can be at most " + length() + ")");
    return new SplittableImmutableIntArray(data, this.start + start, this.start + end, true);
  }

  /**
   * Returns the number of accessible elements in the array with this object.
   * Equivalent to <code>last-first</code>.
   *
   * @return the length of the bounded array.
   */
  public int length()
  {
    return len;
  }

  /**
   * Takes this object and converts it into an int array that has the exact same data.
   * This array is not immutable (its an array, not a SplittableImmutableIntArray).
   * Data is copied over.
   *
   * @return the int array that is equivalent to this object.
   */
  public int[] toArray()
  {
    int[] array = new int[len];
    for (int i = 0; i < len; i++)
      array[i] = data[start + i];
    return array;
  }

  /**
   * Converts this SplittableImmutableIntArray into a {@link java.awt.image.DataBufferInt} object, to be used for Images.
   * COPIES THE ARRAY!
   *
   * @return a new {@link java.awt.image.DataBufferInt} object with the stored data and returns it.
   */
  public DataBufferInt toDataBuffer()
  {
    return new DataBufferInt(toArray(),len);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
      return false;
    if (!(obj instanceof SplittableImmutableIntArray))
      return false;
    SplittableImmutableIntArray other = (SplittableImmutableIntArray) obj;

    if (data == other.data)
      return true;

    if (length() != other.length())
      return false;

    for (int i = 0; i < len; i++)
      if (get(i) != other.get(i))
        return false;

    return true;
  }

  /**
   * Trims the SplittableImmutableIntArray into one that doesnt have leading/trailing data in the internal array.
   * Copies the array.
   * <p>
   * The new object will work identical to this object. This action is completely useless.
   * .equals on this object will even return true.
   *
   * @return A new SplittableImmutableIntArray object that has been trimmed of its leading and trailing values in the internal array. Will work the same as this object.
   */
  @Deprecated
  public SplittableImmutableIntArray trim()
  {
    return new SplittableImmutableIntArray(this.toArray(), 0, len, true);
  }

  /**
   * Returns an iterator over the available elements.
   *
   * @return an iterator for this SplittableImmutableIntArray object.
   */
  @Override
  public Iterator iterator()
  {
    return new SIIAIterator(this);
  }

  /**
   * class SplittableImmutableIntArrayIterator (aka: SIIAIterator) (Better Name Needed)
   * <p>
   * Its an iterator for the SplittableImmutableIntArray class.
   * In case you wanted to for-each a SplittableImmutableIntArray like an array.
   * <p>
   * Also this is a private class and doesn't need a java-doc. Oh well.
   *
   * @author Stanley S.
   * @version 1.0
   */
  private class SIIAIterator implements Iterator
  {
    protected SplittableImmutableIntArray siia; // The array being iterated.
    protected int index; // Index of next element to output.

    /**
     * PACKAGE-PRIVATE constructor for SIIAIterator.
     * Creates a new iterator for SIIAIterator starting at the first element.
     * What did you expect?
     *
     * @param array The SIIA object that will be iterated.
     */
    SIIAIterator(SplittableImmutableIntArray array)
    {
      siia = array;
      index = 0;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next()} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    public boolean hasNext()
    {
      return index >= 0 && index < siia.length();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    public Object next() throws NoSuchElementException
    {
      if (!hasNext())
        throw new NoSuchElementException("There are no more elements in this direction on the array!");
      return siia.get(index++);
    }
  }
}
