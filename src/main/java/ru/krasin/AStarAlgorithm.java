package ru.krasin;

import java.util.*;
import java.util.stream.Collectors;

public class AStarAlgorithm {

    // Класс Node представляет точку в сетке
    public static class Node implements Comparable<Node> {
        private final Location location;
        int f, g, h;
        // Конструктор для создания узла
        public Node(Location location, int g, int h) {
            this.location = location;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }
        // Получение координат узла
        public Location getLocation() {
            return location;
        }
        // Получение значения F (сумма G и H)
        public int getF() {
            return f;
        }
        // Получение значения G (стоимость пути от начальной точки до текущей)
        public int getG() {
            return g;
        }
        // Реализация сравнения для приоритетной очереди
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.f, other.f);
        }
    }
    // Класс Location представляет координаты точки в сетке
    public record Location(int x, int y) {
        // Преобразование координат в массив
        public int[] toArray() {
            return new int[]{x, y};
        }
        // Переопределение метода toString для удобного вывода
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
    // Массив смещений для движения в соседние ячейки
    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private static int[][] maze;
    private static int rows, cols;

    // Основной метод A* алгоритма
    public static List<Location> aStar(int[][] inputMaze, Location start, Location end) {
        // Инициализация лабиринта и размеров
        maze = inputMaze;
        rows = maze.length;
        cols = maze[0].length;
        // Приоритетная очередь для открытого множества
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        // Карта для быстрого доступа к узлам в открытом множестве
        Map<Location, Node> openSetMap = new HashMap<>();
        // Карта для хранения закрытого множества
        Map<Location, Node> closedSet = new HashMap<>();
        // Создание стартового узла
        Node startNode = new Node(start, 0, heuristic(start.toArray(), end.toArray()));
        openSet.add(startNode);
        openSetMap.put(start, startNode);
        // Основной цикл A* алгоритма
        while (!openSet.isEmpty()) {
            // Извлечение узла с наименьшей стоимостью F
            Node current = openSet.poll();
            openSetMap.remove(current.getLocation());
            // Проверка, достигнута ли конечная точка
            if (current.getLocation().equals(end)) {
                // Путь найден, возвращаем его
                return reconstructPath(current, closedSet).stream()
                        .map(arr -> new Location(arr[0], arr[1]))
                        .collect(Collectors.toList());
            }
            // Помещаем узел в закрытое множество
            closedSet.put(current.getLocation(), current);
            // Перебор соседей текущего узла
            for (int[] dir : DIRS) {
                int newRow = current.getLocation().x() + dir[0];
                int newCol = current.getLocation().y() + dir[1];
                // Проверка на валидность и проходимость ячейки
                if (isValid(newRow, newCol, maze) && maze[newRow][newCol] == 0) {
                    // Рассчитываем временную стоимость G для соседа
                    int tentativeG = current.getG() + 1;
                    // Рассчитываем эвристическое расстояние H до конечной точки
                    int h = heuristic(new int[]{newRow, newCol}, end.toArray());
                    // Общая стоимость F
                    int f = tentativeG + h;
                    // Создаем узел для соседа
                    Location neighborLoc = new Location(newRow, newCol);
                    Node neighbor = new Node(neighborLoc, tentativeG, h);
                    // Проверяем, обработан ли уже этот сосед
                    if (closedSet.containsKey(neighborLoc)) {
                        continue;
                    }
                    // Проверяем, содержится ли сосед в открытом множестве
                    // и если нет или если новый путь короче, добавляем его в открытое множество
                    if (!openSetMap.containsKey(neighborLoc) || f < openSetMap.get(neighborLoc).getF()) {
                        openSet.add(neighbor);
                        openSetMap.put(neighborLoc, neighbor);
                    }
                }
            }
        }
        // Если цикл завершился и открытое множество пусто, путь не найден
        return new ArrayList<>();
    }

    // Проверка, находится ли ячейка в пределах сетки
    public static boolean isValid(int row, int col, int[][] maze) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
    // Эвристическая функция - расстояние Манхэттенское
    public static int heuristic(int[] start, int[] end) {
        return Math.abs(start[0] - end[0]) + Math.abs(start[1] - end[1]);
    }
    // Генерация ключа для использования в мапах
    private static String key(Node node) {
        return node.getLocation().x() + "-" + node.getLocation().y();
    }
    // Восстановление пути от конечной точки до начальной
    private static List<int[]> reconstructPath(Node endNode, Map<Location, Node> closedSet) {
        List<int[]> path = new ArrayList<>();
        Node current = endNode;
        // Проход назад от конечной точки к начальной
        while (current != null) {
            path.add(new int[]{current.getLocation().x(), current.getLocation().y()});
            current = closedSet.get(current.getLocation());
        }
        // Инвертирование пути, чтобы он был от начальной точки к конечной
        Collections.reverse(path);
        return path;
    }

    // Вывод лабиринта с отмеченным путем
    private static void printMaze(List<Location> path, int[] start, int[] end) {
        if (maze != null) {
            // Создаем копию лабиринта для отображения пути
            int[][] displayMaze = Arrays.stream(maze)
                    .map(int[]::clone)
                    .toArray(int[][]::new);
            // Устанавливаем "*" в ячейки пути
            for (Location location : path) {
                displayMaze[location.x()][location.y()] = 0;
            }
            // Вывод лабиринта с обозначенным путем
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < cols; y++) {
                    int[] currentPos = {x, y};

                    if (Arrays.equals(currentPos, start)) {
                        System.out.print("S ");
                    } else if (Arrays.equals(currentPos, end)) {
                        System.out.print("G ");
                    } else if (displayMaze[x][y] == 0) {
                        System.out.print("* ");
                    } else if (displayMaze[x][y] == 1) {
                        System.out.print("X ");
                    }
                }
                System.out.println();
            }
        }
    }

    // Основной метод программы
    public static void main(String[] args) {
        // Пример лабиринта
        int[][] labyrinth = {
                {1, 0, 0, 1, 1},
                {1, 0, 1, 0, 1},
                {0, 0, 0, 1, 0},
                {0, 1, 0, 0, 1},
                {1, 0, 1, 0, 0}
        };
        // Начальная и конечная точки
        Location start = new Location(0, 0);
        Location end = new Location(4, 4);

        // Вызов A* алгоритма для поиска пути
        List<Location> path = aStar(labyrinth, start, end);

        // Условие true, false
        if (!path.isEmpty()) {
            System.out.println("\nПуть найден:");
            printMaze(path, start.toArray(), end.toArray());
        } else {
            System.out.println("\nНет пути.");
        }
    }
}




