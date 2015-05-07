(ns metadactyl.service.apps
  (:use [kameleon.uuids :only [uuidify]]
        [korma.db :only [transaction]]
        [slingshot.slingshot :only [try+ throw+]])
  (:require [cemerick.url :as curl]
            [clojure.tools.logging :as log]
            [clojure-commons.error-codes :as ce]
            [mescal.de :as agave]
            [metadactyl.clients.notifications :as cn]
            [metadactyl.persistence.jobs :as jp]
            [metadactyl.persistence.oauth :as op]
            [metadactyl.protocols]
            [metadactyl.service.apps.agave]
            [metadactyl.service.apps.combined]
            [metadactyl.service.apps.de]
            [metadactyl.service.apps.jobs :as jobs]
            [metadactyl.user :as user]
            [metadactyl.util.config :as config]
            [metadactyl.util.json :as json-util]
            [metadactyl.util.service :as service]))

(defn- authorization-uri
  [server-info username state-info]
  (str (assoc (curl/url (:auth-uri server-info))
         :query {:response_type "code"
                 :client_id     (:client-key server-info)
                 :redirect-uri  (:redirect-uri server-info)
                 :state         (op/store-authorization-request username state-info)})))

(defn- authorization-redirect
  [server-info username state-info]
  (throw+ {:error_code ce/ERR_TEMPORARILY_MOVED
           :location   (authorization-uri server-info username state-info)}))

(defn- has-access-token
  [{:keys [api-name] :as server-info} username]
  (seq (op/get-access-token api-name username)))

(defn- get-access-token
  [{:keys [api-name] :as server-info} state-info username]
  (if-let [token-info (op/get-access-token api-name username)]
    (assoc (merge server-info token-info)
      :token-callback  (partial op/store-access-token api-name username)
      :reauth-callback (partial authorization-redirect server-info username state-info))
    (authorization-redirect server-info username state-info)))

(defn- get-agave-client
  [state-info username]
  (let [server-info (config/agave-oauth-settings)]
    (agave/de-agave-client-v2
     (config/agave-base-url)
     (config/agave-storage-system)
     (partial get-access-token (config/agave-oauth-settings) state-info username)
     (config/agave-jobs-enabled))))

(defn- get-agave-apps-client
  [state-info {:keys [username] :as user}]
  (metadactyl.service.apps.agave.AgaveApps.
   (get-agave-client state-info username)
   (partial has-access-token (config/agave-oauth-settings) username)
   user))

(defn- get-apps-client-list
  [user state-info]
  (vector (metadactyl.service.apps.de.DeApps. user)
          (when (and user (config/agave-enabled))
            (get-agave-apps-client state-info user))))

(defn- get-apps-client
  ([user]
     (get-apps-client user ""))
  ([user state-info]
     (metadactyl.service.apps.combined.CombinedApps.
      (remove nil? (get-apps-client-list user state-info))
      user)))

(defn- get-apps-client-for-username
  ([username]
     (get-apps-client-for-username username ""))
  ([username state-info]
     (get-apps-client (user/load-user username) state-info)))

(defn get-app-categories
  [user params]
  (let [client (get-apps-client user "type=apps")]
    {:categories (transaction (.listAppCategories client params))}))

(defn list-apps-in-category
  [user category-id params]
  (let [state-info (str "type=apps&app-category=" category-id)
        client     (get-apps-client user state-info)]
    (.listAppsInCategory client category-id params)))

(defn search-apps
  [user {:keys [search] :as params}]
  (.searchApps (get-apps-client user) search params))

(defn add-app
  [user app]
  (.addApp (get-apps-client user) app))

(defn preview-command-line
  [user app]
  (.previewCommandLine (get-apps-client user) app))

(defn list-app-ids
  [user]
  (.listAppIds (get-apps-client user)))

(defn delete-apps
  [user deletion-request]
  (.deleteApps (get-apps-client user) deletion-request))

(defn get-app-job-view
  [user app-id]
  (.getAppJobView (get-apps-client user) app-id))

(defn delete-app
  [user app-id]
  (.deleteApp (get-apps-client user) app-id))

(defn relabel-app
  [user app]
  (.relabelApp (get-apps-client user) app))

(defn update-app
  [user app]
  (.updateApp (get-apps-client user) app))

(defn copy-app
  [user app-id]
  (.copyApp (get-apps-client user) app-id))

(defn get-app-description
  [user app-id]
  (.getAppDescription (get-apps-client user) app-id))

(defn get-app-details
  [user app-id]
  (.getAppDetails (get-apps-client user) app-id))

(defn remove-app-favorite
  [user app-id]
  (.removeAppFavorite (get-apps-client user) app-id))

(defn add-app-favorite
  [user app-id]
  (.addAppFavorite (get-apps-client user) app-id))

(defn app-publishable?
  [user app-id]
  {:publishable (.isAppPublishable (get-apps-client user) app-id)})

(defn make-app-public
  [user app]
  (.makeAppPublic (get-apps-client user) app))

(defn delete-app-rating
  [user app-id]
  (.deleteAppRating (get-apps-client user) app-id))

(defn rate-app
  [user app-id rating]
  (.rateApp (get-apps-client user) app-id rating))

(defn get-app-task-listing
  [user app-id]
  (.getAppTaskListing (get-apps-client user) app-id))

(defn get-app-tool-listing
  [user app-id]
  (.getAppToolListing (get-apps-client user) app-id))

(defn get-app-ui
  [user app-id]
  (.getAppUi (get-apps-client user) app-id))

(defn add-pipeline
  [user pipeline]
  (.addPipeline (get-apps-client user) pipeline))

(defn update-pipeline
  [user pipeline]
  (.updatePipeline (get-apps-client user) pipeline))

(defn copy-pipeline
  [user app-id]
  (.copyPipeline (get-apps-client user) app-id))

(defn edit-pipeline
  [user app-id]
  (.editPipeline (get-apps-client user) app-id))

(defn list-jobs
  [user params]
  (.listJobs (get-apps-client user) params))

(defn- format-job-submission-response
  [job-info]
  {:id         (:id job-info)
   :name       (:name job-info)
   :status     (:status job-info)
   :start-date (:startdate job-info)})

(defn submit-job
  [{username :shortUsername email :email :as user} submission]
  (json-util/log-json "submission" submission)
  (let [job-info (.submitJob (get-apps-client user) submission)]
    (cn/send-job-status-update username email job-info)
    (format-job-submission-response job-info)))

(defn check-next-step-submission
  [job-id external-id]
  (let [job-step (jobs/lock-job-step job-id external-id)
        job      (jobs/lock-job job-id)]
    (.buildNextStepSubmission (get-apps-client-for-username (:username job)) job-step job)))

(defn update-job-status
  ([external-id status end-date]
     (let [{:keys [job-id]} (jobs/get-unique-job-step external-id)]
       (update-job-status job-id external-id status end-date)))
  ([job-id external-id status end-date]
     (transaction
      (let [job-step (jobs/lock-job-step job-id external-id)
            job      (jobs/lock-job job-id)
            batch    (when-let [parent-id (:parent-id job)] (jobs/lock-job parent-id))]
        (-> (get-apps-client-for-username (:username job))
            (jobs/update-job-status job-step job batch status end-date))))))

(defn- sync-job-status
  [{:keys [username id] :as job}]
  (try+
   (log/info "synchronizing the job status for" id)
   (transaction (jobs/sync-job-status (get-apps-client-for-username username) job))
   (catch Object _
     (log/error (:throwable &throw-context) "unable to sync the job status for job" id))))

(defn sync-job-statuses
  []
  (log/info "synchronizing job statuses")
  (try+
   (dorun (map sync-job-status (jp/list-incomplete-jobs)))
   (catch Object _
     (log/error (:throwable &throw-context)
                "error while obtaining the list of jobs to synchronize.")))
  (log/info "done syncrhonizing job statuses"))

(defn update-job
  [user job-id body]
  (jobs/update-job user job-id body))

(defn delete-job
  [user job-id]
  (jobs/delete-job user job-id)
  nil)

(defn delete-jobs
  [user body]
  (jobs/delete-jobs user (:analyses body))
  nil)