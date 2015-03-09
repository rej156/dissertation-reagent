(ns test.components.tabs.goal-tab
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [instaparse.core :as insta]
            [reagent-forms.core :refer [bind-fields]]
            [test.components.nav :as nav]
            [test.components.application :as application]))

(defn option-goal-content [option option-name]
  [:div.collapsible-body
   [:ol.collapsible {:data-collapsible "accordion"}
    [:li [:h5 "Progressing Goals"]]
    [:div.divider]
    (for [goal (get-in @application/core-values-state [option option-name
                                                       :goals])]
      [:li {:key (str goal)}
       [:div.collapsible-header [:b (str (:name goal))]]
       [:div.collapsible-body [:p (str (:description goal))]]])
    [:li [:h5 "Completed Goals"]]
    [:div.divider]
    (for [completed-goal (get-in @application/core-values-state [option option-name
                                                       :completed-goals])]
      [:li {:key (str completed-goal)}
       [:div.collapsible-header [:b (str (:name completed-goal))]]
       [:div.collapsible-body [:p (str (:description completed-goal))]]])
    ]])

(defn scaffolded-component []
  [:div.goals-tab
   (nav/component :goals)
   [:div.container
    [:div.row
     [:div.col.s12
      [:ul.collapsible {:data-collapsible "expandable"
                        :id "expandable"}
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Career"]]
        (option-goal-content 0 :career)]
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Finances"]]
        (option-goal-content 1 :finances)]
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Health"]]
        (option-goal-content 2 :health)]
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Family"]]
        (option-goal-content 3 :family)]
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Romance"]]
        (option-goal-content 4 :romance)]
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Personal Growth"]]
        (option-goal-content 5 :personal-growth)]
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Fun"]]
        (option-goal-content 6 :fun)]
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Physical Environment"]]
        (option-goal-content 7 :physical-environment)]
       ]]
     ]]])

(defn component []
  (reagent/create-class
   {:component-did-mount #(try
                            (-> (js/$ ".collapsible")
                              (.collapsible)
                              {:expandable true})
                            (catch :default e
                              ))
    :render scaffolded-component}))
