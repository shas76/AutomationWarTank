package workers;

import java.util.Random;

import shas.Consts;
import shas.GlobalVars;

public class GeneralProcessingWorker extends AbstractWorker {



	@Override
	public void doWork() throws Exception {
		for (String URL : GlobalVars.config.getGeneralProcessingURLs()) {
			setGoToURL(URL);
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
	public void doAfterWork() {
		Random rnd = new Random();
		long timeOut = (int) ((GlobalVars.config.getSleepInterval() + (rnd
				.nextDouble() * GlobalVars.config.getRandomInterval())) * Consts.msInMinunte);
		GlobalVars.logger.Logging("Wating " + (int) (timeOut / 1000)
				+ " seconds.", this);
		setGoToURL("");
		threadPause(timeOut);
	}

}
