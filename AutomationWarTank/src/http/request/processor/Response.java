package http.request.processor;

import java.io.Serializable;

public class Response implements Serializable {

	private static final long serialVersionUID = -2812001349784140704L;

	private String redirectUrl;

	private String redirectMethod;

	// in milliseconds
	private long delay;

	public Response(String redirectUrl, String redirectMethod, int delay) {
		super();
		this.redirectUrl = redirectUrl;
		this.redirectMethod = redirectMethod;
		this.delay = delay;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public String getRedirectMethod() {
		return redirectMethod;
	}

	public void setRedirectMethod(String redirectMethod) {
		this.redirectMethod = redirectMethod;
	}

	@Override
	public String toString() {
		return "Response [redirectUrl=" + redirectUrl + ", redirectMethod=" + redirectMethod + ", delay=" + delay + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (delay ^ (delay >>> 32));
		result = prime * result + ((redirectMethod == null) ? 0 : redirectMethod.hashCode());
		result = prime * result + ((redirectUrl == null) ? 0 : redirectUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Response)) {
			return false;
		}
		Response other = (Response) obj;
		if (delay != other.delay) {
			return false;
		}
		if (redirectMethod == null) {
			if (other.redirectMethod != null) {
				return false;
			}
		} else if (!redirectMethod.equals(other.redirectMethod)) {
			return false;
		}
		if (redirectUrl == null) {
			if (other.redirectUrl != null) {
				return false;
			}
		} else if (!redirectUrl.equals(other.redirectUrl)) {
			return false;
		}
		return true;
	}

}
