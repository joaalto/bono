(ns bono.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-tools.dom :as domt :include-macros true]
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

(def app-state (atom {}))

(defn display-item [{:keys [name price]}]
  (str "Item: " name " price: " price))

(defn item-view [item owner]
  (reify
    om/IRender
    (render [this]
      (domt/li nil (display-item item)))))

(defn find-items [app]
  (edn-xhr
   {:method :get
    :url "/items"
    :on-complete #(om/update! app [:items] (vec %))}))

(defn add-item [app owner]
  (let [item-name (->
                   (gdom/getElement "item-name") .-value)
        item-price (->
                    (gdom/getElement "item-price") .-value)
        item {:name item-name :price item-price}
        ]

    (print "Item: " item)

    (edn-xhr
     {:method :post
      :url "/items"
      :data item
      :on-complete
      (fn [items]
        (om/update! app [:items] (vec items))
        )
      }
     )
   ))

(defn input-field [label id]
  (domt/div {:class "input-field"}
    (domt/label label)
    (domt/input {:id id})
  ))

(defn add-item-view [app owner]
  (reify
    om/IInitState
      (init-state [_] {:update (chan)})

    om/IRender
      (render[this]
        (domt/div {:class input-container}
          (input-field "Item" "item-name")
          (input-field "Price" "item-price")
          (domt/button
            {:on-click #(add-item app owner)} "Add item")
       ))))

(defn item-list [app owner]
  (reify
    om/IInitState
      (init-state [_] (find-items app))
    om/IRenderState
      (render-state [this items]
        (domt/div nil
          (domt/h2 nil "Items")
          (apply dom/ul nil
            (om/build-all item-view (:items app)))
         ))))

(defn app-view [app owner]
  (reify
    om/IRender
      (render [this]
        (domt/div nil
          (om/build item-list app)
          (om/build add-item-view app))
        )
    )
  )

(om/root
  app-view
  app-state
  {:target (gdom/getElement "app")})
