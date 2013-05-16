package synchronize.gui;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.TextInput;

import synchronize.core.Database;
import synchronize.core.SearcherSingleton;

public class Main implements Application {
    private SyncWindow window = null;
    @BXML private TextInput searchField;
    
    public static void main(String[] args) {
    	DesktopApplicationContext.main(Main.class, args);
    }

    @Override
    public void startup(Display display, Map<String, String> properties)
        throws Exception {
        BXMLSerializer bxmlSerializer = new BXMLSerializer();
        window = (SyncWindow)bxmlSerializer.readObject(Main.class, "/synchronize/bxml/window.bxml");		
        window.open(display);
        SearcherSingleton.initInstance(window);
        Database.getInstance().init();
        Database.getInstance().getAccessLogs();
    }

    @Override
    public boolean shutdown(boolean optional) {
        if (window != null) {
            window.close();
        }

        return false;
    }

    @Override
    public void suspend() {
    }

    @Override
    public void resume() {
    }
}