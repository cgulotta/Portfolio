package cmsc433.p3;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DeadEndFillingTask extends SkippingMazeSolver implements Runnable{

	int start, end, mazeW = maze.width;
	Position mazeStart = maze.getStart(), mazeEnd = maze.getEnd();
	private final CountDownLatch latch;

	long Tstart, Tend, T;

	public DeadEndFillingTask(int start, int end ,Maze maze, CountDownLatch latch){
		super(maze);
		this.start = start;
		this.end = end;
		this.latch = latch;
	}

	public void run() {
		//fill from each position
		Boolean unrollable = ((end - start)%2==0)&&(mazeW%2==0);
		//System.out.println("Unrollable: "+ unrollable);
		if(unrollable){
			for(int h = start; h < end; h+=2){
				for(int w = 0; w < mazeW; w+=2){
					Position current = new Position(w,h);
					if ((maze.getMoves(current).size() == 1) && !current.equals(mazeStart) &&!current.equals(mazeEnd)){
						cut(follow(current,maze.getMoves(current).getFirst()));
					}
					current = new Position(w+1,h);
					if ((maze.getMoves(current).size() == 1) && !current.equals(mazeStart) &&!current.equals(mazeEnd)){
						cut(follow(current,maze.getMoves(current).getFirst()));
					}
					current = new Position(w,h+1);
					if ((maze.getMoves(current).size() == 1) && !current.equals(mazeStart) &&!current.equals(mazeEnd)){
						cut(follow(current,maze.getMoves(current).getFirst()));
					}
					current = new Position(w+1,h+1);
					if ((maze.getMoves(current).size() == 1) && !current.equals(mazeStart) &&!current.equals(mazeEnd)){
						cut(follow(current,maze.getMoves(current).getFirst()));
					}
				}
			}
		}else{
			for(int h = start; h < end; h++){
				for(int w = 0; w < mazeW; w++){
					Position current = new Position(w,h);
					if ((maze.getMoves(current).size() == 1) && !current.equals(mazeStart) &&!current.equals(mazeEnd)){
						cut(follow(current,maze.getMoves(current).getFirst()));
					}
				}
			}
		}
		latch.countDown();
	}

	private void cut(Choice ch) {

		if(ch.from == Direction.EAST){
			maze.setEast(ch.at);
		}if(ch.from == Direction.SOUTH){
			maze.setSouth(ch.at);
		}if(ch.from == Direction.WEST){
			maze.setEast(new Position(ch.at.col-1,ch.at.row));
		}if(ch.from == Direction.NORTH){
			maze.setSouth(new Position(ch.at.col,ch.at.row-1));}
	}

	public Choice follow(Position at, Direction dir)
	{
		LinkedList<Direction> choices;
		Direction go_to = dir, came_from = dir.reverse();
		//maze.setColor(at, 1);
		at = at.move(go_to);
		do{
			//maze.setColor(at, 1);
			if (at.equals(maze.getEnd())) return new Choice(at,go_to.reverse(),null);
			if (at.equals(maze.getStart())) return new Choice(at,go_to.reverse(),null);
			choices = maze.getMoves(at);
			choices.remove(came_from);

			if (choices.size() == 1){
				go_to = choices.getFirst();
				at = at.move(go_to);
				came_from = go_to.reverse();
			}
		} while (choices.size() == 1);

		// return new Choice(at,choices);
		//maze.setColor(at, 0);
		Choice ret = new Choice(at, came_from, choices);
		return ret;
	}

	public List<Direction> solve() {
		return null;
	}


}

