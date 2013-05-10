package synchronize.gui;

import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.TreeView;

public class ResetButton implements ButtonPressListener {
	
	private TreeView tree;
	
	public ResetButton(TreeView tree) {
		this.tree = tree;
	}

	@Override
	public void buttonPressed(Button button) {
		tree.clearSelection();
	}

}
