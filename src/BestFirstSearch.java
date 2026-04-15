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
}
