package com.game.core.utils.config;

import com.game.core.entities.bullet.BulletType;
import com.game.core.utils.config.enums.AreaTypeEnum;
import com.game.core.utils.config.enums.BoundsTypeEnum;
import com.game.core.utils.config.enums.SpawnerTypeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Map;

public class SceneConfig {
    @NotBlank private String id;
    @NotNull @Positive private float tileWidth;
    @NotNull @Positive private float tileHeight;
    private boolean isDebug;
    @NotBlank private String name;
    @NotNull @Valid private List<@Valid AreaConfig> areas;
    @NotEmpty @Valid private List<@Valid SpawnerConfig> spawners;
    @NotEmpty private List<@NotBlank String> backgroundTiles;
    @NotEmpty private List<@NotBlank String> blocks;
    @NotEmpty private List<@NotBlank String> overlayTiles;
    @NotNull @Valid private MappingsConfig mappings;
    @NotNull @Valid private PlayerConfig player;
    @NotNull @Valid private BulletConfig bullet;
    @NotNull @Valid private BonusConfig bonus;

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

    public BonusConfig getBonus() { return bonus; }
    public void setBonus(BonusConfig bonus) { this.bonus = bonus; }

    public boolean isDebug() { return isDebug; }
    public void setDebug(boolean debug) { isDebug = debug; }

    public static class BonusConfig {
        @NotBlank private String texture;
        @Valid private BoundsConfig hitbox;

        public BoundsConfig getHitbox() { return hitbox; }
        public void setHitbox(BoundsConfig hitbox) { this.hitbox = hitbox; }

        public String getTexture() { return texture; }
        public void setTexture(String texture) { this.texture = texture; }
    }

    public static class BulletConfig {
        @NotEmpty @Valid private Map<BulletType, @NotBlank String> textures;
        @NotNull @Positive private float timeToDestroy;
        @NotNull @Positive private int damage;
        @NotNull @Valid private BoundsConfig hitbox;

        public Map<BulletType, String> getTextures() { return textures; }
        public void setTextures(Map<BulletType, String> textures) { this.textures = textures; }

        public float getTimeToDestroy() { return timeToDestroy; }
        public void setTimeToDestroy(float timeToDestroy) { this.timeToDestroy = timeToDestroy; }

        public int getDamage() { return damage; }
        public void setDamage(int damage) { this.damage = damage; }

        public BoundsConfig getHitbox() { return hitbox; }
        public void setHitbox(BoundsConfig hitbox) { this.hitbox = hitbox; }
    }

    public static class PlayerConfig {
        @NotNull @Positive private int maxHealth;
        @NotNull @Positive private int maxBulletsCount;
        @NotNull @Positive private float bulletsCooldown;
        @NotNull @Positive private float bulletsReloadDelay;
        @NotNull @Positive private float speed;
        @NotNull @Positive private float rotationSpeed;
        @NotNull @Positive private float shieldHitboxMultiplier;
        @NotNull @Valid private BoundsConfig hitbox;

        public BoundsConfig getHitbox() { return hitbox; }
        public void setHitbox(BoundsConfig hitbox) { this.hitbox = hitbox; }

        public int getMaxHealth() { return maxHealth; }
        public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

        public int getMaxBulletsCount() { return maxBulletsCount; }
        public void setMaxBulletsCount(int maxBulletsCount) { this.maxBulletsCount = maxBulletsCount; }

        public float getBulletsReloadDelay() { return bulletsReloadDelay; }
        public void setBulletsReloadDelay(float bulletsReloadDelay) { this.bulletsReloadDelay = bulletsReloadDelay; }

        public float getSpeed() { return speed; }
        public void setSpeed(float speed) { this.speed = speed; }

        public float getRotationSpeed() { return rotationSpeed; }
        public void setRotationSpeed(float rotationSpeed) { this.rotationSpeed = rotationSpeed; }

        public float getBulletsCooldown() { return bulletsCooldown; }
        public void setBulletsCooldown(float bulletsCooldown) { this.bulletsCooldown = bulletsCooldown; }

        public float getShieldHitboxMultiplier() { return shieldHitboxMultiplier; }
        public void setShieldHitboxMultiplier(float shieldHitboxMultiplier) { this.shieldHitboxMultiplier = shieldHitboxMultiplier; }
    }

    public static class AreaConfig {
        @NotNull private AreaTypeEnum type;
        @NotNull @Positive private int col;
        @NotNull @Positive private int row;
        @NotNull @Valid private BoundsConfig bounds;

        public AreaTypeEnum getType() { return type; }
        public void setType(AreaTypeEnum type) { this.type = type; }

        public int getCol() { return col; }
        public void setCol(int col) { this.col = col; }

        public int getRow() { return row; }
        public void setRow(int row) { this.row = row; }

        public BoundsConfig getBounds() { return bounds; }
        public void setBounds(BoundsConfig bounds) { this.bounds = bounds; }
    }

    public static class BoundsConfig {
        @NotNull private BoundsTypeEnum type;
        private float width;
        private float height;
        private float radius;

        public BoundsTypeEnum getType() { return type; }
        public void setType(BoundsTypeEnum type) { this.type = type; }

        public float getRadius() { return radius; }
        public void setRadius(float radius) { this.radius = radius; }

        public float getWidth() { return width; }
        public void setWidth(float width) { this.width = width; }

        public float getHeight() { return height; }
        public void setHeight(float height) { this.height = height; }
    }

    public static class SpawnerConfig {
        @NotNull private SpawnerTypeEnum type;
        @NotNull @Positive private int col;
        @NotNull @Positive private int row;
        @NotBlank private String texture;
        private float cooldown; // only for bonusspawner
        private String playerTexture; // only for playerspawner

        public SpawnerTypeEnum getType() { return type; }
        public void setType(SpawnerTypeEnum type) { this.type = type; }

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
        @NotEmpty @Valid private Map<String, @NotNull MappingTileConfig> tiles;
        @NotEmpty @Valid private Map<String, @NotNull MappingBlockConfig> blocks;

        public Map<String, MappingTileConfig> getTiles() { return tiles; }
        public void setTiles(Map<String, MappingTileConfig> tiles) { this.tiles = tiles; }

        public Map<String, MappingBlockConfig> getBlocks() { return blocks; }
        public void setBlocks(Map<String, MappingBlockConfig> blocks) { this.blocks = blocks; }
    }

    public static class MappingTileConfig {
        @NotBlank private String texture;
        private boolean defaultSize;

        public String getTexture() { return texture; }
        public void setTexture(String texture) { this.texture = texture; }

        public boolean isDefaultSize() { return defaultSize; }
        public void setDefaultSize(boolean defaultSize) { this.defaultSize = defaultSize; }
    }

    public static class MappingBlockConfig extends MappingTileConfig {
        private boolean isBreakable;
        private BoundsConfig hitbox;

        public boolean isBreakable() { return isBreakable; }
        public void setBreakable(boolean breakable) { isBreakable = breakable; }

        public BoundsConfig getHitbox() { return hitbox; }
        public void setHitbox(BoundsConfig hitbox) { this.hitbox = hitbox; }
    }
}
