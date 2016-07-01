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
                                  :active true))))


;; find index of (sorted-map) in @todos(vector) using (sorted-map) :id
(defn find-index [id]
  (.indexOf @todos (into {} (filter #(= id (% :id)) @todos))))

;; delete element in @todos (vector)
(defn todo-delete-from-todos [index]
  (vec (concat (vec (take (dec index) @todos)) (vec (drop index @todos)))))

;; another way to remove element in @todos using filterv
(defn todo-delete [temp-arg id]
  (filterv #(not= id (% :id)) @temp-arg))


;; update :active key by id
(defn todo-checked [id val]
  (swap! @todos (update-in @todos [(find-index id) :active] val)))

;;
(defn todo-active [temp-arg]
  (fn []
    (- (count temp-arg) (->> temp-arg
                             (filter :active)
                             count))))

;; update todo status(:active)
(defn change-status [id val]
  (swap! @todos (update-in @todos [(find-index id) :active] val)))

;; count all todos......
(defn count-all-todos []
  (count @todos))

;; count active todos
(defn count-active-todos []
  (count (filterv #(= true (:active %)) @todos)))

;; show active todos
(defn show-active-todos [temp-arg]
  (filterv #(= true (:active %)) @temp-arg))

;; count complete todos
(defn count-completed-todos []
  (count (filterv #(= false (:active %)) @todos)))

;; show completed todos
(defn show-completed-todos [temp-arg]
  (filterv #(= false (:active %)) @temp-arg))

;; clear completed todos
(defn delete-all-completed-todos [temp-arg]
  (filterv #(= false (:active %)) @temp-arg))


(defn header []
  [:div.page-header
   [:h3 "todo application"]])

;;
(defn todo-input [text]
  (let [input (reagent/atom text)]
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


;; show todos................
(defn todos-show [temp-arg]
  [:div
   [:table
    [:thead
     ;;[:tr [:td "active"] [:td "todos"] [:td "action"]]
     ]
    (for [text @temp-arg]
      ^{:key text}
      [:tr
       [:td
        [:input {:type "checkbox"
                 :id (:id temp-arg)
                 ;;:checked true
                 ;;:checked false
                 :checked (zero? todo-active)
                 :on-change #(reset! @todos (todo-checked (:id temp-arg) (pos? todo-active)))
                 }]]
       [:td
        (:todo text)]
       [:td
        [:input {:type "button"
                 :value "X"
                 :on-click #(reset! todo-temp (todo-delete todo-temp (:id text)))
                 }]]])]])


;; footer part.........
(defn footer [temp-arg]
  (let []
    (fn []
      [:div
       [:span
        [:input {:type "button"
                 :value "all"
                 :on-click #(reset! temp-arg (todos-show @todos))}]] " "
       [:span
        [:input {:type "button"
                 :value "active"
                 :on-click #(reset! temp-arg (show-active-todos @todos))}]] " "
       [:span
        [:input {:type "button"
                 :value "completed"
                 :on-click #(reset! temp-arg (show-completed-todos @todos))}]] " "
       [:span
        [:input {:type "button"
                 :value "clear-completed"
                 :on-click #(reset! temp-arg (delete-all-completed-todos @todos))}]] " "]
      )))

;; main..........

(defn todo-app []
  (let [input (reagent/atom "")]
    (fn []
      [:div
       [:div
        [header]]
       [:div
        [todo-input input]]
       [:div
        [todos-show todo-temp]
        [:div [:BR]
         [footer todo-temp]]]])))

(defn init []
  (reagent/render [todo-app] (.getElementById js/document "my-app-area")))

(init)
