package thadoop.server;

public abstract class Server extends Thread{
	public abstract void startMe();
	public abstract void stopMe();
}
