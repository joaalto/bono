(ns bono.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [file-response resource-response response]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [prone.middleware :as prone]
            [bono.db :as db]
            ))

(defroutes app-routes
  (GET "/" [] (file-response "resources/public/index.html"))
  (GET "/items" [] (db/find-items))
  (POST "/items" {params :params} (db/insert-item params) )
  (route/resources "/")
  (route/not-found "Not Found"))

(defn new-item [item]
  (db/insert-item item)
)

(def app
  (-> app-routes
    (wrap-restful-format )
    (prone/wrap-exceptions)
   ))
