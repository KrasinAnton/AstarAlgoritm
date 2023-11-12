package ru.krasin;

import java.util.*;
import java.util.stream.Collectors;


public class AStarAlgorithm {
    public static class Node implements Comparable<Node> {
        private Location location;
        int f, g, h;

        public Node(Location location, int g, int h) {
            this.location = location;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }

        public Location getLocation() {
            return location;
        }

        public int getF() {
            return f;
        }

        public int getG() {
            return g;
        }

        public int getH() {
            return h;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public void setF(int f) {
            this.f = f;
        }

        public void setG(int g) {
            this.g = g;
        }

        public void setH(int h) {
            this.h = h;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.f, other.f);
        }
    }

        public static class Location {
            private final int x;
            private final int y;

            public Location(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }

            public int[] toArray() {
                return new int[]{x, y};
            }

            @Override
            public String toString() {
                return "(" + x + ", " + y + ")";
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Location location = (Location) o;
                return x == location.x && y == location.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }
        }

    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private static int[][] maze;
    private static int rows, cols;

    public static List<Location> aStar(int[][] inputMaze, Location start, Location end) {
        maze = inputMaze;
        rows = maze.length;
        cols = maze[0].length;

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Map<String, Node> openSetMap = new HashMap<>();
        Map<String, Node> closedSet = new HashMap<>();

        Node startNode = new Node(start, 0, heuristic(start.toArray(), end.toArray()));
        openSet.add(startNode);
        openSetMap.put(key(startNode), startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            openSetMap.remove(key(current));

            if (current.getLocation().equals(end)) {
                // Путь найден, возвращаем его
                return reconstructPath(current, closedSet).stream()
                        .map(arr -> new Location(arr[0], arr[1]))
                        .collect(Collectors.toList());
            }

            closedSet.put(key(current), current);

            for (int[] dir : DIRS) {
                int newRow = current.getLocation().getX() + dir[0];
                int newCol = current.getLocation().getY() + dir[1];

                if (isValid(newRow, newCol) && maze[newRow][newCol] == 0) {
                    int tentativeG = current.getG() + 1;
                    int h = heuristic(new int[]{newRow, newCol}, end.toArray());
                    int f = tentativeG + h;

                    Node neighbor = new Node(new Location(newRow, newCol), tentativeG, h);

                    if (closedSet.containsKey(key(neighbor))) {
                        continue; // Этот сосед уже обработан
                    }

                    if (!openSetMap.containsKey(key(neighbor)) || f < openSetMap.get(key(neighbor)).getF()) {
                        openSet.add(neighbor);
                        openSetMap.put(key(neighbor), neighbor);
                    }
                }
            }
        }

        // Нет пути
        return new ArrayList<>();
    }


    private static boolean isValid(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private static int heuristic(int[] start, int[] end) {
        // Просто эвристика - расстояние Манхэттенское
        return Math.abs(start[0] - end[0]) + Math.abs(start[1] - end[1]);
    }

    private static String key(Node node) {
        return node.getLocation().getX() + "-" + node.getLocation().getY();
    }

    private static List<int[]> reconstructPath(Node endNode, Map<String, Node> closedSet) {
        List<int[]> path = new ArrayList<>();
        Node current = endNode;

        while (current != null) {
            path.add(new int[]{current.getLocation().getX(), current.getLocation().getY()});
            current = closedSet.get(key(current));
        }

        Collections.reverse(path);
        return path;
    }

    private static void printMaze(List<Location> path, int[] start, int[] end) {
        if (maze != null) {
            System.out.println("Path Coordinates: " + path.stream()
                    .map(Location::toString)
                    .collect(Collectors.toList()));

            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < cols; y++) {
                    int[] currentPos = {x, y};

                    if (Arrays.equals(currentPos, start)) {
                        System.out.print("S ");
                    } else if (Arrays.equals(currentPos, end)) {
                        System.out.print("G ");
                    } else if (path.stream().anyMatch(p -> Arrays.equals(p.toArray(), currentPos))) {
                        System.out.print("* ");
                    } else {
                        System.out.print(maze[x][y] == 0 ? "  " : "X ");
                    }
                }
                System.out.println();
            }
        }
    }


    public static void main(String[] args) {
        int[][] labyrinth = {
                {1, 0, 1, 1, 1},
                {0, 1, 1, 0, 1},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 0, 1},
                {1, 0, 1, 0, 0}
        };

        Location start = new Location(0, 0);
        Location end = new Location(4, 4);

        List<Location> path = aStar(labyrinth, start, end);

        if (!path.isEmpty()) {
            System.out.println("\nПуть найден:");
            printMaze(path, start.toArray(), end.toArray());
        } else {
            System.out.println("\nНет пути.");
        }
    }
}




