package thadoop.server;

public abstract class Server extends Thread{
	public abstract void startMe() throws ClassNotFoundException;
	public abstract void stopMe();
}
