(ns test.components.introduction.mood-assessment-4
  (:require [test.session :refer [global-put! local-put! global-state prefs-state prefs]]
            [reagent.core :as reagent]
            [secretary.core :as secretary]
            [test.components.introduction.mood-assessment :as assessment]
            [reagent-forms.core :refer [bind-fields]]))

(defn try-move-next []
  (if-not (or (< (get-in @prefs [:mood :assured]) 1)
              (> (get-in @prefs [:mood :assured]) 7))
    (do
      (set! (.-location js/window) "#/introduction/mood-evaluation")
      (swap! prefs assoc-in [:mood :score]
             (- (reduce + (vals (prefs-state :mood))) 24)))))

(defn input [id label]
  [:div.input-field.col.s12
   [:input {:min 1
            :max 7
            :field :numeric
            :id id}]
   [:label {:for id} label]])

(def form-template
  [:div.row
   [:h2 (vals (get assessment/moods 4))]
   (input :mood.assured "On a scale of 1 to 7")
   [:strong {:field :alert :id :mood.assured :event #(or (< % 1) (> % 7))} "Please enter a rating between 1 and 7 inclusive!"]
   [:br]
   [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)} "Continue"]
   ])

(defn component []
  [:div.container
   [bind-fields form-template prefs]])
