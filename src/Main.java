import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        task5();
        task6();
    }

    //mean of an integer list
    static double mean(List<Integer> data) {
        int sum = 0;
        for (int v : data) sum += v;
        return (double) sum / data.size();
    }

    //variance of an integer list
    static double variance(List<Integer> data) {
        double m = mean(data);
        double sumSq = 0;
        for (int v : data) sumSq += (v - m) * (v - m);
        return sumSq / data.size();
    }

    static void printResult(String name, SearchResult r) {
        System.out.println("\n--- " + name + " ---");
        System.out.println("Visited Nodes : " + r.visitedNodesList);
        System.out.println("Time (mins)   : " + r.timeCost);
        System.out.println("Final Path    : " +
                (r.finalPath != null ? r.finalPath : "No path found"));
        System.out.println("Path Length   : " +
                (r.finalPath != null ? r.finalPath.size() : 0));
    }

    static void printStats(String label,
                           List<Integer> times,
                           List<Integer> paths) {
        System.out.println("\n====== " + label + " STATISTICS ======");
        System.out.printf("  Time  - Mean: %.2f min  |  Variance: %.2f%n",
                mean(times), variance(times));
        System.out.printf("  Path  - Mean: %.2f nodes|  Variance: %.2f%n",
                mean(paths), variance(paths));
    }

    //task 5 - run IDDFS and Best first search on 3 random mazes
    static void task5(){
        System.out.println("==== Task 5 - 3 runs ====\n");
        int runs = 3;
        List<Integer> iddfs_times = new ArrayList<>();
        List<Integer> bfs_times   = new ArrayList<>();
        List<Integer> iddfs_paths = new ArrayList<>();
        List<Integer> bfs_paths   = new ArrayList<>();

        for (int run = 1; run <= runs; run++) {
            System.out.println("\n==== RUN " + run + " ====");
            Maze maze = new Maze();
            maze.printMaze();

            // IDDFS
            IDDFS iddfs = new IDDFS(maze);
            SearchResult r1 = iddfs.search();
            printResult("IDDFS", r1);
            iddfs_times.add(r1.timeCost);
            iddfs_paths.add(r1.finalPath != null ? r1.finalPath.size() : 0);

            // Best First Search (Chebyshev)
            BestFirstSearch bfs = new BestFirstSearch(maze);
            SearchResult r2 = bfs.searchChebyshev();
            printResult("Best First Search (Chebyshev)", r2);
            bfs_times.add(r2.timeCost);
            bfs_paths.add(r2.finalPath != null ? r2.finalPath.size() : 0);
        }

        //mean and variance
        printStats("IDDFS", iddfs_times, iddfs_paths);
        printStats("Best First Search (Chebyshev)", bfs_times, bfs_paths);

        //comparison
        System.out.println("\n====== TASK 5 COMPARATIVE ANALYSIS ======");
        System.out.println();
        System.out.println("1. Completeness:");
        System.out.println("   IDDFS      : Complete – always finds a path if one exists.");
        System.out.println("   BFS (h)    : Not complete in general, it can get stuck in loops or dead ends if revisited nodes aren't tracked.If visited nodes are tracked, it becomes complete on finite graphs.");
        System.out.println();
        System.out.println("2. Optimality:");
        System.out.println("   IDDFS      : Optimal for unit-cost edges (finds shortest depth path).");
        System.out.println("   BFS (h)    : Not guaranteed optimal –  it greedily expands the node closest to the goal using only h(n), ignoring the actual cost g(n) accumulated so far.");
        System.out.println();
        System.out.println("3. Time Complexity:");
        System.out.println("   IDDFS      : O(b^d) per iteration; explores many nodes repeatedly.");
        System.out.println("   BFS (h)    : O(b^m) where m is the max depth of the search space; guided by heuristic, typically far fewer nodes explored.");
        System.out.println();
        System.out.printf( "   Mean time  IDDFS=%.2f  BFS=%.2f minutes%n",
                mean(iddfs_times), mean(bfs_times));
        System.out.printf( "   Variance   IDDFS=%.2f  BFS=%.2f%n",
                variance(iddfs_times), variance(bfs_times));
    }

    //Task 6 - repeat with 3 diff distance metrics , 3 mazes each
    static void task6() {
        System.out.println("\n\n==== Task 6 - 3 Distance metrics ====\n");
        DistanceMetric[] metrics = {
                DistanceMetric.OCTILE,
                DistanceMetric.EUCLIDEAN,
                DistanceMetric.MANHATTAN
        };

        int runs = 3;
        for (DistanceMetric metric : metrics) {
            System.out.println("\n\n===Metric: " + metric + " ===");
            List<Integer> iddfs_times = new ArrayList<>();
            List<Integer> bfs_times = new ArrayList<>();
            List<Integer> iddfs_paths = new ArrayList<>();
            List<Integer> bfs_paths = new ArrayList<>();

            for (int run = 1; run <= runs; run++) {
                System.out.println("\n  ==== Run " + run + " ====");
                Maze maze = new Maze();
                maze.printMaze();

                // IDDFS (distance-metric independent – runs same each time)
                IDDFS iddfs = new IDDFS(maze);
                SearchResult r1 = iddfs.search();
                printResult("IDDFS", r1);
                iddfs_times.add(r1.timeCost);
                iddfs_paths.add(r1.finalPath != null ? r1.finalPath.size() : 0);

                // BFS with current metric
                BestFirstSearch bfs = new BestFirstSearch(maze, metric);
                SearchResult r2 = bfs.search();
                printResult("BFS (" + metric + ")", r2);
                bfs_times.add(r2.timeCost);
                bfs_paths.add(r2.finalPath != null ? r2.finalPath.size() : 0);
            }
            printStats("IDDFS with " + metric, iddfs_times, iddfs_paths);
            printStats("BFS   with " + metric, bfs_times, bfs_paths);
        }
    }
}
