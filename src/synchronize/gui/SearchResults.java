/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronize.gui;

import java.net.URL;
import java.util.List;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.LinkedList;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.FillPane;
import org.apache.pivot.wtk.ScrollPane;
import org.apache.pivot.wtk.TaskAdapter;

import pdfsearch.SearchResult;

/**
 *
 * @author Lars
 */
public class SearchResults extends ScrollPane implements Bindable, SearcherListener {
	@BXML private FillPane results = null;
	ResultTask resultTask = null;
	volatile boolean waitForAbort = false;
	
	public void wtbContent(){
		results.removeAll();
	}
	
	protected void resetResultTask(){
		resultTask = null;
	}
	
	protected void resetAbortFlag(){
		waitForAbort = false;
	}
	
	public void onSearch(final List<SearchResult> items){
		results.removeAll();
		
		resultTask = new ResultTask(items, results);
        TaskListener<Void> taskListener = new TaskListener<Void>() {
            @Override
            public void taskExecuted(Task<Void> task) {
				System.out.println("Finished! " + items.size());
            }

            @Override
            public void executeFailed(Task<Void> task) {
                System.err.println(task.getFault());
            }
			
        };

        resultTask.execute(new TaskAdapter<Void>(taskListener));
	}

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources rsrcs) {
		wtbContent();
	}
	
}
