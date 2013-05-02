package synchronize.test;

import java.io.IOException;
import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Border;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.FileBrowserSheet;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.MenuBar;
import org.apache.pivot.wtk.MenuHandler;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.TextInputContentListener;
import org.apache.pivot.wtk.TextInputSelectionListener;
import org.apache.pivot.wtk.effects.DropShadowDecorator;

public class MenuBars extends Frame implements Bindable {
    @BXML private FileBrowserSheet fileBrowserSheet;
    @BXML private TabPane tabPane = null;
	@BXML PushButton back;
	@BXML Border backBorder;

    private MenuHandler menuHandler = new MenuHandler.Adapter() {
        TextInputContentListener textInputTextListener = new TextInputContentListener.Adapter() {
            @Override
            public void textChanged(TextInput textInput) {
                updateActionState(textInput);
            }
        };

        TextInputSelectionListener textInputSelectionListener = new TextInputSelectionListener() {
            @Override
            public void selectionChanged(TextInput textInput, int previousSelectionStart,
                int previousSelectionLength) {
                updateActionState(textInput);
            }
        };

        @Override
        public void configureMenuBar(Component component, MenuBar menuBar) {
            if (component instanceof TextInput) {
                TextInput textInput = (TextInput)component;

                updateActionState(textInput);
                Action.getNamedActions().get("paste").setEnabled(true);

                textInput.getTextInputContentListeners().add(textInputTextListener);
                textInput.getTextInputSelectionListeners().add(textInputSelectionListener);
            } else {
                Action.getNamedActions().get("cut").setEnabled(false);
                Action.getNamedActions().get("copy").setEnabled(false);
                Action.getNamedActions().get("paste").setEnabled(false);
            }
        }

        @Override
        public void cleanupMenuBar(Component component, MenuBar menuBar) {
            if (component instanceof TextInput) {
                TextInput textInput = (TextInput)component;
                textInput.getTextInputContentListeners().remove(textInputTextListener);
                textInput.getTextInputSelectionListeners().remove(textInputSelectionListener);
            }
        }

        private void updateActionState(TextInput textInput) {
            Action.getNamedActions().get("cut").setEnabled(textInput.getSelectionLength() > 0);
            Action.getNamedActions().get("copy").setEnabled(textInput.getSelectionLength() > 0);
        }
    };

    public MenuBars() {
        Action.getNamedActions().put("fileNew", new Action() {
            @Override
            public void perform(Component source) {
                BXMLSerializer bxmlSerializer = new BXMLSerializer();
                bxmlSerializer.getNamespace().put("menuHandler", menuHandler);

                Component tab;
                try {
                    tab = new Border((Component)bxmlSerializer.readObject(MenuBars.class, "menubar.bxml"));
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                } catch (SerializationException exception) {
                    throw new RuntimeException(exception);
                }

                tabPane.getTabs().add(tab);
                TabPane.setTabData(tab, "Document " + tabPane.getTabs().getLength());
                tabPane.setSelectedIndex(tabPane.getTabs().getLength() - 1);
            }
        });

        Action.getNamedActions().put("fileOpen", new Action() {
            @Override
            public void perform(Component source) {
                fileBrowserSheet.open(MenuBars.this);
            }
        });

        Action.getNamedActions().put("synchronize", new Action(false) {
            @Override
            public void perform(Component source) {
            }
        });

        Action.getNamedActions().put("help", new Action(false) {
            @Override
            public void perform(Component source) {
            }
        });

        Action.getNamedActions().put("about", new Action(false) {
            @Override
            public void perform(Component source) {
            }
        });
    }

    @Override
    public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
    	
    	DropShadowDecorator backDecorator = new DropShadowDecorator(1,3,3);
		backDecorator.setShadowColor("#000000");
		backDecorator.setShadowOpacity(0.8f);
		backBorder.getDecorators().add(backDecorator);
    }
}