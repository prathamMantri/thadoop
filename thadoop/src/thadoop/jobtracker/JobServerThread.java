package thadoop.jobtracker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import thadoop.debug.Debug;
import thadoop.message.JobMessage;
import thadoop.message.Message;
import thadoop.server.Server;

public class JobServerThread extends Server{
	private int portNumber;
	private boolean stopFlag;
	private ServerSocket serverSocket;
	private InetAddress myIp;
	private long jobId;
	private Map<Long,ExecMapReduce> mapReduceObj;
	
	JobServerThread(int portNumber){
		this.portNumber = portNumber;
		this.stopFlag = false;
		this.jobId = 1;
		this.mapReduceObj = new HashMap<Long,ExecMapReduce>();
	}
	
	/*
	 * stopMe used to stop the server
	 */
	public void stopMe(){
		this.stopFlag = true;
		try {
			
			Socket clientSocket = new Socket(myIp, portNumber);
			 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			 out.println("DUMMY");
			 out.close();
			 clientSocket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	/*
	 * startMe used to start the server
	 */
	public void startMe() throws ClassNotFoundException{
		try {
			this.myIp  = InetAddress.getLocalHost();
			serverSocket = new ServerSocket(portNumber,50,myIp);
			Debug.infoMessage("Server started at "+this.myIp.getHostName() + "at port" +this.portNumber);
			while(!stopFlag){
				Socket clientSocket = serverSocket.accept();
				ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());
				Message message = (Message) is.readObject();
				Debug.debug("a message received at job server");
				switch(message.messageID){
					//start a thread
				case MAPREDUCE_JOB:
					this.jobId++;
					ExecMapReduce mr = new ExecMapReduce(this.jobId,message);
					this.mapReduceObj.put(this.jobId, mr);
					mr.processJob();
					break;
				case MAPPER_JOB_REPLY:
					JobMessage jmm = (JobMessage) message;
					ExecMapReduce emm = this.mapReduceObj.get(jmm.getJobId());
					emm.updateTaskStatusForMappers(jmm.getTaskId());
					break;
				case REDUCER_JOB_REPLY:
					JobMessage jmr = (JobMessage) message;
					ExecMapReduce emr = this.mapReduceObj.get(jmr.getJobId());
					emr.updateTaskStatusForMappers(jmr.getTaskId());
					break;
				default:
					break;
				}	
			}
		}catch (SocketTimeoutException e){
			
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void run(){
		try {
			this.startMe();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
