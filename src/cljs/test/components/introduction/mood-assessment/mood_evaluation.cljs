(ns test.components.introduction.mood-evaluation
  (:require [test.session :refer [merge-atoms global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [test.components.introduction.mood-assessment :as assessment]
            [reagent-forms.core :refer [bind-fields]]))

(def gratitude (atom {}))

(defn try-move-next []
  (set! (.-location js/window) "#/application"))

(defn component []
  [:div.container
   [:div.row
    [:div.col.s12
     [:h2 (str "Your mood is " (get-in @prefs [:mood :score]) " on a scale between -18 and +18")]]]
   (if (>= (get-in @prefs [:mood :score]) 0)
     [:div.row
      [:div.mood-good
       [:h3  "Looks like your mood is good!"]
       [:button.btn.waves-effect.waves-light {:on-click #(set! (.-location
                                                                js/window)
                                                               "#/application")}
        "Let's start improving my life Liz!" [:i.mdi-content-send.right]]]
      ]

     [:div.row
      [:div.down-mood
      [:div.col.s12
       [:h4  "Looks like your mood is a little down."]
       [:h4 "Would you like to try a little exercise to boost your mood?"]
       [:div.btn-group
        [:button.btn.waves-effect.waves-light {:on-click #(swap! gratitude assoc :gratitude-status "yes")}"Sure!"]
        [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)} "Not right now Liz"]]
       (if (= "yes" (:gratitude-status @gratitude))
         [:div.input-field.col.s12.gratitude-exercise
          [:h3 "Let's try this:"]
          [:h4 "Think of one good thing that happened to you during the past
  week. Something for which you feel grateful for."]
          [:br]
          [:h4 "What is it?*"]
          [:textarea.materialize-textarea {:id :grateful-event
                                           :on-change #(swap! gratitude assoc :gratitude-reason (.. % -target -value))
                                           :rows 4
                                           :cols 50}]
          [:h4 {:for :grateful-reason} "Why are you grateful for it?"]
          [:textarea.materialize-textarea {:id :grateful-reason
                                           :on-change #(swap! gratitude assoc :gratitude-text (.. % -target -value))
                                           :rows 4
                                           :cols 50}]
          [:br]
          [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)}
           "Let's start improving my life Liz!" [:i.mdi-content-send.right]]]
         )
       ]]]
     )
   ])
