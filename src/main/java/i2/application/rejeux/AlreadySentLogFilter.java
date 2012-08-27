package i2.application.rejeux;

import i2.application.rejeux.model.ModSecurityLog;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlreadySentLogFilter {

	private static final Logger LOG = LoggerFactory.getLogger(AlreadySentLogFilter.class);

	public boolean isAlreadySent(@Header("fileNames") String[] fileNames, @Body ModSecurityLog log) { 
		List<String> fileNamesList = Arrays.asList(fileNames);

		LOG.debug("Noms de fichier à filtrer : {}", fileNamesList.toString()); 

		boolean isAlreadySent = !fileNamesList.contains(log.getId());
		LOG.debug("Log avec l'id  {} : {}", log.getId(), isAlreadySent?"envoi...":"déjà envoyé");
		if (!isAlreadySent) {
			LOG.info("Envoi du message d'id {}", log.getId());
		}
		return isAlreadySent; 
	}
}
