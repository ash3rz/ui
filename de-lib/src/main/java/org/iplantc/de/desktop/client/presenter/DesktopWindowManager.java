package org.iplantc.de.desktop.client.presenter;

import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.SavedWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.client.views.windows.WindowInterface;
import org.iplantc.de.desktop.client.views.windows.util.WindowFactory;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.events.ServiceDown;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.WindowManager;

import java.util.List;
import java.util.Stack;

/**
 * Window manager for the DE desktop.
 * <p/>
 * Accepts window configs
 *
 * @author jstroot
 */
public class DesktopWindowManager {

    private final WindowFactory windowFactory;
    private final WindowManager windowManager;
    private Element desktopContainer;
    private Window sticky;

    @Inject
    DesktopWindowManager(final WindowManager windowManager,
                         final WindowFactory windowFactory) {
        this.windowManager = windowManager;
        this.windowFactory = windowFactory;
    }

    public void closeActiveWindow() {
        final List<Widget> reverse = Lists.reverse(windowManager.getStack());
        for (Widget w : reverse) {
            if (w instanceof Window) {
                ((Window) w).hide();
                return;
            }
        }
    }

    public void show(final SavedWindowConfig savedWindowConfig) {
        final WindowConfig wc = ConfigFactory.getConfig(savedWindowConfig) ;
        String windowId = constructWindowId(wc);
        for (Widget w : windowManager.getWindows()) {
            String currentId = ((Window) w).getStateId();
            if (windowId.equals(currentId)) {
                ((WindowInterface) w).asWindow().show();
                return;
            }
        }
        getOrCreateWindow(wc).get(new AsyncCallback<WindowInterface>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(WindowInterface window) {
                if (desktopContainer != null) {
                    window.setContainer(desktopContainer);
                }
                window.show(wc, constructWindowId(wc), true);
                resizeOversizeWindow(window);
                moveOutOfBoundsWindow(window);
                if (sticky != null) {
                    windowManager.bringToFront(sticky);
                }
            }
        });
    }

    /**
     * Shows the last focused instance of a window of the give type, or creates a new window.
     * This method also provides a 'cycling' affect, where windows of the given WindowType are in
     * focus (on top of the stack) are sent to the back so the next WindowType can be focused.
     * <p/>
     * This method works on the assumption that the {@code Window.getStateId()} begins with the
     * string value of its corresponding {@code WindowType}.
     *
     * @param windowType the window type to be shown
     */
    public void show(WindowType windowType) {
        Stack<Window> multiWindowStack = new Stack<>();
        // Look for existing window type, then show it
        boolean wasLast = false;
        for (Widget w : windowManager.getStack()) {
            Window window = (Window) w;
            if (Strings.nullToEmpty(window.getStateId()).startsWith(windowType.toString())) {
                multiWindowStack.push(window);
                wasLast = true;
            } else {
                wasLast = false;
            }
        }
        if(!multiWindowStack.isEmpty()){
            Window toFront;
            if((multiWindowStack.size() == 1) || !wasLast){
                toFront = multiWindowStack.pop();
            } else {
                toFront = multiWindowStack.get(0);
            }
            toFront.show();
            windowManager.bringToFront(toFront);
        } else {
            // If window type could not be found, create and show one
            show(getDefaultConfig(windowType), false);
        }
        if (sticky != null) {
            windowManager.bringToFront(sticky);
        }

    }

    public void show(final WindowConfig config, final boolean updateExistingWindow) {
        // Creates window if a window matching the given config isn't found.
        String windowId = constructWindowId(config);
        for (Widget w : windowManager.getWindows()) {
            String currentId = ((Window) w).getStateId();
            if (windowId.equals(currentId)) {
                if(updateExistingWindow){
                    ((WindowInterface) w).update(config);
                } else {
                    // Window already exists, so no need to call other SHOW(config, "", bool) method
                    ((WindowInterface) w).asWindow().show();
                }
                windowManager.bringToFront(w);
                return;
            }
        }
        getOrCreateWindow(config).get(new AsyncCallback<WindowInterface>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(WindowInterface window) {
                if (!window.isVisible() && (windowManager.getActive() != null)) {
                    final Point position = ((Window) windowManager.getActive()).getElement().getPosition(true);
                    position.setX(position.getX() + 10);
                    position.setY(position.getY() + 20);

                    final Point adjustedPosition = getAdjustedPosition(position);
                    window.setPagePosition(adjustedPosition.getX(), adjustedPosition.getY());
                }
                if (desktopContainer != null) {
                    window.setContainer(desktopContainer);
                }

                window.show(config, constructWindowId(config), true);
                resizeOversizeWindow(window);
                moveOutOfBoundsWindow(window);
                if (sticky != null) {
                    windowManager.bringToFront(sticky);
                }
                window.logWindowOpenToIntercom(config);
            }
        });
    }

    public void serviceDown(ServiceDown event) {
        List<WindowType> types = event.getWindowTypes();
        for (Widget w : windowManager.getStack()) {
            Window window = (Window)w;
            for (WindowType type : types) {
                if (Strings.nullToEmpty(window.getStateId()).startsWith(type.toString())) {
                    WindowInterface windowInterface = (WindowInterface)window;
                    windowInterface.serviceDown(event.getSelectionHandler());
                }
            }
        }
    }

    public void serviceUp(List<WindowType> windowTypes) {
        for (Widget w : windowManager.getStack()) {
            Window window = (Window)w;
            for (WindowType type : windowTypes) {
                if (Strings.nullToEmpty(window.getStateId()).startsWith(type.toString())) {
                    WindowInterface windowInterface = (WindowInterface)window;
                    windowInterface.serviceUp();
                }
            }
        }
    }

    private void resizeOversizeWindow(WindowInterface window) {
        Scheduler.get().scheduleFinally(()-> {  //wait for react to render DOM
            int desktopWidth = desktopContainer.getClientWidth();
            int desktopHeight = desktopContainer.getClientHeight();
            int windowWidth = window.asWindow().getOffsetWidth();
            int windowHeight = window.asWindow().getOffsetHeight();
            if (windowWidth > desktopWidth) {
                window.asWindow().setWidth(desktopWidth);
            }
            if (windowHeight > desktopHeight) {
                window.asWindow().setHeight(desktopHeight);
            }
        });
    }

    private void moveOutOfBoundsWindow(WindowInterface window) {
        Scheduler.get().scheduleFinally(()-> {  //wait for react to render DOM
            final int desktopContainerBottom = desktopContainer.getAbsoluteBottom();
            final int desktopContainerRight = desktopContainer.getAbsoluteRight();
            final int windowRight = window.asWindow().getElement().getAbsoluteRight();
            final int windowBottom = window.asWindow().getElement().getAbsoluteBottom();
            if (windowRight > desktopContainerRight) {
                window.setPagePosition(0, window.asWindow().getAbsoluteTop());
            }
            if (windowBottom > desktopContainerBottom) {
                window.setPagePosition(window.asWindow().getAbsoluteLeft(), 0);
            }
        });
    }

    String constructWindowId(WindowConfig config) {
        String windowType = config.getWindowType().toString();
        String tag = config.getTag();
        return !Strings.isNullOrEmpty(tag) ? windowType + "_" + tag : windowType;
    }

    /**
     * Adjusts the given position to account for the given window's size.
     *  @param position the desired position, which will be adjusted if necessary.
     *
     */
    Point getAdjustedPosition(Point position) {
        return position;
    }


    WindowConfig getDefaultConfig(WindowType windowType) {
        return ConfigFactory.getDefaultConfig(windowType);
    }

    /**
     * Retrieves existing windows which correspond to the given config. If no existing window can be
     * found, this method will attempt to create one.
     * <p/>
     * Method also constructs and set a unique id to the window.
     *
     * @param config an object used to uniquely identify a window.
     * @return the window corresponding to the given config, null is the window could not be found.
     */
    AsyncProviderWrapper<? extends WindowInterface> getOrCreateWindow(final WindowConfig config) {
        return windowFactory.build(config);
    }

    void setDesktopContainer(Element desktopContainer) {
        this.desktopContainer = desktopContainer;
    }

    public void stickWindowToTop(Window window) {
        this.sticky = window;
    }

    /**
     * Returns true if the user has the specified WindowType already open in the DE
     * @param windowType
     * @return
     */
    public boolean isOpen(WindowType windowType) {
        return windowManager.getWindows().stream()
                            .filter(window -> ((Window)window).getStateId().startsWith(windowType.toString()))
                            .count() > 0;
    }
}
