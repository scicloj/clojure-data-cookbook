(ns dev
  (:require [nextjournal.clerk :as clerk]))

(defn start! []
  (clerk/serve! {:browse? true})
  :ready)

(defn start-and-watch! []
  (clerk/serve! {:browse? true :watch-paths ["book" "data"]}))

(defn show [notebook]
  (clerk/show! notebook))
