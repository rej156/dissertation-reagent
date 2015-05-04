(ns test.components.introduction.mood-assessment-initial
  (:require [test.session :refer [global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [reagent-forms.core :refer [bind-fields]]))

(defn try-move-next []
  (if-not (or (< (get-in @prefs [:mood :happy]) 1)
          (> (get-in @prefs [:mood :happy]) 7))
    (secretary/dispatch! "/introduction/mood-assessment")))

(defn input [id label]
  [:div.input-field.col.s12
   [:input {:min 1
            :max 7
            :type :number
            :field :numeric
            :id id}]
   [:label {:for id} label]])

(def form-template
  [:div.row
   [:h2 "How happy are you?"]
   (input :mood.happy "On a scale of 1 to 7 (1 being the lowest)")
   [:strong {:field :alert :id :mood.happy :event #(or (< % 1) (> % 7))} "Please enter a rating between 1 and 7 inclusive!"]
   [:br]
   [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)} "Continue"]])

(defn component []
  [:div.container
   [bind-fields form-template prefs]])
