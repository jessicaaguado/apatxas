package com.jagusan.apatxas.sqlite.modelView;

public class GastoApatxaListado {

	private Long id;
	private String titulo;
	private Double total;
	private String pagadoPor;

	public GastoApatxaListado(Long id, String titulo, Double total, String pagadoPor) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.total = total;
		this.pagadoPor = pagadoPor;
	}

	public GastoApatxaListado(String titulo, Double total, String pagadoPor) {
		super();
		this.titulo = titulo;
		this.total = total;
		this.pagadoPor = pagadoPor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
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
