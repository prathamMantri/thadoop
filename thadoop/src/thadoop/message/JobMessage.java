package thadoop.message;

import java.nio.file.Path;
import java.util.List;
import thadoop.s3.FileMetaData;

/*
 * This class is used to send Job to Task tracker
 * There can be two types of JOB
 * - Mapper JOB
 * - Reducer JOB
 * The type of job can be Identified from MessageId variable
 */
public class JobMessage extends Message {
	
	private long jobId;
	private long taskId;
	private List<FileMetaData> fileMetaData;
	private MapReduceMessage mapReduceMessage;
	private Path inputPath;
	private Path outputPath;
	
	public JobMessage(MessageID messageID,List<FileMetaData> fileMetaData,MapReduceMessage mapReduceMessage){
		this.messageID = messageID;
		this.fileMetaData = fileMetaData;
		this.mapReduceMessage = mapReduceMessage;
	}

	public List<FileMetaData> getFileMetaData() {
		return fileMetaData;
	}

	public void setFileMetaData(List<FileMetaData> fileMetaData) {
		this.fileMetaData = fileMetaData;
	}

	public Path getInputPath() {
		return inputPath;
	}

	public void setInputPath(Path inputPath) {
		this.inputPath = inputPath;
	}

	public Path getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(Path outputPath) {
		this.outputPath = outputPath;
	}
	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
}
