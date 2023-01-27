(ns hs.server.config
  (:require [mount.core :as mount :refer [defstate]] 
            [aero.core :refer [read-config]]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]))

(def custom-config ".custom/config.edn")

(defstate config
  :start (let [config-file (if (.exists (io/file custom-config))
                             custom-config
                             "config.edn")]
            (log/info "Config initialized from" config-file)
            (read-config (io/file config-file))))


(comment
  (.exists (io/file ".custom/config.edn")) 
  
  )