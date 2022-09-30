package avvall.agente.api;

import com.google.gson.annotations.Expose;

public class GetServNfApi {

	@Expose
	public String i_docnum;
	@Expose
	public String i_nfenum;
	@Expose
	public String i_series = "1";

	public GetServNfApi() {
	}

	public GetServNfApi(
			String i_docnum, 
			String i_nfenum,
			String i_series) {
		super();
		this.i_docnum = i_docnum;
		this.i_nfenum = i_nfenum;
		this.i_series = i_series == null ? "1" : i_series;
	}

	public String getI_Docnum() {
		return i_docnum;
	}

	public void setI_Docnum(String i_docnum) {
		this.i_docnum = i_docnum;
	}

	public String getI_Nfenum() {
		return i_nfenum;
	}

	public void setI_Nfenum(String i_nfenum) {
		this.i_nfenum = i_nfenum;
	}

	public String getI_Series() {
		return i_series;
	}

	public void setI_Series(String i_series) {
		this.i_series = i_series;
	}

}
