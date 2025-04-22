package scene.spawners;

import com.game.core.entities.bonus.BonusType;
import com.game.core.entities.bonus.Bonus;
import com.game.core.entities.Entity;
import com.game.core.factories.BonusFactory;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.spawners.BonusSpawner;
import com.game.core.utils.Timer;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BonusSpawnerTest {
    @Mock private Tile tile;
    @Mock private Bonus bonus;
    @Mock private GraphicsContext gc;
    @Mock private Consumer<Entity> event;
    @Mock private Timer<BonusSpawner> timer;
    @Mock private BonusType bonusType;

    private BonusSpawner bonusSpawner;
    private final float cooldown = 5.0f;

    @BeforeEach
    void setup() {
        bonusSpawner = new BonusSpawner(tile, cooldown);
    }

    @Test
    void constructorThrowsNullPointerExceptionForNullTile() {
        assertThrows(
                NullPointerException.class,
                () -> new BonusSpawner(null, cooldown)
        );
    }

    @Test
    void constructorInitializesCooldownAndTimer() {
        assertEquals(cooldown, bonusSpawner.getCooldown());
        assertNotNull(bonusSpawner.getSpawnTimer());
        assertSame(tile, bonusSpawner.getTile());
    }

    @Test
    void spawnCreatesBonusWhenCurrentBonusIsNull() {
        try (MockedStatic<BonusType> bonusTypeMock = mockStatic(BonusType.class);
             MockedStatic<BonusFactory> bonusFactoryMock = mockStatic(BonusFactory.class)) {

            bonusTypeMock.when(BonusType::randomBonus).thenReturn(bonusType);
            bonusFactoryMock.when(() -> BonusFactory.create(bonusType)).thenReturn(bonus);
            bonusSpawner.addEvent("onEntityCreated", event);
            bonusSpawner.setPos(10f, 20f);

            bonusSpawner.spawn();

            verify(bonus).setPos(10.0f, 20.0f);
            verify(event).accept(bonus);
            bonusTypeMock.verify(BonusType::randomBonus);
            bonusFactoryMock.verify(() -> BonusFactory.create(bonusType));
        }
    }

    @Test
    void spawnCreatesBonusWhenCurrentBonusIsInactive() {
        try (MockedStatic<BonusType> bonusTypeMock = mockStatic(BonusType.class);
             MockedStatic<BonusFactory> bonusFactoryMock = mockStatic(BonusFactory.class)) {

            bonusTypeMock.when(BonusType::randomBonus).thenReturn(bonusType);
            bonusFactoryMock.when(() -> BonusFactory.create(bonusType)).thenReturn(bonus);
            bonusSpawner.addEvent("onEntityCreated", event);
            bonusSpawner.setPos(10f, 20f);

            setCurrentBonus(bonus);
            when(bonus.getState()).thenReturn(false);

            bonusSpawner.spawn();

            verify(bonus).setPos(10.0f, 20.0f);
            verify(event).accept(bonus);
            bonusTypeMock.verify(BonusType::randomBonus);
            bonusFactoryMock.verify(() -> BonusFactory.create(bonusType));
        }
    }

    @Test
    void spawnDoesNothingWhenCurrentBonusIsActive() {
        try (MockedStatic<BonusType> bonusTypeMock = mockStatic(BonusType.class);
             MockedStatic<BonusFactory> bonusFactoryMock = mockStatic(BonusFactory.class)) {

            setCurrentBonus(bonus);
            when(bonus.getState()).thenReturn(true);
            bonusSpawner.addEvent("onEntityCreated", event);

            bonusSpawner.spawn();

            verify(bonus).getState();
            verifyNoMoreInteractions(bonus);
            verifyNoInteractions(event);
            bonusTypeMock.verifyNoInteractions();
            bonusFactoryMock.verifyNoInteractions();
        }
    }

    @Test
    void addEventStoresEvent() {
        bonusSpawner.addEvent("onEntityCreated", event);

        assertSame(event, bonusSpawner.getEvent("onEntityCreated"));
    }

    @Test
    void getEventReturnsNullIfEventNotFound() {
        Consumer<Entity> result = bonusSpawner.getEvent("nonExistingEvent");

        assertNull(result);
    }

    @Test
    void drawCallsTileDraw() {
        bonusSpawner.setPos(10f, 20f);
        bonusSpawner.draw(gc);

        verify(tile).draw(gc, 10.0f, 20.0f);
    }

    @Test
    void updateCallsTimerUpdate() {
        double deltaTime = 0.016;
        setSpawnTimer(timer);

        bonusSpawner.update(deltaTime);

        verify(timer).update(eq(deltaTime), eq(bonusSpawner), eq(null));
    }

    @Test
    void getSpawnTimerReturnsCorrectTimer() {
        assertNotNull(bonusSpawner.getSpawnTimer());
        assertEquals(cooldown, bonusSpawner.getSpawnTimer().getBaseTime());
    }

    private void setCurrentBonus(Bonus bonus) {
        try {
            Field field = BonusSpawner.class.getDeclaredField("currentBonus");
            field.setAccessible(true);
            field.set(bonusSpawner, bonus);
        } catch (Exception e) {
            fail("Unable to use reflection for set currentBonus");
        }
    }

    private void setSpawnTimer(Timer<BonusSpawner> timer) {
        try {
            Field field = BonusSpawner.class.getDeclaredField("spawnTimer");
            field.setAccessible(true);
            field.set(bonusSpawner, timer);
        } catch (Exception e) {
            fail("Unable to use reflection for set spawnTimer");
        }
    }
}