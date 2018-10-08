package http.request.processor;

import java.io.Serializable;
import java.util.List;

import org.apache.http.NameValuePair;

import shas.Consts;

public class Request implements Serializable {

	private static final long serialVersionUID = 4749902566305707655L;

	private String url;

	private String method;

	private List<NameValuePair> parameters;

	public Request(String url) {
		this(url, Consts.GET_METHOD);
	}

	public Request(String url, String method) {
		this(url, method, null);
	}

	public Request(String url, String method,
			List<NameValuePair> parameters) {
		super();
		this.url = url;
		this.method = method.toUpperCase();
		this.parameters = parameters;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url.toUpperCase();
	}

	public List<NameValuePair> getParameters() {
		return parameters;
	}

	public void setParameters(List<NameValuePair> parameters) {
		this.parameters = parameters;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String toString() {
		return "request [url=" + url + ", method=" + method + ", parameters="
				+ parameters + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		if (!(obj instanceof Request)) {
			return false;
		}
		Request other = (Request) obj;
		if (method == null) {
			if (other.method != null) {
				return false;
			}
		} else if (!method.equals(other.method)) {
			return false;
		}
		if (parameters == null) {
			if (other.parameters != null) {
				return false;
			}
		} else if (!parameters.equals(other.parameters)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		return true;
	}

}