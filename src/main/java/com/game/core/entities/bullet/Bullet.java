package com.game.core.entities.bullet;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.entities.Entity;
import com.game.core.entities.Player;
import com.game.core.factories.BoundsFactory;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.Timer;
import com.game.core.utils.config.SceneConfig;

public class Bullet extends Entity {
    private final Player owner;
    private final BulletType type;
    private final int damage;
    private final float timeToDestroy;
    private Timer<Bullet> destroyTimer;

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
        setSpeed(getOwner().getSpeed() + speed);
        createDestroyTimer();
    }

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

    private void createDestroyTimer() {
        setDestroyTimer(new Timer<>(getTimeToDestroy(), (x) -> x.setState(false)));
    }

    public BulletType getType() { return this.type; }
    public Player getOwner() { return this.owner; }
    public int getDamage() { return this.damage; }
    public float getTimeToDestroy() { return this.timeToDestroy; }

    public Timer<Bullet> getDestroyTimer() { return destroyTimer; }
    private void setDestroyTimer(Timer<Bullet> timer) { destroyTimer = timer; }

    public void setStartPosition() {
        float x = owner.getX();
        float y = owner.getY();
        float[] dirs = getVelocity();
        if (getOwner().getHitbox() instanceof CircleBounds circle) {
            x += circle.getRadius() * 4.5f * dirs[0];
            y += circle.getRadius() * 4.5f * dirs[1];
        } else if (getOwner().getHitbox() instanceof RectangleBounds rect) {

        }

        setPos(x, y);
    }

    public static class builder {
        private Player owner;
        private int damage;
        private float speed;
        private float rotationAngle;
        private float timeToDestroy;
        private Tile tile;
        private Bounds hitbox;
        private final BulletType type;

        public builder(BulletType type) {
            this.type = type;
        }

        public builder owner(Player owner) {
            this.owner = owner;
            this.rotationAngle(owner.getRotationAngle());
            return this;
        }

        public builder config(SceneConfig.BulletConfig config) {
            this
                .damage(config.getDamage())
                .speed(config.getSpeed())
                .timeToDestroy(config.getTimeToDestroy())
                .hitbox(BoundsFactory.createFromConfig(config.getHitbox()));
            this.tile = new Tile(config.getTextures().get(this.type), null);
            return this;
        }

        public builder timeToDestroy(float time) {
            this.timeToDestroy = time;
            return this;
        }

        public builder hitbox(Bounds hitbox) {
            this.hitbox = hitbox;
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
            this.rotationAngle = rotationAngle;
            return this;
        }

        public Bullet build() {
            return new Bullet(owner, type, tile, hitbox, timeToDestroy, damage, speed, rotationAngle);
        }
    }
}
