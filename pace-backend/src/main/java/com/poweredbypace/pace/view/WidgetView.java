package com.poweredbypace.pace.view;

import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.poweredbypace.pace.domain.widget.Widget;

public class WidgetView extends AbstractView {
	
	private Widget widget;

	public WidgetView(Widget w) {
		super();
		this.widget = w;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Set the content type.
		response.setContentType(getContentType());

		ServletOutputStream out = response.getOutputStream();
		out.println(widget.render());
		out.flush();

	}

}
