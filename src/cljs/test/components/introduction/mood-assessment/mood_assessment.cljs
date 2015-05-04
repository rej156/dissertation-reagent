(ns test.components.introduction.mood-assessment
  (:require [test.session :refer [global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [reagent-forms.core :refer [bind-fields]]))

(def moods [{:confidence "How confident?"}
            {:cheerfulness "How cheerful?"}
            {:proudness "How proud?"}
            {:easygoing "How easygoing?"}
            {:assured "Finally - how assured and brave do you feel right now?"}])

(defn try-move-next []
  (if-not (or (< (get-in @prefs [:mood :confidence]) 1)
              (> (get-in @prefs [:mood :confidence]) 7))
    (secretary/dispatch! "/introduction/mood-assessment-1")))

(defn input [id label]
  [:div.input-field.col.s12
   [:input {:min 1
            :max 7
            :field :numeric
            :id id}]
   [:label {:for id} label]])

(def form-template
  [:div.row
   [:h2 (vals (get moods 0))]
   (input :mood.confidence "On a scale of 1 to 7")
   [:strong {:field :alert :id :mood.confidence :event #(or (< % 1) (> % 7))} "Please enter a rating between 1 and 7 inclusive!"]
   [:br]
   [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)} "Continue"]
   ])

(defn component []
  [:div.container
   [bind-fields form-template prefs]])
