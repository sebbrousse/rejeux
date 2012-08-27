package i2.application.rejeux.converter;

import i2.application.rejeux.ModSecurityReader;
import i2.application.rejeux.model.ModSecurityLog;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;

@Converter
public class ModSecurityLogConverter {

	@Converter
	public static List<ModSecurityLog> toModSecurityLog(byte[] data, Exchange exchange) {
		
		
//		TypeConverter converter = exchange.getContext().getTypeConverter();
//		String s = converter.convertTo(String.class, data);
//		
//		if (s == null || s.length() == 0) {
//			throw new IllegalArgumentException("Données incorrectes, impossible de lire le flux");
//		} 
		
		// TODO ajouter la lecture du flux en ByteArrayInputStream
		
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		
		
		ModSecurityReader reader = new ModSecurityReader(bais);
		return reader.readSections();
	}
}
