(ns test.components.introduction.mood-assessment
  (:require [test.session :refer [global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent]
            [reagent-forms.core :refer [bind-fields]]))

(def counter (atom 0))

(def moods [{:confidence "How confident?"}
            {:cheerfulness "How cheerful?"}
            {:proudness "How proud?"}
            {:easygoing "How easygoing?"}
            {:assured "Finally - how assured and brave do you feel right now?"}])

(defn try-move-next []
  (if-not (= 4 @counter)
    (swap! counter inc)
    (.log js/console "You motherfucking beast!")))
    ;;(set! (.-location js/window) "#/application/main")))

(defn input [id]
  [:div.id
   [:input.form-control {:default-value 3
                         :min 1
                         :max 7
                         :field :numeric
                         :id id}]])

(def form-template
  [:div.form-template
   [:h1 (vals (get moods @counter))]
   (input (symbol (str "mood." (name (first (keys (get moods @counter)))))))
   [:button {:on-click #(try-move-next)} "Continue"]
   ])

(defn component []
  [:div.mood-assessment
   [bind-fields form-template prefs]
   ])
