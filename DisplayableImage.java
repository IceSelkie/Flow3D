import java.awt.geom.AffineTransform;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.BufferedImage;

/**
 * class DisplayableImage extends {@link BufferedImage} implements {@link RenderedImage}
 * <p>
 * Acts as a buffered (rendered) image that can easily be displayed to a Graphics2D.
 *
 * @author Stanley S.
 * @version 0.1 (Supposed to work. But something somewhere doesn't.)
 */
public class DisplayableImage extends BufferedImage
{
  /**
   * {@link AffineTransform} that contains the data for the translations/scales to be displayed on the screen where it needs to be.
   * CURRENTLY NOT NEEDED GIVEN IMPLEMENTATION OF {@link Display#drawImage(DisplayableImage, double, double)}.
   */
  protected AffineTransform affine;
  private static final ColorModel cm = new DirectColorModel(32, 0x00ff0000, 0x0000ff00, 0x000000ff, 0xff000000);

  /**
   * Constructor for DisplayableImage.
   * <p>
   * Creates a DisplayableImage given a {@link WritableRaster}.
   *
   * @param raster
   */
  public DisplayableImage(WritableRaster raster)
  {
    super(cm, raster, cm.isAlphaPremultiplied(), null);

    affine = new AffineTransform(1, 0, 0, 1, 0, 0);
  }

  /**
   * Gets the contained {@link AffineTransform}.
   * NOTE: THIS IS CURRENTLY OVERWRITTEN EVERYTIME IT IS READ.
   *
   * @return The {@link AffineTransform} that tells where to draw the image.
   */
  public AffineTransform getAffine()
  {
    return affine;
  }

  /**
   * Sets the contained {@link AffineTransform}.
   * NOTE: The data of the {@link AffineTransform} can be overwritten without replacing the object, and that is done every time anyways.
   *
   * @param affine The new {@link AffineTransform} to be used.
   */
  public void setAffine(AffineTransform affine)
  {
    this.affine = affine;
  }

  /**
   * Centers the image by replacing the contained {@link AffineTransform}.
   */
  public void center()
  {
    setAffine(new AffineTransform(affine.getScaleX(), 0, 0, affine.getScaleY(), (getWidth() * affine.getScaleX()) / 2D, getHeight() * affine.getScaleY() / 2D));
  }
}
