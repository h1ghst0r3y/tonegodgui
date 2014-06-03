/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.scrolling;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;

/**
 *
 * @author t0neg0d
 */
public class ScrollPanel extends Element {
	public Element innerBounds, scrollableArea;
	public ScrollPanelBarV vScrollBar;
	public ScrollPanelBarH hScrollBar;
	private float scrollSize = 25;
	
	public ScrollPanel(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, Vector4f.ZERO, null);
		setAsContainerOnly();
		
		innerBounds = new Element(screen, UID + "innerBounds", Vector2f.ZERO, dimensions, resizeBorders, defaultImg);	
		innerBounds.setScaleEW(true);
		innerBounds.setScaleNS(true);
		innerBounds.setDocking(Docking.SW);
		
		scrollableArea = new Element(screen, UID + "scrollableArea", Vector2f.ZERO, dimensions, Vector4f.ZERO, null);	
		scrollableArea.setScaleEW(false);
		scrollableArea.setScaleNS(false);
		scrollableArea.setDocking(Docking.NW);
		scrollableArea.setAsContainerOnly();
		
		innerBounds.addChild(scrollableArea);
		scrollableArea.setClippingLayer(innerBounds);
		addChild(innerBounds);
		
		vScrollBar = new ScrollPanelBarV(this);
		addChild(vScrollBar, true);
		hScrollBar = new ScrollPanelBarH(this);
		addChild(hScrollBar, true);
		
	}
	
	public void addScrollableContent(Element el) {
		scrollableArea.addChild(el);
		el.setClippingLayer(innerBounds);
		el.setClipPadding(innerBounds.getClipPadding());
		el.setDocking(Docking.SW);
		reshape();
	}
	
	public void removeScrollableContent(Element el) {
		scrollableArea.removeChild(el);
		reshape();
	}
	
	private void reshape() {
		scrollableArea.sizeToContent();
		scrollableArea.setY(innerBounds.getHeight()-scrollableArea.getHeight());
		setVThumbSize();
		setHThumbSize();
		innerBounds.setClipPadding(5);
		scrollableArea.setControlClippingLayer(innerBounds);
	}
	
	public void setScrollSize(float scrollSize) {
		this.scrollSize = scrollSize;
	}
	
	public float getScrollSize() {
		return this.scrollSize;
	}
	
	@Override
	public void controlResizeHook() {
		boolean vHide = false,
				vShow = false,
				hHide = false,
				hShow = false;
		boolean vResize = false,
				hResize = false;
		boolean vDir, hDir;
		
		if (getHeight() < scrollableArea.getHeight()) {
			if (innerBounds.getWidth() == getWidth())
				vResize = true;
			if (!vScrollBar.getIsVisible())
				vShow = true;
			vDir = false;
		} else {
			if (innerBounds.getWidth() == getWidth()-scrollSize)
				vResize = true;
			if (vScrollBar.getIsVisible())
				vHide = true;
			vDir = true;
		}
		
		if (getWidth() < scrollableArea.getWidth()) {
			if (innerBounds.getHeight() == getHeight())
				hResize = true;
			if (!hScrollBar.getIsVisible())
				hShow = true;
			hDir = false;
		} else {
			if (innerBounds.getHeight() == getHeight()-scrollSize)
				hResize = true;
			if (hScrollBar.getIsVisible())
				hHide = true;
			hDir = true;
		}
		
		if (vResize) {
			if (!vDir) {
				innerBounds.setWidth(getWidth()-scrollSize);
			} else {
				innerBounds.setWidth(getWidth());
			}
		}
		if (hResize) {
			if (!hDir) {
				innerBounds.setHeight(getHeight()-scrollSize);
				innerBounds.setY(scrollSize);
			} else {
				innerBounds.setHeight(getHeight());
				innerBounds.setY(0);
			}
		}
		if (vShow)		vScrollBar.show();
		else if (vHide)	vScrollBar.hide();
		if (hShow)		{
			hScrollBar.show();
			scrollableArea.setY(scrollableArea.getY()-scrollSize);
		}
		else if (hHide)	{
			hScrollBar.hide();
			scrollableArea.setY(scrollableArea.getY()+scrollSize);
		}
		
		setVThumbSize();
		setHThumbSize();
		if (scrollableArea.getWidth() > innerBounds.getWidth() && scrollableArea.getX() < 0) {
			scrollToRight();
		} else if (scrollableArea.getWidth() < innerBounds.getWidth()) {
			scrollToLeft();
		}
		setHThumbPositionToScrollArea();
		if (scrollableArea.getHeight() > innerBounds.getHeight() && scrollableArea.getY() > 0) {
			scrollToBottom();
		} else if (scrollableArea.getHeight() < innerBounds.getHeight()) {
			scrollToTop();
		}
		setVThumbPositionToScrollArea();
	}
	
	public void setScrollAreaPadding(float padding) {
		innerBounds.setClipPadding(padding);
	}
	
	//<editor-fold desc="Vertical Scrolling">
	public float getScrollableAreaVerticalPosition() {
		return innerBounds.getHeight()-(scrollableArea.getY()+scrollableArea.getHeight());
	}
	
	public float getScrollBoundsHeight() {
		return this.innerBounds.getHeight();
	}
	
	public float getScrollableAreaHeight() {
		return scrollableArea.getHeight();
	}
	
	/**
	 * Returns the height difference between the scrollable area's total height and the
	 * scroll panel's bounds.
	 * 
	 * Note: This returns a negative float value if the scrollable area is smaller than it's bounds.
	 * 
	 * @return 
	 */
	public float getVerticalScrollDistance() {
		float diff =  scrollableArea.getHeight()-innerBounds.getHeight();
		return diff;
	}
	
	public void scrollToTop() {
		scrollableArea.setY(-getVerticalScrollDistance());
		setVThumbPositionToScrollArea();
	}
	
	public void scrollToBottom() {
		scrollableArea.setY(0);
		setVThumbPositionToScrollArea();
	}
	
	public void scrollYTo(float y) {
		scrollableArea.setY(0);
	}
	
	public void scrollYBy(float incY) {
		
	}
	
	private float getVThumbRatio() {
		float ratio = innerBounds.getHeight()/scrollableArea.getHeight();
		if (ratio > 1f)
			ratio = 1f;
		return ratio;
	}
	
	public void setVThumbSize() {
		float ratio = getVThumbRatio();
		vScrollBar.thumb.setWidth(25);
		vScrollBar.thumb.setHeight(vScrollBar.track.getHeight()*ratio);
	}
	
	public void setVThumbPositionToScrollArea() {
		float relY = (FastMath.abs(scrollableArea.getY())/getVerticalScrollDistance());
		vScrollBar.thumb.setY((vScrollBar.track.getHeight()-vScrollBar.thumb.getHeight())*relY);
	}
	
	public void setScrollAreaPositionToVThumb() {
		float relY = (vScrollBar.thumb.getY()/(vScrollBar.track.getHeight()-vScrollBar.thumb.getHeight()));
		scrollableArea.setY(-(getVerticalScrollDistance()*relY));
	}
	//</editor-fold>
	
	//<editor-fold desc="Horizontal Scrolling">
	public float getScrollableAreaHorizontalPosition() {
		return innerBounds.getWidth()-(scrollableArea.getX()+scrollableArea.getWidth());
	}
	
	public float getScrollBoundsWidth() {
		return this.innerBounds.getWidth();
	}
	
	public float getScrollableAreaWidth() {
		return scrollableArea.getWidth();
	}
	
	/**
	 * Returns the width difference between the scrollable area's total width and the
	 * scroll panel's bounds.
	 * 
	 * Note: This returns a negative float value if the scrollable area is smaller than it's bounds.
	 * 
	 * @return 
	 */
	public float getHorizontalScrollDistance() {
		float diff =  scrollableArea.getWidth()-innerBounds.getWidth();
		return diff;
	}
	
	public void scrollToLeft() {
		scrollableArea.setX(0);
		setHThumbPositionToScrollArea();
	}
	
	public void scrollToRight() {
		scrollableArea.setX(-getHorizontalScrollDistance());
		setHThumbPositionToScrollArea();
	}
	
	public void scrollXTo(float x) {
		scrollableArea.setX(0);
	}
	
	public void scrollXBy(float incX) {
		
	}
	
	private float getHThumbRatio() {
		float ratio = innerBounds.getWidth()/scrollableArea.getWidth();
		if (ratio > 1f)
			ratio = 1f;
		return ratio;
	}
	
	public void setHThumbSize() {
		float ratio = getHThumbRatio();
		hScrollBar.thumb.setHeight(25);
		hScrollBar.thumb.setWidth(hScrollBar.track.getWidth()*ratio);
	}
	
	public void setHThumbPositionToScrollArea() {
		float relX = (FastMath.abs(scrollableArea.getX())/getHorizontalScrollDistance());
		hScrollBar.thumb.setX((hScrollBar.track.getWidth()-hScrollBar.thumb.getWidth())*relX);
	}
	
	public void setScrollAreaPositionToHThumb() {
		float relX = (hScrollBar.thumb.getX()/(hScrollBar.track.getWidth()-hScrollBar.thumb.getWidth()));
		scrollableArea.setX(-(getHorizontalScrollDistance()*relX));
	}
	//</editor-fold>
}