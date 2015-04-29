package thadoop.tasktracker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import thadoop.debug.Debug;
import thadoop.message.JobMessage;
import thadoop.message.Message;
import thadoop.server.Server;

public class TaskServerThread extends Server{
	private int portNumber;
	private boolean stopFlag;
	private ServerSocket serverSocket;
	private InetAddress myIp;
	ConcurrentHashMap<Long, Integer> taskStatus;
	
 public TaskServerThread(int portNumber){
		this.portNumber = portNumber;
		this.stopFlag = false;
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
			serverSocket = new ServerSocket(this.portNumber,50,myIp);
			Debug.infoMessage("Server started at "+this.myIp.getHostName() + "at port " +this.portNumber);
			while(!stopFlag){
				Debug.debug("Server starts and listening for clients to connect");
				Socket clientSocket = serverSocket.accept();
				ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());
				Message message = (JobMessage) is.readObject();
				Debug.debug("a message received at task server");	
				new ProcessThread(message,taskStatus).start();
			}
		}catch (SocketTimeoutException e){	
			Debug.errorMessage("error occured while starting the server");
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			Debug.errorMessage("error occured while starting the server");
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