package com.theshmittahapp.android.models;

import java.io.Serializable;

public class Produce implements Serializable {
	
	private String name = null;
	private String startDate = null;
    private String startYear = null;
	private String endDate = null;
    private String endYear = null;
    private String sefichimFrom = null;
    private String sefichimTo = null;
	private String prepared = null;
	private String peeled = null;
	private String seeds = null;
	private String peel = null;
	private String squeezed = null;
	private String comments = null;
	
	public Produce(String name, String startDate, String startYear, String endDate, String endYear,
                   String sefichimFrom, String sefichimTo, String prepared, String peeled,
                   String seeds, String peel, String squeezed, String comments) {
		this.name = name;
		this.startDate = startDate;
        this.startYear = startYear;
		this.endDate = endDate;
        this.endYear = endYear;
        this.sefichimFrom = sefichimFrom;
        this.sefichimTo = sefichimTo;
		this.prepared = prepared;
		this.peeled = peeled;
		this.seeds = seeds;
		this.peel = peel;
		this.squeezed = squeezed;
		this.comments = comments;
	}

	public String getName() {
		return name;
	}
	
	public String getStartDate() {
		return startDate;
	}

    public String getStartYear() {
        return startYear;
    }

    public String getEndDate() {
		return endDate;
	}

    public String getEndYear() {
        return endYear;
    }

    public String getSefichimFrom() {
        return sefichimFrom;
    }

    public String getSefichimTo() {
        return sefichimTo;
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