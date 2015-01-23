(ns test.components.introduction.home-page
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [reagent.core :refer [atom]]
            [test.session :as session :refer [prefs prefs-state]]))

(def above-800 (atom true))

(defn dynamic-component-positioning []
  (if (< 644 (.-innerWidth js/window))
    (reset! above-800 true)
    (reset! above-800 false)))

(defn photo [];;[img-url title author date flickr-url]
  [:div.row
   [:img {:src "https://avatars.githubusercontent.com/u/109629?v=3&s=32"}]
   ])

(defn home-page []
  (set! (.-onresize js/window) dynamic-component-positioning)
  [:div.home-page
   [:div.row
    [:div {:class ".col-md-8 col-md-offset-2"}
     [:div.container
      [:h2 "Flickr Public Feed"]
      [:div.photos
       (if @above-800
         [:h2 "It's above 800!"]
         [:h2 "It's below 800!"])
       (photo)
       ]
      ]]]

   ])
