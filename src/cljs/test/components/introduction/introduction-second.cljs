(ns test.components.introduction.second
  (:require [test.session :as session :refer [global-put! global-state prefs-state]]))

(defn try-move-next []
  (set! (.-location js/window) "#/introduction/third"))

(defn second []
  [:div.introduction-second
   [:h1 (str "Hi! " (prefs-state :first-name))]
   [:h2 "Nice to \"meet\" :)"]
   [:h2 "I am software (as I'm sure you can tell), designed to help people
  pursue their dreams and aspirations."]
   [:button {:on-click #(try-move-next)} "Continue"]])
