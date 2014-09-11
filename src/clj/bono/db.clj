(ns bono.db
  "Handle database operations."
  (:require [monger.core :as mg]
            [monger.collection :as mc]
   )
  (:import [com.mongodb MongoOptions ServerAddress]
           [org.bson.types.ObjectId]))

;; Connect to localhost, default port
(let [conn (mg/connect)
      db (mg/get-db conn "bono")
      coll "items"
     ]

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

