/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronize;

import java.net.URL;
import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.FillPane;
import org.apache.pivot.wtk.ScrollPane;

/**
 *
 * @author Lars
 */
public class SearchResults extends ScrollPane implements Bindable {
	@BXML private FillPane results = null;
	
	
	@Override
	public void initialize(Map<String, Object> map, URL url, Resources rsrcs) {
		// WHATUP
		try{
			
			
			for(int i = 0; i < 10; i++)
				results.add(wtbResult());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private SearchResultItem wtbResult() throws Exception{
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
			SearchResultItem result = (SearchResultItem)bxmlSerializer.readObject(SearchResultItem.class, "searchresultitem.bxml");
			
			result.setAbstract("whatup!!! 8-)");
			result.setModifiedDate("01-01-1970");
			return result;
	}
	
}
