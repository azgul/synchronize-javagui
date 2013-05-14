/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronize.gui;

import java.net.URL;
import java.nio.file.Path;
import org.apache.pivot.wtk.media.Image;

/**
 *
 * @author Lars
 */
public class Language {
	private String code;
	private String name;
	private Image icon;
	
	public Language(String code, String name, String iconPath){
		this.code = code;
		this.name = name;
		
		try{
			this.icon = Image.load(new URL(iconPath));
		}catch(Exception e){
			System.err.println("Error loading image for language " + name);
			e.printStackTrace();
		}
	}
	
	public Language(String code, String name, Path iconPath){
		this.code = code;
		this.name = name;
		
		try{
			icon = Image.load(iconPath.toUri().toURL());
		}catch(Exception e){
			System.err.println("Error loading image for language " + name);
			e.printStackTrace();
		}
	}
	
	public String getCode(){ return code; }
	public String getName(){ return name; }
	public Image getIcon(){ return icon; }
}
