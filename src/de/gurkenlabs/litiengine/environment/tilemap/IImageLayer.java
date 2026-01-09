package de.gurkenlabs.litiengine.environment.tilemap;

import java.awt.Color;
import javax.annotation.Nullable;

public interface IImageLayer extends ICustomPropertyProvider, ILayer {

  /**
   * Gets the image.
   *
   * @return the image
   */
  @Nullable
  public IMapImage getImage();

  /**
   * Gets the transparent color.
   *
   * @return the transparent color
   */
  @Nullable
  public Color getTransparentColor();
}
