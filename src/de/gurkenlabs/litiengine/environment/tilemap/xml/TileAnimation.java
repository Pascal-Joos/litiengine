package de.gurkenlabs.litiengine.environment.tilemap.xml;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.tilemap.ITileAnimation;
import de.gurkenlabs.litiengine.environment.tilemap.ITileAnimationFrame;
import java.util.List;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class TileAnimation implements ITileAnimation {
  @Nullable
  @XmlElement(name = "frame", type = Frame.class)
  private List<ITileAnimationFrame> frames;

  private transient int totalDuration;

  @Nullable
  @Override
  public List<ITileAnimationFrame> getFrames() {
    return this.frames;
  }

  @Override
  public int getTotalDuration() {
    if (this.totalDuration > 0) {
      return this.totalDuration;
    }

    List<ITileAnimationFrame> frames = this.getFrames();
    if (frames == null || frames.isEmpty()) {
      return 0;
    }

    for (ITileAnimationFrame frame : frames) {
      if (frame != null) {
        this.totalDuration += frame.getDuration();
      }
    }

    return this.totalDuration;
  }

  @Override
  public ITileAnimationFrame getCurrentFrame() {
    long time = Game.time().sinceEnvironmentLoad() % this.getTotalDuration();
    List<ITileAnimationFrame> frames = this.getFrames();
    if (frames == null) {
      throw new AssertionError();
    }
    for (ITileAnimationFrame frame : frames) {
      time -= frame.getDuration();
      if (time <= 0) {
        return frame;
      }
    }
    throw new AssertionError(); // we should never reach this line
  }
}
