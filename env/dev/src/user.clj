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

(defn clerk []
  (require 'clerk-setup)
  (in-ns 'clerk-setup))

(defn clay []
  (require 'clay-setup)
  (in-ns 'clay-setup))

(defn claykind []
  (require 'claykind-setup)
  (in-ns 'claykind-setup))

(defn portal []
  (require 'portal-setup)
  (in-ns 'portal-setup))

(help)
