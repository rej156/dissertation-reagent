(ns test.components.introduction.third
  (:require [test.session :refer [global-put! global-state prefs-state]]))

(defn third []
  [:div.introduction-third
   [:h1 (str "Hi!" (prefs-state :first-name))]
   [:h2 "Nice to \"meet\" :)"]
   [:h2 "I am software (as I'm sure you can tell), designed to help people
  pursue their dreams and aspirations."]
   [:a {:href "#/introduction/third"} "Continue"]])
