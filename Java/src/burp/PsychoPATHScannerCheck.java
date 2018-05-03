package burp;

import java.util.List;

import uk.co.pentest.psychoPATH.IntruderPayloadGenerator; // the new payload generator class
import uk.co.pentest.psychoPATH.PsychoTab;

abstract class PsychoPATHScannerCheck implements IScannerCheck {
	protected IBurpExtenderCallbacks callbacks;
	protected IExtensionHelpers helpers;
	protected IntruderPayloadGenerator generator;	
        protected IHttpService checkHttpService;
	public PsychoPATHScannerCheck(IBurpExtenderCallbacks cb, PsychoTab tab) {
		callbacks = cb;
		helpers = callbacks.getHelpers();
	}
        protected boolean createCheckHttpService(String host, int port, boolean https) 
        {			
            if((host==null) || ((port<1) || (port>65535))) 
            { 
			return false;
            } 
            else if(host.isEmpty() || ((port<1) || (port>65535))) 
            {
			return false;
            }		
            if(checkHttpService==null) 
            { //HttpService object not yet created, attempt to create			
			checkHttpService = helpers.buildHttpService(host,port,https);
            } 
            else 
            { 
                //HttpService object already created, compare to inputted settings and recreate if different
                String currHost = checkHttpService.getHost();
		int currPort = checkHttpService.getPort();
		String currHttps = checkHttpService.getProtocol();
		if(!(currHost.equals(host) && (currPort==port) && (currHttps.equalsIgnoreCase("http"+(https ? "s" : ""))))) 
			checkHttpService = helpers.buildHttpService(host,port,https);	
            }
            return true;
	}
	@Override 
	public List<IScanIssue> doPassiveScan(IHttpRequestResponse baseRequestResponse) {
		return null;
	}
	
	@Override
	public abstract int consolidateDuplicateIssues(IScanIssue existingIssue,IScanIssue newIssue);
	
	@Override
	public abstract List<IScanIssue> doActiveScan(IHttpRequestResponse baseRequestResponse,IScannerInsertionPoint insertionPoint);
}
