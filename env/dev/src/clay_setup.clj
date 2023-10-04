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

(clay/swap-options!
  assoc
  :remote-repo {:git-url "https://github.com/scicloj/kindly-noted"
                :branch "main"}
  :quarto {:format {:html {:toc true
                           :toc-depth 4
                           :theme :spacelab}}
           :highlight-style :solarized
           :code-block-background true
           :embed-resources false
           :execute {:freeze true}})

(help)
