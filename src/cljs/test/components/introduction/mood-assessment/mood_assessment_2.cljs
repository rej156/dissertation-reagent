(ns test.components.introduction.mood-assessment-2
  (:require [test.session :refer [global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent]
            [secretary.core :as secretary]
            [test.components.introduction.mood-assessment :as assessment]
            [reagent-forms.core :refer [bind-fields]]))

(defn try-move-next []
  (if-not (or (< (get-in @prefs [:mood :proudness]) 1)
              (> (get-in @prefs [:mood :proudness]) 7))
    (secretary/dispatch! "/introduction/mood-assessment-3")))

(defn input [id label]
  [:div.input-field.col.s12
   [:input {:min 1
            :max 7
            :field :numeric
            :id id}]
   [:label {:for id} label]])

(def form-template
  [:div.row
   [:h2 (vals (get assessment/moods 2))]
   (input :mood.proudness "On a scale of 1 to 7")
   [:strong {:field :alert :id :mood.proudness :event #(or (< % 1) (> % 7))} "Please enter a rating between 1 and 7 inclusive!"]
   [:br]
   [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)} "Continue"]
   ])

(defn component []
  [:div.container
   [bind-fields form-template prefs]])
