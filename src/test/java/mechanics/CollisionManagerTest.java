package mechanics;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.collisions.CollisionManager;
import com.game.core.entities.Entity;
import com.game.core.entities.Player;
import com.game.core.scene.areas.Area;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollisionManagerTest {
    @Mock private Collidable mockEntity;
    @Mock private Collidable mockOther;
    @Mock private Bounds mockHitbox;
    @Mock private Player mockPlayer;
    @Mock private Area mockArea;

    private CollisionManager collisionManager;

    @BeforeEach
    void setup() {
        collisionManager = new CollisionManager();
    }

    void configureHitbox() {
        when(mockEntity.getHitbox()).thenReturn(mockHitbox);
        when(mockHitbox.copy()).thenReturn(mock(Bounds.class), mock(Bounds.class)); // two temp bounds
    }

    @Test
    void checkCollisionsFor_shouldReturnTrueFlags_whenIntersectsOnXandY() {
        configureHitbox();
        when(mockHitbox.getX()).thenReturn(10f);
        when(mockHitbox.getY()).thenReturn(20f);

        Bounds newX = mock(Bounds.class);
        Bounds newY = mock(Bounds.class);

        when(newX.intersects(any())).thenReturn(true);
        when(newY.intersects(any())).thenReturn(true);

        when(mockHitbox.copy()).thenReturn(newX, newY);
        when(mockOther.getHitbox()).thenReturn(mock(Bounds.class));

        collisionManager.addObject(mockOther);

        boolean[] result = collisionManager.checkCollisionsFor(mockEntity, 100f, 200f);

        assertTrue(result[0]);
        assertTrue(result[1]);
        verify(mockEntity).onCollision(any(), eq(mockOther));
    }

    @Test
    void checkCollisionsFor_shouldReturnFalseFlags_whenNoCollision() {
        configureHitbox();
        when(mockHitbox.getX()).thenReturn(0f);
        when(mockHitbox.getY()).thenReturn(0f);

        Bounds newX = mock(Bounds.class);
        Bounds newY = mock(Bounds.class);
        when(newX.intersects(any())).thenReturn(false);
        when(newY.intersects(any())).thenReturn(false);

        when(mockHitbox.copy()).thenReturn(newX, newY);
        when(mockOther.getHitbox()).thenReturn(mock(Bounds.class));

        collisionManager.addObject(mockOther);

        boolean[] result = collisionManager.checkCollisionsFor(mockEntity, 100f, 200f);

        assertFalse(result[0]);
        assertFalse(result[1]);
        verify(mockEntity, never()).onCollision(any(), any());
    }

    @Test
    void removeEntities_shouldRemoveGivenEntities() {
        Entity toRemove = mock(Entity.class);
        collisionManager.addObject(toRemove);
        collisionManager.removeEntities(List.of(toRemove));

        when(toRemove.getHitbox()).thenReturn(new CircleBounds(5, 1));

        boolean[] result = collisionManager.checkCollisionsFor(toRemove, 0, 0);
        assertFalse(result[0]);
        assertFalse(result[1]);
    }

    @Test
    void applyAreas_shouldApplyEffect_whenAreaContainsPlayer() {
        when(mockArea.contains(mockPlayer)).thenReturn(true);

        collisionManager.addArea(mockArea);
        collisionManager.applyAreas(mockPlayer);

        verify(mockArea).applyEffect(mockPlayer);
    }

    @Test
    void applyAreas_shouldNotApplyEffect_whenAreaDoesNotContainPlayer() {
        when(mockArea.contains(mockPlayer)).thenReturn(false);

        collisionManager.addArea(mockArea);
        collisionManager.applyAreas(mockPlayer);

        verify(mockArea, never()).applyEffect(mockPlayer);
    }

    @Test
    void addObject_and_addArea_shouldNotThrowExceptions() {
        assertDoesNotThrow(() -> collisionManager.addObject(mockEntity));
        assertDoesNotThrow(() -> collisionManager.addArea(mockArea));
    }
}
