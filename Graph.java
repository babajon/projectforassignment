import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.print.attribute.standard.Finishings;


public class Graph {

	private static Task[] task;
	Queue<Task> qu;
	private int finalTime = 0;
	int slack;

	public Graph(Task[] t) {
		task = t;
		qu = new LinkedList<Task>();
		slack = 0;
	}

	/**
	 * 1 ---inEdge---> 2
	 * 1 <---OutEdge---2
	 */
	public void sort(){

		setEdgeSize();

		checkCycle();

		makeQueue();
		startSchedules();

		makeQueue2();
		setTaskLatestTime();

		findSlack();
		printAll();

		System.out.println("Bye!");
	}

	public void checkCycle(){
		ArrayList<Task> buffer = new ArrayList<Task>();

		int a = 0;
		while(a < task.length){
			deapthSearch(buffer, task[a]);
			a++;
			buffer.clear();
		}
	}

	public void deapthSearch(ArrayList<Task> buffer, Task t){
		t.checkStatus = "visted";

		int b = 0;
		while(b < t.nrOutEdges){

			Task t2 = t.outEdges.get(b);
			String t2status = t.outEdges.get(b).checkStatus;

			if(t2status.equals("notvisted")){
				buffer.add(t2);
				deapthSearch(buffer, t2);

			}else if(t2status == "visted"){
				buffer.add(t2);

				for(int c = 0; c < buffer.size(); c++){

					System.out.println("Cycle on: " + buffer.get(c).getId());
				}
				System.out.println("");
				System.exit(1);
			}

			b++;
		}
		t.checkStatus = "DONE";
	}

	public void makeQueue(){

		for (int a = 0; a<task.length; a++){
			Task t = task[a];

			if(t.inEdges.isEmpty()){
				System.out.println("*Staring with " + t.getId() + " in queue");
				qu.add(t);
				t.setEarliestStart(0);
				t.setEndTime(t.getEarliestStart() + t.getTime());
			}
		}
	}

	/**
	 * Topological Sort
	 */
	public void startSchedules(){

		while (qu.size() != 0) {

			Task t = qu.element();
			System.out.print("Queue: " + qu.element().getId());

			int b = 0;
			if (t.outEdges.isEmpty() == false){
				while(b < t.outEdges.size()){

					// leser utgraden til tasken 
					Task c = t.outEdges.get(b);
					System.out.print(" \t"+t.getId() + " outEdges is " + c.getId() + " ");

					if(c.visit == false){
						c.visit = true;
						c.setEarliestStart(t.getEndTime());
						c.setEndTime(c.getEarliestStart() + c.getTime());

					}else if(c.getEarliestStart() < t.getEndTime()){
						c.setEarliestStart(t.getEndTime());
						c.setEndTime(c.getEarliestStart() + c.getTime());
					}

					//minker inngrad sin Predecessors
					c.decCntPredecessors();

					if(c.getCntPredecessors() == 0){
						qu.add(c);
					}
					if(finalTime < c.getEndTime()){
						finalTime = c.getEndTime();
					}
					b++;
				}

			}
			t.finshed = true;
			qu.remove();	
			System.out.println();
		}
	}
	
	public void makeQueue2(){

		for (int a = 0; a<task.length; a++){
			Task t = task[a];

			if(t.outEdges.isEmpty()){
				System.out.println("*Staring with " + t.getId() + " in queue");
				qu.add(t);
				t.setEndTime(t.getEarliestStart() + t.getTime());
				t.setLatestStart(finalTime-t.getTime());
			} 
		}
	}

	public void setTaskLatestTime(){

		while (qu.size() != 0){

			Task t = qu.element();

			if(t.outEdges.isEmpty() || t.nrOutEdges == 0) {

				for(int b = 0; b<t.inEdges.size(); b++){

					Task t2 = t.inEdges.get(b);

					//System.out.println("ID: " + t2.getId() + "early " + t.getEarliestStart() + " time " + t2.getTime());

					t2.setLatestStart(t.getEarliestStart()-t2.getTime());
					t2.nrOutEdges--;

					if(t2.nrOutEdges == 0){
						qu.add(t2);
					}
					//System.out.println("Task: " + t.getId() + " LastetTime: " + latestTime + " ->>" + t2.getId());
					//System.out.print("   indreTask " + t2.getId()+ " LatestTime: " + t2.getLatestStart() + "\n");
				}
				//System.out.println();
			}
			qu.remove();	
			//	System.out.println();
		}
	}

	public void findSlack(){

		int a = 0;
		while (a < task.length){
			Task t = task[a];
			//System.out.println(" id " + t.getId() + " earliest " + t.getEarliestStart() + " latest " + t.getLatestStart());
			slack = t.getLatestStart() - t.getEarliestStart();
			System.out.println("Task " + t.getId() + " slack is " + slack);

			a++;
			slack = 0;
		}
	}

	public void setEdgeSize(){

		for(int a = 0; a<task.length; a++){
			task[a].nrInEdges = task[a].inEdges.size();
			task[a].nrOutEdges = task[a].outEdges.size();
		}
	}
	
	public void printAll(){
		System.out.println("");
		System.out.println();

		int manPower = 0;
		int time = 0;
		boolean writeTime = false;
		while(time <= finalTime){

			for(int a = 0; a<task.length; a++){
				Task t = task[a];

				if(t.getEarliestStart() == time){
					if(writeTime){
						System.out.println("Time: "+ time);
						writeTime = false;
					}
					manPower += t.getStaff();
					System.out.println("Started: " +t.getId() + "");
					System.out.println("Total Staff: " + manPower + "\n");
				}else if(t.getEndTime() == time){
					if(writeTime){
						System.out.println("Time: "+ time);
						writeTime = false;
					}
					manPower -= t.getStaff();
					System.out.println("Finished: " +t.getId() + "");
					System.out.println("Total Staff: " + manPower + "\n");
				}
			}
			writeTime = true;
			time++;
		}
		System.out.println("**** Shortest possible project execution is "+ finalTime + " ****");
	}
	
	public void printPredecessors(){
		System.out.println("Printing Predecessors");

		for(int a = 0; a < task.length; a++){
			int aa = task[a].getCntPredecessors();
			System.out.println("Task " + task[a].getId() + " have " + aa + " predecessors");
		}

	}
	
	public void printInEdges(){
		System.out.println("InEdges");
		for(int a = 0; a < task.length; a++){
			System.out.print(task[a].getId() + " " + task[a].getName());
			for(int b = 0; b < task[a].inEdges.size(); b++){
				System.out.print(" " + task[a].inEdges.get(b).getId());

			}
			System.out.println();
		}
		System.out.println("\n");
	}
	
	public void printOutEdges(){
		System.out.println("OutEdges");
		for(int a = 0; a < task.length; a++){
			System.out.print(task[a].getId() + " " + task[a].getName());
			for(int b = 0; b < task[a].outEdges.size(); b++){
				System.out.print(" " + task[a].outEdges.get(b).getId());

			}
			System.out.println();
		}
		System.out.println("\n");
	}
	
	public void printTaskStaff(){

		for(int a = 0; a < task.length; a++){
			Task t = task[a];
			System.out.println("Task " + t.getId() + " staff is " + t.getStaff());
		}
		System.out.println();
	}
	
	public void printLatestTime(){

		for(int a = 0; a < task.length; a++){
			Task t = task[a];
			System.out.println("Task " + t.getId() + " latest time is " + t.getLatestStart());
		}
		System.out.println();
	}

}