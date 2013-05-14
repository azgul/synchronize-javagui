package synchronize.gui;

import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.wtk.ApplicationContext;

import pdfsearch.Searcher;

public class IndexTask extends Task<Integer> {
	
	private Searcher searcher;
	private SyncWindow pane;
	
	public IndexTask(Searcher searcher, SyncWindow pane) {
		this.searcher = searcher;
		this.pane = pane;
	}

	@Override
	public Integer execute() throws TaskExecutionException {
		ApplicationContext.queueCallback(new Runnable() {
            @Override
            public void run() {
    			pane.progress.setEnabled(true);
    			pane.progress.setVisible(true);
            }
        });
		
		return searcher.buildIndex();
	}

}
