(ns bono.db
  "Handle database operations."
  (:require [monger.core :as mg]
            [monger.collection :as mc]
  )
  (:import [com.mongodb MongoOptions ServerAddress]
           [org.bson.types.ObjectId]))

(defn get-db
  "Connect to MONGOLAB_URI if it exists, else default port on localhost."
  [mongolab-uri]
  (if mongolab-uri
    ((mg/connect-via-uri mongolab-uri) :db)
    (mg/get-db (mg/connect) "bono")))

(let [mongolab-uri (System/getenv "MONGOLAB_URI")
      db (get-db mongolab-uri)
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

  (defn delete-item [id]
    (mc/remove-by-id db "items" id)
    (find-items))
)
