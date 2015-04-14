(ns test.components.tabs.vision-tab
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [instaparse.core :as insta]
            [reagent-forms.core :refer [bind-fields]]
            [test.components.nav :as nav]
            [test.components.application :as application]
            [test.components.modules.gratitude :refer [gratitude-log]]))

(defn vision-item [option]
  (let [editing (atom false)]
    (fn []
      (if-not (clojure.string/blank? (get-in @application/core-values-state
  [option (keyword (application/option-name option)) :vision]))
          (if-not @editing
        [:div.not-editing
         [:p (get-in @application/core-values-state [option (keyword
                                                             (application/option-name option)) :vision])
          [:i.mdi-editor-border-color.small.right {:on-click #(do (reset!
                                                                   editing true)
                                                                  (.preventDefault
                                                                   %)
                                                                  )}]]]
        [:div.editing
         [:div.col.s11
          [:input {:type "text"
                   :value (str (get-in @application/core-values-state [option
                                                                       (keyword (application/option-name option)) :vision]))
                   :on-change #(swap! application/core-values-state assoc-in [option
                                                                              (keyword
                                                                               (application/option-name option))
                                                                              :vision] (-> % .-target .-value))}]]
         [:div.col.s1
          [:p.right {:on-click #(do (reset! editing false)
                                    (.preventDefault %))} "Save"]]
         ])
          [:h5.center {:on-click #(set! (.-location js/window) (str "#/modules/visions?current_option=" option))} "Add a vision!"]))
    ))

(defn scaffolded-component []
  [:div.visions-tab
   (nav/component :visions)
   [:div.container
    [:div.row
     [:div.col.s12
      [:h4.center "Visions log"]
      [:ul.collapsible {:data-collapsible "expandable"
                        :id "expandable"}
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Career"]]
        [:div.collapsible-body
         [vision-item 0]
         ]]
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Finances"]]
        [:div.collapsible-body
         [vision-item 1]]
        ]
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Health"]]
        [:div.collapsible-body
         [vision-item 2]]
        ]
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Family"]]
        [:div.collapsible-body
         [vision-item 3]]
        ]
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Romance"]]
        [:div.collapsible-body
         [vision-item 4]]
        ]
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Personal Growth"]]
        [:div.collapsible-body
         [vision-item 5]]
        ]
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Fun"]]
        [:div.collapsible-body
         [vision-item 6]]
        ]
       [:li
        [:div.collapsible-header
         [:i.large.mdi-content-add.left] [:h5 "Physical Environment"]]
        [:div.collapsible-body
         [vision-item 7]]
        ]
       ]]]]])

(defn component []
  (reagent/create-class
   {:component-did-mount #(try
                            (-> (js/$ ".collapsible")
                                (.collapsible)
                                {:expandable true})
                            (catch :default e
                              ))
    :render scaffolded-component}))
