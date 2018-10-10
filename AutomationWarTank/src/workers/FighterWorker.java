package workers;

import java.util.Date;

import http.request.processor.Request;
import http.request.processor.Response;
import shas.AutomationWarTank;
import shas.Consts;
import shas.GlobalVars;

public class FighterWorker extends AbstractWorker {

	private Request request = null;

	private long delay = 0;

	@Override
	public void doWork() throws Exception {

		// Check Battle
		if (request == null) {
			GlobalVars.logger.Logging("Check WarTime.", this);
			Date currentTime = AutomationWarTank.extractTime(new Date());
			int index = 0;
			for (Date time : GlobalVars.config.getBattleTimes()) {
				if (time.getTime() > currentTime.getTime()) {
					delay = (time.getTime() - currentTime.getTime()) / Consts.ONE_SECOND - 20;
					request = new Request(Consts.siteAddress + GlobalVars.config.getBattleURLs()[index]);
					GlobalVars.logger.Logging("Waiting for battle!!! URL " + request.getUrl() + " seconds " + delay,
							this);
					break;
				}
				index++;
			}
		} else {
			GlobalVars.logger.Logging("Goto battle!!! URL" + request.getUrl(), this);
			while (!isHasToStop()) {
				Response responce = getHttpRequestProcessor().processRequest(request);
				if ("".equals(responce.getRedirectUrl())) {
					break;
				}
				request = new Request(responce.getRedirectUrl(), responce.getRedirectMethod());
				threadPause(responce.getDelay());
			}
			request = null;
		}

	}

	@Override
	public void doAfterWork() {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getCountOfIdleSeconds() {
		return (int) delay;
	}
}
