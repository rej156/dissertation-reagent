(ns test.components.introduction.second
  (:require [test.session :as session :refer [global-put! global-state prefs-state]]))

(defn try-move-next []
  (set! (.-location js/window) "#/introduction/third"))

(defn component []
  [:div.container
   [:div.row
    [:div.col.s12
     [:h1 (str "Hi! " (prefs-state :first-name))]
     [:h4 "Nice to \"meet\" :)"]
     [:h4 "I am software (as I'm sure you can tell), designed to help people
  pursue their dreams and aspirations."]
     [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)} "Continue"]]]])
