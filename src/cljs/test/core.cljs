(ns test.core
  (:require [reagent.core :as reagent]
            [test.session :refer [global-state]]
            [test.routes :as routes]))

(defn page-render []
  [:div.app-container
   [(global-state :current-page)]])

(defn page-component []
  (reagent/create-class {:component-will-mount routes/app-routes
                         :render page-render}))

(defn main []
  (reagent/render-component [page-component]
                            (.getElementById js/document "app")))
