(ns bono.db
  "Handle database operations."
  (:require [monger.core :as mg]
            [monger.collection :as mc]
   )
  (:import [com.mongodb MongoOptions ServerAddress]
           [org.bson.types.ObjectId]))

;; Connect to localhost, default port


(let [conn (mg/connect)
      db (mg/get-db conn "bono")]
  (defn insert-item [item]
    (println item)
    (mc/insert db "items" item)
  )
)

(defn find-items []
  mc/find "bono")
