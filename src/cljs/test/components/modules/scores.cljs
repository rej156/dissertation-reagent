(ns test.components.modules.scores
  (:require [reagent-forms.core :refer [bind-fields]]
            [test.session :as session :refer [prefs prefs-state]]
            [test.components.application :as application]
            [clojure.string :as str]))

(defn try-move-next []
  (if-not (> (count (-> (get @application/core-values-state
                             application/current-option)
                        (vals)
                        (first)
                        (:history))) 1)
    (swap! application/core-values-state assoc-in [application/current-option (keyword
                                                                               (application/option-name application/current-option)) :history] "a"))
  (swap! application/history-state conj (str "Added Score: "
                                             (str/capitalize (str (application/option-name
                                                                         application/current-option))) " " (-> (get @application/core-values-state
                                                                                                                   application/current-option)
                                                                                                              (vals)
                                                                                                              (first)
                                                                                                              (:score))))
  (set! (.-location js/window) "#/application")

  (application/reset-vars!))

(defn component []
  [:div.container
   [:div.row
    [:h4 (str "How would you now rate your satisfaction in the "
              (application/option-name application/current-option) " area of your life?")]
    [:div.input-field.col.s12
     [:input {:type "number"
              :min 0
              :max 7
              :id (str (application/option-name application/current-option))
              :on-change #(swap! application/core-values-state assoc-in [application/current-option (keyword
                                                                                                     (application/option-name
                                                                                                      application/current-option)) :score] (js/parseInt (.. % -target -value)))
              }]
     [:label {:for (str (application/option-name application/current-option))}
      "On a scale of 1 to 8"]]
    [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)} "Save"]]])

;; Update history with string at the end
;; (swap! core-values-state assoc-in [first-option (keyword
;;   (option-name first-option)) :history] (str (option-history first-option) "a"))
