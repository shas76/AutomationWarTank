package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StopInterface extends Remote {
	void stop() throws RemoteException;
	void reloadConfiguration() throws RemoteException;
}
