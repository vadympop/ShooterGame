package behaviour;

import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.collisions.CollisionVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CollidableGameObjectTest {
    private static class TestCollidableGameObject extends CollidableGameObject {
        public TestCollidableGameObject(Bounds hitbox) {
            super(hitbox);
        }

        @Override
        public void onCollision(CollisionVisitor visitor, Collidable other) {}
    }

    @Mock private Bounds hitbox;
    private CollidableGameObject gameObject;

    @BeforeEach
    void setUp() {
        gameObject = new TestCollidableGameObject(hitbox);
    }

    @Test
    void constructorSetsHitbox() {
        assertEquals(hitbox, gameObject.getHitbox());
    }

    @Test
    void constructorThrowsOnNullHitbox() {
        assertThrows(
                NullPointerException.class,
                () -> new TestCollidableGameObject(null)
        );
    }

    @Test
    void setHitboxUpdatesHitbox() {
        Bounds newHitbox = mock(Bounds.class);
        gameObject.setHitbox(newHitbox);
        assertEquals(newHitbox, gameObject.getHitbox());
    }

    @Test
    void setHitboxThrowsOnNull() {
        assertThrows(
                NullPointerException.class,
                () -> gameObject.setHitbox(null)
        );
    }

    @Test
    void containsDelegatesToHitbox() {
        CollidableGameObject other = new TestCollidableGameObject(mock(Bounds.class));
        when(hitbox.contains(other.getHitbox())).thenReturn(true);

        assertTrue(gameObject.contains(other));
        verify(hitbox).contains(other.getHitbox());
    }

    @Test
    void intersectsDelegatesToHitbox() {
        CollidableGameObject other = new TestCollidableGameObject(mock(Bounds.class));
        when(hitbox.intersects(other.getHitbox())).thenReturn(true);

        assertTrue(gameObject.intersects(other));
        verify(hitbox).intersects(other.getHitbox());
    }

    @Test
    void setPosUpdatesHitboxPosition() {
        gameObject.setPos(10.0f, 20.0f);
        verify(hitbox).setPos(10.0f, 20.0f);
    }

    @Test
    void setPosDoesNothingIfHitboxIsNull() {
        // Temporarily set hitbox to null using reflection to bypass requireNonNull
        try {
            Field hitboxField = CollidableGameObject.class.getDeclaredField("hitbox");
            hitboxField.setAccessible(true);
            hitboxField.set(gameObject, null);
        } catch (Exception e) {
            fail("Failed to set hitbox to null for testing");
        }

        gameObject.setPos(10.0f, 20.0f); // Should not throw or call anything
    }

    @Test
    void toStringIncludesHitboxAndSuper() {
        when(hitbox.toString()).thenReturn("Bounds[x=0,y=0,w=10,h=10]");
        String result = gameObject.toString();
        assertTrue(result.contains("CollidableGameObject{hitbox=Bounds[x=0,y=0,w=10,h=10]}"));
        assertTrue(result.contains("->"));
    }
}