(ns test.components.introduction.mood-evaluation
  (:require [test.session :refer [merge-atoms global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [test.components.introduction.mood-assessment :as assessment]
            [test.components.application :refer [core-values-state]]
            [test.components.modules.gratitude :refer [gratitude-log]]
            [reagent-forms.core :refer [bind-fields]]))

(def gratitude (atom {}))

(defn close-intervention-modal []
  (-> (js/$ "#modal1")
      (.closeModal)))

(defn open-intervention-modal []
  (-> (js/$ "#modal1")
      (.leanModal {:dismissable false}))
  (-> (js/$ "#modal1")
      (.openModal)))

(defn try-move-next []
  (if-not (empty? @gratitude)
    (swap! gratitude-log update-in [:gratitude-log]
         conj @gratitude))
  (swap! gratitude-log assoc-in [:mood-counter :show-status] 0)
  (reset! gratitude {})
  (close-intervention-modal)
  (swap! prefs assoc :entered-main 1)
  (set! (.-location js/window) "#/application"))

(defn ppi-modal []
  [:div.modal.modal-fixed-footer
   {:id "modal1"}
   [:div.modal-content
    [:ul.collection.with-header
     [:li.collection-header [:h5 (str "Remember ," (prefs-state :first-name) " other ways you can boost your mood are:")]]
     [:li.collection-item "Thank someone directly"]
     [:li.collection-item "Nurture relationships"]
     [:li.collection-item "Savor the moment"]
     [:li.collection-item "Mentally replay a happy day"]
     [:li.collection-item "Do a random act of kindness"]]
    [:h4 "You just performed a positive psychology intervention with Liz!"]
    ]
   [:div.modal-footer
    [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)}
     "Thanks coach Liz, I'll do that!" [:i.mdi-content-send.right]]]])

(defn component []
  [:div.container
   (ppi-modal)
   [:div.row
    [:div.col.s12
     [:h2 (str "Your mood is " (get-in @prefs [:mood :score]) " on a scale between -18 and +18")]]]
   (if (>= (get-in @prefs [:mood :score]) 0)
     [:div.row
      [:div.mood-good
       [:h3  "Looks like your mood is good!"]
       [:button.btn.waves-effect.waves-light {:on-click #(do (swap! prefs assoc
                                                                    :entered-main
                                                                    1)
                                                             (set! (.-location
                                                                    js/window)
                                                                   "#/application"))}
        "Let's start improving my life Liz!" [:i.mdi-content-send.right]]]
      ]
     [:div.row
      [:div.down-mood
       [:div.col.s12
        [:h4  "Looks like your mood is a little down."]
        [:h4 "Would you like to try a little exercise to boost your mood?"]
        [:div.btn-group
         [:button.btn.waves-effect.waves-light {:on-click #(swap! gratitude assoc :gratitude-status "yes")}"Sure!"]
         [:button.btn.waves-effect.waves-light {:on-click #(try-move-next)} "Not right now Liz"]]
        (if (= "yes" (:gratitude-status @gratitude))
          [:div.input-field.col.s12.gratitude-exercise
           [:h3 "Let's try this:"]
           [:h4 "Think of one good thing that happened to you during the past
  week. Something for which you feel grateful for."]
           [:br]
           [:h4 "What is it?*"]
           [:textarea.materialize-textarea {:id :grateful-event
                                            :on-change #(swap! gratitude assoc :gratitude-reason (.. % -target -value))
                                            :rows 4
                                            :cols 50}]
           [:h4 {:for :grateful-reason} "Why are you grateful for it?"]
           [:textarea.materialize-textarea {:id :grateful-reason
                                            :on-change #(swap! gratitude assoc :gratitude-description (.. % -target -value))
                                            :rows 4
                                            :cols 50}]
           [:br]

           [:button.btn.waves-effect.waves-light {:on-click #(open-intervention-modal)}
            "Let's start improving my life Liz!"]]
          )
        ]]]
     )
   ])
