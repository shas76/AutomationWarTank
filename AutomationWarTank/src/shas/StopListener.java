package shas;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StopListener extends UnicastRemoteObject implements StopInterface   {

	private static final long serialVersionUID = 2L;

	protected StopListener() throws RemoteException {
		super();
	}


	private boolean stop = false;
	private boolean needReloadConfig = false;
	
	public void setNeedReloadConfig(boolean needReloadConfig) {
		this.needReloadConfig = needReloadConfig;
	}


	public boolean isNeedReloadConfig() {
		return needReloadConfig;
	}


	public boolean isStop() {
		return stop;
	}


	public void stop() {
		stop= true;
	}


	@Override
	public void reloadConfiguration() throws RemoteException {
		needReloadConfig = true;	
	}

}
