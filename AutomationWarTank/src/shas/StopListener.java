package shas;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StopListener
  extends UnicastRemoteObject
  implements StopInterface
{
  private static final long serialVersionUID = 2L;
  private boolean stop = false;
  private boolean needReloadConfig = false;
  
  protected StopListener()
    throws RemoteException
  {}
  
  public void setNeedReloadConfig(boolean needReloadConfig)
  {
    this.needReloadConfig = needReloadConfig;
  }
  
  public boolean isNeedReloadConfig()
  {
    return this.needReloadConfig;
  }
  
  public boolean isStop()
  {
    return this.stop;
  }
  
  public void stop()
  {
    this.stop = true;
  }
  
  public void reloadConfiguration()
    throws RemoteException
  {
    this.needReloadConfig = true;
  }
}
