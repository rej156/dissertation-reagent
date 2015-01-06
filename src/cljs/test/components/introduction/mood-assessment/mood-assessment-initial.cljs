(ns test.components.introduction.mood-assessment-initial
  (:require [test.session :refer [global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [reagent-forms.core :refer [bind-fields]]))

(defn try-move-next []
  (secretary/dispatch! "/introduction/mood-assessment"))

(defn input [id]
  [:div.id
   [:input.form-control {:min 1
                         :max 7
                         :field :numeric
                         :id id}]])

(def form-template
  [:div.form-template
   [:h1 "How happy are you?"]
   (input :mood.happy)
   [:button {:on-click #(try-move-next)} ""]
   ])

(defn component []
  [:div.mood-assessment
   [bind-fields form-template prefs]])
