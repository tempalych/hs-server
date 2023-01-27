(ns hs.server.core
  (:gen-class)
  (:require [mount.core :as mount :refer [defstate]]
            [ring.adapter.jetty :refer [run-jetty]]
            [hs.server.config :refer [config]]
            [hs.server.db :refer [db]]
            [hs.server.protocol :refer [protocol]]
            [hs.server.cors :refer [wrap-cors]] 
            [cheshire.core :as json]
            [compojure.core :refer [defroutes GET POST DELETE PUT]]
            [hs.server.controller.patient :refer [patients-list
                                                  patient-new
                                                  patient-delete
                                                  patient-update
                                                  patient-search]]
            [hs.server.controller.page-404 :refer [page-404]]
            [clojure.tools.logging :as log]
            [clojure.walk :refer [keywordize-keys]]))

(defn get-request-body [request]
  (log/info "request" request)
  (let [body (slurp (:body request))
        keywordized-body (-> body
                             json/parse-string
                             keywordize-keys)] 
    keywordized-body))

(defroutes app
  (GET "/patients" _ (patients-list db))
  (POST "/patients/find" request (patient-search db (get-request-body request)))
  (PUT "/patients" request (patient-update db (get-request-body request)))
  (POST "/patients" request (patient-new db (get-request-body request)))
  (DELETE "/patients" request (patient-delete db (get-request-body request)))
  page-404)

(def wrapped-app
  (-> app
      wrap-cors))

(defstate server
  :start (let [jetty-opt (:jetty config)] 
           (run-jetty wrapped-app jetty-opt))
  :stop (.stop server))

(defn -main
  [& _]
  (log/info "HS: Server started")
  (mount/start))



(comment 
  (mount/start)
  (mount/stop)
  )