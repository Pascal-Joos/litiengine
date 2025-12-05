package de.gurkenlabs.litiengine.graphics;

import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.util.Imaging;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class CreatureShadowImageEffect extends ImageEffect {
  private final Creature creature;
  private final Color shadowColor;
  private float offsetX;
  private float offsetY;

  public CreatureShadowImageEffect(Creature creature, Color shadowColor) {
    super("creatureShadow");
    this.creature = creature;
    this.shadowColor = shadowColor;
  }

  public Creature getCreature() {
    return this.creature;
  }

  public float getOffsetX() {
    return this.offsetX;
  }

  public CreatureShadowImageEffect setOffsetX(float offsetX) {
    this.offsetX = offsetX;
    return this;
  }

  public float getOffsetY() {
    return this.offsetY;
  }

  public CreatureShadowImageEffect setOffsetY(float offsetY) {
    this.offsetY = offsetY;
    return this;
  }

  @Override
  public BufferedImage apply(BufferedImage image) {
    if (this.getCreature().isDead()) {
      return image;
    }

    final BufferedImage buffer =
        Imaging.getCompatibleImage(image.getWidth() * 2 + 2, image.getHeight() * 2);
    if (buffer == null) {
      return image;
    }
    final Graphics2D graphics = buffer.createGraphics();
    float x = image.getWidth() / 2.0f;
    float y = image.getHeight() / 2.0f;

    this.drawShadow(
        graphics, image.getWidth(), image.getHeight(), x + this.offsetX, y + this.offsetY);

    ImageRenderer.render(graphics, image, x, y);
    graphics.dispose();
    return buffer;
  }

  protected Ellipse2D getShadowEllipse(
      final float spriteWidth, final float spriteHeight, float offsetX, float offsetY) {
    final double ellipseWidth = 0.60 * spriteWidth;
    final double ellipseHeight = 0.20 * spriteWidth;
    final double startX = (spriteWidth - ellipseWidth) / 2.0;
    final double startY = spriteHeight - ellipseHeight;
    return new Ellipse2D.Double(startX + offsetX, startY + offsetY, ellipseWidth, ellipseHeight);
  }

  protected void drawShadow(
      final Graphics2D graphics,
      final float spriteWidth,
      final float spriteHeight,
      float offsetX,
      float offsetY) {
    graphics.setColor(this.shadowColor);
    final RenderingHints hints = graphics.getRenderingHints();
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    graphics.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    graphics.fill(getShadowEllipse(spriteWidth, spriteHeight, offsetX, offsetY));
    graphics.setRenderingHints(hints);
  }
}
