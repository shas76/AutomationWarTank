package workers;

import http.request.processor.HttpRequestProcessor;
import shas.Consts;
import shas.GlobalVars;

public abstract class AbstractWorker implements Runnable {

	private boolean hasToStop = false;

	private HttpRequestProcessor httpRequestProcessor = new HttpRequestProcessor();

	public boolean isHasToStop() {
		return hasToStop;
	}

	public void setHasToStop(boolean hasToStop) {
		this.hasToStop = hasToStop;
	}

	public HttpRequestProcessor getHttpRequestProcessor() {
		return httpRequestProcessor;
	}

	protected void init() throws Exception {

	}

	@Override
	public void run() {
		int countOfIdleIteraction = 0;
		while (!isHasToStop()) {
			if (countOfIdleIteraction > 0) {
				countOfIdleIteraction--;
			} else {
				try {
					init();
					// while (!isHasToStop()) {
					GlobalVars.logger.Logging("Start next Iteration.", this);
					doWork();
					GlobalVars.logger.Logging("The Iteration was ended.", this);
					doAfterWork();
					// }
				} catch (Exception e) {
					GlobalVars.logger.Logging(e, this);
				}
				countOfIdleIteraction = getCountOfIdleSeconds() / 5;
				GlobalVars.logger.Logging("Wating " + (int) (countOfIdleIteraction * 5) + " seconds.", this);
			}

			threadPause(5 * Consts.ONE_SECOND);
		}
	}

	public abstract void doWork() throws Exception;

	protected abstract void doAfterWork();

	protected abstract int getCountOfIdleSeconds();

	// public abstract void doBeforeWhile();

	public void threadPause(long sleepInterval) {
		try {
			Thread.sleep(sleepInterval);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
