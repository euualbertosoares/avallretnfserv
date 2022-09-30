package avvall.agente.rfc.connector.client;

public class JcoConnProperties {

	private String lang;
	private String client;
	private String passwd;
	private String user;
	private String sysnr;
	private String ashost;

	public JcoConnProperties() {
	}

	public JcoConnProperties(
		String lang,
		String client,
		String passwd,
		String user,
		String sysnr,
		String ashost) {
		super();
		this.lang = lang;
		this.client = client; 
		this.passwd = passwd;
		this.user = user;
		this.sysnr = sysnr;
		this.ashost = ashost;
	}

	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getSysnr() {
		return sysnr;
	}
	public void setSysnr(String sysnr) {
		this.sysnr = sysnr;
	}
	public String getAshost() {
		return ashost;
	}
	public void setAshost(String ashost) {
		this.ashost = ashost;
	} 
}
