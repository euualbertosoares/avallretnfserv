package avvall.agente.properties;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Preferencias {

	private static BufferedReader br;

	public enum Propriedades {

		GET_SERV_NF_URL("getServNf.url"),
		
		JCO_CLIENT_LANG("jco.client.lang"),
		JCO_CLIENT_CLIENT("jco.client.client"),
		JCO_CLIENT_PASSWORD("jco.client.passwd"),
		JCO_CLIENT_USER("jco.client.user"),
		JCO_CLIENT_SYSNR("jco.client.sysnr"),
		JCO_CLIENT_ASHOST("jco.client.ashost");
		
		private String key;

		private Propriedades(String n) {
			this.key = n;
		}
	}

	public static String get(Propriedades propriedade) {
		return getProperty(propriedade.key);
	}

	private static final String getProperty(String key) {

		String fileName = "avvallretnfserv-env.properties";
        FileInputStream fstream = null;
        String line = "";
        String value = "";
        try {
            fstream = new FileInputStream(fileName);

            br = new BufferedReader(new InputStreamReader(fstream));

            while ((line = br.readLine()) != null) {
                if(line.startsWith(key)){
                	String [] values = line.split("=");
                	for (int i = 1; i < values.length; i++) {
                		if (i > 1) {
                    		value = value + "=" + values[i].trim();
                		} else {
                    		value = value + values[i].trim();
                		}
					}
                    break;
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Propriedades.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (fstream != null) {
				try {
					fstream.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
        return value;

	}
}
