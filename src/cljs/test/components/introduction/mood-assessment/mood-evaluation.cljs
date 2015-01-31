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
  [:div.form-template
   [:h2 (str "Your mood is " (get-in @prefs [:mood :score]) " on a scale between -18 and +18")]
   [:div.down-mood {:field :container :visible? #(> 0 (get-in @prefs [:mood :score]))}
    [:h3  "Looks like your mood is a little down."]
    [:h3 "Would you like to try a little exercise to boost your mood?"]
    [:div.btn-group {:field :single-select :id :show-gratitude-exercise}
     [:button.btn.btn-default {:key :yes} "Yes"]
     [:button.btn.btn-default {:key :no} "No"]]
    [:div.exercise {:field :container :visible? #(= :yes
                                                    (:show-gratitude-exercise
                                                     %))}
     [:h3 "Let's try this:"]
     [:h4 "Think of one good thing that happened to you during the past
  week. Something for which you feel grateful for."]
     [:h4 "What is it?*"]
     [:textarea {:field :textarea :id :grateful-event
                 :rows 4
                 :cols 50}]
     [:h4 "Why are you grateful for it?"]
     [:textarea {:field :textarea :id :grateful-reason
                 :rows 4
                 :cols 50}]
     [:br]
     [:button {:on-click #(try-move-next)} "Continue"]
     ]]
   [:div.mood-good {:field :container :visible? #(<= 0 (get-in @prefs [:mood :score]))}
    [:h3  "Looks like your mood is good!"]
    [:button {:on-click #(set! (.-location js/window) "#/application")} "Continue"]]])

(defn component []
  [:div.mood-evaluation
   [bind-fields form-template gratitude]])
