(ns test.components.introduction.fifth
  (:require [test.session :refer [global-put! local-put! global-state prefs-state prefs]]
            [reagent.core :as reagent]
            [reagent-forms.core :refer [bind-fields]]))

(defn input [label type id alert]
  [:div.row
   [:div.input-field.col.s12
    [:input {:field type :id id}]
    [:label {:for id} label]
    ;; [:div.alert.alert-danger
    ;;  {:field :alert :id id :event (if (= type :text) empty? nil?)}
    ;;  alert]
    ]])

(defn label [name label]
  [:label {:for name} label])

(defn radio [label name value]
  [:p
   [:input {:type "radio" :value value :id value :name name
            :on-click #(local-put! prefs name value)}]
   [:label {:for value} label]])

(defn select [label id options]
  [:div.row
   [:div.col.s12
    [:label label]
    [:select.browser-default {:field :list :id id}
     (for [option options]
       [:option {:key (keyword option)} option])]]])

(def form-template
  [:div.row
   [:div.col.s12
    (input "What is your last name?" :text :last-name "Last name is empty!")
    (label "gender" "Are you a man or a woman?")
    (radio "Male" :gender "Male")
    (radio "Female" :gender "Female")
    (input "How old are you?" :numeric :age "You haven't chosen an age!")
    (select "Please select relationship status" :relationship-status ["Married" "Single"])
    (label "children" "Do you have children?")
    (radio "Yes" :children "Yes")
    (radio "No" :children "No")]])

(def second-form-template
  (input "How many children do you have?" :numeric :children-no "You haven't
  said how many children you have!"))

(defn try-move-next []
  (when-not (and
             (empty? (prefs-state :last-name))
             (empty? (or (prefs-state :Married) (prefs-state :Single))))
    (set! (.-location js/window) "#/introduction/sixth")))

(defn component []
  [:div.container
   [:h2 "Tell me more about you!"]
   [bind-fields form-template prefs]
   (if (= (prefs-state :children) "Yes")
     [bind-fields second-form-template prefs])
   [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)} "Okay Liz!"
    [:i.mdi-content-send.right]]])
