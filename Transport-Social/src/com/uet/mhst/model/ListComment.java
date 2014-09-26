package com.uet.mhst.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.uet.mhst.itemendpoint.model.Comment;

public class ListComment implements Serializable {
	private List<Cmt> listComments;
	private Long id;

	public ListComment() {
		super();
		// TODO Auto-generated constructor stub
		listComments = new ArrayList<Cmt>();
	}

	public ListComment(List<Comment> listComments, Long id) {
		super();
		this.listComments = new ArrayList<Cmt>();
		if (listComments != null) {

			for (int i = 0; i < listComments.size(); i++) {
				this.listComments.add(new Cmt(listComments.get(i)));
			}
		}
		this.id = id;
	}

	public void add(Comment comment) {

		this.listComments.add(new Cmt(comment));
	}

	public List<Cmt> getListComments() {
		return listComments;
	}

	public void setListComments(List<Cmt> listComments) {
		this.listComments = listComments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
