/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronize;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.FillPane;
import org.apache.pivot.wtk.ScrollPane;
import pdfsearch.SearchResult;

/**
 *
 * @author Lars
 */
public class SearchResults extends ScrollPane implements Bindable {
	@BXML private FillPane results = null;
	
	public void wtbContent(){
		results.clear();
		
		try{
			for(int i = 0; i < 2; i++)
				results.add(wtbResult());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void refresh(List<SearchResult> items){
		results.clear();
		
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
		
		for( SearchResult item : items ){
			try{
				SearchResultItem result = (SearchResultItem)bxmlSerializer.readObject(SearchResultItem.class, "searchresultitem.bxml");
				result.setAbstract(item.getAbstract());
				result.setModifiedDate(item.getModifiedDate());
				result.setTitle("(" + item.getCategory() + ") " + item.getTitle());
				result.setLanguage(SearchResultItem.LANG.getLanguage(item.getLanguage()));
				
				results.add(result);
			}catch(IOException e){
				e.printStackTrace();
			}catch(SerializationException e){
				e.printStackTrace();
			}
		}
		
	}
	
	private SearchResultItem wtbResult() throws Exception{
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
		SearchResultItem result = (SearchResultItem)bxmlSerializer.readObject(SearchResultItem.class, "searchresultitem.bxml");
			
		result.setAbstract("whatup!!! 8-)");
		result.setModifiedDate("01-01-1970");
		result.setTitle("(Datasheet) MIn super seje fil");
		
		int min = 0;
		int max = SearchResultItem.LANG.values().length-1;
		int rand = min + (int)(Math.random() * ((max - min) + 1));
			
		result.setLanguage(SearchResultItem.LANG.values()[rand]);
		return result;
	}

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources rsrcs) {
		wtbContent();
	}
	
}
