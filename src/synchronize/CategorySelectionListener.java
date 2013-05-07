package synchronize;

import org.apache.pivot.collections.Sequence;
import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.TreeViewSelectionListener;

public class CategorySelectionListener implements TreeViewSelectionListener {

	@Override
	public void selectedNodeChanged(TreeView tree, Object previous) {
		System.out.println("Testing" + previous.getClass().getName());
	}

	@Override
	public void selectedPathAdded(TreeView arg0, Path arg1) {
		System.out.println("Test2");
	}

	@Override
	public void selectedPathRemoved(TreeView arg0, Path arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectedPathsChanged(TreeView tree, Sequence<Path> previousPath) {
		System.out.println("Another test");
		
		/*
		for(Path selected : tree.getSelectedPaths()) {
			previousPath.add(selected);
		}
		tree.setSelectedPaths(previousPath);*/
	}

}
