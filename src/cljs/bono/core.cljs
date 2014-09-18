(ns bono.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-tools.dom :as ot :include-macros true]
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

(defn delete-item [event app item-id]
  (print "id: " item-id)
  (print "app-state: " @app)
  (edn-xhr
    {:method :delete
     :url (str "/items/" item-id)
     :data item-id
     :on-complete #(om/update! app [:items] (vec %))
    }))

(defn find-items [app]
  (edn-xhr
   {:method :get
    :url "/items"
    :on-complete #(om/update! app [:items] (vec %))}))

(defn add-item [app item]
    (om/update! app :err-msg {"item-price" ""})
    (edn-xhr
     {:method :post
      :url "/items"
      :data item
      :on-complete
      (fn [items]
        (om/update! app [:items] (vec items))
        )
      }))

(defn validate-input [event app owner]
  (let
    [item-name (-> "item-name" gdom/getElement .-value)
     item-price (-> "item-price" gdom/getElement .-value)
     item {:name item-name :price item-price}]

    (.preventDefault event)
    (print item)

    (if (js/isNaN (js/parseFloat item-price))
      (om/update! app :err-msg {"item-price" "Not a number!"})
      (add-item app item))
 ))

(defn error-msg [app owner opts]
  (reify
    om/IInitState
    (init-state [_] {:err-msg ""})

    om/IRenderState
    (render-state [this state]
                  (ot/div {:class "error"}
                          (ot/span {:class "error"} (get-in app [:err-msg (:el-id opts)]))))
    ))

(defn input-field [label id app owner]
  (ot/div {:class "input-field"}
          (ot/div {:class "input-label"}
                  (ot/label label))
          (ot/input {:id id})
          (om/build error-msg app {:opts {:el-id id}})
          ))

(defn add-item-view [app owner]
  (reify
    om/IInitState
      (init-state [_] {:update (chan)})

    om/IRender
      (render [this]
        (ot/form {:class "input-container"}
          (input-field "Item" "item-name" app owner)
          (input-field "Price" "item-price" app owner)
          (ot/button
            {:id "submit-button" :on-click #(validate-input % app owner)} "Add item")
       ))))

(defn display-item [app]
  (map (fn [item]
    (let [{:keys [_id name price]} item]

    (ot/div {:class "list-item"}
      (ot/div {:class "item-field"} (str "Item: " name))
      (ot/div {:class "item-field"} (str "Price: " price))
      (ot/div
        (ot/button
          {:id "delete-button" :class "delete-button"
           :on-click #(delete-item % app _id)} "Delete")))
      ))
      (:items app))
  )

(defn item-list [app owner]
  (reify
    om/IInitState
      (init-state [_] (find-items app))
    om/IRenderState
      (render-state [this items]
        (ot/div nil
          (ot/h2 nil "Items")
          (ot/div
           (display-item app))
         ))))

(defn app-view [app owner]
  (reify
    om/IRender
      (render [this]
        (ot/div {:class "column-main"}
          (om/build add-item-view app)
          (om/build item-list app))
        )
    )
  )

(om/root
  app-view
  app-state
  {:target (gdom/getElement "app")})
