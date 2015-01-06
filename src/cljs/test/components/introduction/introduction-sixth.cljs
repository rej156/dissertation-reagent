(ns test.components.introduction.sixth
  (:require [test.session :refer [global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent]
            [reagent-forms.core :refer [bind-fields]]))

(defn input [label type id]
  [:div.id
   [:label label]
   [:input.form-control {:field type :id id}]])

(defn try-move-next []
  (set! (.-location js/window) "#/introduction/seventh"))

(def form-template
  [:div.form-template
   [:h2 (str "What would be the best way for me to help you " (prefs-state
                                                               :first-name)
             "?*")]
   [:h2 "Why are you here? Mark all the ones what apply."]
   [:h3 "Choose as many as you like"]
   (input "I need clarity - can't get my mind to focus" :checkbox :coaching-goals.clarity )
   (input "I'd like to be able to cope better with life, to be more silient" :checkbox :coaching-goals.resilient )
   (input "There's a specific area in my life I want to improve" :checkbox :coaching-goals.specific )
   (input "I'd like to become calmer and reduce my stress" :checkbox :coaching-goals.stress )
   (input "I want to figure the next chapter in my life" :checkbox :coaching-goals.next-chapter )
   (input "I want to identify goals and be held accountable to pursuing them" :checkbox :coaching-goals.accountable )
   (input "I'd like to adopt a more positive attitude" :checkbox :coaching-goals.positivity )
   (input "I am looking for more meaning and purpose with my life" :checkbox :coaching-goals.purpose )
   (input "I am simply curious about you Liz and want to see how this thing works" :checkbox :coaching-goals.liz )
   [:button {:on-click #(try-move-next)} "Submit"]])

(defn component []
  [:div.introduction-sixth
   [bind-fields form-template prefs]])
