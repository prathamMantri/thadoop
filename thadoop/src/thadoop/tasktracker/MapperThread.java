package thadoop.tasktracker;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;

import thadoop.debug.Debug;
import thadoop.mapreduce.MapReduce;
import thadoop.message.JobMessage;
import thadoop.message.Message;
import thadoop.message.MessageID;
import thadoop.s3.FileMetaData;
import thadoop.s3.S3FileSystem;

public class MapperThread extends Thread {

	private MapReduce mapReduce;
	private List<FileMetaData> fileMetaData;
	private ConcurrentHashMap<Long, Integer> taskStatus;
	private long taskID;
	private long jobID;
	private long noOfReducers;
	S3FileSystem s3FileSystem = new S3FileSystem("testpm");
	

	MapperThread(JobMessage jobMessage, ConcurrentHashMap<Long, Integer> taskStatus) {
		this.mapReduce= jobMessage.getMapReduceMessage().getMapreduce();
		this.fileMetaData=jobMessage.getFileMetaData();
		this.taskStatus=taskStatus;
		this.taskID=jobMessage.getTaskId();
		this.jobID=jobMessage.getJobId();
		this.noOfReducers=jobMessage.getNoOfReducers();
	}

	public void run()
	{
		try {
		List<Map<String, Integer>> mapperOutput = runMapper();
		Map<Integer,List<String>> filesInBucket = putFilesToBucket(mapperOutput);
		this.writeFiles(filesInBucket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Map<Integer, String> readFileFromS3(FileMetaData fileMetaData) throws IOException{

		Map<Integer, String> fileData = new HashMap<Integer, String>();
		int i=0;
		InputStream is =s3FileSystem.getFile(fileMetaData);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
        String line;
        while ((line = reader.readLine()) != null) {
        	fileData.put(i, line);
        	i++;      	
        }
		return fileData;
	} 

	private List<Map<String, Integer>> runMapper() throws IOException

	{
		
		List<Map<String, Integer>> mapperOutput = new ArrayList<Map<String,Integer>>();
		for(FileMetaData fm : this.fileMetaData)
		{
			mapperOutput.add(mapReduce.mapper(readFileFromS3(fm)));// Return list of hashmap	
		}	

		this.taskStatus.put(this.taskID,1);// 0 = job initialised

		return mapperOutput;
	}

	private String formString(String key, Integer value){
		return key+","+value;
	}
	private Map<Integer,List<String>> putFilesToBucket(List<Map<String, Integer>> mapperOutput ) throws IOException
	{
		int fileNo;
		Map<Integer,List<String>> result = new HashMap<Integer,List<String>>();
		mapperOutput = runMapper();
		for(Map<String, Integer> hm : mapperOutput){
			for (String key : hm.keySet())
			{
				fileNo = (int) ((int)(key.hashCode() & Integer.MAX_VALUE) % this.noOfReducers);
				String value = this.formString(key, hm.get(key));
				List<String> temp = result.get(fileNo);
				if(temp == null){
					temp = new ArrayList<String>();
				}
				temp.add(value);
				result.put(fileNo, temp);
			}

		}
		return result;
	}

	private void writeFile(String fileName,List<String> data) throws IOException{
		File file = new File("C:\\"+this.jobID+"\\"+this.taskID+"\\"+fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileUtils fu;
		FileUtils.writeLines(file, data);
	}
	
	private void writeFiles(Map<Integer,List<String>> filesInBucket){
		for(Integer fib : filesInBucket.keySet()){
			try {
				this.writeFile("Mapper"+fib, filesInBucket.get(fib));
			} catch (IOException e) {
				Debug.errorMessage("Error ocurred while writing file"+ e);
			}
		}
		
	}
}



