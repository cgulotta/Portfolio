package cmsc433.p3;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * An efficient single-threaded depth-first solver.
 */
public class ExtendedDFS extends SkippingMazeSolver
{
	Position start, end;
	boolean change_direction = false;
	
    public ExtendedDFS(Maze maze,int i)
    {
        super(maze);
        if(i == 0 || i == 2){
        	start = maze.getStart();
        	end = maze.getEnd();
        	System.out.println("Forward");
        } else if (i == 1 || i == 3) {
        	start = maze.getEnd();
        	end = maze.getStart();
        	System.out.println("Reverse");
        }
        if(i == 2 || i == 3){
        	change_direction = true;
        	System.out.println("Changed Direction: "+change_direction);
        }
    }

    /**
     * Performs a depth-first search for the exit. The solver operates by
     * maintaining a stack of choices. During each iteration, the choice at the
     * top of the stack is examined. If choice.isEmpty() is true, then we have
     * reached a dead-end and must backtrack by popping the stack. If the choice
     * is not empty, then we proceed down the first path in the list of options.
     * If the exit is encountered, then SolutionFound is thrown and we generate
     * the solution path, which we return. At any given point in the execution,
     * the list of first choices yields the current path. That is, if the choice
     * stack is:
     * 
     * <pre>
     * [[E W S] [E W] [S N] [N]]
     * </pre>
     * 
     * Then the current path is given by the list:
     * 
     * <pre>
     * [E E S N]
     * </pre>
     */
    public List<Direction> solve()
    {
        LinkedList<Choice> choiceStack = new LinkedList<Choice>();
        Choice ch;
        
        try
        {
            choiceStack.push(firstChoice(start));
            while (!choiceStack.isEmpty())
            {
                ch = choiceStack.peek();
                if (ch.isDeadend())
                {
                    // backtrack.
                    choiceStack.pop();
                    if (!choiceStack.isEmpty()) {
                    		if(change_direction){
                    			choiceStack.peek().choices.removeLast();
                    		}else{
                    			choiceStack.peek().choices.pop();
                    		}
                    }
                    continue;
                }
                if(change_direction){
                choiceStack.push(follow(ch.at, ch.choices.peekLast()));
                }else{
                choiceStack.push(follow(ch.at, ch.choices.peek()));
                }
            }
            // No solution found.
            return null;
        }
        catch (SolutionFound e)
        {
            Iterator<Choice> iter = choiceStack.iterator();
            LinkedList<Direction> solutionPath = new LinkedList<Direction>();
            while (iter.hasNext())
            {
            	ch = iter.next();
                solutionPath.push(ch.choices.peek());
            }

            if (maze.display != null) maze.display.updateDisplay();
            return pathToFullPath2(solutionPath);
        }
    }
    
    private List<Direction> pathToFullPath2(List<Direction> path)
    {
        Iterator<Direction> pathIter = path.iterator();
        LinkedList<Direction> fullPath = new LinkedList<Direction>();

        // Get full solution path.
        Position curr = maze.getStart();
        Direction go_to = null, came_from = null;
        while (!curr.equals(maze.getEnd()))
        {
            LinkedList<Direction> moves = maze.getMoves(curr);
            moves.remove(came_from);
            if (moves.size() == 1) go_to = moves.getFirst();
            else if (moves.size() > 1) go_to = pathIter.next();
            else if (moves.size() == 0)
            {
                return null;
            }
            fullPath.add(go_to);
            curr = curr.move(go_to);
            came_from = go_to.reverse();
        }

        return fullPath;
    }
}
