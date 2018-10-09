package workers;

import http.request.processor.Request;
import http.request.processor.Response;
import shas.Consts;
import shas.GlobalVars;

public class GeneralProcessingWorker extends AbstractWorker {

	@Override
	public void doWork() throws Exception {
		for (String URL : GlobalVars.config.getGeneralProcessingURLs()) {
			Request request = new Request(Consts.siteAddress + URL);
			while (true) {
				Response responce = getHttpRequestProcessor().processRequest(request);
				if ("".equals(responce.getRedirectUrl())) {
					break;
				}
				request = new Request(responce.getRedirectUrl(), responce.getRedirectMethod());
				threadPause(Consts.ONE_SECOND);
			}
		}
	}

	@Override
	protected void doAfterWork() {

	}

	@Override
	protected int getCountOfIdleSeconds() {
		return 300;
	}

}
