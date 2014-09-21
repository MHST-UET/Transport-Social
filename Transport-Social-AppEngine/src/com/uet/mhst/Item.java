package com.uet.mhst;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Item {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Key id;
	@Persistent
	String idfb, content, address, name;
	Double latitude, longitude;
	Date datetime;
	int status;
	@Persistent(mappedBy = "item", defaultFetchGroup = "true")
	@Element(dependent = "true")
	List<Comment> comment = new ArrayList<Comment>();
	@Persistent(mappedBy = "item", defaultFetchGroup = "true")
	@Element(dependent = "true")
	List<Vote> vote = new ArrayList<Vote>();

	// id: khóa chính trên datastore
	// idfb: id người dùng facebook
	// name: tên
	// latitude: vĩ độ
	// longitude: kinh độ
	// status: trạng thái đoạn đường (1-Tắc, 2-Đông, 3-Tai nạn, 4-OK)
	// img: link image(tạm thời chưa dùng)
	// content: nội dung thêm
	// voteup, votedw dùng cho Vote up, Vote down
	// datetime: thời gian lúc up
	// address: địa điểm trên bản đồ ứng với tọa độ GPS

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public String getIdFB() {
		return idfb;
	}

	public void setIdFB(String idfb) {
		this.idfb = idfb;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getTime() {
		return datetime;
	}

	public void setTime(Date datetime) {
		this.datetime = datetime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Vote> getVote() {
		if (vote == null)
			vote = new ArrayList<Vote>();
		return vote;
	}

	public void setVote(List<Vote> vote) {
		this.vote = vote;
	}

	public List<Comment> getComment() {
		if (comment == null)
			comment = new ArrayList<Comment>();
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
