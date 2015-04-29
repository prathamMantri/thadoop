package thadoop.tasktracker;

import thadoop.debug.Debug;
import thadoop.jobtracker.JobServerThread;
import thadoop.server.Server;

public class TaskServer{
	
private TaskServerThread taskServerThread;
	
	public TaskServer(int portNumber){
		this.taskServerThread = new TaskServerThread(portNumber);
	}
	
	public boolean startTaskServer(){
		Debug.infoMessage("starting task server");
		if(taskServerThread.isAlive()){
			Debug.infoMessage("task server is already running");
			return false;
		}
		this.taskServerThread.start();
		Debug.infoMessage("task server started");
		return true;
	}
	public void stopServer(){
		Debug.infoMessage("Stopping Server....");
		if(!taskServerThread.isAlive()){
			Debug.infoMessage("Server is already stopped ");
			return;
		}
		this.taskServerThread.stopMe();
	}
	public void isServerAlive(){
		if(this.taskServerThread.isAlive()){
			Debug.infoMessage("Yes");
			
		}else{
			Debug.infoMessage("No");
		}
	}
}
