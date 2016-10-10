package kafka.security.authorizer;

import java.security.Principal;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.kafka.common.security.auth.KafkaPrincipal;
import org.apache.log4j.Logger;

import kafka.network.RequestChannel.Session;
import kafka.security.auth.Operation;
import kafka.security.auth.Resource;
import kafka.security.auth.SimpleAclAuthorizer;

public class SSLAuthorizer extends SimpleAclAuthorizer {
	private static Logger log = Logger.getLogger(SSLAuthorizer.class);
	@Override
	public void configure(Map<String, ?> conf) {
		// TODO Auto-generated method stub
		for (Entry<String,?> entry : conf.entrySet()) {
			log.info("Stuff " + entry.getKey() + " " + entry.getValue().toString());
		}
		super.configure(conf);
	}

	@Override
	public boolean authorize(Session session, Operation operation, Resource resource) {		
		if (isSSLPrincipal(session.principal())) {
			return super.authorize(getNewSession(session), operation, resource);
		}
		
		else {
			return super.authorize(session, operation, resource);
		}
	}
	
	protected Session getNewSession(Session session) {
		return new Session(getPrincipal(session.principal()),session.clientAddress());
	}
	
	protected boolean isSSLPrincipal(Principal principal) {
		return principal.getName().contains("CN=");
	}
	
	protected KafkaPrincipal getPrincipal(Principal principal) {
		String pString = getPrincipalString(principal);
		return new KafkaPrincipal("User",pString);
	}
	
	protected String getPrincipalString(Principal principal) {
		String[] pElements = principal.getName().split(",");
		
		for (int i=0, j=pElements.length; i < j; i++) {
			String token = pElements[i];
			if (token.contains("CN")) {
				return token.split("=")[1];
			}		
		}
		
		return principal.getName();
	}
}