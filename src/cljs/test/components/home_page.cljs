(ns test.components.introduction.home-page
  (:require [test.session :as session :refer [global-put! global-state]]))

(defn home-page []
  [:div
   [:button {:on-click #(global-put! :click-count (inc (:click-count global-state)))} "Click me!"]
   [:span (global-state :click-count)]
   [:h2 "Home Page"]
   ])
