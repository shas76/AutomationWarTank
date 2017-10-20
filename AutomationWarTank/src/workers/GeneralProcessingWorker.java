package workers;

import java.util.Random;

import shas.Consts;
import shas.GlobalVars;

public class GeneralProcessingWorker extends AbstractWorker {

	@Override
	public void doWork() throws Exception {
		for (String URL : GlobalVars.config.getGeneralProcessingURLs()) {
			setGoToURL(Consts.siteAddress + URL);
			setMethod(Consts.GET_METHOD);
			while (true) {
				super.doWork();
				if ("".equals(getGoToURL())) {
					break;
				}
				threadPause(Consts.ONE_SECOND);
			}
		}
	}

	@Override
	protected void doAfterWork() {
/*		Random rnd = new Random();
		long timeOut = (int) ((GlobalVars.config.getSleepInterval() + (rnd
				.nextDouble() * GlobalVars.config.getRandomInterval())) * Consts.msInMinunte);
		GlobalVars.logger.Logging("Wating " + (int) (timeOut / 1000)
				+ " seconds.", this);*/
		setGoToURL("");
//		threadPause(Consts.ONE_SECOND);
	}

	@Override
	protected int getCountOfIdleSeconds() {

		return 300;
	}

}
