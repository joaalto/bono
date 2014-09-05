(ns bono.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
  ))

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
        (. js/document (getElementById "item-name"))
                   .-value)
        item-price (->
        (. js/document (getElementById "item-price"))
                   .-value)
       ]
  (print "Add item!" item-name item-price))
        )

(defn item-list [app owner]
  (reify
    om/IRender
    (render [this]
      (dom/div nil
        (dom/h2 nil "Items")
        (apply dom/ul nil
          (om/build-all item-view (:items app)))
        (dom/button
          #js {:onClick #(add-item app owner)} "Add item")
        (dom/label nil "Item")
        (dom/input #js {:id "item-name"})
        (dom/label nil "Price")
        (dom/input #js {:id "item-price"})
       ))))

(om/root
  item-list
  app-state
  {:target (. js/document (getElementById "app"))})
