package com.uet.mhst;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Vote
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Key id;
	String idfb;
	boolean up;
	Item item;

	public Key getId()
	{
		return id;
	}

	public void setId(Key id)
	{
		this.id = id;
	}

	public String getIdfb()
	{
		return idfb;
	}

	public void setIdfb(String idfb)
	{
		this.idfb = idfb;
	}

	public boolean isUp()
	{
		return up;
	}

	public void setUp(boolean up)
	{
		this.up = up;
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
		if (obj != null && obj instanceof Vote)
		{
			Vote vote = (Vote) obj;
			if (vote.getIdfb().matches(this.idfb)) return true;
		}
		return false;
	}

}
