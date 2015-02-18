(ns test.components.introduction.third
  (:require [test.session :refer [global-put! global-state prefs-state]]))

(defn try-move-next []
  (set! (.-location js/window) "#/introduction/fourth"))

(defn component []
  [:div.container
   [:div.row
    [:div.col.s12
     [:h2 "If we work together, we will \"chat\" every few days. I can help you:"]
     [:ul.collection.card-panel
      [:li.collection-item "Identify a clearer vision for your future"]
      [:li.collection-item "Define specific goals"]
      [:li.collection-item "Break down your goals into small action items that fit your
  schedule"]
      [:li.collection-item "Find the energy you need to feel motivated and moving forward"]
      [:li.collection-item "Learn to adopt a more positive attitude towards the events in your life
- and become more resilient"]]
     [:h4 "Would you like to give it a try?*"]
     [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)} "Let's
  give it a try!"]]]])
