package thadoop.cluster;

import java.net.InetAddress;

import com.amazonaws.services.ec2.model.InstanceState;

public class Node {
	
	private String nodeName;
	private InetAddress serverIP;
	private InetAddress	clietIP;
	private int serverPort;
	private int clientPort;
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public InetAddress getServerIP() {
		return serverIP;
	}
	public void setServerIP(InetAddress serverIP) {
		this.serverIP = serverIP;
	}
	public InetAddress getClietIP() {
		return clietIP;
	}
	public void setClietIP(InetAddress clietIP) {
		this.clietIP = clietIP;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public int getClientPort() {
		return clientPort;
	}
	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}

	
	
}
