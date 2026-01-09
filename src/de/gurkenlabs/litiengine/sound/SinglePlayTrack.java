package de.gurkenlabs.litiengine.sound;

import de.gurkenlabs.litiengine.resources.Resources;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import javax.sound.sampled.AudioFormat;

/** A {@code Track} that plays a sound once and then stops. */
public class SinglePlayTrack implements Track {
  private Sound sound;

  private class Iter implements Iterator<Sound> {
    private boolean hasNext = true;

    @Override
    public boolean hasNext() {
      return this.hasNext;
    }

    @Nullable
    @Override
    public Sound next() {
      if (!this.hasNext) {
        throw new NoSuchElementException();
      }
      this.hasNext = false;
      return SinglePlayTrack.this.sound;
    }
  }

  /**
   * Initializes a new {@code SinglePlayTrack} for the specified sound.
   *
   * @param soundName The name of the sound to be played by this track.
   */
  public SinglePlayTrack(String soundName) {
    this(Resources.sounds().get(soundName));
  }

  /**
   * Initializes a new {@code SinglePlayTrack} for the specified sound.
   *
   * @param sound The sound to be played by this track.
   */
  public SinglePlayTrack(Sound sound) {
    this.sound = sound;
  }

  @Override
  public Iterator<Sound> iterator() {
    return new Iter();
  }

  @Nullable
  @Override
  public AudioFormat getFormat() {
    AudioSource localSound = this.sound;
    if (localSound == null) {
      return null;
    }
    return localSound.getFormat();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof SinglePlayTrack && this.sound == ((SinglePlayTrack) obj).sound;
  }

  @Override
  public int hashCode() {
    // add a constant to avoid collisions with LoopedTrack
    return (this.sound == null ? 1 : this.sound.hashCode()) + 0xdb9857d0;
  }

  @Override
  public String toString() {
    return "track: " + (this.sound == null ? "null" : this.sound.getName()) + " (not looped)";
  }
}
