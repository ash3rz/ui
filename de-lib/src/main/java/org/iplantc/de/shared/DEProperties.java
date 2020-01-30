package org.iplantc.de.shared;

import com.google.gwt.core.client.GWT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("nls")
public class DEProperties {
    private static final String HT_PATH_LIST_FILE_IDENTIFIER = "org.iplantc.htPathList.fileIdentifier";

    private static final String MULTI_INPUT_LIST_FILE_IDENTIFIER = "org.iplantc.multiInputPathList.fileIdentifier";

    /**
     * The base URL used to access the Mule services.
     */
    private static final String MULE_SERVICE_BASE_URL =
            "org.iplantc.discoveryenvironment.muleServiceBaseUrl";

    /**
     * The base URL used to access the Mule services.
     */
    private static final String UNPROTECTED_MULE_SERVICE_BASE_URL =
            "org.iplantc.discoveryenvironment.unprotectedMuleServiceBaseUrl";

    /**
     * Properties key of the base URL of the data management services.
     */
    private static final String DATA_MGMT_BASE_URL = "org.iplantc.services.de-data-mgmt.base";

    private static final String PERM_ID_BASE_URL = "org.iplantc.services.permIdRequests";

    /**
     * Perm Id request
     */
    private static final String PERM_REQUEST_BASE_URL = "org.iplantc.services.permIdRequests";

    private static final String DATA_MGMT_ADMIN_BASE_URL =
            "org.iplantc.services.admin.de-data-mgmt.base";

    /**
     * Properties key of the base URL of the file I/O services.
     */
    private static final String FILE_IO_BASE_URL = "org.iplantc.services.file-io.base.secured";

    /**
     * Properties key of the notification polling interval
     */
    private static final String NOTIFICATION_POLL_INTERVAL =
            "org.iplantc.discoveryenvironment.notifications.poll-interval";

    private static final String VICE_POLL_INTERVAL =
            "org.iplantc.discoveryenvironment.vice-logs.poll-interval";

    /**
     * Properties key of the context click enabled option
     */
    private static final String CONTEXT_CLICK_ENABLED =
            "org.iplantc.discoveryenvironment.contextMenu.enabled";


    /**
     * Cyverse support user
     */
    private static final String CYVERSE_SUPPORT_USER = "org.iplantc.discoveryenvironment.analysis.support.user";

    /**
     * The prefix used in each of the private workspace property names.
     */
    private static final String WORKSPACE_PREFIX = "org.iplantc.discoveryenvironment.workspace.";

    /**
     * Properties key for the private workspace
     */
    private static final String PRIVATE_WORKSPACE = WORKSPACE_PREFIX + "rootAppCategory";

    /**
     * Properties key for the private workspace items
     */
    private static final String PRIVATE_WORKSPACE_ITEMS = WORKSPACE_PREFIX + "defaultAppCategories";

    /**
     * Properties key for the default Beta Category ID
     */
    private static final String DEFAULT_BETA_CATEGORY_ID = WORKSPACE_PREFIX + "defaultBetaAppCategoryId";

    /**
     * Properties key for the default HPC Category ID
     */
    private static final String DEFAULT_HPC_CATEGORY_ID = WORKSPACE_PREFIX + "defaultHpcAppCategoryId";

    /**
     * Properties keys for the default {@link org.iplantc.de.client.models.tool.InteractiveApp}
     */
    private static final String DEFAULT_VICE_IMAGE = "org.iplantc.discoveryenvironment.tools.interactiveApps.defaultImage";
    private static final String DEFAULT_VICE_NAME = "org.iplantc.discoveryenvironment.tools.interactiveApps.defaultName";
    private static final String DEFAULT_VICE_CAS_URL = "org.iplantc.discoveryenvironment.tools.interactiveApps.defaultCasUrl";
    private static final String DEFAULT_VICE_CAS_VALIDATE = "org.iplantc.discoveryenvironment.tools.interactiveApps.defaultCasValidate";

    /**
     * Properties keys for the max values for private {@link org.iplantc.de.client.models.tool.ToolContainer} settings
     */
    private static final String TOOLS_MAX_MEM_LIMIT = "org.iplantc.discoveryenvironment.tools.private.maxMemoryLimitValue";
    private static final String TOOLS_MAX_DISK_LIMIT = "org.iplantc.discoveryenvironment.tools.private.maxDiskLimitValue";
    private static final String TOOLS_MAX_CPU_LIMIT = "org.iplantc.discoveryenvironment.tools.private.maxCPULimitValue";

    /**
     * Properties keys for the max values for admin {@link org.iplantc.de.client.models.tool.ToolContainer} settings
     */
    private static final String TOOLS_ADMIN_MAX_MEM_LIMIT = "org.iplantc.discoveryenvironment.tools.admin.maxMemoryLimitValue";
    private static final String TOOLS_ADMIN_MAX_DISK_LIMIT = "org.iplantc.discoveryenvironment.tools.admin.maxDiskLimitValue";
    private static final String TOOLS_ADMIN_MAX_CPU_LIMIT = "org.iplantc.discoveryenvironment.tools.admin.maxCPULimitValue";

    private static final Long ONE_GB = (long)(1024 * 1024 * 1024);
    private static final Long DEFAULT_TOOLS_MAX_MEM_LIMIT = 16 * ONE_GB;    //  16GB
    private static final Long DEFAULT_TOOLS_MAX_DISK_LIMIT = 512 * ONE_GB;  // 512GB
    private static final Double DEFAULT_TOOLS_MAX_CPU_LIMIT = (double)8;

    /**
     * Properties key for the support service URL
     */
    private static final String SUPPORT_SERVICE_URL = "org.iplantc.discoveryenvironment.support";

    /**
     * Properties key of the default Beta Category ID.
     */
    private static final String DEFAULT_TRASH_CATEGORY_ID =
            WORKSPACE_PREFIX + "defaultTrashAppCategoryId";

    private static final String ONTOLOGY_ATTRS = WORKSPACE_PREFIX + "ontologyAttrs";

    private static final String COMMUNITY_ATTR = WORKSPACE_PREFIX + "communityAttr";

    private static final String APPS_WORKSPACE = WORKSPACE_PREFIX + "apps.";

    private static final String APPS_CARD_URL = APPS_WORKSPACE + "cardUrl";

    private static final String APPS_CARD_URL_OPTIONS = APPS_WORKSPACE + "cardUrlOptions";


    /**
     * Properties key for the Beta Avu fields
     */
    private static final String BETA_AVU_IRI = WORKSPACE_PREFIX + "metadata.beta.attr.iri";

    private static final String BETA_AVU_LABEL = WORKSPACE_PREFIX + "metadata.beta.attr.label";

    private static final String BETA_AVU_VALUE = WORKSPACE_PREFIX + "metadata.beta.value";

    private static final String BETA_AVU_UNIT = WORKSPACE_PREFIX + "metadata.beta.unit";

    /**
     * Default community data folder path
     */
    private static final String COMMUNITY_DATA_PATH = "org.iplantc.communitydata.path";

    private static final String IRODS_HOME_PATH = "org.iplantc.irodshome.path";

    private static final String BASE_TRASH_PATH = "org.iplantc.basetrash.path";

    private static final String GROUPER_ALL_ID =
            "org.iplantc.discoveryenvironment.groups.grouper-all-id";

    private static final String GROUPER_ALL_DISPLAY_NAME =
            "org.iplantc.discoveryenvironment.groups.grouper-all-display-name";

    private static final String GROUPER_ID = "org.iplantc.discoveryenvironment.groups.grouper-id";

    /**
     * Intercom settings
     */
    private static final String INTERCOM_ENABLED ="org.iplantc.discoveryenvironment.intercom.enabled";
    private static final String INTERCOM_APP_ID = "org.iplantc.discoveryenvironment.intercom.appId";

    private static final String INTERCOM_COMPANY_ID =
            "org.iplantc.discoveryenvironment.intercom.companyId";

    private static final String INTERCOM_COMPANY_NAME =
            "org.iplantc.discoveryenvironment.intercom.companyName";

    /**
     * The single instance of this class.
     */
    private static DEProperties instance;

    /**
     * The base URL of the data management services.
     */
    private String dataMgmtBaseUrl;

    /**
     * The base URL of the perm id services
     */
    private String permIdBaseUrl;

    /**
     * The base URL of the file I/O services.
     */
    private String fileIoBaseUrl;

    /**
     * The polling interval
     */
    private int notificationPollInterval;

    /**
     *  The VICE logs polling interval;
     *
     */

    private int viceLogsPollInterval;

    /**
     * Context click option
     */
    private boolean contextClickEnabled;

    /**
     * private workspace name
     */
    private String privateWorkspace;

    /**
     * private workspace items
     */
    private String privateWorkspaceItems;

    /**
     * ID of the default Beta Workspace Category
     */
    private String defaultBetaCategoryId;

    private String defaultHpcCategoryId;

    private String defaultTrashCategoryId;

    private String htPathListFileIdentifier;

    private String multiInputPathListFileIdentifier;

    private String betaAvuIri;

    private String betaAvuLabel;

    private String betaAvuValue;

    private String betaAvuUnit;

    private String ontologyAttrs;

    private String communityAttr;

    private String cardUrl;

    private String cardUrlOptions;


    private String grouperAllId;

    private String grouperAllDisplayName;

    private String grouperId;

    private String companyId;

    private String companyName;

    public String getHtPathListFileIdentifier() {
        return htPathListFileIdentifier;
    }

    /**
     * @return the contextClickEnabled
     */
    public boolean isContextClickEnabled() {
        return contextClickEnabled;
    }

    /**
     * The base URL used to access the DE Mule services.
     */
    private String muleServiceBaseUrl;

    /**
     * The base URL used to access the DE Unprotected Mule services.
     */
    private String unproctedMuleServiceBaseUrl;

    /**
     * @return the unproctedMuleServiceBaseUrl
     */
    public String getUnproctedMuleServiceBaseUrl() {
        return unproctedMuleServiceBaseUrl;
    }


    /**
     * Community data path
     */
    private String communityDataPath;

    private String irodsHomePath;

    private String baseTrashPath;

    private String dataMgmtAdminBaseUrl;

    private String supportUser;

    private String supportServiceUrl;

    private String intercomAppId;

    private boolean intercomEnabled;

    private String defaultViceImage;

    private String defaultViceName;

    private String defaultViceCasUrl;

    private String defaultViceCasValidate;

    private Long toolsMaxMemLimit;

    private Long toolsMaxDiskLimit;

    private Double toolsMaxCPULimit;

    private Long toolsAdminMaxMemLimit;

    private Long toolsAdminMaxDiskLimit;

    private Double toolsAdminMaxCPULimit;

    /**
     * Force the constructor to be private.
     */
    private DEProperties() {
    }

    /**
     * Gets the single instance of this class.
     *
     * @return the instance.
     */
    public static DEProperties getInstance() {
        if (instance == null) {
            instance = new DEProperties();
        }
        return instance;
    }

    /**
     * list of properties UI needs
     *
     * @return list of properties
     */
    public List<String> getPropertyList() {
        List<String> keys = new ArrayList<>();
        keys.add(COMMUNITY_DATA_PATH);
        keys.add(IRODS_HOME_PATH);
        keys.add(BASE_TRASH_PATH);
        keys.add(HT_PATH_LIST_FILE_IDENTIFIER);
        keys.add(MULTI_INPUT_LIST_FILE_IDENTIFIER);
        keys.add(MULE_SERVICE_BASE_URL);
        keys.add(DATA_MGMT_BASE_URL);
        keys.add(PERM_ID_BASE_URL);
        keys.add(DATA_MGMT_ADMIN_BASE_URL);
        keys.add(FILE_IO_BASE_URL);
        keys.add(NOTIFICATION_POLL_INTERVAL);
        keys.add(VICE_POLL_INTERVAL);
        keys.add(CONTEXT_CLICK_ENABLED);
        keys.add(PRIVATE_WORKSPACE);
        keys.add(PRIVATE_WORKSPACE_ITEMS);
        keys.add(DEFAULT_BETA_CATEGORY_ID);
        keys.add(DEFAULT_TRASH_CATEGORY_ID);
        keys.add(UNPROTECTED_MULE_SERVICE_BASE_URL);
        keys.add(DEFAULT_HPC_CATEGORY_ID);
        keys.add(BETA_AVU_IRI);
        keys.add(BETA_AVU_LABEL);
        keys.add(BETA_AVU_VALUE);
        keys.add(BETA_AVU_UNIT);
        keys.add(ONTOLOGY_ATTRS);
        keys.add(COMMUNITY_ATTR);
        keys.add(APPS_CARD_URL);
        keys.add(APPS_CARD_URL_OPTIONS);
        keys.add(CYVERSE_SUPPORT_USER);
        keys.add(SUPPORT_SERVICE_URL);
        keys.add(GROUPER_ALL_ID);
        keys.add(GROUPER_ALL_DISPLAY_NAME);
        keys.add(GROUPER_ID);
        keys.add(INTERCOM_APP_ID);
        keys.add(INTERCOM_COMPANY_ID);
        keys.add(INTERCOM_COMPANY_NAME);
        keys.add(INTERCOM_ENABLED);
        keys.add(DEFAULT_VICE_IMAGE);
        keys.add(DEFAULT_VICE_NAME);
        keys.add(DEFAULT_VICE_CAS_URL);
        keys.add(DEFAULT_VICE_CAS_VALIDATE);
        keys.add(TOOLS_MAX_MEM_LIMIT);
        keys.add(TOOLS_MAX_DISK_LIMIT);
        keys.add(TOOLS_MAX_CPU_LIMIT);
        keys.add(TOOLS_ADMIN_MAX_MEM_LIMIT);
        keys.add(TOOLS_ADMIN_MAX_DISK_LIMIT);
        keys.add(TOOLS_ADMIN_MAX_CPU_LIMIT);
        return keys;
    }

    /**
     * Initializes this class from the given set of properties.
     *
     * @param properties the properties that were fetched from the server.
     */
    public void initialize(Map<String, String> properties) {
        dataMgmtBaseUrl = properties.get(DATA_MGMT_BASE_URL);
        dataMgmtAdminBaseUrl = properties.get(DATA_MGMT_ADMIN_BASE_URL);
        fileIoBaseUrl = properties.get(FILE_IO_BASE_URL);
        muleServiceBaseUrl = properties.get(MULE_SERVICE_BASE_URL);
        unproctedMuleServiceBaseUrl = properties.get(UNPROTECTED_MULE_SERVICE_BASE_URL);
        privateWorkspace = properties.get(PRIVATE_WORKSPACE);
        privateWorkspaceItems = properties.get(PRIVATE_WORKSPACE_ITEMS);
        defaultBetaCategoryId = properties.get(DEFAULT_BETA_CATEGORY_ID);
        defaultHpcCategoryId = properties.get(DEFAULT_HPC_CATEGORY_ID);
        defaultTrashCategoryId = properties.get(DEFAULT_TRASH_CATEGORY_ID);
        contextClickEnabled = getBoolean(properties, CONTEXT_CLICK_ENABLED, false);
        notificationPollInterval = getInt(properties, NOTIFICATION_POLL_INTERVAL, 60);
        viceLogsPollInterval = getInt(properties, VICE_POLL_INTERVAL, 10000);
        htPathListFileIdentifier = properties.get(HT_PATH_LIST_FILE_IDENTIFIER);
        multiInputPathListFileIdentifier = properties.get(MULTI_INPUT_LIST_FILE_IDENTIFIER);
        communityDataPath = properties.get(COMMUNITY_DATA_PATH);
        irodsHomePath = properties.get(IRODS_HOME_PATH);
        baseTrashPath = properties.get(BASE_TRASH_PATH);
        permIdBaseUrl = properties.get(PERM_ID_BASE_URL);
        betaAvuIri = properties.get(BETA_AVU_IRI);
        betaAvuLabel = properties.get(BETA_AVU_LABEL);
        betaAvuValue = properties.get(BETA_AVU_VALUE);
        betaAvuUnit = properties.get(BETA_AVU_UNIT);
        ontologyAttrs = properties.get(ONTOLOGY_ATTRS);
        communityAttr = properties.get(COMMUNITY_ATTR);
        cardUrl = properties.get(APPS_CARD_URL);
        cardUrlOptions = properties.get(APPS_CARD_URL_OPTIONS);
        supportUser = properties.get(CYVERSE_SUPPORT_USER);
        supportServiceUrl = properties.get(SUPPORT_SERVICE_URL);
        grouperAllId = properties.get(GROUPER_ALL_ID);
        grouperAllDisplayName = properties.get(GROUPER_ALL_DISPLAY_NAME);
        grouperId = properties.get(GROUPER_ID);
        intercomAppId = properties.get(INTERCOM_APP_ID);
        companyId = properties.get(INTERCOM_COMPANY_ID);
        companyName = properties.get(INTERCOM_COMPANY_NAME);
        intercomEnabled = getBoolean(properties, INTERCOM_ENABLED, false);
        defaultViceImage = properties.get(DEFAULT_VICE_IMAGE);
        defaultViceName = properties.get(DEFAULT_VICE_NAME);
        defaultViceCasUrl = properties.get(DEFAULT_VICE_CAS_URL);
        defaultViceCasValidate = properties.get(DEFAULT_VICE_CAS_VALIDATE);
        toolsMaxMemLimit = getLong(properties, TOOLS_MAX_MEM_LIMIT, DEFAULT_TOOLS_MAX_MEM_LIMIT);
        toolsMaxDiskLimit = getLong(properties, TOOLS_MAX_DISK_LIMIT, DEFAULT_TOOLS_MAX_DISK_LIMIT);
        toolsMaxCPULimit = getDouble(properties, TOOLS_MAX_CPU_LIMIT, DEFAULT_TOOLS_MAX_CPU_LIMIT);
        toolsAdminMaxMemLimit = getLong(properties, TOOLS_ADMIN_MAX_MEM_LIMIT, DEFAULT_TOOLS_MAX_MEM_LIMIT);
        toolsAdminMaxDiskLimit = getLong(properties, TOOLS_ADMIN_MAX_DISK_LIMIT, DEFAULT_TOOLS_MAX_DISK_LIMIT);
        toolsAdminMaxCPULimit = getDouble(properties, TOOLS_ADMIN_MAX_CPU_LIMIT, DEFAULT_TOOLS_MAX_CPU_LIMIT);
    }

    /**
     * Obtains a boolean property value.
     *
     * @param properties   the property map.
     * @param name         the name of the property.
     * @param defaultValue the default value to use.
     * @return the property value or its default value.
     */
    private boolean getBoolean(Map<String, String> properties, String name, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(properties.get(name));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Obtains an integer property value.
     *
     * @param properties   the property map.
     * @param name         the name of the property.
     * @param defaultValue the default value to use.
     * @return the property value or its default value.
     */
    private int getInt(Map<String, String> properties, String name, int defaultValue) {
        try {
            return Integer.parseInt(properties.get(name));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Obtains a Long property value.
     *
     * @param properties   the property map.
     * @param name         the name of the property.
     * @param defaultValue the default value to use.
     * @return the property value or its default value.
     */
    private Long getLong(Map<String, String> properties, String name, Long defaultValue) {
        try {
            return Long.parseLong(properties.get(name));
        } catch (Exception e) {
            GWT.log("DEProps could not parse Long: " + properties.get(name));
            return defaultValue;
        }
    }

    /**
     * Obtains a Double property value.
     *
     * @param properties   the property map.
     * @param name         the name of the property.
     * @param defaultValue the default value to use.
     * @return the property value or its default value.
     */
    private Double getDouble(Map<String, String> properties, String name, Double defaultValue) {
        try {
            return Double.parseDouble(properties.get(name));
        } catch (Exception e) {
            GWT.log("DEProps could not parse Double: " + properties.get(name));
            return defaultValue;
        }
    }

    /**
     * Gets the polling interval for the MessagePoller of the Notification agent service.
     *
     * @return the poll interval in seconds as an int.
     */
    public int getNotificationPollInterval() {
        return notificationPollInterval;
    }

    public int getViceLogsPollInterval() {
        return viceLogsPollInterval;
    }

    /**
     * Gets the base URL of the data management services.
     *
     * @return the URL as a string.
     */
    public String getDataMgmtBaseUrl() {
        return dataMgmtBaseUrl;
    }

    public String getDataMgmtAdminBaseUrl() {
        return dataMgmtAdminBaseUrl;
    }

    /**
     * Gets the base URL of the file I/O services.
     *
     * @return the URL as a string.
     */
    public String getFileIoBaseUrl() {
        return fileIoBaseUrl;
    }

    /**
     * Gets the base URL used to access the DE Mule services.
     *
     * @return the URL as a string.
     */
    public String getMuleServiceBaseUrl() {
        return muleServiceBaseUrl;
    }

    /**
     * @return the privateWorkspace
     */
    public String getPrivateWorkspace() {
        return privateWorkspace;
    }

    /**
     * @return the privateWorkspaceItems
     */
    public String getPrivateWorkspaceItems() {
        return privateWorkspaceItems;
    }

    /**
     * @return the unique ID for the Beta category.
     */
    public String getDefaultBetaCategoryId() {

        return defaultBetaCategoryId;
    }

    public String getDefaultHpcCategoryId() {
        return defaultHpcCategoryId;
    }

    /**
     * @return the defaultTrashAppCategoryId
     */
    public String getDefaultTrashAppCategoryId() {
        return defaultTrashCategoryId;
    }

    public String getCommunityDataPath() {
        return communityDataPath;
    }

    public String getIrodsHomePath() {
        return irodsHomePath;
    }

    public String getBaseTrashPath() {
        return baseTrashPath;
    }

    /**
     * @return
     */
    public String getPermIdBaseUrl() {
        return permIdBaseUrl;
    }

    public String getBetaAvuIri() {
        return betaAvuIri;
    }

    public String getBetaAvuLabel() {
        return betaAvuLabel;
    }

    public String getBetaAvuValue() {
        return betaAvuValue;
    }

    public String getBetaAvuUnit() {
        return betaAvuUnit;
    }

    public String getOntologyAttrs() {
        return ontologyAttrs;
    }

    public String getCommunityAttr() {
        return communityAttr;
    }

    public String getAppsCardUrl() {
        return cardUrl;
    }

    public String getAppsCardUrlOptions() {
        return cardUrlOptions;
    }

    public String getSupportUser() {
        return supportUser;
    }

    public String getSupportServiceUrl() {
        return supportServiceUrl;
    }

    public String getGrouperAllId() {
        return grouperAllId;
    }

    public String getGrouperAllDisplayName() {
        return grouperAllDisplayName;
    }

    public String getGrouperId() {
        return grouperId;
    }

    public String getIntercomAppId() {
        return intercomAppId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public boolean isIntercomEnabled() {
        return intercomEnabled;
    }

    public String getMultiInputPathListFileIdentifier() {
        return multiInputPathListFileIdentifier;
    }

    public String getDefaultViceImage() {
        return defaultViceImage;
    }

    public String getDefaultViceName() {
        return defaultViceName;
    }

    public String getDefaultViceCasUrl() {
        return defaultViceCasUrl;
    }

    public String getDefaultViceCasValidate() {
        return defaultViceCasValidate;
    }

    public Long getToolsMaxMemLimit() {
        return toolsMaxMemLimit;
    }

    public Long getToolsMaxDiskLimit() {
        return toolsMaxDiskLimit;
    }

    public Double getToolsMaxCPULimit() {
        return toolsMaxCPULimit;
    }

    public Long getToolsAdminMaxMemLimit() {
        return toolsAdminMaxMemLimit;
    }

    public Long getToolsAdminMaxDiskLimit() {
        return toolsAdminMaxDiskLimit;
    }

    public Double getToolsAdminMaxCPULimit() {
        return toolsAdminMaxCPULimit;
    }
}
