import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

class Node {
    private int x;
    private int y;
    private int distance;
    private Node predecessor;
    private List<Node> neighborNodes;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.distance = -1;
        this.predecessor = null;
        this.neighborNodes = new ArrayList<>();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int newDistance) {
        this.distance = newDistance;
    }

    public Node getPredecessor() {
        return this.predecessor;
    }

    public void setPredecessor(Node newPredecessor) {
        this.predecessor = newPredecessor;
    }

    public void addNeighborNode(Node newNeighborNode) {
        this.neighborNodes.add(newNeighborNode);
    }

    public List<Node> getNeighborNodes() {
        return this.neighborNodes;
    }

    @Override
    public String toString() {
        String output = String.format("Node[%d, %d] -> (distance:%d, predecessor:", this.getX(), this.getY(),
                this.getDistance());

        if (this.predecessor != null) {
            output += String.format("Node[%d, %d]", this.predecessor.getX(), this.predecessor.getY());
        } else {
            output += "null";
        }

        return output;
    }
}

class KnightsTravails {

    private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final int[][] knightMovements = { { 2, -1 }, { 1, -2 }, { -1, -2 }, { -2, -1 }, { -2, 1 }, { -1, 2 },
            { 1, 2 }, { 2, 1 } };

    private Node[][] nodeList;

    public KnightsTravails() {
        this.nodeList = this.createNodelist(false);
    }

    public Node[][] getNodelist() {
        return this.nodeList;
    }

    private boolean isValidMovement(int rowX, int colY) {
        return (rowX >= 0 && rowX < ROWS && colY >= 0 && colY < COLS);
    }

    // Create adjacency list
    private Node[][] createNodelist(boolean visualize) {
        Node[][] nodeList = new Node[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                nodeList[i][j] = new Node(i, j);
            }
        }

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Node node = nodeList[i][j];
                for (int[] movement : knightMovements) {
                    int newRowX = i + movement[0];
                    int newColY = j + movement[1];
                    if (this.isValidMovement(newRowX, newColY)) {
                        node.addNeighborNode(nodeList[newRowX][newColY]);
                    }
                }
            }
        }
        if (visualize) {
            String output = "";
            for (Node[] nodeRow : nodeList) {
                for (Node node : nodeRow) {
                    output += String.format("[%d,%d], Neighbors: ", node.getX(), node.getY());
                    for (Node neighborNode : node.getNeighborNodes()) {
                        output += String.format("[%d,%d], ", neighborNode.getX(),
                                neighborNode.getY());
                    }
                    output += "\n\n";
                }
            }
            System.out.println(output);
        }

        return nodeList;
    }

    // Create BFS function
    public List<Node> doBFS(int sourceRowX, int sourceColY, int destRowX, int destColY) {
        List<Node> pathArray = new ArrayList<>();
        Deque<Node> queue = new ArrayDeque<>();

        this.nodeList[sourceRowX][sourceColY].setDistance(0);
        queue.push(this.nodeList[sourceRowX][sourceColY]);

        while (!queue.isEmpty()) {
            Node currentNode = queue.pollFirst();

            // Use BFS function to find shortest path from source to target
            if (currentNode.getX() == destRowX && currentNode.getY() == destColY) {
                pathArray.add(currentNode);
                while (currentNode.getPredecessor() != null) {
                    pathArray.add(currentNode.getPredecessor());
                    currentNode = currentNode.getPredecessor();
                }
                Collections.reverse(pathArray);
                return pathArray;
            }

            for (Node neighborNode : currentNode.getNeighborNodes()) {
                if (neighborNode.getDistance() == -1) {
                    neighborNode.setDistance(currentNode.getDistance() + 1);
                    neighborNode.setPredecessor(currentNode);
                    queue.addLast(neighborNode);
                }
            }
        }
        return pathArray;
    }

    public static void main(String[] args) {

        KnightsTravails knightsTravails = new KnightsTravails();
        List<Node> solution = knightsTravails.doBFS(0, 0, 7, 7);

        // Print shortest path
        System.out.println(String.format("You made it in %d moves! Here's your path:", solution.size() - 1));
        for (Node step : solution) {
            System.out.println(step);
        }
    }
}
