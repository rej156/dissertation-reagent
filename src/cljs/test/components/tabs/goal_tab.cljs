(ns test.components.tabs.goals
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [instaparse.core :as insta]
            [reagent-forms.core :refer [bind-fields]]
            [re-frame.core :as re-frame]
            [test.components.nav :as nav]))

(defn scaffolded-component []
  [:div.goals-tab
   (nav/component :goals)
   [:div.container
    [:div.row
     [:div.col.s12
      [:ul.collapsible {:data-collapsible "accordion"}
       [:li
        [:div.collapsible-header
         [:p "First"]]
        [:div.collapsible-body
         [:p "Second"]]]
       [:li
        [:div.collapsible-header
         [:p "Second"]]
        [:div.collapsible-body
         [:p "Third"]]]]]
     ]]])

(defn component []
  (reagent/create-class
   {:component-did-mount #(-> (js/$ ".collapsible")
                              (.collapsible)
                              {:accordion true})
    :render scaffolded-component}))
