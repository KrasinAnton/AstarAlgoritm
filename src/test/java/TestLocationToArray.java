import org.junit.Test;
import ru.krasin.AStarAlgorithm;

import static org.junit.Assert.assertArrayEquals;

public class TestLocationToArray {
    @Test
    public void testLocationToArray() {
        AStarAlgorithm.Location location = new AStarAlgorithm.Location(1, 2);
        int[] expectedArray = {1, 2};

        assertArrayEquals(expectedArray, location.toArray()); //проверяет, что массив,
                                                              // возвращаемый методом toArray,
                                                              // содержит ожидаемые координаты
    }
}
