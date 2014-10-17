package com.jagusan.apatxas.sqlite.modelView;

public class GastoApatxaListado {

	private Long id;
	private String concepto;
	private Double total;
	private String pagadoPor;

	public GastoApatxaListado(Long id, String concepto, Double total, String pagadoPor) {
		super();
		this.id = id;
		this.concepto = concepto;
		this.total = total;
		this.pagadoPor = pagadoPor;
	}

	public GastoApatxaListado(String concepto, Double total, String pagadoPor) {
		super();
		this.concepto = concepto;
		this.total = total;
		this.pagadoPor = pagadoPor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String titulo) {
		this.concepto = titulo;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getPagadoPor() {
		return pagadoPor;
	}

	public void setPagadoPor(String pagadoPor) {
		this.pagadoPor = pagadoPor;
	}

}
