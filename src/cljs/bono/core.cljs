(ns bono.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [goog.dom :as gdom]
            [cljs.core.async :as async :refer [put! chan <!]]
            [om-sync.core :refer [om-sync]]
            [om-sync.util :refer [tx-tag edn-xhr]]
  )
  (:import [goog.net XhrIo]
           goog.net.EventType
           [goog.events EventType])
)

(enable-console-print!)

(def app-state
  (atom
   {:items
    [{:name "Blue shirt" :price 35}
     {:name "Green trousers" :price 55}
     {:name "Beanie hat" :price 30}
    ]}))

(defn display-item [{:keys [name price]}]
  (str "Item: " name " price: " price))

(defn item-view [item owner]
  (reify
    om/IRender
    (render [this]
      (dom/li nil (display-item item)))))

(defn add-item [app owner]
  (let [item-name (->
          (gdom/getElement "item-name") .-value)
        item-price (->
          (gdom/getElement "item-price") .-value)
        item {:name item-name :price item-price}

        xhr (XhrIo.)

        stored-items (->
        (. xhr
           send "/items" "POST" item
           #js {"Content-Type" "application/edn"})
        )

       ]

    (print "Item: " item)
    (print stored-items)

    (om/set-state! owner :items stored-items)
    ;(reset! app-state )

   ))

(defn add-item-view [app owner]
  (reify
    om/IInitState
      (init-state [_] {:update (chan)})

    om/IRender
      (render[this]
        (dom/div nil
          (dom/label nil "Item")
          (dom/input #js {:id "item-name"})
          (dom/label nil "Price")
          (dom/input #js {:id "item-price"})
          (dom/button
            #js {:onClick #(add-item app owner)} "Add item")
       ))))

(defn item-list [app owner]
  (reify
    om/IInitState
      (init-state [_] {:update (chan)})
    om/IRenderState
      (render-state [this items]
        (dom/div nil
          (dom/h2 nil "Items")
          (apply dom/ul nil
            (om/build-all item-view (:items app)))
         ))))

(defn app-view [app owner]
  (reify
    om/IRender
      (render [this]
        (dom/div nil
          (om/build item-list app)
          (om/build add-item-view app))
        )
    )
  )

(om/root
  app-view
  app-state
  {:target (gdom/getElement "app")})
