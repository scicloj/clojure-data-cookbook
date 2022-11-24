(ns user
  (:require [scicloj.clay.v2.api :as clay]))

(defn help []
  (println "Welcome to the Clojure Data Cookbook")
  (println)
  (println "Available commands are:")
  (println)
  (println "(start!)                  ;; Start Clay")
  (println "(show <notebook-name>)    ;; Show the given notebook"))

(defn start! []
  (clay/start!)
  :ready)

(defn show [notebook]
  (clay/show-doc! "chapter_2_input_output/2_1_loading_data.clj" {:toc? true}))
