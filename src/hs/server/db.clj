(ns hs.server.db
  (:require [mount.core :as mount :refer [defstate]]
            [hikari-cp.core :as cp]
            [hs.server.config :refer [config]]
            [clojure.tools.logging :as log]))

(defstate db
  :start (let [pool-opts (:db config)
               store (cp/make-datasource pool-opts)]
           (log/info "Database initialized")
           {:datasource store})
  :stop (-> db :datasource cp/close-datasource))