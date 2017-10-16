package shas;

import httpclients.WarHttpClientWrapper;

import java.io.*;
import java.io.ObjectInputStream.GetField;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.text.html.parser.ParserDelegator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.*;
import org.apache.http.util.*;

import parsers.angarParserCallBack;
import parsers.armoryParserCallBack;
import parsers.bankParserCallBack;
import parsers.battleParserCallBack;
import parsers.buildingsParserCallBack;
import parsers.convoyParserCallBack;
import parsers.fightParserCallBack;
import parsers.goToURLFinderParserCallBack;
import parsers.loginPageParserCallBack;
import parsers.mineParserCallBack;
import parsers.polygonParserCallBack;
import rmi.StopInterface;
import rmi.StopListener;
import utils.Config;
import utils.Logger;

public class AutomationWarTank {

	public static int countSkippedPlayers = 0;
	public static int currentMineProduction = 0;
	public static int currentArmoryProduction = 0;
	public static boolean isTakeProductionMode = false;

	static int printResponseBoby = 0;

	static int iterationCounter = 0;
	static int iteractonThreshold;

	static StopListener rmiServer;

	public static Date extractTime(Date dateToExtract) throws ParseException {
		return new SimpleDateFormat("HH:mm")
				.parse(new SimpleDateFormat("HH:mm").format(dateToExtract));
	}

	private static void init(String args[]) throws MalformedURLException,
			RemoteException, NotBoundException, ConfigurationException,
			ParseException {
		if (args.length != 0) {
			if (isCommand(args[0])) {
				executeCommand(args[0]);
			} else {
				GlobalVars.config = new Config(args[0]);
			}
		} else {
			// by default
			GlobalVars.config = new Config();
		}
		GlobalVars.logger.Logging("RMI server is starting");

		try { // special exception handler for registry creation
			LocateRegistry.createRegistry(1099);
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
		currentMineProduction = 0;
		currentArmoryProduction = 0;
	}

	private static boolean isCommand(String command) {
		return Consts.COMMANDS.contains(command.toLowerCase());

	}

	private static void executeCommand(String command)
			throws MalformedURLException, RemoteException, NotBoundException {
		if (("stop").equals(command)) {
			StopInterface stopProgram = (StopInterface) Naming
					.lookup(Consts.RMI_SEVER_LOCATOR);
			GlobalVars.logger.Logging("Send signal for stopping");
			stopProgram.stop();
			GlobalVars.logger.Logging("Signal was sended");
			return;
		}
		if ("reloadconfig".equals(command)) {
			StopInterface stopProgram = (StopInterface) Naming
					.lookup(Consts.RMI_SEVER_LOCATOR);
			GlobalVars.logger.Logging("Send signal for reload config");
			stopProgram.reloadConfiguration();
			GlobalVars.logger.Logging("Signal was sended");
			return;
		}
	}

	public static void main(String[] args) {
		boolean recivedStopSignal = false;
		GlobalVars.logger.Logging("Programm is starting.");
		try {
			init(args);
		} catch (Exception e) {
			GlobalVars.logger.Logging(e);
			return;
		}
		GlobalVars.logger.Logging("Programm started.");


		while (true) {

			try {
				WarHttpClientWrapper warHttpClient = new WarHttpClientWrapper();
				// Login for registered users
				warHttpClient.nameValuePairs
						.add(new BasicNameValuePair("id1_hf_0", ""));
				warHttpClient.nameValuePairs.add(new BasicNameValuePair("login",
						GlobalVars.config.getUserName()));
				warHttpClient.nameValuePairs.add(new BasicNameValuePair("password",
						GlobalVars.config.getPassword()));
				warHttpClient.executeHttpRequestAndParse(Consts.siteAddress,
						Consts.GET_METHOD);
				warHttpClient.executeHttpRequestAndParse(warHttpClient.goToURL,
						Consts.GET_METHOD);

				warHttpClient.executeHttpRequestAndParse(warHttpClient.goToURL,
						Consts.POST_METHOD, warHttpClient.nameValuePairs);
				if (warHttpClient.goToURL.equals("")) {
					warHttpClient.goToURL = Consts.siteAddress
							+ Consts.angarTab;
				}
				Date battleTime = null;
				while (true) {
					GlobalVars.logger.Logging("Start next Iteration.");
					GlobalVars.logger.Logging("Check WarTime.");
					Date currentTime = AutomationWarTank
							.extractTime(new Date());
					boolean TimeForWar = false;
					String battleUrl = "";
					// Check Battle
					int index = 0;
					for (Date time : GlobalVars.config.getBattleTimes()) {
						if (time.getTime() > currentTime.getTime()
								&& (time.getTime() - currentTime.getTime() < 6 * Consts.msInMinunte)) {
							battleUrl = Consts.siteAddress
									+ GlobalVars.config.getBattleURLs()[index];
							TimeForWar = true;
							battleTime = time;
							GlobalVars.logger.Logging("Goto battle!!! URL"
									+ GlobalVars.config.getBattleURLs()[index]);
							break;
						}
						index++;
					}
					// TO DO check URL contains IN ARRAY
					if (TimeForWar) {
						if (!warHttpClient.isURLBattle(warHttpClient.goToURL)) {
							countSkippedPlayers = 0;
							warHttpClient.goToURL = battleUrl;
						}
					} else {
						if (battleTime != null) {
							if (warHttpClient
									.isURLBattle(warHttpClient.goToURL)
									&& (currentTime.getTime() - battleTime
											.getTime()) > 2 * Consts.msInMinunte
									&& warHttpClient.goToURL
											.contains(Consts.REFRESH)) {
								warHttpClient.goToURL = Consts.siteAddress
										+ Consts.angarTab;
								battleTime = null;
							}
						}
					}
					warHttpClient.executeHttpRequestAndParse(
							warHttpClient.goToURL, warHttpClient.method);
					if (rmiServer.isStop()) {
						recivedStopSignal = true;
						break;
					}
					if (rmiServer.isNeedReloadConfig()) {
						GlobalVars.config.loadingConfiguration();
						rmiServer.setNeedReloadConfig(false);
					}
					GlobalVars.logger.Logging("Wating "
							+ (int) (warHttpClient.timeOut / 1000)
							+ " seconds.");
					Thread.sleep(warHttpClient.timeOut);
				}
			} catch (Exception e) {
				GlobalVars.logger.Logging(e);
				try {
					Thread.sleep(5 * Consts.msInMinunte);
				} catch (InterruptedException e1) {
					GlobalVars.logger.Logging(e1);
				}
			}
			if (recivedStopSignal)
				break;
		}
		GlobalVars.logger.Logging("Programm stopped.");
	}
}
