(ns claykind-setup
  (:require [scicloj.claykind.api :as claykind]))

(defn help []
  (println "Available commands are:")
  (println)
  (println "(build!)                  ;; Compile the markdown of the book"))

(defn build! []
  (claykind/render!))

(help)
