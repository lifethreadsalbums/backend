package com.poweredbypace.pace.util;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.poweredbypace.pace.domain.layout.Element;

public class GeomUtils {
	
	public static Rectangle2D.Float getBoundingBox(Element element) {
		
		float x = Numbers.valueOf(element.getX());
		float y = Numbers.valueOf(element.getY());
		float width = Numbers.valueOf(element.getWidth());
		float height = Numbers.valueOf(element.getHeight());
		
		double angleRad = Math.toRadians( Numbers.valueOf(element.getRotation()) );
		AffineTransform trans = new AffineTransform();
		trans.translate(x, y);
		trans.rotate(angleRad);
		
		//calculate frame bounds
		Point2D.Float p1 = new Point2D.Float(), 
				p2 = new Point2D.Float(), 
				p3 = new Point2D.Float(), 
				p4 = new Point2D.Float();
		trans.transform(new Point2D.Float(0, 0), p1);
		trans.transform(new Point2D.Float(width, 0), p2);
		trans.transform(new Point2D.Float(width, height), p3);
		trans.transform(new Point2D.Float(0, height), p4);
		
		//calculate bounding box
		float minx = Math.min( Math.min(p1.x, p2.x), Math.min(p3.x, p4.x) );
		float maxx = Math.max( Math.max(p1.x, p2.x), Math.max(p3.x, p4.x) );
		float miny = Math.min( Math.min(p1.y, p2.y), Math.min(p3.y, p4.y) );
		float maxy = Math.max( Math.max(p1.y, p2.y), Math.max(p3.y, p4.y) );

		return new Rectangle2D.Float(minx, miny, maxx - minx, maxy - miny);
	}

}
