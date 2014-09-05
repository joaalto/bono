(ns bono.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [file-response resource-response response]]
            [ring.middleware.json :as json]
            [bono.db :as db]
            ))

(defn json-response []
  )

(defroutes app-routes
  (GET "/" [] (file-response "resources/public/index.html"))
  (GET "/items" [] (db/find-items []))
  (POST "/items" {params :params} (db/insert-item params) )
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
