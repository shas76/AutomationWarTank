package workers;


import shas.Consts;
import shas.GlobalVars;

public abstract class AbstractWorker implements Runnable {

	
	private String method;

	private String goToURL;

	private boolean hasToStop = false;

	public String getGoToURL() {
		return goToURL;
	}

	public void setGoToURL(String goToURL) {
		this.goToURL = goToURL;
	}

	public String getMethod() {
		return method;
	}

	public boolean isHasToStop() {
		return hasToStop;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setHasToStop(boolean hasToStop) {
		this.hasToStop = hasToStop;
	}









	protected void init() throws Exception {
		if (goToURL.equals("")) {
			goToURL = Consts.siteAddress + Consts.angarTab;
		}

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
					while (!isHasToStop() ) {
						GlobalVars.logger
								.Logging("Start next Iteration.", this);
						doWork();
						GlobalVars.logger.Logging("The Iteration was ended.",
								this);
						doAfterWork();
					}
				} catch (Exception e) {
					GlobalVars.logger.Logging(e, this);
				}
			}
			if (countOfIdleIteraction == 0) {
				countOfIdleIteraction = getCountOfIdleSeconds()/5; 
				GlobalVars.logger.Logging("Wating "
						+ (int) (countOfIdleIteraction * 5 / 1000)
						+ " seconds.", this);
			}

			threadPause(5 * Consts.ONE_SECOND);
		}
	}

	public void doWork() throws Exception {
//		executeHttpRequestAndParse(goToURL, method);
	};

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
