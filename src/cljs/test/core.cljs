(ns test.core
  (:require [reagent.core :as reagent]
            [test.session :refer [global-state]]
            [test.components.pages :refer [page]]
            [test.routes :as routes]
            [instaparse.core :as insta]))

(def mobile-parser
  (insta/parser
   "S = R V G+
    R = 'a'
    V = 'b'
    G = g s+
    g = 'c'
    s = 'd'"))

(defn page-render []
  [:div.app-container
   [(page)]])

(defn page-component []
  (reagent/create-class {:component-will-mount (do
                                                 (routes/app-routes)
                                                 (.log js/console "mounted!")
                                                 (.log js/console (pr-str
  mobile-parser))
                                                 (.log js/console (str "Parsed
  output:" (nth (get (mobile-parser "abcdddd") 1) 2))))
                         :render page-render}))

(defn main []
  (reagent/render-component [page-component]
                            (.getElementById js/document "app")))
