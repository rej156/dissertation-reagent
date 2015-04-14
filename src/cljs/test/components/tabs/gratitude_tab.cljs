(ns test.components.tabs.gratitude-tab
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [instaparse.core :as insta]
            [reagent-forms.core :refer [bind-fields]]
            [test.components.nav :as nav]
            [test.components.application :as application]
            [test.components.modules.gratitude :refer [gratitude-log]]))

(defn scaffolded-component []
  [:div.goals-tab
   (nav/component :gratitude)
   [:div.container
    [:div.row
     [:div.col.s12
      [:h4.center "Gratitude Reasons"]]]
    [:div.row
     [:div.col.s12
      (for [entry (:gratitude-log @gratitude-log)]
        ^{:key entry}
        [:div.card.col.s6
         [:div.card-content
          [:p.card-title (str (:gratitude-reason entry))]
          [:p (str (:gratitude-description entry))]]
         ])]
     ]]])

(defn component []
  (reagent/create-class
   {:component-did-mount #(try
                            (-> (js/$ ".collapsible")
                                (.collapsible)
                                {:accordion true})
                            (catch :default e
                              ))
    :render scaffolded-component}))
