(ns portal-setup
  (:require [scicloj.kind-portal.v1.api :as kportal]))

(defn help []
  (println "Available commands are:")
  (println)
  (println "()                  ;; Send form to Portal"))

(kportal/kindly-submit-context {:value {:this "is a test"
                                        :of "portal"}})

(help)
