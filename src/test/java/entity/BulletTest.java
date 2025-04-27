package entity;

import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.collisions.CollisionManager;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.entities.Player;
import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.bullet.BulletType;
import com.game.core.exceptions.NotConfiguredException;
import com.game.core.utils.config.SceneConfig;
import com.game.core.utils.config.enums.BoundsTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.*;

import com.game.core.scene.graphics.Tile;

@ExtendWith(MockitoExtension.class)
class BulletTest {
    @Mock Player mockPlayer;
    @Mock CollisionVisitor mockVisitor;
    @Mock Collidable mockOther;
    @Mock SceneConfig.BulletConfig mockBulletConfig;
    @Mock SceneConfig.BoundsConfig mockBoundsConfig;
    @Mock Tile mockTile;

    private Bullet bullet;

    @BeforeEach
    void setup() {
        when(mockPlayer.getRotationAngle()).thenReturn(0f);
        when(mockPlayer.getSpeed()).thenReturn(100f);
    }

    void setupConfigs() {
        when(mockBoundsConfig.getType()).thenReturn(BoundsTypeEnum.RECTANGLE);
        when(mockBoundsConfig.getWidth()).thenReturn(10f);
        when(mockBoundsConfig.getHeight()).thenReturn(10f);

        when(mockBulletConfig.getDamage()).thenReturn(25);
        when(mockBulletConfig.getTimeToDestroy()).thenReturn(2.0f);
        when(mockBulletConfig.getHitbox()).thenReturn(mockBoundsConfig);
        when(mockBulletConfig.getTextures()).thenReturn(Map.of(BulletType.STANDARD, "texture.png"));
    }

    @Test
    void testBuildBullet_withValidConfig_shouldInitializeCorrectly() {
        setupConfigs();

        bullet = new Bullet.builder(BulletType.STANDARD)
                .owner(mockPlayer)
                .config(mockBulletConfig)
                .tile(mockTile)
                .build();

        assertEquals(25, bullet.getDamage());
        assertEquals(2.0f, bullet.getTimeToDestroy(), 0.01f);
        assertEquals(BulletType.STANDARD, bullet.getType());
        assertNotNull(bullet.getDestroyTimer());
    }

    @Test
    void testSetStartPosition_shouldPlaceBulletInFrontOfPlayer() {
        setupConfigs();

        when(mockPlayer.getHitbox()).thenReturn(new CircleBounds(10));
        when(mockPlayer.getX()).thenReturn(100f);
        when(mockPlayer.getY()).thenReturn(200f);

        bullet = new Bullet.builder(BulletType.STANDARD)
                .owner(mockPlayer)
                .config(mockBulletConfig)
                .tile(mockTile)
                .build();

        bullet.setStartPosition();

        assertTrue(bullet.getX() > 100f);
        assertEquals(200f, bullet.getY(), 0.1f);
    }

    @Test
    void testUpdate_shouldMoveAndDestroyAfterTime() {
        setupConfigs();

        bullet = new Bullet.builder(BulletType.STANDARD)
                .owner(mockPlayer)
                .config(mockBulletConfig)
                .tile(mockTile)
                .build();

        CollisionManager mockCm = mock(CollisionManager.class);
        when(mockCm.checkCollisionsFor(any(), anyFloat(), anyFloat())).thenReturn(new boolean[]{false, false});
        doNothing().when(mockCm).applyAreas(any());

        bullet.setCm(mockCm);
        float initialX = bullet.getX();

        bullet.update(1.0);

        assertTrue(bullet.getX() > initialX);
    }

    @Test
    void testOnCollision_shouldDelegateToVisitor() {
        setupConfigs();

        bullet = new Bullet.builder(BulletType.STANDARD)
                .owner(mockPlayer)
                .config(mockBulletConfig)
                .tile(mockTile)
                .build();

        bullet.onCollision(mockVisitor, mockOther);
        verify(mockVisitor).visit(bullet, mockOther);
    }

    @Test
    void testDestroyTimer_shouldDeactivateBulletAfterTime() {
        setupConfigs();

        bullet = new Bullet.builder(BulletType.STANDARD)
                .owner(mockPlayer)
                .config(mockBulletConfig)
                .tile(mockTile)
                .build();

        CollisionManager mockCm = mock(CollisionManager.class);
        when(mockCm.checkCollisionsFor(any(), anyFloat(), anyFloat())).thenReturn(new boolean[]{false, false});
        doNothing().when(mockCm).applyAreas(any());

        bullet.setCm(mockCm);

        assertTrue(bullet.getState());
        bullet.update(3.5);
        assertFalse(bullet.getState());
    }

    @Test
    void testBuildWithoutConfig_shouldThrowException() {
        Bullet.builder builder = new Bullet.builder(BulletType.STANDARD)
                .owner(mockPlayer)
                .tile(mockTile);

        assertThrows(NotConfiguredException.class, builder::build);
    }

    @Test
    void testSetStartPosition_withCircleBounds_shouldOffsetByRadius() {
        CircleBounds circleBounds = new CircleBounds(15f);
        when(mockPlayer.getHitbox()).thenReturn(circleBounds);
        when(mockPlayer.getX()).thenReturn(100f);
        when(mockPlayer.getY()).thenReturn(200f);
        when(mockPlayer.getRotationAngle()).thenReturn(0f); // Напрямок праворуч
        when(mockPlayer.getSpeed()).thenReturn(100f);

        setupConfigs();

        Bullet bullet = new Bullet.builder(BulletType.STANDARD)
                .owner(mockPlayer)
                .config(mockBulletConfig)
                .tile(mockTile)
                .build();

        bullet.setStartPosition();

        float expectedX = 100f + 15f * 2f * 1f; // x + radius * 2 * dirX
        float expectedY = 200f + 15f * 2f * 0f; // y + radius * 2 * dirY

        assertEquals(expectedX, bullet.getX(), 0.01);
        assertEquals(expectedY, bullet.getY(), 0.01);
    }

    @Test
    void testSetStartPosition_withRectangleBounds_shouldOffsetByHalfWidthAndHeight() {
        RectangleBounds rectangleBounds = new RectangleBounds(20f, 40f);
        when(mockPlayer.getHitbox()).thenReturn(rectangleBounds);
        when(mockPlayer.getX()).thenReturn(50f);
        when(mockPlayer.getY()).thenReturn(80f);
        when(mockPlayer.getRotationAngle()).thenReturn(90f); // UP
        when(mockPlayer.getSpeed()).thenReturn(100f);

        setupConfigs();

        Bullet bullet = new Bullet.builder(BulletType.STANDARD)
                .owner(mockPlayer)
                .config(mockBulletConfig)
                .tile(mockTile)
                .build();

        bullet.setStartPosition();

        float expectedX = 50f + 10f * 2f * 0f; // X + halfWidth * 2 * dirX
        float expectedY = 80f + 20f * 2f * 1f; // Y + halfHeight * 2 * dirY

        assertEquals(expectedX, bullet.getX(), 0.01);
        assertEquals(expectedY, bullet.getY(), 0.01);
    }
}
