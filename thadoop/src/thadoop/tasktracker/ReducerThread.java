package thadoop.tasktracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import thadoop.mapreduce.MapReduce;
import thadoop.message.JobMessage;
import thadoop.message.Message;
import thadoop.message.MessageID;
import thadoop.s3.FileMetaData;

public class ReducerThread extends Thread {
	private MapReduce mapReduce;
	private List<FileMetaData> fileMetaData;
	private ConcurrentHashMap<Long, Integer> taskStatus;
	private long taskID;
	private long jobID;
	private long noOfReducers;
	
	
	 
	 ReducerThread(JobMessage jobMessage, ConcurrentHashMap<Long, Integer> taskStatus) {
			this.mapReduce= jobMessage.getMapReduceMessage().getMapreduce();
			this.fileMetaData=jobMessage.getFileMetaData();
			this.taskStatus=taskStatus;
			this.taskID=jobMessage.getTaskId();
			this.jobID=jobMessage.getJobId();
			this.noOfReducers=jobMessage.getNoOfReducers();
	}
		
	
	public void run()
	{
		runReducer();
	}
	
	private void runReducer()

	{
		for(FileMetaData fm : this.fileMetaData)
		{
		//s	mapReduce.reducer();
			
			this.taskStatus.put(this.taskID,1);// 0 = job initialised
		}	
	}
}
