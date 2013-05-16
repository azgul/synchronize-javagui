/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronize.gui;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.TextInputContentListener;

import synchronize.core.SearcherSingleton;
import synchronize.listeners.LazyLoader;

/**
 *
 * @author Lars
 */
public class ContentPane extends TablePane implements Bindable {
	@BXML private TextInput searchField = null;
	@BXML private SearchResults searchResults = null;
	
	public final void search(String s){
		SearcherSingleton.getInstance().search(s);
	}
	
	@Override
	public void initialize(final Map<String, Object> map, URL url, Resources rsrcs) {
		SearcherSingleton.getInstance().getSearchListeners().add(searchResults);
		
		searchResults.getViewportListeners().add(new LazyLoader());
		
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
