(ns user)

(defn help []
  (println "Welcome to the Clojure Data Cookbook")
  (println)
  (println "You can load the following build tools:")
  (println)
  (println "(clerk-setup)")
  (println "(clay-setup)")
  (println "(claykind-setup)")
  (println "(portal-setup)"))

(defn clerk-setup []
  (require 'clerk-setup)
  (in-ns 'clerk-setup))

(defn clay-setup []
  (require 'clay-setup)
  (in-ns 'clay-setup))

(defn claykind-setup []
  (require 'claykind-setup)
  (in-ns 'claykind-setup))

(defn portal-setup []
  (require 'portal-setup)
  (in-ns 'portal-setup))

(help)
