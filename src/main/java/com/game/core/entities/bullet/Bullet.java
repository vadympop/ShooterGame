package com.game.core.entities.bullet;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.entities.Entity;
import com.game.core.entities.Player;
import com.game.core.exceptions.InvalidParameterException;
import com.game.core.exceptions.NotConfiguredException;
import com.game.core.factories.BoundsFactory;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.Timer;
import com.game.core.utils.config.SceneConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Represents a bullet entity in the game, fired by a player or another entity.
 * A bullet has a specific owner, type, speed, damage, and a timer for automatic destruction.
 */
public class Bullet extends Entity {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bullet.class);

    private final Player owner;
    private final BulletType type;
    private final int damage;
    private final float timeToDestroy;
    private Timer<Bullet> destroyTimer;

    /**
     * Initializes a Bullet instance with the specified parameters.
     *
     * @param owner         The player or entity that fired this bullet.
     * @param type          The type of the bullet.
     * @param tile          The graphical tile representing this bullet.
     * @param hitbox        The hitbox used for collision detection.
     * @param timeToDestroy The lifetime of the bullet in seconds before automatic destruction.
     * @param damage        The damage inflicted by this bullet.
     * @param speed         The speed at which the bullet moves.
     * @param rotationAngle The initial rotation angle of the bullet in degrees.
     */
    private Bullet(
            Player owner,
            BulletType type,
            Tile tile,
            Bounds hitbox,
            float timeToDestroy,
            int damage,
            float speed,
            float rotationAngle
    ) {
        super(tile, hitbox);

        this.owner = owner;
        this.damage = damage;
        this.type = type;
        this.timeToDestroy = timeToDestroy;

        setRotationAngle(rotationAngle);
        setSpeed(speed);
        createDestroyTimer();
    }

    /**
     * Specifies behavior when the bullet collides with another object.
     *
     * @param visitor The collision visitor handling the logic for this collision.
     * @param other   The other collidable entity involved in the collision.
     */
    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {
        visitor.visit(this, other);
    }

    @Override
    public void update(double deltaTime) {
        move(deltaTime);
        if (getDestroyTimer() != null)
            getDestroyTimer().update(deltaTime, this, () -> setDestroyTimer(null));
    }

    /**
     * Creates a timer that handles the automatic destruction of the bullet after its lifetime expires.
     */
    private void createDestroyTimer() {
        setDestroyTimer(new Timer<>(getTimeToDestroy(), (x) -> x.setState(false)));
    }

    public BulletType getType() { return this.type; }
    public Player getOwner() { return this.owner; }
    public int getDamage() { return this.damage; }
    public float getTimeToDestroy() { return this.timeToDestroy; }

    public Timer<Bullet> getDestroyTimer() { return destroyTimer; }
    private void setDestroyTimer(Timer<Bullet> timer) { destroyTimer = timer; }


    /**
     * Sets the initial position of the bullet based on the owner's location and velocity.
     * Accounts for the hitbox of the owner when determining start position.
     */
    public void setStartPosition() {
        float[] dirs = getVelocity();
        float xOffset = 0, yOffset = 0;
        if (getOwner().getHitbox() instanceof CircleBounds circle) {
            xOffset = yOffset = circle.getRadius();
        } else if (getOwner().getHitbox() instanceof RectangleBounds rect) {
            xOffset = rect.getWidth() / 2f;
            yOffset = rect.getHeight() / 2f;
        }

        float x = owner.getX() + xOffset * 2f * dirs[0];
        float y = owner.getY() + yOffset * 2f * dirs[1];
        LOGGER.debug("Bullet's start position is x={}, y={}, owner={}", x, y, getOwner());

        setPos(x, y);
    }

    /**
     * Builder class for constructing Bullet instances in a flexible and configurable manner.
     * Provides methods to set individual components of the bullet before building it.
     */
    public static class builder {
        private Player owner;
        private int damage;
        private float speed;
        private float rotationAngle;
        private float timeToDestroy;
        private Tile tile;
        private String texture;
        private Bounds hitbox;
        private final BulletType type;

        /**
         * Initializes the builder with a required bullet type.
         *
         * @param type The type of bullet to be built.
         */
        public builder(BulletType type) {
            this.type = type;
        }

        /**
         * Specifies the owner of the bullet and initializes speed and rotation angle to match the owner.
         *
         * @param owner The player or entity that fired the bullet.
         * @return The current builder instance.
         */
        public builder owner(Player owner) {
            this.owner = Objects.requireNonNull(owner);
            this.rotationAngle(owner.getRotationAngle()).speed(owner.getSpeed());
            return this;
        }

        /**
         * Configures the builder using external configuration data.
         *
         * @param config The configuration object containing bullet settings.
         * @return The current builder instance.
         * @throws NullPointerException If the provided config is null.
         */
        public builder config(SceneConfig.BulletConfig config) {
            Objects.requireNonNull(config);

            this
                .damage(config.getDamage())
                .timeToDestroy(config.getTimeToDestroy())
                .hitbox(BoundsFactory.createFromConfig(config.getHitbox()));
            this.texture = config.getTextures().get(type);
            return this;
        }

        public builder timeToDestroy(float time) {
            if (time <= 0) throw new InvalidParameterException("Bullet's time destroy must be higher than 0");

            this.timeToDestroy = time;
            return this;
        }

        public builder hitbox(Bounds hitbox) {
            this.hitbox = Objects.requireNonNull(hitbox);
            return this;
        }

        public builder damage(int damage) {
            this.damage = damage;
            return this;
        }

        public builder speed(float speed) {
            this.speed = speed;
            return this;
        }

        public builder rotationAngle(float rotationAngle) {
            this.rotationAngle = rotationAngle % 360;
            return this;
        }

        public builder tile(Tile tile) {
            this.tile = tile;
            return this;
        }

        /**
         * Builds a new Bullet instance using the provided parameters.
         *
         * @return A fully constructed Bullet instance.
         * @throws NotConfiguredException If required configuration (e.g., texture) is missing.
         */
        public Bullet build() {
            if (texture == null) throw new NotConfiguredException("Not passed config");

            float coefficient = 1.5f + (float)Math.tanh(speed / 250.0f) * 0.5f;
            this.speed *= coefficient;
            if (tile == null)
                this.tile = new Tile(texture, null);

            return new Bullet(owner, type, tile, hitbox, timeToDestroy, damage, speed, rotationAngle);
        }
    }
}
