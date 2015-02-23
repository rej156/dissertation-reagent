(ns test.components.introduction.mood-assessment-1
  (:require [test.session :refer [global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent]
            [secretary.core :as secretary]
            [test.components.introduction.mood-assessment :as assessment]
            [reagent-forms.core :refer [bind-fields]]))

(defn try-move-next []
  (secretary/dispatch! "/introduction/mood-assessment-2"))

(defn input [id label]
  [:div.input-field.col.s12
   [:input {:min 1
            :max 7
            :field :numeric
            :id id}]
   [:label {:for id} label]])

(def form-template
  [:div.row
   [:h2 (vals (get assessment/moods 1))]
   (input :mood.cheerfulness "On a scale of 1 to 7")
   [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)} "Continue"]
   ])

(defn component []
  [:div.container
   [bind-fields form-template prefs]])
