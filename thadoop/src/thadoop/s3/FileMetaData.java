package thadoop.s3;

import java.net.InetAddress;

public class FileMetaData {
	private String fileName;
	private String filePath;
	private InetAddress nodeIP;
	
	public String getFileName() {
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
	
}
