package synchronize.gui;

import java.util.HashSet;

import org.apache.pivot.collections.Sequence;
import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.collections.immutable.ImmutableList;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Mouse.Button;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.skin.terra.TerraTreeViewSkin;

import synchronize.core.SearcherSingleton;
import synchronize.model.Category;


public class SyncTreeViewSkin extends TerraTreeViewSkin {
	
	private boolean started = false;
	private boolean selectedBefore = false;
	private ImmutableList<Path> selection;

	@Override
	public boolean mouseDown(Component component, Button button, int x, int y) {
		TreeView view = (TreeView)component;
		Path path = view.getNodeAt(y);
		
		selectedBefore = false;
		selection = view.getSelectedPaths();
		if(view.getSelectedPaths().indexOf(path) != -1)
			selectedBefore = true;
		
		boolean consumed = super.mouseDown(component, button, x, y);
		started = !consumed;
		return consumed;
	}
	
	@Override
	public boolean mouseUp(Component component, Button button, int x, int y) {
		boolean consumed = super.mouseUp(component, button, x, y);
		if(started) {
			TreeView view = (TreeView)component;
			Path path = view.getNodeAt(y);
			
			view.setSelectedPaths(selection);
			
			if(selectedBefore)
				view.removeSelectedPath(path);
			else
				view.addSelectedPath(path);
			
			HashSet<Integer> categories = new HashSet<Integer>();
			for(Path p : view.getSelectedPaths()) {
				Category node = (Category)Sequence.Tree.get(view.getTreeData(), p);
				addCategories(categories,node);
			}
			SearcherSingleton.getInstance().search(null, categories);
		}
		
		return consumed;
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
