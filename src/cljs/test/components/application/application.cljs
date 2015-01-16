(ns test.components.application
  (:require [test.session :refer [global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [reagent-forms.core :refer [bind-fields]]))

(defn input [label type id]
  [:div.id
   [:label label]
   [:input.form-control {:field type :id id}]])

(def core-values-state (atom [
                  {:career {:history '()
                            :score nil
                            :vision "I am God."
                            :goals [
                                    {:name "First Career Goal"
                                     :description "Goal description"
                                     :steps ["First step","Second step"]
                                     }]
                            }}
                  {:finances {}}
                  {:health {}}
                  {:family {}}
                  {:romance {}}
                  {:personal-growth {}}
                  {:fun {}}
                  {:physical-environment {}}
                  ]))

(defn try-move-next []
  (set! (.-location js/window) "#/introduction/seventh"))

(def form-template
  [:div.form-template
   [:h1 "lol"]
   ])

(defn component []
  [:div.application
   [bind-fields form-template prefs]])
