package com.poweredbypace.pace.domain.layout;

import java.util.Arrays;
import java.util.List;

public class PageRangeValue {
	
	public static class PageRangeValueCollection {

		private List<PageRangeValue> values;
		
		public List<PageRangeValue> getValues() {
			return values;
		}
		public void setValues(List<PageRangeValue> values) {
			this.values = values;
		}

		public PageRangeValueCollection() {}
		
		public PageRangeValueCollection(List<PageRangeValue> values) {
			this.values = values;
		}
		
		public PageRangeValueCollection(PageRangeValue[] values) {
			this.values = Arrays.asList(values);
		}
		
		public float getValue(int numPages)
		{
			return getValue(numPages, 0.0f);
		}
		
		public float getValue(int numPages, float defaultValue)
		{
			float value = defaultValue;
			for(PageRangeValue item:values)
			{
				if (numPages>=item.getFrom() && numPages<=item.getTo())
				{
					value = item.getValue();
					break;
				}
			}
			return value;
		}
		
	}

	private int from;
	private int to;
	private float value;
	
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	
	
}