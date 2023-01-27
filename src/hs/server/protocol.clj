(ns hs.server.protocol
  (:require [mount.core :as mount :refer [defstate]]
            [clojure.java.jdbc :as jdbc]
            [clojure.instant :as instant]
            [cheshire.core :as json])
  (:import [java.sql Timestamp]
           [java.sql Date]
           [java.time.format DateTimeFormatter]
           [java.time LocalDate]
           [java.time Instant]))

(defstate protocol
  :start (do (extend-protocol jdbc/IResultSetReadColumn
               java.sql.Timestamp
               (result-set-read-column [v _ _]
                 (.toInstant v))
               java.sql.Date
               (result-set-read-column [v _ _]
                 (.toLocalDate v)))

             (extend-protocol jdbc/ISQLValue
               java.time.Instant
               (sql-value [v] (Timestamp/from v))
               java.time.LocalDate
               (sql-value [v] (Date/valueOf v)))

             (extend-protocol cheshire.generate/JSONable
               java.time.LocalDate
               (to-json [dt gen]
                 (cheshire.generate/write-string gen (str dt))))))
