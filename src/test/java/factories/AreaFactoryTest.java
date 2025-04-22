package factories;

import com.game.core.factories.AreaFactory;
import com.game.core.scene.areas.Area;
import com.game.core.scene.areas.KillableArea;
import com.game.core.scene.areas.SlowingArea;
import com.game.core.utils.config.SceneConfig;
import com.game.core.utils.config.enums.AreaTypeEnum;
import com.game.core.utils.config.enums.BoundsTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AreaFactoryTest {
    @Mock private SceneConfig.AreaConfig config;
    @Mock private SceneConfig.BoundsConfig boundsConfig;

    @BeforeEach
    void setup() {
        when(config.getBounds()).thenReturn(boundsConfig);
    }

    @Test
    void testCreateFromConfig_shouldReturnSlowingArea() {
        when(config.getType()).thenReturn(AreaTypeEnum.SLOWING);
        when(boundsConfig.getType()).thenReturn(BoundsTypeEnum.RECTANGLE);
        when(boundsConfig.getWidth()).thenReturn(10f);
        when(boundsConfig.getHeight()).thenReturn(10f);

        Area area = AreaFactory.createFromConfig(config);

        assertInstanceOf(SlowingArea.class, area);
    }

    @Test
    void testCreateFromConfig_shouldReturnKillableArea() {
        when(config.getType()).thenReturn(AreaTypeEnum.KILLABLE);
        when(boundsConfig.getType()).thenReturn(BoundsTypeEnum.CIRCLE);
        when(boundsConfig.getRadius()).thenReturn(5f);

        Area area = AreaFactory.createFromConfig(config);

        assertInstanceOf(KillableArea.class, area);
    }
}
