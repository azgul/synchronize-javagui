package synchronize;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Border;
import org.apache.pivot.wtk.TreeView;

public class Categories extends Border implements Bindable {
	@BXML private TreeView categories;

	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {

    	// Categories tree view stuff
    	categories.setSelectMode(TreeView.SelectMode.MULTI);
    	categories.getComponentMouseButtonListeners().add(new NodeListener());
    	categories.getTreeViewSelectionListeners().add(new CategorySelectionListener());
	}

}
