(ns clay-setup
  (:require [scicloj.clay.v2.api :as clay]))

(defn help []
  (println "Available commands are:")
  (println)
  (println "(start!)                  ;; Start Clay")
  (println "(show <notebook-name>)    ;; Show the given notebook"))

(defn start! []
  (clay-setup/start!)
  :ready)

(defn show [notebook]
  (clay/show-namespace! notebook))

(defn build! []
  false)

(help)
