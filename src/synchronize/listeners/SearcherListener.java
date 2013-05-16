package synchronize.listeners;

import java.util.List;

import synchronize.pdfsearch.SearchResult;

public interface SearcherListener {
	public void onSearch(List<SearchResult> items);
}
