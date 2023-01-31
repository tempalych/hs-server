(ns hs.server.controller.patient
  (:require [clojure.java.jdbc :as jdbc]
            [cheshire.core :as json]
            [clojure.tools.logging :as log]
            [clojure.string :as string])
  (:import [java.time LocalDate]))

(defn exception-json [msg]
  (log/error msg)
  {:status 500
   :headers {"content-type" "application/json"}
   :body {:error-text (str "Internal exception: " msg)}})

(defn patients-list [db]
  (try
    (log/info "Patients list requested")
    (let [patients (jdbc/query db "select id, fname, lname, pname, address, gender,
                                  birthdate, insurance from patients")]
      {:status 200
       :headers {"content-type" "application/json"}
       :body (json/encode patients)})
    (catch Exception e
      (exception-json e))))

(defn patient-search [db request]
  (try
    (log/info "Patients search requested" request)
    (let [{:keys [fname lname]} request
          lname (str (string/lower-case (or lname "")) "%")
          fname (str (string/lower-case (or fname "")) "%")
          query (str "select id, fname, lname, pname, address, gender,
                      birthdate, insurance from patients where 1=1"
                     (when lname " and lower (lname) like ?")
                     (when fname " and lower (fname) like ?"))
          statement (vec
                     (filter some?
                             [query
                              (when lname (str (string/lower-case lname) "%"))
                              (when fname (str (string/lower-case fname) "%"))]))
          patients (jdbc/query db statement)]
      {:status 200
       :headers {"content-type" "application/json"}
       :body (json/encode patients)})
    (catch Exception e
      (exception-json e))))

(defn patient-new [db request]
  (try
    (log/info "New patient requested: " request)
    (let [{:keys [fname
                  lname
                  pname
                  address
                  gender
                  birthdate
                  insurance]} request
          birthdate (if (nil? birthdate)
                      nil
                      (LocalDate/parse birthdate))
          result (jdbc/insert! db :patients {:fname fname
                                             :lname lname
                                             :pname pname
                                             :address address
                                             :gender gender
                                             :birthdate birthdate
                                             :insurance insurance})]
      (log/info result)
      {:status 200
       :headers {"content-type" "application/json"}
       :body (json/encode result)})
    (catch Exception e
      (exception-json e))))

(defn patient-delete [db request]
  (log/info "Delete patient requested: " request)
  (try (let [result (jdbc/delete! db :patients ["id = ?" (:id request)])]
         {:status 200
          :headers {"content-type" "application/json"}
          :body (json/encode result)})
       (catch Exception e
         (exception-json e))))

(defn patient-update [db request]
  (log/info "Update patient requested: " request)
  (try
    (let [{:keys [id 
                  fname
                  lname
                  pname
                  address
                  gender
                  birthdate
                  insurance]} request
          birthdate (if (nil? birthdate)
                      nil
                      (LocalDate/parse birthdate))
          result (jdbc/update! db :patients
                               {:fname fname
                                :lname lname
                                :pname pname
                                :address address
                                :gender gender
                                :birthdate birthdate
                                :insurance insurance}
                               ["id = ?" id])]
      {:status 200
       :headers {"content-type" "application/json"}
       :body (json/encode result)})
    (catch Exception e 
      (exception-json e))))



(comment 
  )