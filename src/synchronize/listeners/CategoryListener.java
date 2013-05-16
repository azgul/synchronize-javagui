package synchronize.listeners;

import synchronize.model.Category;

public interface CategoryListener {
	public void onSelect(Category category);
	public void onNoneSelected();
}
