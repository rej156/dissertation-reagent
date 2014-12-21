(ns test.components.pages
  (:require [test.components.introduction.home-page :refer [home-page]]
            [test.components.introduction.second :refer [introduction-second]]
            ))

(def pages {:home-page home-page
            :introduction-second introduction-second
            :introduction-third introduction-third})
