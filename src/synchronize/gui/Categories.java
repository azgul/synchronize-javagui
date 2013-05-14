package synchronize.gui;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.Theme;
import org.apache.pivot.wtk.TreeView;

public class Categories extends TablePane implements Bindable {
	@BXML private TreeView categories;
	@BXML private PushButton reset;
	
	public Categories(Sequence<Column> content) {
		super(content);
        Theme.getTheme().set(TreeView.class, SyncTreeViewSkin.class);
	}
	
	public Categories() {
		super();
        Theme.getTheme().set(TreeView.class, SyncTreeViewSkin.class);
	}

	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		categories.setNodeRenderer(new CategoryRenderer());
		categories.setTreeData(CategoriesSingleton.getInstance().getTree());
		
		reset.getButtonPressListeners().add(new ResetButton(categories));
	}
	
	

}