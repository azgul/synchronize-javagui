/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronize;

import java.net.URL;
import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.TextInputContentListener;
import pdfsearch.MMapIndexFactory;
import pdfsearch.Searcher;

/**
 *
 * @author Lars
 */
public class ContentPane extends TablePane implements Bindable {
	@BXML private TextInput searchField = null;
	@BXML private SearchResults searchResultsTest = null;
	
	public final void search(String s){
		
		Searcher searcher = new Searcher(new MMapIndexFactory());
		
		try{
			long start = System.currentTimeMillis();
			searchResultsTest.refresh(searcher.search(s));
			long end = System.currentTimeMillis();
			System.out.println("Search time: " + (end-start));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(final Map<String, Object> map, URL url, Resources rsrcs) {
		
		System.out.println(searchResultsTest);
		
		try{
			searchField.getTextInputContentListeners().add(new TextInputContentListener.Adapter(){
				@Override
				public void textChanged(TextInput textInput) {
					if(textInput.getText().length() < 3)
						return;
					
					search(textInput.getText());
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
