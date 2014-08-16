package com.uet.mhst;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.GeoPt;

@Entity
public class Comment
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String idfb, content;
	Date datetime;
	GeoPt point;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Date getTime()
	{
		return datetime;
	}

	public void setTime(Date datetime)
	{
		this.datetime = datetime;
	}

	public String getIdfb()
	{
		return idfb;
	}

	public void setIdfb(String idfb)
	{
		this.idfb = idfb;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public GeoPt getPoint()
	{
		return point;
	}

	public void setPoint(GeoPt point)
	{
		this.point = point;
	}

	@Override
	public boolean equals(Object obj)
	{
		// TODO Auto-generated method stub
		if (obj != null && obj instanceof Comment)
		{
			Comment cm = (Comment) obj;
			if (cm.getId().compareTo(this.id) == 0) { return true; }
		}
		return false;
	}

}