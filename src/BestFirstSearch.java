import java.util.*;

public class BestFirstSearch {
    private Maze maze;

    public BestFirstSearch(Maze maze){
        this.maze = maze;
    }

    //Chebyshev distance heuristic
    private int chebyshev(int nodeId){
        int nx = maze.nodes[nodeId][0];
        int ny = maze.nodes[nodeId][1];
        int gx = maze.nodes[maze.goalNode][0];
        int gy = maze.nodes[maze.goalNode][1];
        return Math.max(Math.abs(nx-gx), Math.abs(ny-gy));
    }

    public SearchResult search(){
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

    // Trace back from goal to start
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
