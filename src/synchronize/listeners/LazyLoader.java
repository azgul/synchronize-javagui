package synchronize.listeners;

import org.apache.pivot.wtk.Viewport;
import org.apache.pivot.wtk.ViewportListener;

import synchronize.gui.SearchResults;

public class LazyLoader extends ViewportListener.Adapter {

	@Override
	public void scrollTopChanged(Viewport scrollPane, int previousScrollTop) {
		SearchResults results = (SearchResults)scrollPane;
		int maxScroll = results.getView().getHeight() - results.getHeight();
		int buffer = results.getHeight();
		// TODO: this adds multiple times for the first scroll
		if(results.getScrollTop() > maxScroll - buffer) {
			results.onSearch();
		}
	}

}
