(ns test.components.introduction.second
  (:require [test.session :as session :refer [global-put! global-state]]))

(defn introduction-second []
  [:div.introduction-second
   [:h1 (str "Hi! " (global-state :first-name))]
   [:h2 "Nice to \"meet\" :)"]
   [:h2 "I am software (as I'm sure you can tell), designed to help people
  pursue their dreams and aspirations."]
   [:a {:href "#/introduction/third"} "Continue"]])
