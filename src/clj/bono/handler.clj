(ns bono.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as json]
            [bono.db :as db]
            ))

(defn json-response []
  )

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/items" [] (db/find-items []))
  (POST "/items" {params :params} (db/insert-item params) )
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
