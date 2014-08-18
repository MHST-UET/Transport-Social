package com.uet.mhst;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.users.User;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@Api(name = "itemendpoint", namespace = @ApiNamespace(ownerDomain = "uet.com", ownerName = "uet.com", packagePath = "mhst"))
public class ItemEndpoint
{

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities
	 *         persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listItem")
	public CollectionResponse<Item> listItem(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit,
			@Nullable @Named("time") Date time)
	{

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Item> execute = null;

		try
		{
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Item.class);
			if (time != null)
			{
				query.setFilter("datetime<time");
				query.declareImports("import java.util.Date");
				query.declareParameters("Date time");
			}
			query.setOrdering("datetime desc");
			if (cursorString != null && cursorString != "")
			{
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null)
			{
				query.setRange(0, limit);
			}

			execute = (List<Item>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null) cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (Item obj : execute)
				;
		}
		finally
		{
			mgr.close();
		}

		return CollectionResponse.<Item> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET
	 * method.
	 * 
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getItem")
	public Item getItem(@Named("id") Long id)
	{
		PersistenceManager mgr = getPersistenceManager();
		Item item = null;
		try
		{
			item = mgr.getObjectById(Item.class, id);
		}
		finally
		{
			mgr.close();
		}
		return item;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity
	 * already exists in the datastore, an exception is thrown. It uses HTTP
	 * POST method.
	 * 
	 * @param item
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertItem", clientIds = { Ids.WEB_CLIENT_ID,
			Ids.ANDROID_CLIENT_ID,
			com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID }, audiences = {
			Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID }, scopes = {
			"https://www.googleapis.com/auth/userinfo.email",
			"https://www.googleapis.com/auth/userinfo.profile" })
	public Item insertItem(Item item, User user) throws UnauthorizedException
	{
		if (user == null) throw new UnauthorizedException("User is Not Valid");
		PersistenceManager mgr = getPersistenceManager();
		try
		{
			if (item.getId() != null)
			{
				if (containsItem(item)) { throw new EntityExistsException(
						"Object already exists"); }
			}
			mgr.makePersistent(item);
		}
		finally
		{
			mgr.close();
		}
		return item;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does
	 * not exist in the datastore, an exception is thrown. It uses HTTP PUT
	 * method.
	 * 
	 * @param item
	 *            the entity to be updated.
	 * @return The updated entity.
	 */

	public Item updateItem(Item item)
	{
		PersistenceManager mgr = getPersistenceManager();
		try
		{
			if (!containsItem(item)) { throw new EntityNotFoundException(
					"Object does not exist"); }
			mgr.makePersistent(item);
		}
		finally
		{
			mgr.close();
		}
		return item;
	}

	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 */

	public void removeItem(@Named("id") Long id)
	{
		PersistenceManager mgr = getPersistenceManager();
		try
		{
			Item item = mgr.getObjectById(Item.class, id);
			mgr.deletePersistent(item);
		}
		finally
		{
			mgr.close();
		}
	}

	private boolean containsItem(Item item)
	{
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try
		{
			mgr.getObjectById(Item.class, item.getId());
		}
		catch (javax.jdo.JDOObjectNotFoundException ex)
		{
			contains = false;
		}
		finally
		{
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager()
	{
		return PMF.get().getPersistenceManager();
	}

	@ApiMethod(name = "vote")
	public void vote(@Named("idstt") Long idstt, Vote vote)
	{
		PersistenceManager mgr = getPersistenceManager();
		Item item = null;
		try
		{
			item = mgr.getObjectById(Item.class, idstt);
			List<Vote> vt = item.getVote();
			if (!vt.contains(vote))
				vt.add(vote);
			else if (vt.contains(vote)) vt.remove(vote);
			item.setVote(vt);
			mgr.makePersistent(item);
//			updateItem(item);
		}
		finally
		{
			mgr.close();
		}
//		Item item = getItem(idstt);
		
	}

	@ApiMethod(name = "comment")
	public void comment(@Named("idstt") Long idstt, Comment cm)
	{
		PersistenceManager mgr = getPersistenceManager();
		Item item = null;
		try
		{
			item = mgr.getObjectById(Item.class, idstt);
			List<Comment> _cm = item.getComment();
			if (!_cm.contains(cm))
				_cm.add(cm);
			else if (_cm.contains(cm)) _cm.remove(cm);
			item.setComment(_cm);
			mgr.makePersistent(item);
//			updateItem(item);
		}
		finally
		{
			mgr.close();
		}
//		Item item = getItem(idstt);
		
	}
}
