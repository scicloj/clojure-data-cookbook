(ns dev
  (:require [scicloj.kindly-default.v1.api :as kindly-default]
            [nextjournal.clerk :as clerk]
            [nextjournal.clerk.viewer :as v]))

(defn start! []
  (kindly-default/setup!)
  (clerk/serve! {:browse? true
                 :port 2222})
  :ready)

(defn start-and-watch! []
  (clerk/serve! {:browse? true :watch-paths ["book" "data"]}))

(defn show [notebook]
  (clerk/show! notebook))
