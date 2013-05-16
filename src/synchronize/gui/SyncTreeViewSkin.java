package synchronize.gui;

import org.apache.pivot.collections.Sequence;
import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Mouse;
import org.apache.pivot.wtk.Mouse.Button;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.skin.terra.TerraTreeViewSkin;

import synchronize.model.Category;


public class SyncTreeViewSkin extends TerraTreeViewSkin {
    private int spacing = 6;
    private int indent = 16;
    private boolean showBranchControls = true;
    
	@Override
	public boolean mouseDown(Component component, Button button, int x, int y) {
        boolean consumed = false;
        TreeView treeView = (TreeView)getComponent();
        Path path = treeView.getNodeAt(y);
		Category nodeInfo = (Category)Sequence.Tree.get(treeView.getTreeData(), path);

        if (nodeInfo != null) {
            int baseNodeX = (nodeInfo.getDepth() - 1) * (indent + spacing);
            System.out.println("NodeX: " + baseNodeX);

            // See if the user clicked on an expand/collapse control of
            // a branch. If so, expand/collapse the branch
            if(nodeInfo instanceof CategoryParent)
            	System.out.println("Parent");
            else
            	System.out.println("Child");
            if (showBranchControls
                && nodeInfo instanceof CategoryParent
                && x >= baseNodeX
                && x < baseNodeX + indent) {

                boolean isExpanded = treeView.isBranchExpanded(path);
                treeView.setBranchExpanded(path, !isExpanded);
                consumed = true;
            }

            // If we haven't consumed the event, then proceed to manage
            // the selection state of the node
            if (!consumed) {

                if (button == Mouse.Button.LEFT) {

                    if(treeView.isNodeSelected(path))
                    	treeView.removeSelectedPath(path);
                    else
                    	treeView.addSelectedPath(path);
                    
                }
            }
            treeView.requestFocus();
        }
		return consumed;
	}
	
	@Override
	public boolean mouseUp(Component component, Button button, int x, int y) {
		return false;
	}
	
	
}
