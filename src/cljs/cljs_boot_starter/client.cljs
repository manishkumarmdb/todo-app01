(ns cljs-boot-starter.client
  (:require [reagent.core :as reagent :refer [atom render]]
            ))

(enable-console-print!)

(defn header []
  [:div.page-header
   [:h3 "ToDo Application"]])

(defonce todos (reagent/atom (sorted-map)))

(defonce counter (reagent/atom 0))

(defn add-todo [text]
  (let [id (swap! counter inc)]
    (swap! todos assoc id {:id id
                           :title text
                           :done false})))

(defn )

(defn todo-app []
  (let [fltr (reagent/atom :all)]
    (fn []
      (let [total-item (vals @todos)
            completed (->> total-item (filter :done) count)
            active (- (count total-item) completed)]
        [:div
         [:]]))) [:div
                  [header]
                  [:table
                   [:tr
                    [:td
                     [:input {:type "text"
                              :id "task"
                              :placeholder "what do you want to add in todos ?"
                              ;;:size "50"
                              :on-save add-todo
                              }]]]]])

(defn init []
  (reagent/render [todo-app] (.getElementById js/document "my-app-area")))

(init)
