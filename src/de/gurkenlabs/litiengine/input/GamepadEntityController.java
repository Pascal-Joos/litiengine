package de.gurkenlabs.litiengine.input;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.IMobileEntity;
import de.gurkenlabs.litiengine.physics.MovementController;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;
import edu.ucr.cs.riple.annotator.util.Nullability;
import java.awt.geom.Point2D;

public class GamepadEntityController<T extends IMobileEntity> extends MovementController<T> {
  private int gamepadId = -1;
  private double gamepadDeadzone = Game.config().input().getGamepadStickDeadzone();
  private double gamepadRightStick = Game.config().input().getGamepadStickDeadzone();
  private boolean rotateWithRightStick = false;

  public GamepadEntityController(final T entity, boolean rotateWithRightStick) {
    super(entity);
    final GamepadManager gamepadManager = Input.gamepads();
    if (gamepadManager != null && gamepadManager.current() != null) {
      this.gamepadId = gamepadManager.current().getId();
    }

    this.rotateWithRightStick = rotateWithRightStick;
    if (gamepadManager != null) {
      gamepadManager.onAdded(
          pad -> {
            if (this.gamepadId == -1) {
              this.gamepadId = pad.getId();
            }
          });
    }

    if (gamepadManager != null) {
      gamepadManager.onRemoved(
          pad -> {
            if (this.gamepadId == pad.getId()) {
              this.gamepadId = -1;
              final GamepadManager currentGamepadManager = Input.gamepads();
              if (currentGamepadManager != null) {
                final Gamepad newGamePad = currentGamepadManager.current();
                if (newGamePad != null) {
                  this.gamepadId = newGamePad.getId();
                }
              }
            }
          });
    }
  }

  @Override
  public void update() {

    this.retrieveGamepadValues();
    super.update();
  }

  public double getGamepadDeadzone() {
    return this.gamepadDeadzone;
  }

  public double getGamepadRightStick() {
    return gamepadRightStick;
  }

  public boolean isRotateWithRightStick() {
    return this.rotateWithRightStick;
  }

  public void setRightStickDeadzone(double gamePadRightStick) {
    this.gamepadRightStick = gamePadRightStick;
  }

  public void setLeftStickDeadzone(double gamePadDeadzone) {
    this.gamepadDeadzone = gamePadDeadzone;
  }

  public void setRotateWithRightStick(boolean rotateWithRightStick) {
    this.rotateWithRightStick = rotateWithRightStick;
  }

  private void retrieveGamepadValues() {
    final GamepadManager gamepadManager = Input.gamepads();
    if (this.gamepadId == -1
        || this.gamepadId != -1
            && (gamepadManager == null || gamepadManager.getById(this.gamepadId) == null)) {
      return;
    }

    final float x =
        Nullability.castToNonnull(Input.gamepads()).get(this.gamepadId).getPollData(Gamepad.Axis.X);
    final float y =
        Nullability.castToNonnull(Input.gamepads()).get(this.gamepadId).getPollData(Gamepad.Axis.Y);

    if (Math.abs(x) > this.gamepadDeadzone) {
      this.setDx(x);
    }

    if (Math.abs(y) > this.gamepadDeadzone) {
      this.setDy(y);
    }

    if (this.isRotateWithRightStick()) {
      final float rightX =
          Nullability.castToNonnull(Input.gamepads())
              .get(this.gamepadId)
              .getPollData(Gamepad.Axis.RX);
      final float rightY =
          Nullability.castToNonnull(Input.gamepads())
              .get(this.gamepadId)
              .getPollData(Gamepad.Axis.RY);
      float targetX = 0;
      float targetY = 0;
      if (Math.abs(rightX) > this.gamepadRightStick) {
        targetX = rightX;
      }
      if (Math.abs(rightY) > this.gamepadRightStick) {
        targetY = rightY;
      }

      if (targetX != 0 || targetY != 0) {
        final Point2D target =
            new Point2D.Double(
                this.getEntity().getCenter().getX() + targetX,
                this.getEntity().getCenter().getY() + targetY);
        final double angle =
            GeometricUtilities.calcRotationAngleInDegrees(this.getEntity().getCenter(), target);
        this.getEntity().setAngle((float) angle);
      }
    }
  }
}
