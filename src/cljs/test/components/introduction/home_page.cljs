(ns test.components.introduction.home-page
  (:require [reagent-forms.core :refer [bind-fields]]
            [test.session :as session :refer [prefs prefs-state]]))

(defn try-move-next []
  (if-not (empty? (prefs-state :first-name))
    (set! (.-location js/window) "#/introduction/second")))

(defn input [label type id alert]
  [:div.input-field.col.s12
   [:input {:field type :id id}]
   [:label {:for "first_name"} label]])

(def form-template
  [:div.container
   [:div.row
    [:h1 "I'm Liz."]
    [:h2 "What is your name?*"]
    (input "First Name" :text :first-name "First name is empty!")
    [:br]
    [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)} "Continue"]
    ]])

(defn component []
  [:div.home-page
   [bind-fields form-template prefs]])
