(ns test.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [test.session :refer [global-state]]
            [test.components.pages :refer [page]]
            [test.routes :as routes]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(defn page-render []
  [:div.app-container
   [(page)]])

(defn flickr-async []
  (go (let [response (<! (http/get "https://api.flickr.com/services/feeds/photos_public.gne?tags=potato&tagmode=all&format=json" ))]
        (.log js/console (:status response))
        (.log js/console (map :items (:body response))))))

(defn page-component []
  (reagent/create-class {:component-will-mount (do
                                                 (routes/app-routes)
                                                 (flickr-async))
                         :render page-render}))

(defn main []
  (reagent/render-component [page-component]
                            (.getElementById js/document "app")))
