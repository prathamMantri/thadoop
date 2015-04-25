package thadoop.s3;

import java.io.Serializable;
import java.net.InetAddress;

public class FileMetaData implements Serializable {

	private String fileName;
	private String filePath;
	private InetAddress nodeIP;
	private long fileSizeInMB;
	private String bucketName;

		public String getFileName()
		{ 
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		public InetAddress getNodeIP() {
			return nodeIP;
		}
		public void setNodeIP(InetAddress nodeIP) {
			this.nodeIP = nodeIP;
		}
		public long getFileSizeInMB() {
			return fileSizeInMB;
		}
		public void setFileSizeInMB(long fileSizeInMB) {
			this.fileSizeInMB = fileSizeInMB;
		}
		
		public String getFileBucket() {
			return bucketName;
		}
		public void setFileBucket(String bucketName) {
			this.bucketName = bucketName;
			
		}	
	}



