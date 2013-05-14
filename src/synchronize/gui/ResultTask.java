package synchronize.gui;

import java.io.IOException;
import java.util.List;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.concurrent.AbortException;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.wtk.ApplicationContext;
import org.apache.pivot.wtk.Container;

import pdfsearch.SearchResult;

public class ResultTask extends Task<Void> {
	
	private List<SearchResult> items;
	private Container results;
	
	public ResultTask(List<SearchResult> items, Container results) {
		this.items = items;
		this.results = results;
	}
	
	

	@Override
	public Void execute() throws TaskExecutionException {
		for( SearchResult item : items ){
			if(abort)
				throw new AbortException();
			
			try{
				BXMLSerializer bxmlSerializer = new BXMLSerializer();
				final SearchResultItem result = (SearchResultItem)bxmlSerializer.readObject(SearchResultItem.class, "/synchronize/bxml/searchresultitem.bxml");
				//result.setAbstract(item.getAbstract());
				result.setAbstract("Bacon ipsum dolor sit amet meatball drumstick spare ribs capicola, salami corned beef t-bone andouille cow short ribs ham. Sirloin turkey t-bone doner hamburger frankfurter ham hock short ribs pig cow venison jerky. Capicola tenderloin ground round, venison shoulder bresaola pork hamburger chicken pastrami ham hock chuck tail short loin brisket.");
				
				result.setModifiedDate(item.getModifiedDate());
				result.setTitle("(" + CategoriesSingleton.getInstance().findById(item.getCategory()).getName() + ") " + item.getTitle());
				result.setLanguage(SearchResultItem.LANG.getLanguage(item.getLanguage()));
				result.setPdfPath(item.getPdf().getAbsolutePath());
				
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
		return null;
	}
	
	private String getCategoryPath(int category) {
		List<Category> categoryPath = CategoriesSingleton.getInstance().getPath(category);
		StringBuilder str = new StringBuilder();
		for(Category cat : categoryPath) {
			str.append(cat.getName());
			str.append(" > ");
		}
		return str.substring(0, -3);
	}

}
