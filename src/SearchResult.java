import java.util.List;

public class SearchResult {
    public List<Integer> visitedNodesList;
    public List<Integer> finalPath;
    public int timeCost;
    //Number of nodes visited= minutes count

    public SearchResult(List<Integer> visited, List<Integer> path){
        this.visitedNodesList= visited;
        this.finalPath= path;
        this.timeCost= visited.size();
    }

}
