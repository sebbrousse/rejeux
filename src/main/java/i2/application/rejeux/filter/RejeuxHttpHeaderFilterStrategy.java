package i2.application.rejeux.filter;

import org.apache.camel.impl.DefaultHeaderFilterStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RejeuxHttpHeaderFilterStrategy extends DefaultHeaderFilterStrategy {
    
	private final Logger logger = LoggerFactory.getLogger(RejeuxHttpHeaderFilterStrategy.class);
	
    public RejeuxHttpHeaderFilterStrategy() {
        initialize();  
    }

    /**
     * Filtre d'exclusion des headers camel et t2k pour les envois HTTP
     */
    protected void initialize() {
        getOutFilter().add("content-length");
        getOutFilter().add("content-type");
        // Add the filter for the Generic Message header
        // http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.5
        getOutFilter().add("Cache-Control");
        getOutFilter().add("Connection");
        getOutFilter().add("Pragma");
        getOutFilter().add("Trailer");
        getOutFilter().add("Transfer-Encoding");
        getOutFilter().add("Upgrade");
        getOutFilter().add("Via");
        getOutFilter().add("Warning");
        
        setLowerCase(true);
        
        // filter headers begin with "Camel" or "org.apache.camel" or "T2K"
        // must ignore case for Http based transports
        setOutFilterPattern("(?i)(Camel|org\\.apache\\.camel|rejeux)[\\.|a-z|A-z|0-9]*");
        logger.debug("Application du Pattern de filtrage des headers HTTP");
    }
}
