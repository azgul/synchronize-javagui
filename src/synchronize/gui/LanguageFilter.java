/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronize.gui;

import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Checkbox;
import org.apache.pivot.wtk.content.ButtonData;

import synchronize.core.SearcherSingleton;
import synchronize.model.Language;

/**
 *
 * @author Lars
 */
public class LanguageFilter extends BoxPane implements Bindable {
	@Override
	public void initialize(Map<String, Object> map, URL url, Resources rsrcs) {
		// Load all languages and add them
		ArrayList<Language> languages = new ArrayList<>();
		
		Path flagPath = FileSystems.getDefault().getPath("res", "flags");
		
		languages.add(new Language("en", "English", flagPath.resolve("en.png")));
		languages.add(new Language("es", "Spanish", flagPath.resolve("es.png")));
		languages.add(new Language("dk", "Danish", flagPath.resolve("dk.png")));
		
		final HashSet<String> selectedLanguages = new HashSet<>();
		
		for(final Language lang : languages){			
			Checkbox box = new Checkbox(new ButtonData(lang.getIcon(), lang.getName()));
			
			box.getButtonPressListeners().add(new ButtonPressListener() {

				@Override
				public void buttonPressed(Button button) {
					if(button.isSelected())
						selectedLanguages.add(lang.getCode());
					else
						selectedLanguages.remove(lang.getCode());
					
					SearcherSingleton.getInstance().search(selectedLanguages);
				}
			});
			
			add(box);
		}
	}
}
