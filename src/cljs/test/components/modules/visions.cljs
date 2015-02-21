(ns test.components.modules.visions
  (:require [reagent-forms.core :refer [bind-fields]]
            [test.session :as session :refer [prefs prefs-state]]
            [test.components.application :as application]
            [clojure.string :as str]))

(defn try-move-next []
  (set! (.-location js/window) "#/application")
  (swap! application/history-state conj (str "Added Best Possible Self: "
                                             (str/capitalize (str (application/option-name
                                                                         application/current-option)))))
  (swap! application/core-values-state assoc-in [application/current-option (keyword
                                                                             (application/option-name
                                                                              application/current-option)) :history] (str (-> (get
                                                                                                                               @application/core-values-state application/current-option)
                                                                                                                              (vals)
                                                                                                                              (first)
                                                                                                                              (:history)) "b"))

  (application/reset-vars!))

(defn component []
  [:div.container
   [:div.row
    [:div.col.s12
     [:h3 (str "I want you to imagine your most ideal future in the "
               (application/option-name application/current-option) " area of
  your life.")]
     [:h4 "Make it as vivid as possible in your mind."]
     [:div.row
      [:div.input-field.col.s12
       [:textarea.materialize-textarea {:rows 4
                                        :id (str (application/option-name application/current-option))
                                        :cols 50
                                        :on-change #(swap! application/core-values-state assoc-in [application/current-option (keyword
                                                                                                                               (application/option-name
                                                                                                                                application/current-option))
                                                                                                   :vision] (.. % -target -value))}]
       [:label {:for (str (application/option-name
                           application/current-option))} "Describe it to Liz in
  full detail."]]]

     [:br]
     [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)} "I
     believe in my future Liz!"]]
    ]
   ])
