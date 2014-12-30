package com.jagusan.apatxas.sqlite.modelView;

import java.io.Serializable;

public class GastoApatxaListado implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String concepto;
	private Double total;
	private Long idPagadoPor;
	private String pagadoPor;

	public GastoApatxaListado(String concepto, Double total, String pagadoPor) {
		super();
		this.concepto = concepto;
		this.total = total;
		this.pagadoPor = pagadoPor;
	}

	public GastoApatxaListado() {
		// TODO Auto-generated constructor stub
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

	public Long getIdPagadoPor() {
		return idPagadoPor;
	}

	public void setIdPagadoPor(Long idPagadoPor) {
		this.idPagadoPor = idPagadoPor;
	}

}
