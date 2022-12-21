package astar;

import java.util.*;
import java.util.Random;

/**
 *
 * @author haleywaddell
 */
class NodeComparator implements Comparator<Node> {

    public int compare(Node node1, Node node2) {
        if (node1.getF() > node2.getF()) {
            return 1;
        } else if (node1.getF() < node2.getF()) {
            return -1;
        }
        return 0;
    }

}
 

public class Environment {

    private int[][] tileDisplay = new int[15][15];
    private boolean valid[][] = new boolean[15][15];
    Random rand = new Random();
    private PriorityQueue<Node> openPQ = new PriorityQueue<Node>(30, new NodeComparator());
    private PriorityQueue<Node> closedPQ = new PriorityQueue<Node>(30, new NodeComparator());

    private Node current;

    // 10% of time ints in display are unpathable marked by (1)'s
    public Environment() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                int possibility = rand.nextInt(10) + 1;

                //if var is 1, the node is unpathable 
                if (possibility == 1) {
                    tileDisplay[i][j] = 1;
                    valid[i][j] = false;
                } else {
                    tileDisplay[i][j] = 0;
                    valid[i][j] = true;
                }
            }
        }

    }

    public void display() {
        //print 0-14 for rows
        System.out.print("  ");
        for (int l = 0; l < 15; l++) {
            if (l < 10) {
                System.out.print(" " + l);
            } else {
                System.out.print(l);
            }

        }
        System.out.println("");

        for (int i = 0; i < 15; i++) {
            if (i < 10) {
                System.out.print(" ");
            }
            // print 0-14 for columns
            System.out.print((i) + " ");
            for (int j = 0; j < 15; j++) {
                System.out.print(tileDisplay[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public void placeCurrent(Node n) {
        current = n;
    }

    public boolean isValid(Node n) {
        // returns true if location is traversable  
        return valid[n.getRow()][n.getCol()];
    }

    public Node[] getNeighbors(Node goal) {
        Node[] tempNode = new Node[4];
        int size = 0;
        /*
                N =  North       (i-1, j) 
                S = South       (i+1, j) 
                E = East        (i, j+1) 
                W = West        (i, j-1) 
         */
        if (current.getCol() + 1 < 15 && tileDisplay[current.getRow()][current.getCol() + 1] == 0) {
            Node east = new Node(current.getRow(), current.getCol() + 1, 0, current);
            east.setH(goal);
            east.setG(current.getG() + 10);
            east.setF();
            tempNode[size] = east;
            size++;
        }
        if (current.getCol() - 1 >= 0 && tileDisplay[current.getRow()][current.getCol() - 1] == 0) {
            Node west = new Node(current.getRow(), current.getCol() - 1, 0, current);
            west.setH(goal);
            west.setG(current.getG() + 10);
            west.setF();
            tempNode[size] = west;
            size++;
        }
        if (current.getRow() + 1 < 15 && tileDisplay[current.getRow() + 1][current.getCol()] == 0) {
            Node south = new Node(current.getRow() + 1, current.getCol(), 0, current);
            south.setH(goal);
            south.setG(current.getG() + 10);
            south.setF();
            tempNode[size] = south;
            size++;
        }
        if (current.getRow() - 1 >= 0 && tileDisplay[current.getRow() - 1][current.getCol()] == 0) {
            Node north = new Node(current.getRow() - 1, current.getCol(), 0, current);
            north.setH(goal);
            north.setG(current.getG() + 10);
            north.setF();
            tempNode[size] = north;
            size++;
        }
        return tempNode;

    }

    public void addOpen(Node node) {
        //check that node is not in openList or closedList
        for (Node each : openPQ) {
            if ((each.getCol() == node.getCol() && each.getRow() == node.getRow()) || tileDisplay[node.getRow()][node.getRow()] == 1) {
                return;
            }
        }
        for (Node each : closedPQ) {
            if (each.getCol() == node.getCol() && each.getRow() == node.getRow()) {
                return;
            }
        }
        openPQ.add(node);
    }

    public Node removeOpen() {
        // this removes the node with lowest F score
        return openPQ.poll();
    }

    public PriorityQueue getOpen() {
        // will return openList, used to trouble shoot problems
        return openPQ;
    }

    public void addClosedPQ(Node node) {
        closedPQ.add(node);
    }

    public void clearOpenClosed() {
        openPQ.clear();
        closedPQ.clear();
    }

    public boolean inOpen(Node check) {
        // checks if the node is in the openList and if not add the node
        // if node is in the openList, check to see if G score can be updated
        for (Node node : openPQ) {
            if (node.getRow() == check.getRow() && node.getCol() == check.getCol() && node.getG() < check.getG()) {
                return true;
            } else if (node.getRow() == check.getRow() && node.getCol() == check.getCol() && node.getG() > check.getG()) {
                //if the node is already present but the G value is lower update this node
                // does not add a new one
                node.setG(current.getG() + 10);
                return true;
            }
        }
        return false;
    }

    public boolean inClosed(Node check) {
        for (Node node : closedPQ) {
            if (node.getRow() == check.getRow() && node.getCol() == check.getCol()) {
                return true;
            }
        }
        return false;
    }

    public Node[] generatePath(Node goal) {
        Node[] r = new Node[30];
        int size = 0;

        Node temp = current;

        if (current == null || current.getParent() == null) {
            return r;
        }
        //adds nodes to r and keeps going unitl you get to the null parent
        while (temp != null) {
            r[size] = temp;
            temp = temp.getParent();
            size++;
        }
        int size2 = size-1;
        // forward node array reverse the r array to print in correct order
        Node [] forward = new Node[30];
        for (int i = 0; i < size; i++) {
            forward[i] = r[size2];
            size2--;
        }
        return forward;
    }
    
    public void printPath(Node[] nodes) {
        System.out.println("\tPath: ");
        
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                System.out.println("\t\t" + nodes[i].toString());
            } 
        }
    }
}