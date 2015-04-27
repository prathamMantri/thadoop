package thadoop.jobtracker;

import java.util.concurrent.ConcurrentHashMap;

import thadoop.message.MapReduceMessage;

public class MapReduceThread extends Thread{
	
	private ConcurrentHashMap<Long,Integer> jobStatus;
	private MapReduceMessage mapReduceMessage;
	
	public MapReduceThread(ConcurrentHashMap<Long,Integer> jobStatus,MapReduceMessage mapReduceMessage){
		this.jobStatus = jobStatus;
		this.mapReduceMessage = mapReduceMessage;
	}
	
}
