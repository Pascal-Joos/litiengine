package de.gurkenlabs.litiengine.environment.tilemap.xml;

import de.gurkenlabs.litiengine.environment.tilemap.IImageLayer;
import de.gurkenlabs.litiengine.environment.tilemap.IMapImage;
import java.awt.Color;
import java.net.URL;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class ImageLayer extends Layer implements IImageLayer {

  @Nullable @XmlElement private MapImage image;

  @Nullable
  @XmlAttribute
  @XmlJavaTypeAdapter(ColorAdapter.class)
  private Color trans;

  @Nullable
  @Override
  public IMapImage getImage() {
    return this.image;
  }

  @Nullable
  @Override
  public Color getTransparentColor() {
    return this.trans;
  }

  @Override
  public int getOffsetX() {
    if (this.isInfiniteMap()) {
      TmxMap map = (TmxMap) this.getMap();
      return super.getOffsetX() - map.getChunkOffsetX() * map.getTileWidth();
    }

    return super.getOffsetX();
  }

  @Override
  public int getOffsetY() {
    if (this.isInfiniteMap()) {
      TmxMap map = (TmxMap) this.getMap();
      return super.getOffsetX() - map.getChunkOffsetY() * map.getTileHeight();
    }

    return super.getOffsetY();
  }

  private boolean isInfiniteMap() {
    return this.getMap() != null && this.getMap().isInfinite() && this.getMap() instanceof TmxMap;
  }

  @Override
  void finish(@Nullable URL location) throws TmxException {
    super.finish(location);
    if (this.image != null) {
      this.image.finish(location);
    }
  }
}
