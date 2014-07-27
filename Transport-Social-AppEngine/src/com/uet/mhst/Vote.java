package com.uet.mhst;

import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

@Api(name = "vote", namespace = @ApiNamespace(ownerDomain = "uet.com", ownerName = "uet.com", packagePath = "mhst"))
public class Vote
{
	@ApiMethod(name = "voteUp")
	public void voteUp(@Named("id") Long id)
	{
		ItemEndpoint endpoint = new ItemEndpoint();
		Item item = endpoint.getItem(id);
		item.voteUp();
		endpoint.updateItem(item);
	}
	@ApiMethod(name = "voteDw")
	public void voteDw(@Named("id") Long id)
	{
		ItemEndpoint endpoint = new ItemEndpoint();
		Item item = endpoint.getItem(id);
		item.voteDw();
		endpoint.updateItem(item);
	}
}
