(ns dev
  (:require [scicloj.clay.v2.api :as clay]))

(defn start! []
  (clay/start!)
  :ready)

(defn show [notebook]
  (clay/show-doc! notebook {:toc? true}))
