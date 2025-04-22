package behaviour;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.exceptions.InvalidParameterException;
import com.game.core.utils.Scaler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class RectangleBoundsTest {
    private RectangleBounds rectangle;
    private RectangleBounds anotherRectangle;
    private CircleBounds circle;

    @BeforeEach
    void setup() {
        rectangle = new RectangleBounds(10, 5, 1);
        rectangle.setPos(0, 0);

        anotherRectangle = new RectangleBounds(5, 5, 1);
        anotherRectangle.setPos(4, 0);

        circle = new CircleBounds(5, 1);
        circle.setPos(0, 0);
    }

    @Test
    void testGetMaxX() {
        assertEquals(5, rectangle.getMaxX(), "Max X should be equal to center X + width / 2.");
    }

    @Test
    void testGetMaxY() {
        assertEquals(2.5, rectangle.getMaxY(), "Max Y should be equal to center Y + height / 2.");
    }

    @Test
    void testGetMinX() {
        assertEquals(-5, rectangle.getMinX(), "Min X should be equal to center X - width / 2.");
    }

    @Test
    void testGetMinY() {
        assertEquals(-2.5, rectangle.getMinY(), "Min Y should be equal to center Y - height / 2.");
    }

    @Test
    void testMaxSize() {
        assertEquals(11.180, rectangle.getMaxSize(), 0.001, "Max size should be the diagonal of the rectangle.");
    }

    @Test
    void testIntersectsWithAnotherRectangle() {
        assertTrue(rectangle.intersects(anotherRectangle), "Rectangles should intersect.");
    }

    @Test
    void testDoesNotIntersectWithAnotherRectangle() {
        anotherRectangle.setPos(20, 0);
        assertFalse(rectangle.intersects(anotherRectangle));
    }

    @Test
    void testDoesNotContainAnotherRectangle() {
        anotherRectangle.setPos(7, 0);
        assertFalse(rectangle.contains(anotherRectangle));
    }

    @Test
    void testIntersectsWithCircle() {
        assertTrue(rectangle.intersects(circle));
    }

    @Test
    void testDoesNotIntersectWithCircle() {
        circle.setPos(20, 0);
        assertFalse(rectangle.intersects(circle));
    }

    @Test
    void testNotContainsCircle() {
        assertFalse(rectangle.contains(circle));
    }

    @Test
    void testMultiply() {
        rectangle.multiply(2);
        assertEquals(20, rectangle.getWidth());
        assertEquals(10, rectangle.getHeight());
    }

    @Test
    void testMultiplyWithInvalidMultiplier() {
        assertThrows(
                InvalidParameterException.class,
                () -> rectangle.multiply(0)
        );
    }

    @Test
    void testConstructorWithScaling() {
        RectangleBounds rect = new RectangleBounds(10, 10, 2);
        assertEquals(20, rect.getHeight());
        assertEquals(20, rect.getWidth());
    }

    @Test
    void testCopy() {
        Scaler mockScaler = mock(Scaler.class);
        try (MockedStatic<Scaler> mocked = mockStatic(Scaler.class)) {
            mocked.when(Scaler::getInstance).thenReturn(mockScaler);
            when(mockScaler.getScale()).thenReturn(1f);

            Bounds copiedRectangle = rectangle.copy();
            assertNotNull(copiedRectangle);
            assertEquals(rectangle.getWidth(), ((RectangleBounds) copiedRectangle).getWidth());
            assertEquals(rectangle.getHeight(), ((RectangleBounds) copiedRectangle).getHeight());
        }
    }
}
