(ns bono.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [file-response resource-response response]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [ring.adapter.jetty :as ring]
            [prone.middleware :as prone]
            [bono.db :as db]
            )
  (:gen-class))

(defroutes app-routes
  (GET "/" [] (file-response "resources/public/index.html"))
  (GET "/items" [] (db/find-items))
  (POST "/items" {params :params} (db/insert-item params) )
  (DELETE "/items/:id" [id] (db/delete-item id))
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

(defn -main []
  (let [port (Integer. (or (System/getenv "PORT") "3000"))]
    (ring/run-jetty app {:port port
                    :join false})))
