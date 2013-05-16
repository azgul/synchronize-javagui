/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronize.core;

import org.apache.pivot.wtk.WTKListenerList;
import synchronize.listeners.CategoryListener;

/**
 *
 * @author Lars
 */
public class UIObservers {
	private static WTKListenerList<CategoryListener> categoryListeners = new WTKListenerList<>();
	
	public static void addCategoryListener(CategoryListener listener){
		categoryListeners.add(listener);
	}
	
	public static WTKListenerList<CategoryListener> getCategoryListeners(){
		return categoryListeners;
	}
}
