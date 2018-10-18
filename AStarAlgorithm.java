package com.ruhuna.supermarket.astar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author N.A.S. Kavinda
 */
public class AStarAlgorithm {

    private final int cols = 50;
    private final int rows = 50;
    
    private final Spot[][] grid = new Spot[cols][rows];
    private final List<Spot> openSet = new ArrayList<>();
    private final List<Spot> closedSet = new ArrayList<>();
    private Spot start;
    private Spot end;
    private List<Spot> path;
	
	// these are the possible paths row and column numbers.
    Set<Integer> pathsCol = new HashSet<>(Arrays.asList(3, 12, 22, 32, 42));
    Set<Integer> pathsRow = new HashSet<>(Arrays.asList(0, 22, 49));

    public static void main(String[] args) {
        // crate grid
        long start = System.nanoTime();
        AStarAlgorithm aStarAlgorithm = new AStarAlgorithm();
        aStarAlgorithm.setUp(); // this method is doing basic initialization 
//        aStarAlgorithm.showGrid(); // this method is used to show the grid point and it neighbors
        aStarAlgorithm.mainFunction(); // this is the function used to calculate the shortest path
        aStarAlgorithm.showShortestPath(); // this method show the i and j value of shortest path.
        System.out.println((System.nanoTime() - start) / 1000000000.0+"s");
    }

    public void addPath(int i, int j) {
        if (pathsRow.contains(i)) {
            this.grid[i][j].path = true;
        }
        if (pathsCol.contains(j)) {
            this.grid[i][j].path = true;
        }
    }

    public void setUp() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                grid[i][j] = new Spot(i, j);
            }
        }

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                grid[i][j].addNeighbors();
                addPath(i, j);
            }
        }

        start = grid[0][0];
        end = grid[cols-1][rows-1];

        openSet.add(start);

    }

    public void showGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                final Spot currentGrid = grid[i][j];
                System.out.println("[" + currentGrid.i + "][" + currentGrid.j + "]");
                final int negighborsArraySize = currentGrid.neighbors.size();
                for (int k = 0; k < negighborsArraySize; k++) {
                    System.out.print("[" + currentGrid.neighbors.get(k).i + "][" + currentGrid.neighbors.get(k).j + "]");
                    if (k != negighborsArraySize - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println("");
            }
        }
    }

    public void showShortestPath() {
        path.forEach(spot -> System.out.println(spot.i + " " + spot.j));
    }

    public void mainFunction() {
        Spot current = null;
        while (openSet.size() > 0) {
            int goal = 0;
            for (int i = 0; i < openSet.size(); i++) {
                if (openSet.get(i).f < openSet.get(goal).f) {
                    goal = i;
                }
            }

            current = openSet.get(goal);

            if (current == end) {
                path = new ArrayList<>();
                Spot temp = current;
                path.add(temp);
                while (temp.previous != null) {
                    path.add(temp.previous);
                    temp = temp.previous;
                }
                return;
            }

            openSet.remove(current);
            closedSet.add(current);

            List<Spot> neighbors = current.neighbors;
            for (int i = 0; i < neighbors.size(); i++) {
                Spot neighbor = neighbors.get(i);

                if (!closedSet.contains(neighbor) && neighbor.path) { // neighbor path shod check hear
//                    System.out.println(current.i + " " + current.j);
                    double tempG = current.g + 1;
                    boolean newPath = false;
                    if (openSet.contains(neighbor)) {
                        if (tempG < neighbor.g) {
                            neighbor.g = tempG;
                            newPath = true;
                        }
                    } else {
                        neighbor.g = tempG;
                        newPath = true;
                        openSet.add(neighbor);
                    }

                    if (newPath) {
                        neighbor.h = heuristic(neighbor);
                        neighbor.f = neighbor.g + neighbor.h;

                        neighbor.previous = current;
                    }
                }
            }
        }
        path = new ArrayList<>();
        Spot temp = current;
        path.add(temp);
        while (temp.previous != null) {
            path.add(temp.previous);
            temp = temp.previous;
        }

    }

    private double heuristic(Spot neighbor) {
        return Math.sqrt(Math.pow((neighbor.i - end.i), 2) + Math.pow((neighbor.j - end.j), 2));
    }

    class Spot {

        private int i;
        private int j;
        private double f;// total distance start point to end point (g+h)
        private double g;// distance to start point to current point.
        private double h; // euclidean distance to current point to end point. 
        private List<Spot> neighbors = new ArrayList<>();
        private Spot previous;
        private boolean path = false;

        public Spot(int i, int j) {
            this.i = i;
            this.j = j;
        }

		// there not considering  diagonal point as a neighbors.
        public void addNeighbors() {
            if (i < cols - 1) {
                this.neighbors.add(grid[i + 1][j]);
            }
            if (i > 0) {
                this.neighbors.add(grid[i - 1][j]);
            }
            if (j < cols - 1) {
                this.neighbors.add(grid[i][j + 1]);
            }
            if (j > 0) {
                this.neighbors.add(grid[i][j - 1]);
            }
			
			        // if you want to add diagonal move uncomment bellow lines 
//        if (i < cols - 1 && j > 0) {
//            this.neighbors.add(grid[i + 1][j - 1]);
//        }
//        if (i > 0 && j > 0) {
//            this.neighbors.add(grid[i - 1][j - 1]);
//        }
//        if (i > 0 && j < cols - 1) {
//            this.neighbors.add(grid[i - 1][j + 1]);
//        }
//        if (i < cols - 1 && j < cols - 1) {
//            this.neighbors.add(grid[i + 1][j + 1 ]);
//        }
        }
    }
}
