package com.poweredbypace.pace.event;

import java.util.List;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.SpreadComment;

public class ProoferUnreadRepliesEvent extends ProoferEvent {
	
	private List<SpreadComment> replies;
	
	public List<SpreadComment> getReplies() {
		return replies;
	}

	public void setReplies(List<SpreadComment> replies) {
		this.replies = replies;
	}

	public ProoferUnreadRepliesEvent() { }

	public ProoferUnreadRepliesEvent(Product p, List<SpreadComment> replies) {
		super(p);
		this.replies = replies;
	}
}
