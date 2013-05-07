package synchronize;

import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.Mouse.Button;
import org.apache.pivot.wtk.TreeView;

public class NodeListener implements ComponentMouseButtonListener {

	@Override
	public boolean mouseClick(Component component, Button button, int x, int y,
			int count) {
		
		TreeView view = (TreeView)component;
		Path path = view.getNodeAt(y);
		
		//TreeNode node = (TreeNode)Sequence.Tree.get(view.getTreeData(), path);
		if(view.getSelectedPaths().indexOf(path) != -1)
			System.out.println("Mouse clicked on selected path");
		else
			System.out.println("Mouse clicked on not selected path");
		return true;
	}

	@Override
	public boolean mouseDown(Component arg0, Button arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseUp(Component arg0, Button arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

}
