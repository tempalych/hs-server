(ns hs.server.controller.patient-test
  (:require [clojure.test :refer [deftest is]]
            [clojure.java.jdbc :as jdbc]
            [cheshire.core :as json]
            [hs.server.controller.patient :as sut]))

(def h2
  {:classname   "org.h2.Driver"
   :subprotocol "h2:mem"
   :subname     "demo;DB_CLOSE_DELAY=-1"
   :user        "sa"
   :password    ""})

(defn create-table-patients [db]
  (jdbc/db-do-commands
   db
   (jdbc/create-table-ddl :patients
                          [[:id "bigint auto_increment"]
                           [:fname "varchar(50)"]
                           [:lname "varchar(50)"]
                           [:pname "varchar(50)"]
                           [:gender "varchar(2)"]
                           [:birthdate "date"]
                           [:address "varchar(1000)"]
                           [:insurance "varchar(100)"]])))

(defn drop-table-patients [db]
  (jdbc/db-do-commands
   db
   (jdbc/drop-table-ddl :patients)))

(deftest patient-new
  (create-table-patients h2)
  (is (= (sut/patient-new h2 {:fname "fname"
                              :lname "lname"
                              :pname "pname"
                              :address "address"
                              :gender "M"
                              :insurance "insurance"})
         {:status 200
          :headers {"content-type" "application/json"}
          :body (json/encode '({:id 1}))}))

  (is (= (sut/patients-list h2)
         {:status 200
          :headers {"content-type" "application/json"}
          :body (json/encode '({:id 1
                                :fname "fname"
                                :lname "lname"
                                :pname "pname"
                                :address "address"
                                :gender "M"
                                :birthdate nil
                                :insurance "insurance"}))}))
  (drop-table-patients h2))

(deftest patient-delete
  (create-table-patients h2)
  (sut/patient-new h2 {:fname "fname"
                       :lname "lname"
                       :pname "pname"
                       :address "address"
                       :gender "M"
                       :insurance "insurance"})
  (is (= (sut/patient-delete h2 {:id 1})
         {:status 200
          :headers {"content-type" "application/json"}
          :body (json/encode '[1])}))
  (drop-table-patients h2))

(deftest patient-update
  (create-table-patients h2)
  (sut/patient-new h2 {:fname "fname"
                       :lname "lname"
                       :pname "pname"
                       :address "address"
                       :gender "M"
                       :insurance "insurance"})
  (is (= (sut/patient-update h2 {:id 1
                                 :fname "fname1"
                                 :lname "lname1"
                                 :pname "pname1"
                                 :address "address1"
                                 :gender "F"
                                 :insurance "insurance1"})
         {:status 200
          :headers {"content-type" "application/json"}
          :body (json/encode '[1])}))

  (is (= (sut/patients-list h2)
         {:status 200
          :headers {"content-type" "application/json"}
          :body (json/encode '({:id 1
                                :fname "fname1"
                                :lname "lname1"
                                :pname "pname1"
                                :address "address1"
                                :gender "F"
                                :birthdate nil
                                :insurance "insurance1"}))}))
  (drop-table-patients h2))


(comment
  (jdbc/query h2 "select * from patients"))
