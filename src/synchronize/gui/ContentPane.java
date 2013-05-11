/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronize.gui;

import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.List;

import org.apache.lucene.index.IndexNotFoundException;
import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.TextInputContentListener;

import pdfsearch.SearchResult;

/**
 *
 * @author Lars
 */
public class ContentPane extends TablePane implements Bindable {
	@BXML private TextInput searchField = null;
	@BXML private SearchResults searchResults = null;
	
	public final void search(String s){
		try{
			long start = System.currentTimeMillis();
			List<SearchResult> items = SearcherSingleton.getInstance().getSearcher().search(s);
			long middle = System.currentTimeMillis();
			System.out.println("Search time: " + (middle-start));
			searchResults.refresh(items);
			long end = System.currentTimeMillis();
			System.out.println("Refresh time: " + (end-middle));
		} catch(NoSuchFileException e) {
			e.printStackTrace();
			Alert.alert(MessageType.ERROR, "The search path " + e.getFile() + " does not exist.", this.getWindow());
		} catch(IndexNotFoundException e) {
			e.printStackTrace();
			Alert.alert(MessageType.ERROR, "The search index could not be built.", this.getWindow());
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(final Map<String, Object> map, URL url, Resources rsrcs) {
		
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
