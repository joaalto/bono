(ns bono.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
  ))

(enable-console-print!)

(print "Jepa!")

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

(defn item-list [app owner]
  (reify
    om/IRender
    (render [this]
      (dom/div nil
        (dom/h2 nil "Items")
        (apply dom/ul nil
          (om/build-all item-view (:items app)))))))


(om/root
  item-list
  app-state
  {:target (. js/document (getElementById "app"))})
