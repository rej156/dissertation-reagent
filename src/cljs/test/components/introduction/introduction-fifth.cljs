(ns test.components.introduction.fifth
  (:require [test.session :refer [global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent]
            [reagent-forms.core :refer [bind-fields]]))

(defn input [label type id alert]
  [:div.id
   [:label label]
   [:input.form-control {:field type :id id}]
   [:div.alert.alert-danger
    {:field :alert :id id :event (if (= type :text) empty? nil?)}
    alert]])

(defn label [name label]
  [:label {:htmlFor name} label])

(defn radio [label id name value]
  [:div.radio
   [:label
    [:input {:field :radio :id id :name name :value value}]
    label]])

(defn select [label id options]
  [:div.select
   [:label label]
   [:select.form-control {:field :list :id id}
    (for [option options]
      [:option {:key (keyword option)} option])]])

(def form-template
  [:div.form-template
   (input "What is your last name?" :text :last-name "Last name is empty!")
   (label "gender" "Are you a man or a woman?")
   (radio "Male" :gender "gender" "Male")
   (radio "Female" :gender "gender" "Female")
   (input "How old are you?" :numeric :age "You haven't chosen an age!")
   (select "Please select relationship status" :relationship-status ["Married" "Single"])
   (label "children" "Do you have children?")
   (radio "Yes" :children "children" "Yes")
   (radio "No" :children "children" "No")])

(def second-form-template
  (input "How many children do you have?" :numeric :children-no "You haven't
  said how many children you have!"))

(defn try-move-next []
  (when-not (and
             (empty? (prefs-state :last-name))
             (empty? (or (prefs-state :Married) (prefs-state :Single))))
    (set! (.-location js/window) "#/introduction/sixth")))

(defn component []
  [:div.introduction-fifth
   [bind-fields form-template prefs]
   (if (= (prefs-state :children) "Yes")
     [bind-fields second-form-template prefs])
   [:button {:on-click #(try-move-next)} "Continue"]])
