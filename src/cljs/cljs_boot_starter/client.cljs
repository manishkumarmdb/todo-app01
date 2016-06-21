(ns cljs-boot-starter.client
  (:require [reagent.core :as reagent :refer [atom render]]
            [goog.dom :as dom]
            ))

(enable-console-print!)

(defn header []
  [:div.page-header
   [:h3 "ToDo Application"]])

(def todos (reagent/atom (sorted-map)))

(def input-id (reagent/atom 1))

(def counter (reagent/atom 0))

(def todo-text (reagent/atom nil))

(defn add-in-todo [task]
  (todos :id (swap! counter inc)
         :task task))

(defn todo-input []
  (let [default-string ""
        title (reagent/atom default-string)]
    [:div
     [:input {:type "text"
              :value @title
              :placeholder "add todo...."
              :on-change #(reset! title (-> .target .-value))
              }]
     " "
     [:input {:type "button"
              :value "add"
              :on-click #(reset! todo-text (swap! todos conj (add-in-todo @title))) (cons title (sorted-map (swap! input-id inc) title))}]]))

(defn checkbox []
  [:div
   [:input {:type "checkbox"
            }]
   " "
   ])

(defn delete-button []
  [:div
   [:input {:type "button"
            :value "remove"
            }]])

(defn todo-list []
  )

(defn add-todo [text]
  (let [id (swap! counter inc)]
    (swap! todos assoc id {:id id
                           :title text
                           :done false})))

(defn todo-app []
  (let [fltr (reagent/atom :all)]
    (fn []
      (let [default-string ""
            title (reagent/atom default-string)
            total-item (vals @todos)
            completed (->> total-item (filter :done) count)
            active (- (count total-item) completed)]
        [:div
         [header]
         [todo-input]
         ]))))

(defn init []
  (reagent/render [todo-app] (.getElementById js/document "my-app-area")))

(init)
