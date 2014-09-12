(ns bono.db
  "Handle database operations."
  (:require [monger.core :as mg]
            [monger.collection :as mc]
  )
  (:import [com.mongodb MongoOptions ServerAddress]
           [org.bson.types.ObjectId]))

(defn connect
  "Connect to MONGOHQ_URL if it exists, else default port on localhost."
  [mongohq-url]
  (if mongohq-url
    ((mg/connect-via-uri mongohq-url) :conn)
    (mg/connect)))

(let [mongohq-url (System/getenv "MONGOHQ_URL")
      conn (connect mongohq-url)
      db (mg/get-db conn "bono")
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
