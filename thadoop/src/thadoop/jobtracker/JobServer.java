package thadoop.jobtracker;

import thadoop.debug.Debug;
import thadoop.server.Server;
public class JobServer{
	
	private JobServerThread jobServerThread;
	
	public JobServer(int portNumber){
		this.jobServerThread = new JobServerThread(portNumber);
	}
	
	public boolean startJobServer(){
		Debug.infoMessage("starting job server");
		if(jobServerThread.isAlive()){
			Debug.infoMessage("job server is already running");
			return false;
		}
		this.jobServerThread.start();
		Debug.infoMessage("job server started");
		return true;
	}
	public void stopServer(){
		Debug.infoMessage("Stopping Server....");
		if(!jobServerThread.isAlive()){
			Debug.infoMessage("Server is already stopped ");
			return;
		}
		this.jobServerThread.stopMe();
	}
	public void isServerAlive(){
		if(this.jobServerThread.isAlive()){
			Debug.infoMessage("Yes");
			
		}else{
			Debug.infoMessage("No");
		}
	}
}
