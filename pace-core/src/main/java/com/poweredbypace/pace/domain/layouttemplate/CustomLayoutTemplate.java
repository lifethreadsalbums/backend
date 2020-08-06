package com.poweredbypace.pace.domain.layouttemplate;

import java.io.IOException;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.base.Objects;
import com.poweredbypace.pace.util.JsonUtil;

@Entity
@DiscriminatorValue("CustomLayoutTemplate")
@JsonIgnoreProperties(ignoreUnknown=true)
public class CustomLayoutTemplate extends LayoutTemplate {

	private static final long serialVersionUID = -7478950819284554401L;
	
	private Object frames;
	private String span;
	private String align;
	
	@Transient
	public Object getFrames() { return frames; }
	public void setFrames(Object frames) { this.frames = frames; }
	
	@JsonIgnore
	@Column(name="FRAMES", columnDefinition = "TEXT")
	public String getFramesJson() throws JsonProcessingException {
		return JsonUtil.serialize(frames);
	}
	public void setFramesJson(String frames) throws JsonParseException, JsonMappingException, IOException {
		this.frames = JsonUtil.deserialize(frames, Object.class);
	}
	
	@Column(name = "SPAN")
	public String getSpan() { return span; }
	public void setSpan(String span) { this.span = span; }
	
	@Column(name = "ALIGN")
	public String getAlign() { return align; }
	public void setAlign(String align) { this.align = align; }
	
	@Override
	public String toString() {
		return Objects.toStringHelper(CustomLayoutTemplate.class.getSimpleName())
				.add("frames", frames)
				.add("publicTemplate", publicTemplate).toString();
	}
	
	public static class Frame implements Serializable {
		private static final long serialVersionUID = -2696351187528339840L;
		
		private Float x;
		private Float y;
		private Float width;
		private Float height;
		private Float rotation = 0F;
		
		public Float getX() { return x; }
		public void setX(Float x) { this.x = x; }
		
		public Float getY() { return y; }
		public void setY(Float y) { this.y = y; }
		
		public Float getWidth() { return width; }
		public void setWidth(Float width) { this.width = width; }
		
		public Float getHeight() { return height; }
		public void setHeight(Float height) { this.height = height; }
		
		public Float getRotation() { return rotation; }
		public void setRotation(Float angle) { this.rotation = angle; }
		
		@Override
		public String toString() {
			return Objects.toStringHelper(Frame.class.getSimpleName())
					.add("x", x)
					.add("y", y)
					.add("width", width)
					.add("height", height)
					.add("angle", rotation).toString();
		}
	}	
	
}