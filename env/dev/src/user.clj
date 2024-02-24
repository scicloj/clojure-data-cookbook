(ns user)

(defn help []
  (println "Welcome to the Clojure Data Cookbook")
  (println)
  (println "Available commands are:")
  (println)
  (println "(build)      ;; build all of the namespaces in the project into a website using quarto"))

(defn dev
  "Load and switch to the 'dev' namespace."
  []
  (require 'dev)
  (help)
  (in-ns 'dev)
  :loaded)

(comment
  (defn clerk-setup []
    (require 'clerk-setup)
    (in-ns 'clerk-setup)
    :loaded-clerk)

  (defn clay-setup []
    (require 'clay-setup)
    (in-ns 'clay-setup))

  (defn claykind-setup []
    (require 'claykind-setup)
    (in-ns 'claykind-setup)))
