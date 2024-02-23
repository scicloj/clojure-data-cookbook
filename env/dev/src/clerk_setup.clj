(ns clerk-setup
  (:require
   [nextjournal.clerk :as clerk]))

(defn help []
  (println "Available commands are:")
  (println)
  (println "(start!)                 ;; Start Clerk")
  (println "(show! <notebook-name>)  ;; Show the given notebook")
  (println ";; Any namespace you want to render with Clerk needs to call (kind-clerk/setup!)")
  (println "(clear-cache!)           ;; Clear the clerk cache")
  (println "(build <dest-folder>)    ;; Build?? (maybe not)"))

(defn start! []
  (clerk/serve! {:browse? true})
  :ready)

(defn show! [notebook]
  (clerk/show! notebook))


(defn clear-cache! []
  (clerk/clear-cache!))

(help)
