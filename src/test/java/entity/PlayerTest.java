package entity;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.effects.Effect;
import com.game.core.entities.Player;
import com.game.core.exceptions.InvalidParameterException;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.utils.config.SceneConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerTest {
    @Mock private Tile tile;
    @Mock private Bounds hitbox;
    @Mock private PlayerSpawner spawner;
    @Mock private Effect effect;

    private SceneConfig.BulletConfig bulletConfig;

    @BeforeEach
    void setup() {
        bulletConfig = new SceneConfig.BulletConfig();
    }

    Player createPlayer() {
        return new Player(
                spawner,
                hitbox,
                tile,
                bulletConfig,
                10, // maxHealth
                5,  // maxBulletsCount
                1.5f, // shieldHitboxMultiplier
                1f,  // reload delay
                0.5f,  // cooldown
                100f, // defaultSpeed
                30f   // rotationSpeed
        );
    }

    @Test
    void testConstructor_initializesFieldsCorrectly() {
        Player player = createPlayer();
        assertEquals(10, player.getHealth());
        assertEquals(10, player.getMaxHealth());
        assertFalse(player.isDead());
        assertFalse(player.isMoving());
        assertNotNull(player.getSm());
    }

    @Test
    void testTakeDamage_reducesHealth() {
        Player player = createPlayer();
        player.takeDamage(3);
        assertEquals(7, player.getHealth());
    }

    @Test
    void testTakeDamage_lethalDamage_setsDead() {
        Player player = createPlayer();
        player.takeDamage(20); // more than health
        assertTrue(player.isDead());
    }

    @Test
    void testTakeDamage_withShield_doesNotReduceHealth() {
        Player player = createPlayer();
        player.setHasShield(true);
        player.takeDamage(5);
        assertEquals(10, player.getHealth());
        assertFalse(player.isDead());
    }

    @Test
    void testRespawn_setsStateProperly() {
        Player player = createPlayer();
        player.takeDamage(20);
        player.respawn(100f, 200f);
        assertEquals(10, player.getHealth());
        assertFalse(player.isDead());
        assertTrue(player.isHasShield());
    }

    @Test
    void testApplyEffect_setsEffectAndSchedulesRemoval() {
        Player player = createPlayer();

        when(effect.getDuration()).thenReturn(1.0f);
        doNothing().when(effect).apply(player);
        doNothing().when(effect).remove(player);

        boolean applied = player.applyEffect(effect);

        assertTrue(applied);
        assertEquals(effect, player.getActiveEffect());

        // simulate update to remove effect
        player.update(1.0);
        player.update(1.0);
        assertNull(player.getActiveEffect());
    }

    @Test
    void testApplyEffect_whenAlreadyHasEffect_returnsFalse() {
        Player player = createPlayer();

        doNothing().when(effect).apply(player);

        player.applyEffect(effect);
        boolean secondTry = player.applyEffect(effect);

        assertFalse(secondTry);
    }

    @Test
    void testRotationWhileNotMoving() {
        Player player = createPlayer();
        float angleBefore = player.getRotationAngle();

        player.update(1.0); // 1 sec
        float angleAfter = player.getRotationAngle();

        assertNotEquals(angleBefore, angleAfter);
    }

    @Test
    void testMoving() {
        Player player = createPlayer();

        player.onKeyPressed();
        assertTrue(player.isMoving());

        player.onKeyReleased();
        assertFalse(player.isMoving());
    }

    @Test
    void testChangeRotationDirection_flipsDirection() {
        Player player = createPlayer();
        int before = player.getRotationDirection();

        player.changeRotationDirection();
        assertEquals(-before, player.getRotationDirection());
    }

    @Test
    void testSetShieldMultiplier_invalidValue_throwsException() {
        Player player = createPlayer();
        assertThrows(
                InvalidParameterException.class,
                () -> player.setShieldHitboxMultiplier(0f)
        );
    }

    @Test
    void testSetMaxHealth_invalidValue_throwsException() {
        assertThrows(InvalidParameterException.class, () -> {
            new Player(
                    spawner,
                    hitbox,
                    tile,
                    bulletConfig,
                    0,
                    5,
                    1.5f,
                    1f,
                    0.5f,
                    100f,
                    30f
            );
        });
    }
}
