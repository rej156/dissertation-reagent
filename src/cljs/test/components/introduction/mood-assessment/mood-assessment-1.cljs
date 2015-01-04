(ns test.components.introduction.mood-assessment-1
  (:require [test.session :refer [global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent]
            [secretary.core :as secretary]
            [test.components.introduction.mood-assessment :as assessment]
            [reagent-forms.core :refer [bind-fields]]))

(defn try-move-next []
  (secretary/dispatch! "/introduction/mood-assessment-2"))

(defn input [id]
  [:div.id
   [:input.form-control {:min 1
                         :max 7
                         :field :numeric
                         :id id}]])

(def form-template
  [:div.form-template
   [:h1 (vals (get assessment/moods 1))]
   (input :mood.cheerfulness)
   [:button {:on-click #(try-move-next)} "Continue"]
   ])

(defn component []
  [:div.mood-assessment
   [bind-fields form-template prefs]])
