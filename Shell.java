import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;

public class Shell {
	int nrOftasks = 0;
	int taskID = 0;
	String taskName = "";
	int taskTime = 0;
	int manpowerReq = 0;
	int predecessors = 0;
	private static Task[] task;

	void readFromFile(String filename){
		int count = 0;
		System.out.println("Reading file " + filename);

		File openfile = new File(filename);
		Scanner scan;

		try {
			System.out.println();
			scan = new Scanner(openfile);
			//System.out.println(scan.hasNextLine());

			nrOftasks = scan.nextInt();
			//System.out.println("Nr of tasks read-> " + nrOftasks);

			task = new Task[nrOftasks];
			for(int a = 0; a<nrOftasks; a++){
				int b = a + 1;
				task[a] = new Task(b);
			}

			while(scan.hasNextInt()) {

				taskID = scan.nextInt();
				taskID -= 1;
				//System.out.print(taskID+1);
				//	task[taskID].setId(taskID);

				taskName = scan.next();
				//System.out.print("\t" +taskName);
				task[taskID].setName(taskName);

				taskTime = scan.nextInt();
				//System.out.print(" " +taskTime);
				task[taskID].setTime(taskTime);

				manpowerReq = scan.nextInt();
				//System.out.print(" " +manpowerReq +"\t");
				task[taskID].setStaff(manpowerReq);

				int  a = scan.nextInt();
				
				while(a != 0){

					Task t = task[a-1];
					Task t2 = task[taskID];
					
					t.outEdges.add(t2);
					t2.inEdges.add(t);
					t2.setCntPredecessors();
					
					a = scan.nextInt();
				}
				
				a = 0;
				
				//System.out.print("\n");
				count++;
			}
		}
		catch(FileNotFoundException e) {
			System.out.println("Could not find file " + e);
		}
		Graph graph = new Graph(task);
		graph.sort();
	}


	void saveToFile(){
		File file = new File("output.txt");

		try {
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter f2 = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(f2);
			bw.write("Salik dar Oblig 1 Inf2220 \n Statistikk : \n");
			bw.write("depth er  : ");
			bw.write("\n minste er : ");
			bw.write("\n storste er : ");
			bw.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("Could not find file " + e);
		}
		catch (IOException e2 ){
			e2.printStackTrace();
		}
	}

}


