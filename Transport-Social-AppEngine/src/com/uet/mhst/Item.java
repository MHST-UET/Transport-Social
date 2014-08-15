package com.uet.mhst;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Item
{
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key id;
	@Persistent
	String idfb, name, img, content, address;
	GeoPt point;
	Date datetime;
	int status;
	List<Vote> vote = new ArrayList<Vote>();
	@Order(extensions = @Extension(vendorName="datanucleus",key="list-ordering", value="datetime asc"))
	List<Comment> comment = new ArrayList<Comment>();

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

	public Key getId()
	{
		return id;
	}

	public void setId(Key id)
	{
		this.id = id;
	}

	public String getIdFB()
	{
		return idfb;
	}

	public void setIdFB(String idfb)
	{
		this.idfb = idfb;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getImg()
	{
		return img;
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public Date getTime()
	{
		return datetime;
	}

	public void setTime(Date datetime)
	{
		this.datetime = datetime;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public GeoPt getPoint()
	{
		return point;
	}

	public void setPoint(GeoPt point)
	{
		this.point = point;
	}

	public List<Vote> getVote()
	{
		return vote;
	}

	public void setVote(List<Vote> vote)
	{
		this.vote = vote;
	}

	public List<Comment> getComment()
	{
		return comment;
	}

	public void setComment(List<Comment> comment)
	{
		this.comment = comment;
	}

}
