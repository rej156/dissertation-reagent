(ns test.components.introduction.home-page
  (:require [reagent-forms.core :refer [bind-fields]]
            [test.session :as session :refer [prefs prefs-state]]))

(defn try-move-next []
  (if-not (empty? (prefs-state :first-name))
    (set! (.-location js/window) "#/introduction/second")))

(defn input [label type id]
  [:div.id
   [:label label]
   [:input.form-control {:field type :id id}]])

(def form-template
  [:div.form-template
   [:h1 "I'm Liz."]
   [:h2 "What is your name?*"]
   (input "First Name" :text :first-name)
   [:div.alert.alert-danger
    {:field :alert :id :first-name :event empty?}
    "First name is empty!"]
   [:br]
   [:button {:on-click #(try-move-next)} "Continue"]])

(defn component []
  [:div.home-page
   [bind-fields form-template prefs]])
