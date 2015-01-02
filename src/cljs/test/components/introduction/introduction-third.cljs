(ns test.components.introduction.third
  (:require [test.session :refer [global-put! global-state prefs-state]]))

(defn try-move-next []
  (set! (.-location js/window) "#/introduction/fourth"))

(defn component []
  [:div.introduction-third
   [:h2 "If we work together, we will \"chat\" every few days. I can help you:"]
   [:ul
    [:li "Identify a clearer vision for your future"]
    [:li "Define specific goals"]
    [:li "Break down your goals into small action items that fit your
  schedule"]
    [:li "Find the energy you need to feel motivated and moving forward"]
    [:li "Learn to adopt a more positive attitude towards the events in your life
- and become more resilient"]]
   [:h2 "Would you like to give it a try?*"]
   [:button {:on-click #(try-move-next)} "Yes"]
   ])
