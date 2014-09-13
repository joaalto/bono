(ns bono.db
  "Handle database operations."
  (:require [monger.core :as mg]
            [monger.collection :as mc]
  )
  (:import [com.mongodb MongoOptions ServerAddress]
           [org.bson.types.ObjectId]))

(defn connect
  "Connect to MONGOLAB_URI if it exists, else default port on localhost."
  [mongolab-uri]
  (if mongolab-uri
    ((mg/connect-via-uri mongolab-uri) :db)
    (mg/get-db (mg/connect) "bono")))

(let [mongolab-uri (System/getenv "MONGOLAB_URI")
      db (connect mongolab-uri)
      coll "items"]

  (defn id->str [item]
    (assoc item :_id (str (:_id item)))
  )

  (defn find-items []
    (let
      [items (doall (mc/find-maps db coll))]
      (map id->str items)))

  (defn insert-item [item]
    (println item)
    (mc/insert db "items" item)
    (find-items)
  )
)
