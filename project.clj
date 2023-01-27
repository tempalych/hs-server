(defproject hs "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"} 
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [mount "0.1.16"] ; mounting
                 [ring/ring-core "1.9.6"]
                 [ring/ring-jetty-adapter "1.9.6"]
                 [compojure "1.7.0"] ; routing
                 [aero "1.1.6"] ; config
                 [org.clojure/java.jdbc "0.7.8"]
                 [org.postgresql/postgresql "42.1.3"]
                 [hikari-cp "3.0.1"]
                 [hiccup "2.0.0-alpha2"] ; html-markup
                 [cheshire "5.11.0"] ; json
                 [org.clojure/tools.logging "1.2.4"]
                 [ch.qos.logback/logback-classic "1.2.3"]]
  :main ^:skip-aot hs.server.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
              :test {:dependencies [[com.h2database/h2 "2.1.214"]]
                     :plugins [[lein-h2 "0.1.0"]]}})