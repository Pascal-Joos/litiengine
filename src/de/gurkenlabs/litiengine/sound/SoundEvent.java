package de.gurkenlabs.litiengine.sound;

import java.util.EventObject;
import javax.annotation.Nullable;

/**
 * This implementation is used for all events that need to pass a {@code Sound} object to their
 * listeners.
 *
 * @see SoundPlayback#cancel()
 * @see SoundPlayback#finish()
 */
public class SoundEvent extends EventObject {
  private static final long serialVersionUID = -2070316328855430839L;

  @Nullable private final transient Sound sound;

  SoundEvent(Object source, @Nullable Sound sound) {
    super(source);
    this.sound = sound;
  }

  /**
   * Gets the related {@code Sound} instance.
   *
   * @return The sound object.
   */
  @Nullable
  public Sound getSound() {
    return this.sound;
  }

  @Override
  public String toString() {
    return super.toString() + "[sound=" + this.sound.getName() + "]";
  }
}
