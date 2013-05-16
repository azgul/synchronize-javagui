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
		System.out.println("After mousedown");
		return consumed;
	}
	
	@Override
	public boolean mouseUp(Component component, Button button, int x, int y) {
		System.out.println("Before mouseup");
		boolean consumed = super.mouseUp(component, button, x, y);
		System.out.println("After super mouseup");
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
			System.out.println("Before search");
			SearcherSingleton.getInstance().search(null, categories);
			System.out.println("After search");
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
