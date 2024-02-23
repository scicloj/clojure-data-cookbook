(ns portal-setup
  (:require [scicloj.kind-portal.v1.api]))

(defn help []
  (println "Loaded Portal. Available commands are:")
  (println "Add these functions to your emacs config and call one of the send fns to eval a form using portal:")
  (println "https://raw.githubusercontent.com/scicloj/kind-portal/main/kind-portal.el"))
