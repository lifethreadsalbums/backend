package com.poweredbypace.pace.event;

import java.util.List;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.SpreadComment;

public class ProoferPhotographerUnreadRepliesEvent extends ProoferUnreadRepliesEvent {
	
	public ProoferPhotographerUnreadRepliesEvent() { }

	public ProoferPhotographerUnreadRepliesEvent(Product p, List<SpreadComment> replies) {
		super(p, replies);
	}
}
