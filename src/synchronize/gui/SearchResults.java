/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronize.gui;

import java.net.URL;
import java.util.List;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
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
public class SearchResults extends ScrollPane implements Bindable {
	@BXML private FillPane results = null;
	
	public void wtbContent(){
		results.removeAll();
	}
	
	public void refresh(List<SearchResult> items){
		results.removeAll();
		
		
		ResultTask resultTask = new ResultTask(items, results);
        TaskListener<Void> taskListener = new TaskListener<Void>() {
            @Override
            public void taskExecuted(Task<Void> task) {
                System.out.println("Added search results.");
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
