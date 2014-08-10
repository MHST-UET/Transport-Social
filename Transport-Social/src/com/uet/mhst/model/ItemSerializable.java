package com.uet.mhst.model;

import java.io.Serializable;
import java.util.Date;

import com.google.api.client.util.DateTime;
import com.uet.mhst.itemendpoint.model.Item;

public class ItemSerializable implements Serializable {

	String idfb, name, lat, lag, img, content, address;
	DateTime datetime;
	int voteup, votedw, status;

	public ItemSerializable(Item item) {
		super();

		this.idfb = item.getIdFB();
		this.name = item.getName();
		this.lat = item.getLat();
		this.lag = item.getLag();
		this.img = item.getImg();
		this.address = item.getAddress();
		this.datetime = item.getTime();
		this.content = item.getContent();
		this.voteup = item.getVoteUp();
		this.votedw = item.getVoteDw();
		this.status = item.getStatus();
	}

	public String getIdFB() {
		return idfb;
	}

	public String getLat() {
		return lat;
	}

	public String getLag() {
		return lag;
	}

	public String getName() {
		return name;
	}

	public String getImg() {
		return img;
	}

	public int getStatus() {
		return status;
	}

	public DateTime getTime() {
		return datetime;
	}

	public int getVoteUp() {
		return voteup;
	}

	public int getVoteDw() {
		return votedw;
	}

	public String getContent() {
		return content;
	}

	public String getAddress() {
		return address;
	}

}
