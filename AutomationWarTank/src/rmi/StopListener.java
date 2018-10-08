package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import shas.Consts;
import shas.GlobalVars;

public class StopListener extends UnicastRemoteObject implements StopInterface {
	private static final long serialVersionUID = 2L;

	public StopListener() throws RemoteException {
		super();
	}

	public void stop() {
		GlobalVars.command = Consts.COMMANDS.get(0);// mainThread.n();
		synchronized (GlobalVars.monitor) {
			GlobalVars.monitor.notify();
			
		}
		
	}

	@Override
	public void reloadConfiguration() throws RemoteException {
		GlobalVars.command = Consts.COMMANDS.get(1);// mainThread.n();
		synchronized (GlobalVars.monitor) {
			GlobalVars.monitor.notify();
			
		}
	}
}
