package synchronize.core;

import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.pivot.collections.HashMap;

import synchronize.gui.CategoryParent;
import synchronize.model.Category;

import com.google.gson.Gson;

public class CategoriesSingleton {
	private static CategoriesSingleton instance;
	private CategoryParent root;
	
	public static CategoriesSingleton getInstance() {
		if(instance == null)
			instance = new CategoriesSingleton();
		return instance;
	}
	
	public CategoryParent getTree() {
		return root;
	}
	
	public Category findById(int id) {
		return root.findById(id);
	}
	
	public List<Category> getPath(int id) {
		ArrayList<Category> path = new ArrayList<Category>();
		Category child = findById(id);
		path.add(child);
		while(child.getParent() != null) {
			path.add(child.getParent());
			child = child.getParent();
		}
		return path;
	}
	
	private CategoriesSingleton() {
		Category[] categories = parseJSON();
		root = makeTree(categories);
	}
	
	private CategoryParent makeTree(Category[] cats) {
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
				CategoryParent parent = parents.get(c.getParentId());
				if(parent != null) {
					c.setParent(parent);
					parents.get(parent.add(c));
				} else
					missingParent.add(c);
			}
		}
		
		for(Category cat : missingParent) {
			CategoryParent par = parents.get(cat.getParentId());
			par.add(cat);
			cat.setParent(par);
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
