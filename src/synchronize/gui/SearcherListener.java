package synchronize.gui;

import java.util.List;

import pdfsearch.SearchResult;

public interface SearcherListener {
	public void onSearch(List<SearchResult> items);
}
