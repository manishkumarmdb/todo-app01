(ns cljs-boot-starter.client
  (:require [reagent.core :as reagent :refer [atom render]]
            [goog.dom :as dom]
            ))

(enable-console-print!)

(def todos (reagent/atom []))

(def counter (reagent/atom 0))

(def todo-temp (reagent/atom nil))

(defn add-in-todo [text]
  (let [id (swap! counter inc)]
    (swap! todos conj (sorted-map :id id
                                  :todo text
                                  :status true))))

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
                :on-click #(reset! todo-temp (add-in-todo @input))}]
       ])))

;; find index of (sorted-map) in @todos(vector) using (sorted-map) :id
(defn find-index [id]
  (.indexOf @todos (into {} (filter #(= id (% :id)) @todos))))

;; delete element in @todos (vector)
(defn todo-delete-from-todos [index]
  (vec (concat (vec (take (dec index) @todos)) (vec (drop index @todos)))))

;; another way to remove element in @todos using filterv
(defn todo-delete [temp-arg id]
  (filterv #(not= id (% :id)) @temp-arg))

;; show todos
(defn todos-show [temp-arg]
  [:div
   [:table
    (for [text @temp-arg]
      ^{:key text}
      [:tr
       [:td
        [:input {:type "checkbox"
                 :id (:id temp-arg)
                 ;;:checked true
                 ;;:checked false
                 :on-click #()
                 }]]
       [:td
        (:todo text)]
       [:td
        [:input {:type "button"
                 :value "X"
                 :on-click #(reset! todo-temp (todo-delete todo-temp (:id text)))
                 }]]])]])

;; count all todos
(defn count-all-todos []
  (count @todos))

;; count active todos
(defn count-active-todos []
  (count (filterv #(= true (:active %)) @todos)))

;; show active todos
(defn show-active-todos []
  (filterv #(= true (:active %)) @todos))

;; count complete todos
(defn count-completed-todos []
  (count (filterv #(= false (:active %)) @todos)))

;; show completed todos
(defn show-completed-todos []
  (filterv #(= false (:active %)) @todos))

;; clear completed todos
(defn delete-all-completed-todos []
  (filterv #(= false (:active %)) @todos))

(defn footer []
  (let [allt (count-all-todos)
        actt (count-active-todos)
        comt (count-completed-todos)]
    (fn []
      [:div
       [:span
        [:input {:type "button"
                 :value (str "all : " allt)
                 :on-click #(todos-show @todos)}]] " "
       [:span
        [:input {:type "button"
                 :value (str "active : " actt)
                 :on-click #(show-active-todos)}]] " "
       [:span
        [:input {:type "button"
                 :value (str "completed : " comt)
                 :on-click #(show-completed-todos)}]] " "
       [:span
        [:input {:type "button"
                 :value "clear-completed"
                 :on-click #(delete-all-completed-todos)}]] " "]
      )))

(defn header []
  [:div.page-header
   [:h3 "todo application"]])

(defn todo-app []
  (let [input (reagent/atom "")]
    (fn []
      [:div
       [:div
        [header]]
       [:div
        [todo-input input]]
       [:div
        [todos-show todo-temp]]
       [:div [:BR]
        [footer]]])))

(defn init []
  (reagent/render [todo-app] (.getElementById js/document "my-app-area")))

(init)
