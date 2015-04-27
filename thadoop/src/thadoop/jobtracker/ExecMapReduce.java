package thadoop.jobtracker;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thadoop.debug.Debug;
import thadoop.message.JobMessage;
import thadoop.message.MapReduceMessage;
import thadoop.message.Message;
import thadoop.message.MessageID;
import thadoop.s3.FileMetaData;
import thadoop.s3.S3FileAPI;

public class ExecMapReduce {
	
	private long jobId;
	private long taskId;
	private MapReduceMessage mapReduceMessage;
	private Map<Long,Character> taskStatus;
	private Map<InetAddress,List<FileMetaData>> fileGroup ;
	private long numberOfMappers;
	private long numberOfReducers;
	
	public ExecMapReduce(long jobId,Message message){
		this.jobId = jobId;
		this.mapReduceMessage = (MapReduceMessage) message;
		this.taskId = 0;
		this.taskStatus = new HashMap<Long,Character>();
	}
	
	private JobMessage populateJobMessageMapper(MessageID messageID,List<FileMetaData> fileMetaData){
		JobMessage jm = new JobMessage(messageID,fileMetaData,mapReduceMessage);
		jm.setJobId(jobId);
		jm.setTaskId(taskId);
		jm.setInputPath(this.mapReduceMessage.getMapreduce().getInputPath());
		jm.setOutputPath(this.mapReduceMessage.getMapreduce().getOutputPath());
		jm.setNoOfMappers(numberOfMappers);
		jm.setNoOfReducers(numberOfReducers);
		this.taskStatus.put(taskId, 'I');//task is initialized
		taskId++;
		return jm;
	}
	private List<FileMetaData> populateFileMetaDataForReducer(long reducerNum){
		List<FileMetaData> fileMetaData = new ArrayList<FileMetaData>();
		for(long i=0;i<this.numberOfMappers;i++){
			String filePath = "/"+this.jobId+"/"+i+"/"+reducerNum+".txt";
			FileMetaData fm = new FileMetaData();
			fm.setFilePath(filePath);
			fileMetaData.add(fm);
		}
		return fileMetaData;
	}
	private JobMessage populateJobMessageReducer(MessageID messageID,long reducerNum){
		JobMessage jm = new JobMessage(messageID,this.populateFileMetaDataForReducer(reducerNum),mapReduceMessage);
		jm.setJobId(jobId);
		taskId++;
		jm.setTaskId(taskId);
		jm.setInputPath(this.mapReduceMessage.getMapreduce().getInputPath());
		jm.setOutputPath(this.mapReduceMessage.getMapreduce().getOutputPath());
		jm.setNoOfMappers(numberOfMappers);
		jm.setNoOfReducers(numberOfReducers);
		this.taskStatus.put(taskId, 'I');//task is initialized
		return jm;
	}
	
	private List<FileMetaData> getFileMetaDataFroms3(){
		S3FileAPI s3api = new S3FileAPI();
		List<FileMetaData> fileMetaData = s3api.fetchMeataData(this.mapReduceMessage.getMapreduce().getInputPath().toString());
		return fileMetaData;
	}
	
	private Map<InetAddress,List<FileMetaData>>  combineFiles(List<FileMetaData> fileMetaData){
		CombineFiles cf = new CombineFiles(fileMetaData);
		Map<InetAddress,List<FileMetaData>> fileGroup = cf.groupFiles();
		return fileGroup;
	}
	
	private void sendMessageToWorker(InetAddress workerIp,int portNo,JobMessage jm){
		try{
			Socket s = new Socket(workerIp,portNo);
			OutputStream os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(jm);
			oos.close();
			os.close();
			s.close();
		}catch(Exception e){
			System.out.println(e);
		}	
	}
	
	private void lauchMappers(){
		this.numberOfMappers = fileGroup.size();
		this.numberOfReducers = fileGroup.size()/2;
		for(InetAddress ip: fileGroup.keySet()){
			JobMessage jm = this.populateJobMessageMapper(MessageID.MAPREDUCE_JOB, fileGroup.get(ip));
			this.sendMessageToWorker(ip, 8080, jm);
		}
		
	}
	private boolean isAllMappersCompleted(){
		for(long taskId:this.taskStatus.keySet()){
			if(this.taskStatus.get(taskId) == 'I')
				return false;
		}
		return true;
	}
	private void launchReducers(){
		InetAddress[] workerNodes = (InetAddress[]) this.fileGroup.entrySet().toArray();
		for(long reducerNum=0;reducerNum<numberOfReducers;reducerNum++){
			JobMessage jm = this.populateJobMessageReducer(MessageID.REDUCER_JOB, reducerNum);
			this.sendMessageToWorker(workerNodes[(int) reducerNum], 8080, jm);
		}
	}
	public void processJob(){
		//fetch all meta-data from directory given
		List<FileMetaData> fileMetaData = this.getFileMetaDataFroms3();
		if(fileMetaData == null){
			Debug.errorMessage("file Meta data list is empty");
			return;
		}
		//apply combine files
		fileGroup = this.combineFiles(fileMetaData);
		
		//populate job message 
		//send it to task trackers
		this.lauchMappers();
	
	}
	public void updateTaskStatusForMappers(long taskId){
		this.taskStatus.put(taskId,'C');
		if(this.isAllMappersCompleted()){
			this.launchReducers();
		}
	}
	public void updateTaskStatusForReducers(long taskId){
		this.taskStatus.put(taskId,'C');
	}
}
