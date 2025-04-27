package mechanics;

import com.game.core.shooting.DelayedWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class DelayedWrapperTest {
    @Mock private Consumer<String> mockAction;

    @Test
    void shouldNotExecuteBeforeDelayExpires() {
        DelayedWrapper<String> wrapper = new DelayedWrapper<>(2.0, "Hello", mockAction);

        boolean result = wrapper.canExecute(1.0);
        assertFalse(result);
        verify(mockAction, never()).accept(any());
    }

    @Test
    void shouldExecuteWhenDelayExpiresExactly() {
        DelayedWrapper<String> wrapper = new DelayedWrapper<>(1.0, "Hello", mockAction);

        boolean result = wrapper.canExecute(1.0);
        assertTrue(result);
        verify(mockAction, times(1)).accept("Hello");
    }

    @Test
    void shouldExecuteWhenDelayGoesBelowZero() {
        DelayedWrapper<String> wrapper = new DelayedWrapper<>(0.5, "Hello", mockAction);

        boolean result = wrapper.canExecute(1.0);
        assertTrue(result);
        verify(mockAction, times(1)).accept("Hello");
    }

    @Test
    void shouldExecuteOnlyOnce() {
        DelayedWrapper<String> wrapper = new DelayedWrapper<>(0.5, "Hello", mockAction);

        // First call should execute
        assertTrue(wrapper.canExecute(1.0));
        verify(mockAction, times(1)).accept("Hello");
    }
}
