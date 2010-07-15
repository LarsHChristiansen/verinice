//package de.sernet.sync.sync;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.logging.Logger;
//import javax.xml.namespace.QName;
//import javax.xml.ws.Service;
//import javax.xml.ws.WebEndpoint;
//import javax.xml.ws.WebServiceClient;
//import javax.xml.ws.WebServiceFeature;
//
//import de.sernet.sync.sync.SyncWS;
//
///**
// * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.6 in JDK 6 Generated
// * source version: 2.1
// * 
// */
//@WebServiceClient(name = "syncWSService", targetNamespace = "http://www.sernet.de/sync/sync", wsdlLocation = "http://localhost:8080/veriniceserver/sync/sync.wsdl")
//public class SyncWSService extends Service {
//
//	private final static URL SYNCWSSERVICE_WSDL_LOCATION;
//	private final static Logger logger = Logger
//			.getLogger(de.sernet.sync.sync.SyncWSService.class.getName());
//
//	static {
//		URL url = null;
//		try {
//			URL baseUrl;
//			baseUrl = de.sernet.sync.sync.SyncWSService.class.getResource(".");
//			url = new URL(baseUrl,
//					"http://localhost:8080/veriniceserver/sync/sync.wsdl");
//
//		} catch (MalformedURLException e) {
//
//			logger
//					.warning("Failed to create URL for the wsdl Location: 'file:/Users/carmen/Studium/Semester4/Semesterprojekt/SyncWS-Requests/syncWSService.wsdl', retrying as a local file");
//			logger.warning(e.getMessage());
//		}
//		SYNCWSSERVICE_WSDL_LOCATION = url;
//	}
//
//	public SyncWSService(URL wsdlLocation, QName serviceName) {
//		super(wsdlLocation, serviceName);
//	}
//
//	public SyncWSService() {
//		super(SYNCWSSERVICE_WSDL_LOCATION, new QName(
//				"http://www.sernet.de/sync/sync", "syncWSService"));
//
//		System.out
//				.println("address: " + SYNCWSSERVICE_WSDL_LOCATION.toString());
//	}
//
//	/**
//	 * 
//	 * @return returns SyncWS
//	 */
//	@WebEndpoint(name = "syncWSPort")
//	public SyncWS getSyncWSPort() {
//		return super.getPort(new QName("http://www.sernet.de/sync/sync",
//				"syncWSPort"), SyncWS.class);
//	}
//
//	/**
//	 * 
//	 * @param features
//	 *            A list of {@link javax.xml.ws.WebServiceFeature} to configure
//	 *            on the proxy. Supported features not in the
//	 *            <code>features</code> parameter will have their default
//	 *            values.
//	 * @return returns SyncWS
//	 */
//	@WebEndpoint(name = "syncWSPort")
//	 public SyncWS getSyncWSPort(WebServiceFeature features) {
//	//public SyncWS getSyncWSPort(WebServiceFeature... features) {
//		return super.getPort(new QName("http://www.sernet.de/sync/sync",
//				"syncWSPort"), SyncWS.class, features);
//	}
//
//}