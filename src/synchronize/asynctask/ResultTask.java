package synchronize.asynctask;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.concurrent.AbortException;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.wtk.ApplicationContext;
import org.apache.pivot.wtk.Container;

import synchronize.core.CategoriesSingleton;
import synchronize.core.SearcherSingleton;
import synchronize.gui.SearchResultItem;
import synchronize.model.Category;
import synchronize.pdfsearch.SearchResult;

public class ResultTask extends Task<Void> {
	
	private Container results;
	private int runningCounter = 0;
	List<SearchResult> items;
	
	public ResultTask(Container results) {
		this.results = results;
	}
	
	public synchronized void wakeUp() {
		notifyAll();
	}

	@Override
	public synchronized Void execute() throws TaskExecutionException {
		while(true) {
			boolean tryAgain = false;
			if(!SearcherSingleton.getInstance().getLastResults().equals(items)) {
				items = SearcherSingleton.getInstance().getLastResults();
				runningCounter = 0;
				ApplicationContext.queueCallback(new Runnable(){
					public void run() {
						results.removeAll();
					}
				});
			}
			int start = runningCounter;
			for( int i = start; i < items.size(); i++, runningCounter++ ){
				SearchResult item = items.get(i);
				if(i - start >= 10)
					break;
				
				
				// abort if the abort flag was set
				if(abort)
					throw new AbortException("Task was aborted.");
				
				// break out of the loop to start over if the search results has changed
				if(!items.equals(SearcherSingleton.getInstance().getLastResults())) {
					tryAgain = true;
					break;
				}
				
				try{
					BXMLSerializer bxmlSerializer = new BXMLSerializer();
					final SearchResultItem result = (SearchResultItem)bxmlSerializer.readObject(SearchResultItem.class, "/synchronize/bxml/searchresultitem.bxml");
					//result.setAbstract(item.getAbstract());
					// TODO: remove dummy abstract
					result.setAbstract("Bacon ipsum dolor sit amet meatball drumstick spare ribs capicola, salami corned beef t-bone andouille cow short ribs ham. Sirloin turkey t-bone doner hamburger frankfurter ham hock short ribs pig cow venison jerky. Capicola tenderloin ground round, venison shoulder bresaola pork hamburger chicken pastrami ham hock chuck tail short loin brisket.");
					
					result.setModifiedDate(item.getModifiedDate());
					result.setTitle("#"+(i+1)+" (" + CategoriesSingleton.getInstance().findById(item.getCategory()).getName() + ") " + item.getTitle());
					result.setLanguage(SearchResultItem.LANG.getLanguage(item.getLanguage()));
					result.setPdfPath(item.getPdf().getAbsolutePath());
					result.setBreadcrumbs(getCategoryPath(item.getCategory()));
					
					// schedule add result
					ApplicationContext.queueCallback(new Runnable() {
	                    @Override
	                    public void run() {
	        				results.add(result);
	                    }
	                });
				}catch(IOException e){
					e.printStackTrace();
				}catch(SerializationException e){
					e.printStackTrace();
				}
			}
			// wait only if we did not break out of the loop to start over
			if(!tryAgain) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
					throw new AbortException("Wait was interrupted with the following message " + e.getMessage());
				}
			}
		}
	}
	
	private String getCategoryPath(int category) {
		List<Category> categoryPath = CategoriesSingleton.getInstance().getPath(category);
		StringBuilder str = new StringBuilder();
		Collections.reverse(categoryPath);
		for(Category cat : categoryPath) {
			str.append(cat.getName());
			str.append(" > ");
		}
		return str.substring(0, str.length()-3);
	}

}
