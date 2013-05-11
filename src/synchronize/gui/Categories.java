package synchronize.gui;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.HashMap;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.Theme;
import org.apache.pivot.wtk.TreeView;

import synchronize.api.Category;

import com.google.gson.Gson;

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
		// read categories from json
		categories.setNodeRenderer(new CategoryRenderer());
		categories.getTreeViewSelectionListeners().add(new CategorySearchFilter());
		Category[] cats = parseJSON();
		List<Category> data = makeList(cats);
		categories.setTreeData(data);
		
		reset.getButtonPressListeners().add(new ResetButton(categories));
	}
	
	private List<Category> makeList(Category[] cats) {
		CategoryParent root = getRootCategory();
		ArrayList<Category> missingParent = new ArrayList<Category>();
		// build parent list
		HashMap<Integer,CategoryParent> parents = new HashMap<Integer,CategoryParent>();
		for(Category cat : cats) {
			if(cat.getParentId() != 0) {
				CategoryParent tmp = null;
				parents.put(cat.getParentId(),tmp);
			}
		}
		
		for(Category cat : cats) {
			Category c;
			if(parents.containsKey(cat.getId())) {
				CategoryParent parent = new CategoryParent(cat);
				c = parent;
				parents.put(cat.getId(), parent);
			} else {
				c = cat;
			}
			
			if(cat.getParentId() == 0) {
				root.add(c);
			} else {
				if(parents.get(cat.getParentId()) != null)
					parents.get(cat.getParentId()).add(c);
				else
					missingParent.add(c);
			}
		}
		
		for(Category cat : missingParent) {
			parents.get(cat.getParentId()).add(cat);
		}
		
		return root;
	}
	
	private CategoryParent getRootCategory() {
		Gson gson = new Gson();
		return gson.fromJson("{'name':'Root','image':'','id':0,'parentId':0}", CategoryParent.class);
	}
	
	private Category[] parseJSON() {
		Gson gson = new Gson();
		Path sample = FileSystems.getDefault().getPath("res", "sample-data", "categories-sample.json");
		try {
			return gson.fromJson(Files.newBufferedReader(sample, Charset.forName("UTF-8")), Category[].class);
		} catch(Exception e) {
			e.printStackTrace();
			return new Category[0];
		}
	}

}