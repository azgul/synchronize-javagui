package synchronize;

import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
		parseJSON();
		
		reset.getButtonPressListeners().add(new ResetButton(categories));
	}
	
	private void parseJSON() {
		
	}

}
