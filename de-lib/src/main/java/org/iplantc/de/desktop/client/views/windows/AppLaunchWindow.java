package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.apps.widgets.client.events.AnalysisLaunchEvent;
import org.iplantc.de.apps.widgets.client.events.AnalysisLaunchEvent.AnalysisLaunchEventHandler;
import org.iplantc.de.apps.widgets.client.events.AppTemplateFetched;
import org.iplantc.de.apps.widgets.client.view.AppLaunchView;
import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.commons.client.views.window.configs.AppWizardConfig;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.client.events.WindowHeadingUpdatedEvent;
import org.iplantc.de.desktop.shared.DeModule;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

import com.sencha.gxt.widget.core.client.container.SimpleContainer;

/**
 * @author jstroot
 */
public class AppLaunchWindow extends IplantWindowBase implements AnalysisLaunchEventHandler,
                                                                 AppTemplateFetched.AppTemplateFetchedHandler {

    private final AppLaunchView.Presenter presenter;
    private final DEClientConstants deClientConstants;
    private AppLaunchView.AppLaunchViewAppearance appearance;
    private String systemId;
    private String appId;

    @Inject
    AppLaunchWindow(final AppLaunchView.Presenter presenter,
                    final DEClientConstants deClientConstants,
                    AppLaunchView.AppLaunchViewAppearance appearance) {
        this.presenter = presenter;
        this.deClientConstants = deClientConstants;
        this.appearance = appearance;

        setSize("640", "375");
        setMinWidth(300);
        setMinHeight(350);
        setBorders(false);
        ensureDebugId(DeModule.WindowIds.APP_LAUNCH_WINDOW);

        presenter.addAnalysisLaunchHandler(this);
        presenter.addAppTemplateFetchedHandler(this);
    }

    @Override
    public WindowState getWindowState() {
        AppWizardConfig config = ConfigFactory.appWizardConfig(systemId, appId);
        config.setAppTemplate(AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(presenter.getAppTemplate())));
        return createWindowState(config);
    }

    private boolean isMatchingId(HasQualifiedId id) {
        return id.getSystemId().equalsIgnoreCase(systemId) && id.getId().equalsIgnoreCase(appId);
    }

    @Override
    public void onAnalysisLaunch(AnalysisLaunchEvent analysisLaunchEvent) {
        if (isMatchingId(analysisLaunchEvent.getAppTemplateId())) {
            hide();
        }
    }

    @Override
    public <C extends WindowConfig> void show(final C windowConfig,
                                              final String tag,
                                              final boolean isMaximizable) {
        final AppWizardConfig config1 = (AppWizardConfig) windowConfig;
        systemId = Strings.isNullOrEmpty(config1.getSystemId())
                ? deClientConstants.deSystemId()
                : config1.getSystemId();
        appId = config1.getAppId();
        init(config1);
        super.show(windowConfig, tag, isMaximizable);
    }

    private void init(AppWizardConfig config) {
        SimpleContainer sc = new SimpleContainer();
        this.setWidget(sc);

        sc.mask(appearance.loadingMask());
        presenter.go(this, config);
    }

    @Override
    public void onAppTemplateFetched(AppTemplateFetched event) {
        AppTemplate template = event.getTemplate();
        if (template != null) {
            if (template.isAppDisabled()) {
                hide();
                return;
            }
            setHeading(template.getLabel());
            fireEvent(new WindowHeadingUpdatedEvent());
        }
        forceLayout();
    }
}
