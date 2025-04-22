package behaviour;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.exceptions.InvalidParameterException;
import com.game.core.utils.Scaler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class CircleBoundsTest {
    private CircleBounds circle1;
    private CircleBounds circle2;
    private RectangleBounds rectangleBounds;

    @BeforeEach
    void setup() {
        circle1 = new CircleBounds(5, 1);
        circle2 = new CircleBounds(3, 1);
        rectangleBounds = new RectangleBounds(4, 4, 1);
    }

    @Test
    void testConstructorWithSpecificScale() {
        CircleBounds circle = new CircleBounds(10, 2);
        assertEquals(20, circle.getRadius());
    }

    @Test
    void testIntersectWithCircle() {
        CircleBounds circle1 = new CircleBounds(5, 1);
        circle1.setPos(0, 0);
        CircleBounds circle2 = new CircleBounds(5, 1);
        circle2.setPos(5, 0);

        assertTrue(circle1.intersects(circle2));
    }

    @Test
    void testIntersectWithRectangle() {
        circle1.setPos(0, 0);
        rectangleBounds.setPos(4, 0);

        assertTrue(circle1.intersects(rectangleBounds));
    }

    @Test
    void testContainWithCircle() {
        circle1.setPos(0, 0);
        circle2.setPos(2, 0);

        assertTrue(circle1.contains(circle2));
    }

    @Test
    void testContainWithRectangle() {
        circle1.setPos(0, 0);
        rectangleBounds.setPos(2, 2);

        assertFalse(circle1.contains(rectangleBounds));
    }

    @Test
    void testMultiply() {
        circle1.multiply(2f);
        assertEquals(10, circle1.getRadius());
    }

    @Test
    void testMultiplyWithInvalidParameter() {
        assertThrows(
                InvalidParameterException.class,
                () -> circle1.multiply(-1f)
        );
    }

    @Test
    void testCopy() {
        Scaler mockScaler = mock(Scaler.class);
        try (MockedStatic<Scaler> mocked = mockStatic(Scaler.class)) {
            mocked.when(Scaler::getInstance).thenReturn(mockScaler);
            when(mockScaler.getScale()).thenReturn(1f);

            CircleBounds copy = (CircleBounds) circle1.copy();
            assertNotSame(circle1, copy);
            assertEquals(circle1.getRadius(), copy.getRadius());
        }
    }

    @Test
    void testSetRadiusWithInvalidValue() {
        assertThrows(
                InvalidParameterException.class,
                () -> new CircleBounds(-5)
        );
    }

    @Test
    void testToString() {
        assertTrue(circle1.toString().contains("radius="));
    }

    @Test
    void testMaxSize() {
        assertEquals(10, circle1.getMaxSize());
    }

    @Test
    void testGetMaxX() {
        assertEquals(5, circle1.getMaxX(), "Max X should be equal to center X + radius");
    }

    @Test
    void testGetMaxY() {
        assertEquals(5, circle1.getMaxY(), "Max Y should be equal to center Y + radius.");
    }

    @Test
    void testGetMinX() {
        assertEquals(-5, circle1.getMinX(), "Min X should be equal to center X - radius");
    }

    @Test
    void testGetMinY() {
        assertEquals(-5, circle1.getMinY(), "Min Y should be equal to center Y - radius");
    }

    @Test
    void testMaxXWithOffset() {
        circle1.setPos(10, 0);
        assertEquals(15, circle1.getMaxX(), "Max X should be equal to center X + radius with offset");
    }

    @Test
    void testMaxYWithOffset() {
        circle1.setPos(0, 10);
        assertEquals(15, circle1.getMaxY(), "Max Y should be equal to center Y + radius with offset");
    }

    @Test
    void testMinXWithOffset() {
        circle1.setPos(10, 0);
        assertEquals(5, circle1.getMinX(), "Min X should be equal to center X - radius with offset");
    }

    @Test
    void testMinYWithOffset() {
        circle1.setPos(0, 10);
        assertEquals(5, circle1.getMinY(), "Min Y should be equal to center Y - radius with offset");
    }
}
