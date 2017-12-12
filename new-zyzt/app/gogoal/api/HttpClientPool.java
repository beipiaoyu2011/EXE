package gogoal.api;

import org.apache.http.HttpVersion;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import play.Play;

public enum HttpClientPool {

	pool;
	
	private DefaultHttpClient client = null;
	
	HttpClientPool() {
	    SchemeRegistry schreg = new SchemeRegistry();    
	    schreg.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));   
	    schreg.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));         
	    
		PoolingClientConnectionManager conMgr = new PoolingClientConnectionManager();
		conMgr.setMaxTotal(200);
		conMgr.setDefaultMaxPerRoute(conMgr.getMaxTotal());
		
	    PoolingClientConnectionManager pccm = new PoolingClientConnectionManager(schreg);  
		pccm.setDefaultMaxPerRoute(1024);
	    pccm.setMaxTotal(1024);
		
	    HttpParams params = new BasicHttpParams();   
	    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);   
	    HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");   
	    HttpProtocolParams.setUseExpectContinue(params, true);
	    
	    params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
	    params.setParameter(CoreConnectionPNames.SO_TIMEOUT, Integer.parseInt(Play.configuration.getProperty("http.timeout", "10000")));
	    params.setParameter(ClientPNames.CONN_MANAGER_TIMEOUT, 500L);
	    params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
		
	    client = new DefaultHttpClient(pccm, params);
	}
	
	protected DefaultHttpClient getClient() {
		return this.client;
	}
}
