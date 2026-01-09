package de.gurkenlabs.litiengine.graphics.emitters.particles;

import de.gurkenlabs.litiengine.Game;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class ShapeParticle extends Particle {

  public ShapeParticle(float width, float height) {
    super(width, height);
  }

  protected abstract Shape getShape(final Point2D emitterOrigin);

  @Override
  public Rectangle2D getBoundingBox(Point2D origin) {
    return this.getShape(origin).getBounds2D();
  }

  @Override
  public void render(final Graphics2D g, final Point2D emitterOrigin) {
    final Color color = this.getColor();
    g.setColor(
        new Color(
            color.getRed() / 255f,
            color.getGreen() / 255f,
            color.getBlue() / 255f,
            this.getOpacity()));

    if (this.isOutlineOnly() || this instanceof LineParticle) {
      Game.graphics()
          .renderOutline(
              g,
              this.getShape(emitterOrigin),
              new BasicStroke(1.0f / Game.graphics().getBaseRenderScale()),
              this.isAntiAliased());
    } else {
      Game.graphics().renderShape(g, this.getShape(emitterOrigin), this.isAntiAliased());
    }
  }
}
