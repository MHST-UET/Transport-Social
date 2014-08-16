package com.uet.mhst;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Vote
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String idfb;
	boolean up;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
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

	@Override
	public boolean equals(Object obj)
	{
		// TODO Auto-generated method stub
		if (obj != null && obj instanceof Vote)
		{
			Vote vote = (Vote) obj;
			if (vote.getIdfb().matches(this.idfb)) { return true; }
		}
		return false;
	}

}
