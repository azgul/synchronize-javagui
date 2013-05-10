package synchronize.gui;

import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.collections.immutable.ImmutableList;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Mouse.Button;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.skin.terra.TerraTreeViewSkin;

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
		}
		
		return consumed;
	}
	
}
