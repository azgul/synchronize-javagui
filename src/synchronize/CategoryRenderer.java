package synchronize;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import org.apache.pivot.collections.Dictionary;
import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.wtk.Dimensions;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.TreeView.NodeCheckState;
import org.apache.pivot.wtk.content.TreeBranch;
import org.apache.pivot.wtk.content.TreeViewNodeRenderer;
import org.apache.pivot.wtk.media.Image;

public class CategoryRenderer extends TreeViewNodeRenderer {


	@Override
	public void render(Object node, Path path, int rowIndex, TreeView treeView,
			boolean expanded, boolean selected, NodeCheckState checkState, boolean highlighted,
			boolean disabled) {
		if (node != null) {
            Image icon = null;
            String text = null;

            if (node instanceof Category) {
                Category catNode = (Category)node;

                /*if (expanded
                    && catNode instanceof CategoryParent) {
                    CategoryParent catParent = (CategoryParent)catNode;
                    //icon = Image.load(new URL())

                    if (icon == null) {
                        icon = treeBranch.getIcon();
                    }
                } else {
                    icon = treeNode.getIcon();
                }*/

                text = catNode.getName();
            } else {
                text = node.toString();
            }

            // Update the image view
            /*imageView.setImage(icon);
            imageView.getStyles().put("opacity",
                (treeView.isEnabled() && !disabled) ? 1.0f : 0.5f);*/

            // Update the label
            label.setText(text != null ? text : "");

            if (text == null) {
                label.setVisible(false);
            } else {
                label.setVisible(true);

                Font font = (Font)treeView.getStyles().get("font");
                label.getStyles().put("font", font);

                Color color;
                if (treeView.isEnabled() && !disabled) {
                    if (selected) {
                        if (treeView.isFocused()) {
                            color = (Color)treeView.getStyles().get("selectionColor");
                        } else {
                            color = (Color)treeView.getStyles().get("inactiveSelectionColor");
                        }
                    } else {
                        color = (Color)treeView.getStyles().get("color");
                    }
                } else {
                    color = (Color)treeView.getStyles().get("disabledColor");
                }

                label.getStyles().put("color", color);
            }
        }
	}

	@Override
	public String toString(Object node) {
		return node.toString();
	}
	
}
