package com.uet.mhst;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Comment
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Key id;
	String idfb, content;
	Date datetime;
	GeoPt point;
	Item item;

	public Key getId()
	{
		return id;
	}

	public void setId(Key id)
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

	public Item getItem()
	{
		return item;
	}

	public void setItem(Item item)
	{
		this.item = item;
	}

	@Override
	public boolean equals(Object obj)
	{
		// TODO Auto-generated method stub
		if (this.id == null) return false;
		if (obj != null && obj instanceof Comment)
		{
			Comment cm = (Comment) obj;
			if (cm.getId().compareTo(this.id) == 0) return true;
		}
		return false;
	}

}