package thadoop.jobtracker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import thadoop.debug.Debug;
import thadoop.message.Message;
import thadoop.server.Server;

public class JobServerThread extends Server{
	private int portNumber;
	private boolean stopFlag;
	private ServerSocket serverSocket;
	private InetAddress myIp;
	
	JobServerThread(int portNumber){
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
			serverSocket = new ServerSocket(portNumber,50,myIp);
			Debug.infoMessage("Server started at "+this.myIp.getHostName() + "at port" +this.portNumber);
			while(!stopFlag){
				Socket clientSocket = serverSocket.accept();
				ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());
				Message message = (Message) is.readObject();
				Debug.debug("a message received at job server");
				switch(message.messageID){
					//start a thread
				}	
				//(new ProcessThread(clientSocket,this.fileList)).start();
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
