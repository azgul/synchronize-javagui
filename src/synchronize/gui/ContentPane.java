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
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.ListButtonSelectionListener;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.TextInputContentListener;

import synchronize.core.SearcherSingleton;

/**
 *
 * @author Lars
 */
public class ContentPane extends TablePane implements Bindable {
	@BXML private TextInput searchField = null;
	@BXML private SearchResults searchResults = null;
	@BXML private ListButton listButton = null;
	
	public final void search(String s){
		SearcherSingleton.getInstance().search(s);
	}
	
	public final void search(String s, String sortBy){
		SearcherSingleton.getInstance().search(s, sortBy);
	}
	
	@Override
	public void initialize(final Map<String, Object> map, URL url, Resources rsrcs) {
		SearcherSingleton.getInstance().getSearchListeners().add(searchResults);
		try{
			searchField.getTextInputContentListeners().add(new TextInputContentListener.Adapter(){
				@Override
				public void textChanged(TextInput textInput) {
					if(textInput.getText().length() < 3)
						return;
					
					search(textInput.getText());
				}
			});
			
			listButton.getListButtonSelectionListeners().add(new ListButtonSelectionListener() {

				@Override
				public void selectedIndexChanged(ListButton lb, int i) {
					//String selectedItem = (String)lb.getSelectedItem();
				}

				@Override
				public void selectedItemChanged(ListButton lb, Object o) {
					String selectedItem = (String)lb.getSelectedItem();
					search(searchField.getText(), selectedItem);
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
