package thadoop.jobclient;

import java.io.IOException;
import java.io.ObjectInputStream;
	
import java.io.ObjectOutputStream;
	
import java.net.InetAddress;
import java.net.Socket;
	
import java.net.SocketException;
import java.net.UnknownHostException;

import thadoop.mapreduce.MapReduce;
import thadoop.message.MapReduceMessage;
import thadoop.message.MessageID;
	
 
	
public class MapReduceClient {
	
    private Socket socket = null;
    private InetAddress ipaddress;
    private int portnumber;
	

    private boolean isConnected = false;
    MapReduce mapreduce;
	
 
	
    public MapReduceClient(InetAddress ipaddress,int portnumber, MapReduce mapreduce) {
    	
    	this.ipaddress = ipaddress;
    	this.portnumber = portnumber;
    	this.mapreduce = mapreduce;
    	
    	
    }
    public void communicate() {
	
         ObjectInputStream inputStream = null;
    	
         ObjectOutputStream outputStream = null;
    	
    	//System.out.println("I am in client's comunicate");
    	while (!isConnected) {
	
            try {
            	
	
                socket = new Socket(ipaddress, portnumber);
	
               // System.out.println("Connected");
	
                isConnected = true;
	
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                //mapreduce = new MapReduce();
                
                MapReduceMessage mrmsg = new MapReduceMessage(MessageID.MAPREDUCE_JOB, mapreduce);
                
                System.out.println("Object to be written = " + mrmsg.getMapreduce().mapper());
                System.out.println("Object to be written = " + mrmsg.getId());
	
                outputStream.writeObject(mrmsg);
	
 
	
 
	
            } catch (SocketException se) {
	
                se.printStackTrace();
	
                // System.exit(0);
	
            } catch (IOException e) {
	
                e.printStackTrace();
	
            }
	
        }
	
    }
	
 
	
    public static void main(String[] args) throws UnknownHostException {
    	
    	MapReduce mapreduce = new MapReduce();
    	InetAddress Address = InetAddress.getLocalHost();
        MapReduceClient mrclient = new MapReduceClient(Address, 4445, mapreduce);
	
        mrclient.communicate();
	
    }
	
}
