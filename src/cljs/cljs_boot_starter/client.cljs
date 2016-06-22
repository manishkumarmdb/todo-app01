(ns cljs-boot-starter.client
  (:require [reagent.core :as reagent :refer [atom render]]
            [goog.dom :as dom]
            ))

(enable-console-print!)

(enable-console-print!)

(defn header []
  [:div.page-header
   [:h3 "ToDo Application"]])

(def todos (reagent/atom []))

(def input-id (reagent/atom 1))

(def counter (reagent/atom 0))

(def todo-temp (reagent/atom ""))

(defn add-in-todo [text]
  (let [id (swap! counter inc)]
    (sorted-map :id id
                :todo text
                :status true)))

(defn checkbox []
  [:div
   [:input {:type "checkbox"
            ;;:checked true
            }]
   " "
   ])

(defn delete-button []
  [:div
   [:input {:type "button"
            :value "remove"
            ;;:on-click #(remove key map....)
            }]])

(defn todos-list []
  [:div
   [:table
    (for [id @todos]
      ^{:key id}
      [:tr
       [:td
        [checkbox]]
       [:td
        (:todo id)]
       [:td
        [delete-button]]])]])

(defn todo-input []
  (let [input (reagent/atom "")]
    (fn []
      [:div
       [:input {:type "text"
                :placeholder "add todos"
                :on-change #(reset! input (-> % .-target .-value))
                }]
       " "
       [:input {:type "button"
                :value "add"
                :on-click #(swap! todos conj (add-in-todo @input))}]
       ])
    ))

(defn footer []
  (let []
    (fn []
      [:div
       [:span
        [:input {:type "button"
                 :value "all"}]] " "
       [:span
        [:input {:type "button"
                 :value "active"}]] " "
       [:span
        [:input {:type "button"
                 :value "completed"}]] " "
       [:span
        [:input {:type "button"
                 :value "clear-completed"}]] " "]
      )))

(defn todo-app []
  [:div
   [:div
    [header]]
   [:div
    [todo-input]]
   [:div
    [todos-list]]
   [:div
    [footer]]])

(defn init []
  (reagent/render [todo-app] (.getElementById js/document "my-app-area")))

(init)
