package tonegod.gui.controls.extras;

import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;

public abstract class DropElement extends Element
{
	private boolean allowSwapping;
	private boolean readOnly;

	public DropElement(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String texturePath, boolean allowSwapping, boolean readOnly)
	{
		super(screen, UID, position, dimensions, resizeBorders, texturePath);
		this.allowSwapping = allowSwapping;
		this.readOnly = readOnly;
	}

	public DropElement(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String texturePath, boolean allowSwapping, boolean readOnly)
	{
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, texturePath, allowSwapping, readOnly);
	}

	public boolean isAllowSwapping()
	{
		return allowSwapping;
	}

	public boolean isReadOnly()
	{
		return readOnly;
	}

	public void setAllowSwapping(boolean allowSwapping)
	{
		this.allowSwapping = allowSwapping;
	}

	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}

	public abstract void onDragReceived(DragElement dragElement, Element previousParent);

	public abstract void onDragLost(DragElement dragElement, Element nextParent);
}
