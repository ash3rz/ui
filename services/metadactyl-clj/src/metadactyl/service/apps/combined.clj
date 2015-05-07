(ns metadactyl.service.apps.combined
  "This namespace contains an implementation of metadactyl.protocols.Apps that interacts with one
  or more other implementations. This implementation expects at most one the implementations that
  it interacts with to allow users to add new apps and edit existing ones. If this is not the case
  then the first app in the list that is capable of adding or editing apps wins."
  (:use [metadactyl.util.assertions :only [assert-not-nil]])
  (:require [metadactyl.persistence.jobs :as jp]
            [metadactyl.service.apps.job-listings :as job-listings]
            [metadactyl.service.apps.combined.job-view :as job-view]
            [metadactyl.service.apps.combined.jobs :as combined-jobs]
            [metadactyl.service.apps.combined.util :as util]))

(deftype CombinedApps [clients user]
  metadactyl.protocols.Apps

  (getUser [_]
    user)

  (getClientName [_]
    jp/combined-client-name)

  (getJobTypes [_]
    (apply concat (map #(.getJobTypes %) clients)))

  (listAppCategories [_ params]
    (apply concat (map #(.listAppCategories % params) clients)))

  (hasCategory [_ category-id]
    (some #(.hasCategory % category-id) clients))

  (listAppsInCategory [_ category-id params]
    (assert-not-nil
     [:category-id category-id]
     (when-let [client (first (filter #(.hasCategory % category-id) clients))]
       (.listAppsInCategory client category-id params))))

  (searchApps [_ search-term params]
    (->> (map #(.searchApps % search-term (select-keys params [:search])) clients)
         (remove nil?)
         (util/combine-app-search-results params)))

  (canEditApps [_]
    (some #(.canEditApps %) clients))

  (addApp [_ app]
    (.addApp (util/get-apps-client clients) app))

  (previewCommandLine [_ app]
    (.previewCommandLine (util/get-apps-client clients) app))

  (listAppIds [_]
    (apply merge-with concat (map #(.listAppIds %) clients)))

  (deleteApps [_ deletion-request]
    (.deleteApps (util/get-apps-client clients) deletion-request))

  (getAppJobView [_ app-id]
    (job-view/get-app app-id clients))

  (deleteApp [_ app-id]
    (.deleteApp (util/get-apps-client clients) app-id))

  (relabelApp [_ app]
    (.relabelApp (util/get-apps-client clients) app))

  (updateApp [_ app]
    (.updateApp (util/get-apps-client clients) app))

  (copyApp [_ app-id]
    (.copyApp (util/get-apps-client clients) app-id))

  (getAppDescription [_ app-id]
    (->> (map #(.getAppDescription % app-id) clients)
         (remove nil?)
         (first)))

  (getAppDetails [_ app-id]
    (->> (map #(.getAppDetails % app-id) clients)
         (remove nil?)
         (first)))

  (removeAppFavorite [_ app-id]
    (.removeAppFavorite (util/get-apps-client clients) app-id))

  (addAppFavorite [_ app-id]
    (.addAppFavorite (util/get-apps-client clients) app-id))

  (isAppPublishable [_ app-id]
    (.isAppPublishable (util/get-apps-client clients) app-id))

  (makeAppPublic [_ app]
    (.makeAppPublic (util/get-apps-client clients) app))

  (deleteAppRating [_ app-id]
    (.deleteAppRating (util/get-apps-client clients) app-id))

  (rateApp [_ app-id rating]
    (.rateApp (util/get-apps-client clients) app-id rating))

  (getAppTaskListing [_ app-id]
    (->> (map #(.getAppTaskListing % app-id) clients)
         (remove nil?)
         (first)))

  (getAppToolListing [_ app-id]
    (->> (map #(.getAppToolListing % app-id) clients)
         (remove nil?)
         (first)))

  (getAppUi [_ app-id]
    (.getAppUi (util/get-apps-client clients) app-id))

  (addPipeline [self pipeline]
    (.formatPipelineTasks self (.addPipeline (util/get-apps-client clients) pipeline)))

  (formatPipelineTasks [_ pipeline]
    (reduce (fn [acc client] (.formatPipelineTasks client acc)) pipeline clients))

  (updatePipeline [self pipeline]
    (.formatPipelineTasks self (.updatePipeline (util/get-apps-client clients) pipeline)))

  (copyPipeline [self app-id]
    (.formatPipelineTasks self (.copyPipeline (util/get-apps-client clients) app-id)))

  (editPipeline [self app-id]
    (.formatPipelineTasks self (.editPipeline (util/get-apps-client clients) app-id)))

  (listJobs [self params]
    (job-listings/list-jobs self user params))

  (loadAppTables [_ app-ids]
    (apply concat (map  #(.loadAppTables % app-ids) clients)))

  (submitJob [self submission]
    (if-let [apps-client (util/apps-client-for-job submission clients)]
      (.submitJob apps-client submission)
      (job-listings/list-job self (combined-jobs/submit user clients submission))))

  (translateJobStatus [_ job-type status]
    (->> (map #(.translateJobStatus % job-type status) clients)
         (remove nil?)
         (first)))

  (updateJobStatus [self job-step job status end-date]
    (combined-jobs/update-job-status self clients job-step job status end-date))

  (getDefaultOutputName [_ io-map source-step]
    (.getDefaultOutputName (util/apps-client-for-app-step clients source-step) io-map source-step))

  (getJobStepStatus [_ job-step]
    (.getJobStepStatus (util/apps-client-for-job-step clients job-step) job-step))

  (buildNextStepSubmission [self job-step job]
    (combined-jobs/build-next-step-submission self clients job-step job)))