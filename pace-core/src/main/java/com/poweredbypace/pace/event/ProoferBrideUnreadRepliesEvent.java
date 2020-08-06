package com.poweredbypace.pace.event;

import java.util.List;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.SpreadComment;

public class ProoferBrideUnreadRepliesEvent extends ProoferUnreadRepliesEvent {
	
	public ProoferBrideUnreadRepliesEvent() { }

	public ProoferBrideUnreadRepliesEvent(Product p, List<SpreadComment> replies) {
		super(p, replies);
	}
}
