(ns bono.db
  "Handle database operations."
  (:require [monger.core :as mg]
            [monger.collection :as mc]
   )
  (:import [com.mongodb MongoOptions ServerAddress]
           [org.bson.types.ObjectId]))

;; Connect to localhost, default port
;;(let [conn (mg/connect)])
(mg/connect)

(defn insert-item [item]
  (println (str item))
  mc/insert "bono" item)

(defn find-items []
  mc/find "bono")
