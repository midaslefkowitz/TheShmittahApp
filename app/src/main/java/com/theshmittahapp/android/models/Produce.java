package com.theshmittahapp.android.models;

import java.io.Serializable;

public class Produce implements Serializable {
	
	private String name = null;
	private String startDate = null;
	private String endDate = null;
	private String prepared = null;
	private String peeled = null;
	private String seeds = null;
	private String peel = null;
	private String squeezed = null;
	private String comments = null;
	
	public Produce(String name, String startDate, String endDate, String prepared, 
			String peeled, String seeds, String peel, String squeezed, String comments) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.prepared = prepared;
		this.peeled = peeled;
		this.seeds = seeds;
		this.peel = peel;
		this.squeezed = squeezed;
		this.comments = comments;
	}
	
	/*
	public Produce(String name, String startDate, String endDate, String prepared, 
			boolean peeled, String seeds, String peel, boolean squeezed) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.prepared = prepared;
		this.peeled = peeled;
		this.seeds = seeds;
		this.peel = peel;
		this.squeezed = squeezed;
	}
	
	public Produce(String name, String startDate) {
		this.name = name;
		this.startDate = startDate;
	}
	*/
	
	public String getName() {
		return name;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public String getPrepared() {
		return prepared;
	}

	public String getPeeled() {
		return peeled;
	}
	
	public String getSeeds() {
		return seeds;
	}
	
	public String getPeel() {
		return peel;
	}
	
	public String getSqueezed() {
		return squeezed;
	}
	
	public String getComments() {
		return comments;
	}
}