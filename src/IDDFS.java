import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IDDFS {
    private Maze maze;
    private List<Integer> visitedAll;

    public IDDFS(Maze maze){
        this.maze = maze;
    }

    public SearchResult search(){
        //try increasing depth limits
        for(int limit =0; limit < Maze.total; limit++){
            visitedAll = new ArrayList<>();
            Set<Integer> visited = new HashSet<>();
            visited.add(maze.startNode);

            List<Integer> path = new ArrayList<>();
            path.add(maze.startNode);

            List<Integer> result = dls(maze.startNode, limit, path, visited);

            if (result != null) {
                System.out.println("IDDFS found goal at depth limit: " + limit);
                return new SearchResult(visitedAll, result);
            }
        }
        //if no path found
        return new SearchResult(visitedAll, null);
    }

    //dls
    private List<Integer> dls(int current, int depth, List<Integer> path, Set<Integer> visited){
        visitedAll.add(current); //track explored nodes

        //goal found
        if(current == maze.goalNode){
            return new ArrayList<>(path);
        }

        //depth limit reached
        if (depth == 0) {
            return null;
        }

        //Explore neighbors in increasing order
        for(int[] neighbor : maze.getNeighbors(current)){
            int nextNode = neighbor[0];

            if(!visited.contains(nextNode)){
                visited.add(nextNode);
                path.add(nextNode);

                List<Integer> result = dls(nextNode, depth -1, path, visited);
                if(result !=null){
                    return result;
                }

                //backtrack
                path.remove(path.size() - 1);
                visited.remove(nextNode);
            }
        }
        return null; //not found at this depth
    }
}
