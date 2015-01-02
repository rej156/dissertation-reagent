(ns test.components.introduction.fourth
  (:require [test.session :refer [global-put! global-state prefs-state]]
            [secretary.core :as secretary]))

(defn component []
  [:div.introduction-fourth
   [:h2 "Great!"]
   [:h2 "To get started, I need to know a little more about you."]
   [:button {:on-click #(set! (.-location js/window) "#/introduction/fifth")} "Continue"]])
