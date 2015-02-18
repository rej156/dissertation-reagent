(ns test.components.introduction.mood-evaluation
  (:require [test.session :refer [merge-atoms global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [test.components.introduction.mood-assessment :as assessment]
            [reagent-forms.core :refer [bind-fields]]))

(def gratitude (atom {}))

(defn try-move-next []
  (do
    ;;conj-in [:gratitude []] (merge-atoms prefs gratitude)
    (set! (.-location js/window) "#/application")
    ))

(def form-template
  [:div.row
   [:div.down-mood {:field :container :visible? #(> 0 (get-in @prefs [:mood :score]))}
    [:div.col.s12
     [:h4  "Looks like your mood is a little down."]
     [:h4 "Would you like to try a little exercise to boost your mood?"]
     [:div.btn-group {:field :single-select :id :show-gratitude-exercise}
      [:button.btn.waves-effect.waves-light {:key :yes} "Sure!"]
      [:button.btn.waves-effect.waves-light {:key :no} "Not right now Liz"]]
     [:div.input-field.col.s12.gratitude-exercise {:field :container :visible? #(= :yes
                                                                                   (:show-gratitude-exercise
                                                                                    %))}
      [:h3 "Let's try this:"]
      [:h4 "Think of one good thing that happened to you during the past
  week. Something for which you feel grateful for."]
      [:br]
      [:h4 "What is it?*"]
      [:textarea.materialize-textarea {:field :textarea :id :grateful-event
                                       :rows 4
                                       :cols 50}]
      [:h4 {:for :grateful-reason} "Why are you grateful for it?"]
      [:textarea.materialize-textarea {:field :textarea :id :grateful-reason
                                       :rows 4
                                       :cols 50}]
      [:br]
      [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)}
       "Let's start improving my life Liz!" [:i.mdi-content-send.right]]]]]
   [:div.mood-good {:field :container :visible? #(<= 0 (get-in @prefs [:mood :score]))}
    [:h3  "Looks like your mood is good!"]
    [:button.btn.waves-effect.waves-light {:on-click #(set! (.-location
                                                             js/window)
                                                            "#/application")} "Let's start improving my life Liz!" [:i.mdi-content-send.right]]]])

(defn component []
  [:div.container
   [:div.row
    [:div.col.s12
     [:h2 (str "Your mood is " (-> @prefs
                                   (:mood)
                                   (:score)) " on a scale between -18 and
  +18")]]]
   [bind-fields form-template gratitude]])
