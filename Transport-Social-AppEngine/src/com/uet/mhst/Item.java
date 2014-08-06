package com.uet.mhst;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Item {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Long id;
	@Persistent
	String idfb, name, lat, lag, img, content, address;
	@Persistent
	Date datetime;
	int voteup, votedw, status;
	
	// id: khóa chính trên datastore
	// idfb: id người dùng facebook
	// name: tên
	// lat: vĩ độ
	// lag: kinh độ
	// status: trạng thái đoạn đường (1-Tắc, 2-Đông, 3-Tai nạn, 4-OK)
	// img: link image(tạm thời chưa dùng)
	// content: nội dung thêm
	// voteup, votedw dùng cho Vote up, Vote down
	// datetime: thời gian lúc up
	
	public Item() {
		voteup = 0;
		votedw = 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdFB() {
		return idfb;
	}

	public void setIdFB(String idfb) {
		this.idfb = idfb;
	}
	
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}
	
	public String getLag() {
		return lag;
	}

	public void setLag(String lag) {
		this.lag = lag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
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

	public int getVoteUp() {
		return voteup;
	}
	
	public void voteUp()
	{
		voteup++;
	}
	
	public void voteDw()
	{
		votedw++;
	}

	public int getVoteDw() {
		return votedw;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
