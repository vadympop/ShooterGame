package scene.spawners;

import com.game.core.entities.Entity;
import com.game.core.entities.Player;
import com.game.core.factories.PlayerFactory;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.shooting.ShootingManager;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerSpawnerTest {
    @Mock private Tile tile;
    @Mock private Tile playerTile;
    @Mock private GraphicsContext gc;
    @Mock private Player player;
    @Mock private Consumer<Entity> event;
    @Mock private ShootingManager mockSm;

    private PlayerSpawner playerSpawner;

    @BeforeEach
    void setup() {
        playerSpawner = new PlayerSpawner(tile, playerTile);
    }

    @Test
    void spawn_createsPlayerIfNull() {
        try (MockedStatic<PlayerFactory> mocked = mockStatic(PlayerFactory.class)) {
            when(player.getSm()).thenReturn(mockSm);
            mocked.when(() -> PlayerFactory.create(playerSpawner)).thenReturn(player);
            playerSpawner.addEvent("onEntityCreated", event);
            playerSpawner.spawn();
            assertNotNull(playerSpawner.getPlayer());
            verify(event).accept(player);
        }
    }

    @Test
    void spawn_callsRespawnWhenPlayerExists() {
        playerSpawner.setPlayer(player);
        playerSpawner.spawn();

        verify(player).respawn(playerSpawner.getX(), playerSpawner.getY());
    }

    @Test
    void addEvent_storesEvent() {
        playerSpawner.addEvent("onEntityCreated", event);

        assertEquals(event, playerSpawner.getEvent("onEntityCreated"));
    }

    @Test
    void getEvent_returnsNullIfEventNotFound() {
        Consumer<Entity> result = playerSpawner.getEvent("nonExistingEvent");

        assertNull(result);
    }

    @Test
    void draw_callsTileDraw() {
        playerSpawner.draw(gc);

        verify(tile).draw(eq(gc), anyFloat(), anyFloat());
    }

    @Test
    void setPlayer_setsPlayerCorrectly() {
        playerSpawner.setPlayer(player);

        assertEquals(player, playerSpawner.getPlayer());
    }

    @Test
    void getPlayerTile_returnsCorrectTile() {
        assertEquals(playerTile, playerSpawner.getPlayerTile());
    }

    @Test
    void getPlayerKillsCount_returnsSameWithPlayerKillsCount() {
        playerSpawner.setPlayer(player);
        player.incrementKillsCount();

        assertEquals(playerSpawner.getPlayerKillsCount(), player.getKillsCount());
    }
}
