package cmsc433.p3;


import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This file needs to hold your solver to be tested. 
 * You can alter the class to extend any class that extends MazeSolver.
 * It must have a constructor that takes in a Maze.
 * It must have a solve() method that returns the datatype List<Direction>
 *   which will either be a reference to a list of steps to take or will
 *   be null if the maze cannot be solved.
 */
public class StudentMTMazeSolver extends SkippingMazeSolver
{
	private int nWorkers;
	private int workload;
	private int mazeH = maze.height,mazeA=maze.height*maze.width;
	private ExecutorService exec;
	private CountDownLatch latch;
	LinkedList<Direction> answer;
	MazeSolver finisher = new STMazeSolverDFS(maze);
	
	/********************************DEAD END FILLING******************************
	 *   1. Create Executor Service
	 *   2. Divide workload
	 *   3. submit tasks to executor
	 *   4.    tasks find and fill dead ends
	 *   5. DFS from start to finish */

	public StudentMTMazeSolver(Maze maze)
	{
		super(maze);
		nWorkers = (mazeA/125000)+1;
		if(nWorkers > Runtime.getRuntime().availableProcessors()){
			nWorkers = Runtime.getRuntime().availableProcessors();
		}
		workload = mazeH/nWorkers;
		exec = Executors.newFixedThreadPool(nWorkers);
		latch = new CountDownLatch(nWorkers);
		
		for(int k = 0; k < nWorkers-1; k++){
			DeadEndFillingTask currentTask = new DeadEndFillingTask(k*workload,(k+1)*workload,maze,latch);
			//submit tasks
			exec.submit(currentTask);
		}
		DeadEndFillingTask currentTask = new DeadEndFillingTask((nWorkers-1)*workload,mazeH,maze,latch);
		currentTask.run();
		exec.shutdown();

		try {
			latch.await();
		}
		catch (InterruptedException e1) {System.out.println("Interrupted at reduction!");}
	}

	

	public List<Direction> solve()
	{
		//Solve!	
		return finisher.solve();

	}
	
//	private class Solver extends Thread{
//		private ExtendedDFS solver;
//		public Solver(int i){
//			solver = new ExtendedDFS(maze,i);
//		}
//		public void run(){
//			answer = (LinkedList<Direction>) solver.solve();
//			latch.countDown();
//		}
//	}
}
