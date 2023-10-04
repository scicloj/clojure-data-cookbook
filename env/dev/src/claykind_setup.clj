(ns claykind-setup
  (:require [scicloj.claykind.api :as claykind]))

(defn help []
  (println "Available commands are:")
  (println)
  (println "(build!)                  ;; Compile the markdown of the book"))

(defn build! []
  ;; you can also call this at the command line with
  ;; `clojure -M:dev -m scicloj.claykind.main --verbose
  (claykind/render!))

(help)
