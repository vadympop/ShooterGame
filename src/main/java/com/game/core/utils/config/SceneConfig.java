package com.game.core.utils.config;

import java.util.List;
import java.util.Map;

public class SceneConfig {
    private String id;
    private float tileWidth;
    private float tileHeight;
    private String bonusTexture;
    private String name;
    private List<AreaConfig> areas;
    private List<SpawnerConfig> spawners;
    private List<String> backgroundTiles;
    private List<String> blocks;
    private List<String> overlayTiles;
    private MappingsConfig mappings;
    private PlayerConfig player;
    private BulletConfig bullet;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<AreaConfig> getAreas() { return areas; }
    public void setAreas(List<AreaConfig> areas) { this.areas = areas; }

    public List<SpawnerConfig> getSpawners() { return spawners; }
    public void setSpawners(List<SpawnerConfig> spawners) { this.spawners = spawners; }

    public List<String> getBackgroundTiles() { return backgroundTiles; }
    public void setBackgroundTiles(List<String> backgroundTiles) { this.backgroundTiles = backgroundTiles; }

    public List<String> getBlocks() { return blocks; }
    public void setBlocks(List<String> blocks) { this.blocks = blocks; }

    public List<String> getOverlayTiles() { return overlayTiles; }
    public void setOverlayTiles(List<String> overlayTiles) { this.overlayTiles = overlayTiles; }

    public MappingsConfig getMappings() { return mappings; }
    public void setMappings(MappingsConfig mappings) { this.mappings = mappings; }

    public float getTileWidth() { return tileWidth; }
    public void setTileWidth(float tileWidth) { this.tileWidth = tileWidth; }

    public float getTileHeight() { return tileHeight; }
    public void setTileHeight(float tileHeight) { this.tileHeight = tileHeight; }

    public PlayerConfig getPlayer() { return player; }
    public void setPlayer(PlayerConfig player) { this.player = player; }

    public BulletConfig getBullet() { return bullet; }
    public void setBullet(BulletConfig bullet) { this.bullet = bullet; }

    public String getBonusTexture() { return bonusTexture; }
    public void setBonusTexture(String bonusTexture) { this.bonusTexture = bonusTexture; }

    public static class BulletConfig {
        private String texture;
        private float speed;
        private float timeToDestroy;

        public String getTexture() { return texture; }
        public void setTexture(String texture) { this.texture = texture; }

        public float getSpeed() { return speed; }
        public void setSpeed(float speed) { this.speed = speed; }

        public float getTimeToDestroy() { return timeToDestroy; }
        public void setTimeToDestroy(float timeToDestroy) { this.timeToDestroy = timeToDestroy; }
    }

    public static class PlayerConfig {
        private int maxHealth;
        private int maxBulletsCount;
        private int bulletDamage;
        private float bulletsReloadDelay;
        private float speed;
        private float rotationSpeed;
        private CircleBoundsConfig hitbox;

        public CircleBoundsConfig getHitbox() { return hitbox; }
        public void setHitbox(CircleBoundsConfig hitbox) { this.hitbox = hitbox; }

        public int getMaxHealth() { return maxHealth; }
        public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

        public int getMaxBulletsCount() { return maxBulletsCount; }
        public void setMaxBulletsCount(int maxBulletsCount) { this.maxBulletsCount = maxBulletsCount; }

        public int getBulletDamage() { return bulletDamage; }
        public void setBulletDamage(int bulletDamage) { this.bulletDamage = bulletDamage; }

        public float getBulletsReloadDelay() { return bulletsReloadDelay; }
        public void setBulletsReloadDelay(float bulletsReloadDelay) { this.bulletsReloadDelay = bulletsReloadDelay; }

        public float getSpeed() { return speed; }
        public void setSpeed(float speed) { this.speed = speed; }

        public float getRotationSpeed() { return rotationSpeed; }
        public void setRotationSpeed(float rotationSpeed) { this.rotationSpeed = rotationSpeed; }
    }

    public static class AreaConfig {
        private int type; // 0 - KillableArea, 1 - SlowingArea
        private int col;
        private int row;
        private BoundsConfig bounds;

        public int getType() { return type; }
        public void setType(int type) { this.type = type; }

        public int getCol() { return col; }
        public void setCol(int col) { this.col = col; }

        public int getRow() { return row; }
        public void setRow(int row) { this.row = row; }

        public BoundsConfig getBounds() { return bounds; }
        public void setBounds(BoundsConfig bounds) { this.bounds = bounds; }
    }

    public static class BoundsConfig {
        private String type;
        private float width;
        private float height;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public float getWidth() { return width; }
        public void setWidth(float width) { this.width = width; }

        public float getHeight() { return height; }
        public void setHeight(float height) { this.height = height; }
    }

    public static class SpawnerConfig {
        private int type; // 0 = PlayerSpawner, 1 = BonusSpawner
        private int col;
        private int row;
        private String texture;
        private float cooldown; // only for bonusspawner
        private String playerTexture; // only for playerspawner

        public int getType() { return type; }
        public void setType(int type) { this.type = type; }

        public int getCol() { return col; }
        public void setCol(int col) { this.col = col; }

        public int getRow() { return row; }
        public void setRow(int row) { this.row = row; }

        public float getCooldown() { return cooldown; }
        public void setCooldown(float cooldown) { this.cooldown = cooldown; }

        public String getPlayerTexture() { return playerTexture; }
        public void setPlayerTexture(String playerTexture) { this.playerTexture = playerTexture; }

        public String getTexture() { return texture; }
        public void setTexture(String texture) { this.texture = texture; }
    }

    public static class MappingsConfig {
        private Map<String, MappingTileConfig> tiles;
        private Map<String, String> spawners;
        private Map<String, MappingBlockConfig> blocks;

        public Map<String, MappingTileConfig> getTiles() { return tiles; }
        public void setTiles(Map<String, MappingTileConfig> tiles) { this.tiles = tiles; }

        public Map<String, String> getSpawners() { return spawners; }
        public void setSpawners(Map<String, String> spawners) { this.spawners = spawners; }

        public Map<String, MappingBlockConfig> getBlocks() { return blocks; }
        public void setBlocks(Map<String, MappingBlockConfig> blocks) { this.blocks = blocks; }
    }

    public static class MappingTileConfig {
        private String texture;
        private boolean defaultSize;

        public String getTexture() { return texture; }
        public void setTexture(String texture) { this.texture = texture; }

        public boolean isDefaultSize() { return defaultSize; }
        public void setDefaultSize(boolean defaultSize) { this.defaultSize = defaultSize; }
    }

    public static class MappingBlockConfig extends MappingTileConfig {
        private boolean isBreakable;
        private RectangleBoundsConfig hitbox;

        public boolean isBreakable() { return isBreakable; }
        public void setBreakable(boolean breakable) { isBreakable = breakable; }

        public RectangleBoundsConfig getHitbox() { return hitbox; }
        public void setHitbox(RectangleBoundsConfig hitbox) { this.hitbox = hitbox; }
    }

    public static class CircleBoundsConfig {
        private float radius;

        public float getRadius() { return radius; }
        public void setRadius(float radius) { this.radius = radius; }
    }

    public static class RectangleBoundsConfig {
        private float width;
        private float height;

        public float getWidth() { return width; }
        public void setWidth(float width) { this.width = width; }

        public float getHeight() { return height; }
        public void setHeight(float height) { this.height = height; }
    }
}
