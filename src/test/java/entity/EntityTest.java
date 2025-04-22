package entity;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.collisions.CollisionManager;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.entities.Entity;
import com.game.core.exceptions.NotConfiguredException;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.config.ConfigLoader;
import com.game.core.utils.config.SceneConfig;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntityTest {
    private static class TestEntity extends Entity {
        public TestEntity(Tile tile, Bounds hitbox) {
            super(tile, hitbox);
        }

        public void movePublic(double dt) { move(dt); }
        public void setRotationAnglePublic(float angle) { setRotationAngle(angle); }

        @Override
        public void update(double deltaTime) {}

        @Override
        public void onCollision(CollisionVisitor visitor, Collidable other) {}

        @Override
        public void render(GraphicsContext gc) {
            super.render(gc);
        }
    }

    @Mock Tile tile;
    @Mock Bounds hitbox;
    @Mock CollisionManager cm;
    @Mock GraphicsContext gc;

    private TestEntity entity;

    @BeforeEach
    void setup() {
        entity = new TestEntity(tile, hitbox);
    }

    private void setupConfig() {
        SceneConfig mockConfig = mock(SceneConfig.class);
        when(mockConfig.isDebug()).thenReturn(false);

        ConfigLoader loader = ConfigLoader.getInstance();

        try {
            Field configField = ConfigLoader.class.getDeclaredField("config");
            configField.setAccessible(true);
            configField.set(loader, mockConfig);
        } catch (Exception e) {
            fail("Fail to use reflection");
        }
    }

    @Test
    void testVelocity_defaultAngle_shouldReturnUnitX() {
        entity.setRotationAnglePublic(0f);
        float[] velocity = entity.getVelocity();
        assertEquals(1f, velocity[0], 0.0001);
        assertEquals(0f, velocity[1], 0.0001);
    }

    @Test
    void testVelocity_90Degrees_shouldReturnUnitY() {
        entity.setRotationAnglePublic(90f);
        float[] velocity = entity.getVelocity();
        assertEquals(0f, velocity[0], 0.0001);
        assertEquals(1f, velocity[1], 0.0001);
    }

    @Test
    void testMove_appliesMovementAndCollision() {
        entity.setCm(cm);
        entity.setSpeed(10f);
        entity.setRotationAnglePublic(0f);
        entity.setPos(5, 5);

        when(cm.checkCollisionsFor(any(), anyFloat(), anyFloat()))
                .thenReturn(new boolean[]{false, false});

        entity.movePublic(1.0);

        assertEquals(15f, entity.getX(), 0.0001);
        assertEquals(5f, entity.getY(), 0.0001);
        verify(cm).applyAreas(entity);
    }

    @Test
    void testMove_withCollisionPreventsMovement() {
        entity.setCm(cm);
        entity.setSpeed(10f);
        entity.setRotationAnglePublic(0f);
        entity.setPos(0, 0);

        when(cm.checkCollisionsFor(any(), anyFloat(), anyFloat()))
                .thenReturn(new boolean[]{true, false});

        entity.movePublic(1.0);

        assertEquals(0f, entity.getX(), 0.0001);
        assertEquals(0f, entity.getY(), 0.0001);
    }

    @Test
    void testMove_withoutCollisionManager_shouldThrow() {
        entity.setSpeed(1f);
        assertThrows(
                NotConfiguredException.class,
                () -> entity.movePublic(1.0)
        );
    }

    @Test
    void testDraw_shouldCallRenderAndDrawHitbox() {
        setupConfig();

        entity.setState(true);
        entity.draw(gc);
        verify(tile).draw(any(), anyFloat(), anyFloat());
    }

    @Test
    void testRotationAngle_wrapsAround360() {
        entity.setRotationAnglePublic(370f);
        assertEquals(10f, entity.getRotationAngle(), 0.0001);
    }

    @Test
    void testSettersAndGetters() {
        entity.setSpeed(5f);
        assertEquals(5f, entity.getSpeed());

        entity.setState(false);
        assertFalse(entity.getState());

        entity.setRotationAnglePublic(180f);
        assertEquals(180f, entity.getRotationAngle());
    }
}
