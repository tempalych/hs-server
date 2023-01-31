(ns hs.server.controller.page-404)

(defn page-404 [_]
  {:status 404
   :headers {"content-type" "text/plain"}
   :body "404: Page not found"})