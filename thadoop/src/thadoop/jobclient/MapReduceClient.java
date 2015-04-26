package thadoop.jobclient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import thadoop.debug.Debug;
import thadoop.mapreduce.MapReduce;
import thadoop.message.MapReduceMessage;
import thadoop.message.MessageID;

/*
 * This class is used to submit MapReduce code
 * to Job server
 * It uses MapReduceMessage
 */
 
	
public class MapReduceClient {
	
    private InetAddress ipaddress;
    private int portnumber;
    private MapReduce mapreduce;
    
    public MapReduceClient(InetAddress ipaddress,int portnumber, MapReduce mapreduce) {
    	this.ipaddress = ipaddress;
    	this.portnumber = portnumber;
    	this.mapreduce = mapreduce;
    	
    }
    
    public void submitJob() throws IOException {
    	Socket socket = new Socket(ipaddress, portnumber);
        ObjectOutputStream outputStream = null;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        MapReduceMessage mrm = new MapReduceMessage(MessageID.MAPPER_JOB,this.mapreduce);
        outputStream.writeObject(mrm);  
        Debug.message("Message sent to server");
        socket.close();
    }
}
