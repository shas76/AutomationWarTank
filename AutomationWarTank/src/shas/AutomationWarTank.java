package shas;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.configuration.ConfigurationException;

import rmi.StopInterface;
import rmi.StopListener;
import utils.Config;
import workers.AbstractWorker;
import workers.GeneralProcessingWorker;

public class AutomationWarTank {

	// static int iterationCounter = 0;
	// static int iteractonThreshold;

	static StopListener rmiServer;

	public static Date extractTime(Date dateToExtract) throws ParseException {
		return new SimpleDateFormat("HH:mm").parse(new SimpleDateFormat("HH:mm").format(dateToExtract));
	}

	private static void init(String args[]) throws MalformedURLException, RemoteException, NotBoundException,
			ConfigurationException, ParseException {
		GlobalVars.logger.Logging("RMI server is starting");

		try { // special exception handler for registry creation
			LocateRegistry.createRegistry(Consts.RMI_SERVER_PORT);
			GlobalVars.logger.Logging("RMI registry created.");
		} catch (RemoteException e) {
			GlobalVars.logger.Logging("RMI registry already exists.");
		}

		// Instantiate RmiServer
		try {
			rmiServer = new StopListener();
			Naming.rebind(Consts.RMI_SEVER_LOCATOR, rmiServer);
			GlobalVars.logger.Logging("PeerServer bound in registry");
		} catch (Exception e) {
			GlobalVars.logger.Logging(e);
			return;
		}
		GlobalVars.logger.Logging("RMI  started");
		GlobalVars.currentMineProduction = 0;
		GlobalVars.currentArmoryProduction = 0;
	}

	private static boolean isCommand(String command) {
		return Consts.COMMANDS.contains(command.toLowerCase());

	}

	private static void executeCommand(String command) throws MalformedURLException, RemoteException, NotBoundException {
		if (("stop").equals(command)) {
			StopInterface stopProgram = (StopInterface) Naming.lookup(Consts.RMI_SEVER_LOCATOR);
			GlobalVars.logger.Logging("Send signal for stopping");
			stopProgram.stop();
			GlobalVars.logger.Logging("Signal was sended");
			return;
		}
		if ("reloadconfig".equals(command)) {
			StopInterface stopProgram = (StopInterface) Naming.lookup(Consts.RMI_SEVER_LOCATOR);
			GlobalVars.logger.Logging("Send signal for reload config");
			stopProgram.reloadConfiguration();
			GlobalVars.logger.Logging("Signal was sended");
			return;
		}
	}

	public static void main(String[] args) {
		GlobalVars.logger.Logging("Programm is starting.");
		try {
			if (args.length != 0) {
				if (isCommand(args[0])) {
					executeCommand(args[0]);
					return;
				} else {
					GlobalVars.config = new Config(args[0]);
				}
			} else {
				// by default
				GlobalVars.config = new Config();
			}

			init(args);
		} catch (Exception e) {
			GlobalVars.logger.Logging(e);
			return;
		}
		AbstractWorker generalProcessingWorker = new GeneralProcessingWorker();
		new Thread(generalProcessingWorker).start();
		GlobalVars.logger.Logging("Programm started.");

		try {
			while (true) {
				synchronized (GlobalVars.monitor) {
					GlobalVars.monitor.wait();
				}
				if (Consts.COMMANDS.get(0).equals(GlobalVars.command)) {
					break;
				}
				if (Consts.COMMANDS.get(1).equals(GlobalVars.command)) {
					GlobalVars.command = "";
					GlobalVars.config.loadingConfiguration();
				}
			}

		} catch (InterruptedException | ConfigurationException | ParseException e) {
			GlobalVars.logger.Logging(e);
		}

		generalProcessingWorker.setHasToStop(true);
		/*
		 * try {
		 * 
		 * if (warHttpClient.goToURL.equals("")) { warHttpClient.goToURL =
		 * Consts.siteAddress + Consts.angarTab; } Date battleTime = null; while
		 * (true) { GlobalVars.logger.Logging("Start next Iteration.");
		 * GlobalVars.logger.Logging("Check WarTime."); Date currentTime =
		 * AutomationWarTank .extractTime(new Date()); boolean TimeForWar =
		 * false; String battleUrl = ""; // Check Battle int index = 0; for
		 * (Date time : GlobalVars.config.getBattleTimes()) { if (time.getTime()
		 * > currentTime.getTime() && (time.getTime() - currentTime.getTime() <
		 * 6 * Consts.msInMinunte)) { battleUrl = Consts.siteAddress +
		 * GlobalVars.config.getBattleURLs()[index]; TimeForWar = true;
		 * battleTime = time; GlobalVars.logger.Logging("Goto battle!!! URL" +
		 * GlobalVars.config.getBattleURLs()[index]); break; } index++; } // TO
		 * DO check URL contains IN ARRAY if (TimeForWar) { if
		 * (!warHttpClient.isURLBattle(warHttpClient.goToURL)) {
		 * countSkippedPlayers = 0; warHttpClient.goToURL = battleUrl; } } else
		 * { if (battleTime != null) { if (warHttpClient
		 * .isURLBattle(warHttpClient.goToURL) && (currentTime.getTime() -
		 * battleTime .getTime()) > 2 * Consts.msInMinunte &&
		 * warHttpClient.goToURL .contains(Consts.REFRESH)) {
		 * warHttpClient.goToURL = Consts.siteAddress + Consts.angarTab;
		 * battleTime = null; } } }
		 * 
		 * if (rmiServer.isStop()) { recivedStopSignal = true; break; } if
		 * (rmiServer.isNeedReloadConfig()) {
		 * GlobalVars.config.loadingConfiguration();
		 * rmiServer.setNeedReloadConfig(false); }
		 * GlobalVars.logger.Logging("Wating " + (int) (warHttpClient.timeOut /
		 * 1000) + " seconds."); Thread.sleep(warHttpClient.timeOut); } } catch
		 * (Exception e) { GlobalVars.logger.Logging(e); try { Thread.sleep(5 *
		 * Consts.msInMinunte); } catch (InterruptedException e1) {
		 * GlobalVars.logger.Logging(e1); } } if (recivedStopSignal) break;
		 */

		GlobalVars.logger.Logging("Programm stopped.");
	}
}
