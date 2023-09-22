(ns user
  (:require [scicloj.clay.v2.api :as clay]))

(defn help []
  (println "Welcome to the Clojure Data Cookbook")
  (println)
  (println "Available commands are:")
  (println)
  (println "(start!)                  ;; Start Clerk")
  (println "(start-and-watch!)        ;; Start Clerk and reload on all changes to the `book` namespaces")
  (println "(show <notebook-name>)    ;; Show the given notebook"))

(defn dev
  "Load and switch to the 'dev' namespace."
  []
  (require 'dev)
  (help)
  (in-ns 'dev)
  :loaded)


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
