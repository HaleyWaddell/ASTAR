package astar;

import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author haleywaddell
 */
public class AStar {

    public static void main(String[] args) {

        Environment env = new Environment();

        env.display();
        
        // lets user enter start/goal as many times as they want
        while (true) {
            Scanner input = new Scanner(System.in);
            
            //  exit program by entering something other than y or Y
            // y or Y enters program
            System.out.print("Continue (Y/N): ");
            String cont = input.nextLine();
            if(!cont.equalsIgnoreCase("y")) {
                break;
            }
            
            // selection for row/column for start and goal node
            
            System.out.println("Select Start Node (0 - 14): ");
            System.out.print("\tPick row: ");

            int row = input.nextInt();

            System.out.print("\tPick Coloumn: ");
            int col = input.nextInt();
            Node start = new Node(row, col, 0);

            // adds start to open
            env.addOpen(start);

            System.out.println("Select Goal Node (0 - 14): ");
            System.out.print("\tPick row: ");
            row = input.nextInt();
            System.out.print("\tPick Coloumn: ");
            col = input.nextInt();

            Node goal = new Node(row, col, 0);

            // MAIN loop for A*
            while (true) {
                
                //check if openList still has nodes
                if (!env.getOpen().isEmpty()) {
                    
                    // remove node with lowest F
                    Node current = env.removeOpen();
                    env.placeCurrent(current);
                    
                    // check if current equals goal and generate the path/solution
                    if (current.equals(goal)) {
                        System.out.println("********Solution found!***********");

//                        e.printList(e.generatePath(current), start);
//                        System.out.println(e.generatePath(current));
                        env.printPath(env.generatePath(goal));
//                        System.out.println("\t\tGoal Node: " + "[" + goal.getRow() + ", " + goal.getCol() + "]");
                        
                        // clear openList and closedList to start a new search
                        env.clearOpenClosed();
                        break;
                        
                    } else {
                        
                        // this will continually generate neighbor nodes based on the current node
                        Node neighbors[] = env.getNeighbors(goal);
                        for (Node n : neighbors) {
                            //check for null to avoid exception
                            if (n == null) {
                                break;
                            } else if (!env.inClosed(n) && env.isValid(n) && !env.inOpen(n)) {
                                env.addOpen(n);
                            }
                        }
                        //e.printOpen();
                    }
                    // add current to closed after visit is concluded
                    env.addClosedPQ(current);

                } else {
                    System.out.println("Try again. No path could be found");
                    break;
                }
            }
        }
    }
}