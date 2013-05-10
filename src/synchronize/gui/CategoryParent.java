package synchronize.gui;

import java.util.Comparator;
import java.util.Iterator;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.ListListener;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.ListenerList;
import org.apache.pivot.wtk.media.Image;

import synchronize.api.Category;

public class CategoryParent extends Category implements List<Category> {
	
	protected transient ArrayList<Category> children;
    private ListListenerList<Category> listListeners = new ListListenerList<Category>();
    
    protected transient Image expandedIcon;
	
	public CategoryParent(Category category) {
		this();
		id = category.getId();
		parentId = category.getParentId();
		name = category.getName();
		image = category.getImage();
	}
	
	public CategoryParent() {
		children = new ArrayList<Category>();
	}

	@Override
	public Category get(int index) {
		if(children.getLength() > index)
			return children.get(index);
		else
			return null;
	}

	@Override
	public int indexOf(Category category) {
		return children.indexOf(category);
	}

	@Override
	public int remove(Category category) {
		return children.remove(category);
	}

	@Override
	public Comparator<Category> getComparator() {
		if(children.getComparator() == null) {
			Comparator<Category> comp = new Comparator<Category>() {
	
				@Override
				public int compare(Category cat1, Category cat2) {
					return cat1.getId() - cat2.getId();
				}
				
			};
			children.setComparator(comp);
		}
		return children.getComparator();
	}

	@Override
	public boolean isEmpty() {
		return children.isEmpty();
	}

	@Override
	public Iterator<Category> iterator() {
		return children.iterator();
	}

	@Override
	public int add(Category category) {
		return children.add(category);
	}

	@Override
	public void clear() {
		children.clear();
	}

	@Override
	public int getLength() {
		return children.getLength();
	}

	@Override
	public ListenerList<ListListener<Category>> getListListeners() {
		return listListeners;
	}

	@Override
	public void insert(Category category, int index) {
		children.insert(category, index);
	}

	@Override
	public Sequence<Category> remove(int index1, int index2) {
		return children.remove(index1, index2);
	}

	@Override
	public void setComparator(Comparator<Category> comparator) {
		children.setComparator(comparator);
	}

	@Override
	public Category update(int index, Category category) {
		return children.update(index, category);
	}

}
