import java.util.*;

public class Maze {
    public int[][] nodes; //nodes[id] = {col,row}
    public int startNode;
    public int goalNode;
    public Set<Integer> barrierNodes;
    public static final int size = 6;
    public static final int total = 36;

    public Maze(){
        // node_id = col * 6 +row
        nodes = new int[total][2];
        for(int col=0; col<size; col++){
            for(int row=0; row<size; row++){
                int id = col * size +row;
                nodes[id][0] = col; //x
                nodes[id][1] = row; //y
            }
        }
        randomSetup();
    }
    private void randomSetup(){
        Random rand = new Random();

        //Start node (0-11)
        startNode = rand.nextInt(12);

        //Goal node (24-35)
        goalNode = 24 + rand.nextInt(12);

        //Barrier nodes - 4 randoms
        List<Integer> remainingNodes = new ArrayList<>();
        for(int i=0; i<total; i++){
            if(i != startNode && i != goalNode){
                remainingNodes.add(i);
            }
        }
        Collections.shuffle(remainingNodes);
        barrierNodes = new HashSet<>(); //empty set to store barrier nodes - no duplicates
        for(int i=0; i<4; i++){ //get first 4 nodes from shuffled list
            barrierNodes.add(remainingNodes.get(i));
        }
    }

    //get neighbors in increasing node number order
    public List<int[]> getNeighbors(int nodeId){
        //get node's coordinates
        int x = nodes[nodeId][0];
        int y = nodes[nodeId][1];
        //empty list to collect valid neighbors
        List<int[]> neighbors = new ArrayList<>();

        for(int dx = -1; dx <= 1; dx++){
            for(int dy =-1; dy <= 1; dy++){
                if(dx ==0 && dy ==0) continue; //center = current node

                //get the coordinates of the potential neighbor
                int nx = x + dx;
                int ny = y +dy;

                //stay inside grid
                if(nx >= 0 && nx <size && ny >= 0 && ny <size){
                    int neighborId = nx * size +ny;

                    //no barriers
                    if(!barrierNodes.contains(neighborId)){
                        int cost_type = (dx != 0 && dy != 0) ? 2 : 1;
                        // 1 = straight, 2 =diagonal
                        neighbors.add(new int[]{neighborId, cost_type});
                    }
                }
            }
        }

        //sort by nodeId
        neighbors.sort(Comparator.comparingInt(a -> a[0]));
        return neighbors;
    }

    public void printMaze(){
        System.out.println("\nMAZE SETUP");
        System.out.println("Start  : Node " + startNode +
                " (x=" + nodes[startNode][0] + ", y=" + nodes[startNode][1] + ")");
        System.out.println("Goal   : Node " + goalNode +
                " (x=" + nodes[goalNode][0] + ", y=" + nodes[goalNode][1] + ")");
        System.out.println("Barriers: " + barrierNodes);
    }
}
