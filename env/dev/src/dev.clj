(ns dev
  (:require [scicloj.kindly-default.v1.api :as kindly-default]
           [nextjournal.clerk :as clerk]
           [nextjournal.clerk.viewer :as v]))

(defn start! []
  (clerk/serve! {:browse? true})
  :ready)

(defn start-and-watch! []
  (clerk/serve! {:browse? true :watch-paths ["book" "data"]}))

(defn show [notebook]
  (clerk/show! notebook))
