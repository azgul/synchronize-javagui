package synchronize.listeners;

import java.util.HashSet;

import org.apache.pivot.collections.Sequence;
import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.TreeViewSelectionListener;

import synchronize.core.SearcherSingleton;
import synchronize.core.UIObservers;
import synchronize.gui.CategoryParent;
import synchronize.model.Category;

public class CategorySelection extends TreeViewSelectionListener.Adapter {

	@Override
	public void selectedPathAdded(TreeView treeView, Path path) {
		Category c = (Category)Sequence.Tree.get(treeView.getTreeData(), path);
			
		for(CategoryListener cl : UIObservers.getCategoryListeners()){
			cl.onSelect(c);
		}
	}
	
	@Override
	public void selectedPathsChanged(TreeView treeView, Sequence<Path> previousSelection) {
		HashSet<Integer> categories = new HashSet<Integer>();
		for(Path p : treeView.getSelectedPaths()) {
			Category node = (Category)Sequence.Tree.get(treeView.getTreeData(), p);
			addCategories(categories,node);
		}
		System.out.println("Before search");
		SearcherSingleton.getInstance().search(null, categories);
		System.out.println("After search");
	}

	@Override
	public void selectedPathRemoved(TreeView treeView, Path path) {
		if(treeView.getSelectedPaths().getLength() == 0) {
			for(CategoryListener cl : UIObservers.getCategoryListeners())
				cl.onNoneSelected();
		} else {
			Category c = (Category)Sequence.Tree.get(treeView.getTreeData(), treeView.getSelectedPath());
				
			for(CategoryListener cl : UIObservers.getCategoryListeners()){
				cl.onSelect(c);
			}
		}
	}

	private void addCategories(HashSet<Integer> set, Category node) {
		set.add(node.getId());
		if(node instanceof CategoryParent) {
			CategoryParent parent = (CategoryParent)node;
			for(Category child : parent) {
				addCategories(set, child);
			}
		}
			
	}

}
