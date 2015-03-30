package shas;

import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract interface StopInterface
  extends Remote
{
  public abstract void stop()
    throws RemoteException;
  
  public abstract void reloadConfiguration()
    throws RemoteException;
}
