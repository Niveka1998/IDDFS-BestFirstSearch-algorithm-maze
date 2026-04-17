import java.util.*;

public class BestFirstSearch {
    private Maze maze;
    private DistanceMetric metric;

    public BestFirstSearch(Maze maze, DistanceMetric metric){
        this.maze = maze;
        this.metric = metric;
    }

    public BestFirstSearch(Maze maze){
        this.maze=maze;
    }
    private double heuristic(int nodeId) {
        int nx = maze.nodes[nodeId][0];
        int ny = maze.nodes[nodeId][1];
        int gx = maze.nodes[maze.goalNode][0];
        int gy = maze.nodes[maze.goalNode][1];
        int dx = Math.abs(nx - gx);
        int dy = Math.abs(ny - gy);

        switch (metric) {
            case OCTILE:
                // Octile distance: exact cost for 8-directional movement
                // diagonal steps cost sqrt(2), straight steps cost 1
                // h = (sqrt(2)-1)*min(|dx|,|dy|) + max(|dx|,|dy|)
                return (Math.sqrt(2) - 1) * Math.min(dx, dy) + Math.max(dx, dy);

            case EUCLIDEAN:
                // Straight-line distance
                return Math.sqrt(dx * dx + dy * dy);

            case MANHATTAN:
                // Axis-aligned steps only
                return dx + dy;

            default:
                throw new IllegalStateException("Unknown metric: " + metric);
        }
    }
    //Chebyshev distance heuristic
    private int chebyshev(int nodeId){
        int nx = maze.nodes[nodeId][0];
        int ny = maze.nodes[nodeId][1];
        int gx = maze.nodes[maze.goalNode][0];
        int gy = maze.nodes[maze.goalNode][1];
        return Math.max(Math.abs(nx-gx), Math.abs(ny-gy));
    }

    public SearchResult searchChebyshev(){
        List<Integer> visited = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();

        //priority queue - lower heuristic = high priority
        PriorityQueue<int[]> queue = new PriorityQueue<>(
                Comparator.comparingInt(a -> a[0])
        );

        //Track paths
        Map<Integer, Integer> cameFrom = new HashMap<>();
        cameFrom.put(maze.startNode, -1); //start has no parent

        queue.offer(new int[]{chebyshev(maze.startNode), maze.startNode});

        while(!queue.isEmpty()){
            int[] current = queue.poll();
            int node = current[1];

            if (seen.contains(node)) continue;
            seen.add(node);
            visited.add(node);

            //goal found
            if (node == maze.goalNode) {
                List<Integer> path = reconstructPath(cameFrom, node);
                return new SearchResult(visited, path);
            }

            // Explore neighbors
            for (int[] neighbor : maze.getNeighbors(node)) {
                int nextNode = neighbor[0];
                if (!seen.contains(nextNode)) {
                    if (!cameFrom.containsKey(nextNode)) {
                        cameFrom.put(nextNode, node);
                    }
                    queue.offer(new int[]{chebyshev(nextNode), nextNode});
                }
            }
        }
        return new SearchResult(visited,null); //no path found
    }


    public SearchResult search() {
        List<Integer>         visited  = new ArrayList<>();
        Set<Integer>          seen     = new HashSet<>();
        Map<Integer, Integer> cameFrom = new HashMap<>();

        // Priority queue ordered by heuristic value (lower = better)
        PriorityQueue<double[]> queue = new PriorityQueue<>(
                Comparator.comparingDouble(a -> a[0])
        );

        cameFrom.put(maze.startNode, -1);
        queue.offer(new double[]{heuristic(maze.startNode), maze.startNode});

        while (!queue.isEmpty()) {
            double[] current = queue.poll();
            int node = (int) current[1];

            if (seen.contains(node)) continue;
            seen.add(node);
            visited.add(node);

            if (node == maze.goalNode) {
                List<Integer> path = reconstructPath(cameFrom, node);
                return new SearchResult(visited, path);
            }

            for (int[] neighbor : maze.getNeighbors(node)) {
                int next = neighbor[0];
                if (!seen.contains(next)) {
                    if (!cameFrom.containsKey(next)) {
                        cameFrom.put(next, node);
                    }
                    queue.offer(new double[]{heuristic(next), next});
                }
            }
        }
        return new SearchResult(visited, null); // no path found
    }

    //Trace back from the goal to start
    private List<Integer> reconstructPath(Map<Integer, Integer> cameFrom, int goal) {
        List<Integer> path = new ArrayList<>();
        int current = goal;
        while (current != -1) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

}
