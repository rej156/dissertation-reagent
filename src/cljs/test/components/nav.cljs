(ns test.components.nav
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [instaparse.core :as insta]
            [reagent-forms.core :refer [bind-fields]]
            ))

(defn component [tab]
  [:nav
   [:div.nav-wrapper
    [:div.row
     [:div.col.s12
      [:ul
       [:div.col.s2
        [(if (= tab :main)
           :li.active
           :li)
         [:a {
              :href "/#/application"
              }
          "Home"]]]
       [:div.col.s2
        [(if (= tab :goals)
           :li.active
           :li)
         [:a {
              :href "/#/tabs/goals"
              } "Goals"]]]
       [:div.col.s2
        [(if (= tab :past)
           :li.active
           :li)
         [:a {
              :href "#"
              } "Past"]]]
       [:div.col.s2
        [(if (= tab :present)
           :li.active
           :li)
         [:a {
              :href "#"
              } "Present"]]]
       [:div.col.s2
        [(if (= tab :future)
           :li.active
           :li)
         [:a {
              :href "#"
              } "Future"]]]]]]
    ]])
