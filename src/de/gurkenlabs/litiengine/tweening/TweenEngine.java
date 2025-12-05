package de.gurkenlabs.litiengine.tweening;

import de.gurkenlabs.litiengine.Game;
import java.util.HashMap;
import java.util.Map;

public class TweenEngine implements IUpdateable {

  private final Map<Tweenable, Map<TweenType, Tween>> tweens;

  public TweenEngine() {
    this.tweens = new HashMap<>();
  }

  public Map<Tweenable, Map<TweenType, Tween>> getTweens() {
    return this.tweens;
  }

  public Tween tween(
      final Tweenable target,
      final TweenType type,
      final int duration,
      final float... targetValues) {
    return this.tween(target, type, duration, null, targetValues);
  }

  public Tween tween(
      final Tweenable target,
      final TweenType type,
      final int duration,
      final TweenFunction easingFunction,
      final float... targetValues) {
    if (target == null || type == null) {
      return null;
    }

    final Tween tween = new Tween(target, type, duration);
    if (easingFunction != null) {
      tween.ease(easingFunction);
    }

    tween.target(targetValues).begin();

    this.tweens.computeIfAbsent(target, t -> new HashMap<>()).put(type, tween);

    Game.loop().attach(this);
    return tween;
  }

  public Tween getTween(final Tweenable target, final TweenType type) {
    if (target == null || type == null) {
      return null;
    }

    final Map<TweenType, Tween> targetTweens = this.tweens.get(target);
    if (targetTweens == null) {
      return null;
    }

    return targetTweens.get(type);
  }

  public Tween stop(final Tweenable target, final TweenType type) {
    final Tween tween = this.getTween(target, type);
    if (tween != null) {
      tween.stop();
    }
    return tween;
  }

  /** Terminate. */
  @Override
  public void terminate() {
    Game.loop().detach(this);
  }

  /** Updates all registered Tweens by applying the {@code TweenEquation}. */
  @Override
  public void update() {
    for (final Tweenable target : this.getTweens().keySet()) {
      for (final Tween tween : this.getTweens().get(target).values()) {
        if (tween.hasStopped()) {
          continue;
        }
        final long elapsed = Game.time().since(tween.getStartTime());
        if (elapsed >= tween.getDuration()) {
          tween.stop();
          continue;
        }
        final float[] currentValues = new float[tween.getTargetValues().length];
        final TweenEquation equation = tween.getEquation();
        if (equation == null) {
          // No easing equation specified; fall back to linear interpolation.
          final float progress = elapsed / (float) tween.getDuration();
          for (int i = 0; i < tween.getTargetValues().length; i++) {
            currentValues[i] =
                tween.getStartValues()[i]
                    + (tween.getTargetValues()[i] - tween.getStartValues()[i]) * progress;
          }
        } else {
          final float eased = equation.compute(elapsed / (float) tween.getDuration());
          for (int i = 0; i < tween.getTargetValues().length; i++) {
            currentValues[i] =
                tween.getStartValues()[i]
                    + eased * (tween.getTargetValues()[i] - tween.getStartValues()[i]);
          }
        }
        tween.getTarget().setTweenValues(tween.getType(), currentValues);
      }
    }
  }
}
