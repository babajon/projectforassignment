import java.util.LinkedList;


public class Task {

	private int id , time , staff ;
	private String name ;
	private int earliestStart , latestStart ;
	private int endTime;
	private int cntPredecessors ;
	int nrOutEdges = 0;
	int nrInEdges = 0;
	boolean finshed;
	boolean visit;
	String checkStatus;
	LinkedList<Task> outEdges;
	LinkedList<Task> inEdges;
	
	Task(int id){
		this.id = id;
		outEdges = new LinkedList<Task>();
		inEdges = new LinkedList<Task>();
		finshed = false;
		visit = false;
		latestStart = Integer.MAX_VALUE;

		checkStatus = "notvisted";
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public void setTime(int time) {
		this.time = time;
	}

	public int getTime() {
		return time;
	}
	
	public void setStaff(int staff) {
		this.staff = staff;
	}

	public int getStaff() {
		return staff;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLatestStart(int latestStart) {
		if(this.latestStart > latestStart)
			this.latestStart = latestStart;
		//System.out.println("ID " + this.id + " Before " + this.latestStart + " newlatest " + latestStart);
	}

	public int getLatestStart() {
		return latestStart;
	}

	public void setEarliestStart(int earliestStart) {
		this.earliestStart = earliestStart;
	}

	public int getEarliestStart() {
		return earliestStart;
	}

	public void decCntPredecessors(){
		cntPredecessors--;
	}
	
	public void setCntPredecessors() {
		cntPredecessors++;
	}

	public void setCntPredecessors(int cntPredecessors) {
		this.cntPredecessors = cntPredecessors;
	}

	public int getCntPredecessors() {
		return cntPredecessors;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getEndTime() {
		return endTime;
	}	
}
