package thadoop.tasktracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import thadoop.message.JobMessage;
import thadoop.message.Message;
import thadoop.message.MessageID;
import thadoop.s3.FileMetaData;

public class ProcessThread extends Thread {
	
	ConcurrentHashMap<Long, Integer> taskStatus;
	private List<String> fileNames;
	private Message message;
	JobMessage jobMessage;
		
	ProcessThread(Message message, ConcurrentHashMap<Long, Integer> taskStatus)
	{
		this.message= message;
		this.taskStatus=taskStatus;
	}
	
	private void processMessage()
	{
		switch(message.messageID)
		{
		case MAPPER_JOB:
			jobMessage = (JobMessage)this.message;
			this.taskStatus.put(jobMessage.getTaskId(), 0);// 0 = job initialised
			new MapperThread(jobMessage, taskStatus).start();
			break;
		case REDUCER_JOB:
			jobMessage = (JobMessage)this.message;
			this.taskStatus.put(jobMessage.getTaskId(), 0);// 0 = job initialised
			new ReducerThread(jobMessage, taskStatus).start();
			break;
		case MAPREDUCE_JOB:
			break;
		default:
			break;
		}		
	}
	
	public void run()
	{
	
		processMessage();
	}

	
	
}
