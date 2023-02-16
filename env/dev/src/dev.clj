(ns dev
  (:require [scicloj.clay.v2.api :as clay]
            [nextjournal.clerk :as clerk]
            [scicloj.kindly-default.v1.api :as kindly-default]))

(kindly-default/setup!)

(defn start! []
  #_(clay/start!)
  (clerk/serve! {:browse? true})
  :ready)

(defn show [notebook]
  #_(clay/show-doc! notebook {:toc? true})
  (clerk/show! notebook))
