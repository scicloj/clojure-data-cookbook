(ns user)

(defn help []
  (println "Welcome to the Clojure Data Cookbook")
  (println)
  (println "You can load the following build tools:")
  (println)
  (println "(clerk)")
  (println "(clay)")
  (println "(claykind)")
  (println "(portal)"))

(defn clerk []
  (require 'clerk-setup)
  (in-ns 'clerk-setup))

(defn clay []
  (require 'clerk-setup)
  (in-ns 'clerk-setup))

(defn claykind []
  (require 'claykind-setup)
  (in-ns 'claykind-setup))

(defn portal []
  (require 'portal-setup)
  (in-ns 'portal-setup))

(help)
