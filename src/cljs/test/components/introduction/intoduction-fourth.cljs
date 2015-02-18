(ns test.components.introduction.fourth
  (:require [test.session :refer [global-put! global-state prefs-state]]
            [secretary.core :as secretary]))

(defn component []
  [:div.container
   [:div.row
    [:div.col.s12
     [:h2 "Great!"]
     [:h4 "To get started, I need to know a little more about you."]
     [:button.btn.waves-effect.waves-light {:on-click #(set! (.-location
                                                              js/window)
  "#/introduction/fifth")} "Continue"]]]])
