package workers;

import java.util.Date;

import http.request.processor.Request;
import shas.AutomationWarTank;
import shas.Consts;
import shas.GlobalVars;

public class FighterWorker extends AbstractWorker {

	private Request request = null;

	private long delay = 0;

	private Date endTime = new Date();

	@Override
	public void doWork() throws Exception {

		// Check Battle
		if (request == null) {
			GlobalVars.logger.Logging("Check WarTime.", this);
			Date currentTime = AutomationWarTank.extractTime(new Date());
			int index = 0;
			for (Date time : GlobalVars.config.getBattleTimes()) {
				if (time.getTime() > currentTime.getTime()) {
					endTime = new Date();
					endTime.setHours(time.getHours());
					endTime.setMinutes(time.getMinutes() + 2);
					request = getHttpRequestProcessor().processRequest(
							new Request(Consts.siteAddress + GlobalVars.config.getBattleURLs()[index]));
					delay = request.getPreviouceResponse().getDelay() / Consts.ONE_SECOND - 30;
					GlobalVars.logger.Logging("Waiting for battle!!! URL=" + request.getUrl() + " seconds " + delay,
							this);
					break;
				}
				index++;
			}
			if (request  == null){
				delay = 900;
				GlobalVars.logger.Logging("Waiting for battle!!! URL=NO URL!!!  seconds " + delay, this);
			}
		} else {
			GlobalVars.logger.Logging("Goto battle!!! URL" + request.getUrl(), this);
			while (!isHasToStop()) {
				request = getHttpRequestProcessor().processRequest(request);
				if (request.getUrl().contains(Consts.REFRESH) && new Date().getTime() > endTime.getTime()) {
					break;
				}
				threadPause(request.getPreviouceResponse().getDelay());
			}
			delay = 0;
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
