package com.uet.mhst.model;

import java.io.Serializable;

import com.google.api.client.util.DateTime;
import com.uet.mhst.itemendpoint.model.Comment;

public class Cmt implements Serializable {
	private Long id;
	private String idfb, name, address;
	private String content;
	private Double lat, lng;
	private DateTime time;

	public Cmt() {
		super();
	}

	public Cmt(Comment comment) {
		// this.id = comment.getId().getId();
		this.address = comment.getAddress();
		this.name = comment.getName();
		this.idfb = comment.getIdfb();
		this.content = comment.getContent();
		this.lat = comment.getLatitude();
		this.lng = comment.getLongitude();
		this.time = comment.getTime();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdfb() {
		return idfb;
	}

	public void setIdfb(String idfb) {
		this.idfb = idfb;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public DateTime getTime() {
		return time;
	}

	public void setTime(DateTime time) {
		this.time = time;
	}

}
