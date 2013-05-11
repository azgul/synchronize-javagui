package synchronize.gui;

import java.util.ArrayList;

import org.apache.pivot.collections.Sequence;
import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.TreeViewSelectionListener;

import synchronize.api.Category;



public class CategorySearchFilter extends TreeViewSelectionListener.Adapter {

	@Override
	public void selectedPathsChanged(TreeView treeView,
			Sequence<Path> previousSelectedPaths) {
		ArrayList<Integer> categories = new ArrayList<Integer>();
		for(Path p : treeView.getSelectedPaths()) {
			Category node = (Category)Sequence.Tree.get(treeView.getTreeData(), p);
			categories.add(node.getId());
		}
		SearcherSingleton.getInstance().search(null, categories);
	}
	
	
	
}
