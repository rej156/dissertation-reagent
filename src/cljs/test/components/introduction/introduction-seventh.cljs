(ns test.components.introduction.seventh
  (:require [test.session :refer [global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent]
            [reagent-forms.core :refer [bind-fields]]))

(defn input [label type id]
  [:div.input-field.col.s12
   [:input {:default-value 3
            :min 1
            :max 7
            :field type
            :id id}]
   [:label {:for id} label]
   ;; [:div.alert.alert-danger
   ;;  {:field :alert :id id :event (if (= type :text) empty? nil?)}
   ;;  alert]
   ])

(defn try-move-next []
  (set! (.-location js/window) "#/introduction/mood-assessment-initial"))

(defn component []
  [:div.container
   [:div.row
    [:div.col.s12
     [:div.row
      [:h2 "So this is what you have told me!"]
      [:div.col.s12
       [:ul.collection.with-header
        [:li.collection-header [:h4 "User Profile:"]]
        [:li.collection-item (str (prefs-state :first-name) " " (prefs-state :last-name))]
        [:li.collection-item (str (prefs-state :age) " years old" (prefs-state :sex))]
        [:li.collection-item (str (name (prefs-state :relationship-status)))]
        [:li.collection-item (if-not (nil? (prefs-state :children-no))
                               (str (prefs-state :children-no) " children")
                               (str "No children"))]]]
      [:div.col.s12
       [:ul.collection.with-header
        [:li.collection-header [:h4 "Coaching goals:"]]
        (for [[k v] (prefs-state :coaching-goals)]
          (if (= v true)
            (condp = (name k)
              "purpose" [:li.collection-item {:key k} "I am looking for more meaning and purpose with my life"]
              "positivity" [:li.collection-item {:key k} "I'd like to adopt a more positive attitude"]
              "accountable" [:li.collection-item {:key k} "I want to identify goals and be held accountable to pursuing them"]
              "next-chapter" [:li.collection-item {:key k} "I want to figure the next chapter in my life"]
              "stress" [:li.collection-item {:key k} "I'd like to become calmer and reduce my stress"]
              "specific" [:li.collection-item {:key k} "There's a specific area in my life I want to improve"]
              "clarity" [:li.collection-item {:key k} "I need clarity - can't get my mind to focus"]
              "resilient" [:li.collection-item {:key k} "I'd like to be able to cope better with life, to be
  more resilient"]
              "liz" [:li.collection-item {:key k} "I am simply curious about you
  Liz and want to see how this thing works"])))]]
      [:div.row
       [:div.col.s12
        [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)}
       "Let's assess my mood Liz!"]]]]]]])
