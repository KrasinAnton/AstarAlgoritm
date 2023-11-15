import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import ru.krasin.AStarAlgorithm;
import java.util.List;
public class TestAStarMethod {
    @Test
    public void testAStarPathFound() {
        int[][] labyrinth = {
                {1, 0, 1, 1, 1},
                {0, 1, 1, 0, 1},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 0, 1},
                {1, 0, 1, 0, 0}
        };
        AStarAlgorithm.Location start = new AStarAlgorithm.Location(0, 0);
        AStarAlgorithm.Location end = new AStarAlgorithm.Location(4, 4);
        List<AStarAlgorithm.Location> path = AStarAlgorithm.aStar(labyrinth, start, end);
        assertFalse(path.isEmpty());                // Проверяется, что возвращенный путь не является пустым,
                                                    // что указывает на то, что алгоритм успешно нашел путь.

        assertEquals(end, path.get(path.size() - 1));// Также проверяется, что последняя точка в пути
                                                     // соответствует конечной точке
    }

    @Test
    public void testAStarNoPathFound() {
        int[][] labyrinth = {
                {1, 0, 1, 1, 1},
                {0, 1, 1, 0, 1},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 1, 1},
                {1, 0, 1, 0, 0}
        };

        AStarAlgorithm.Location start = new AStarAlgorithm.Location(0, 0);
        AStarAlgorithm.Location end = new AStarAlgorithm.Location(4, 4);

        List<AStarAlgorithm.Location> path = AStarAlgorithm.aStar(labyrinth, start, end);

        assertTrue(path.isEmpty());    // Проверяется, что возвращенный путь является пустым,
                                       // что указывает на отсутствие пути от стартовой до конечной точки.
    }
}


