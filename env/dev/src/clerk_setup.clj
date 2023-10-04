(ns clerk-setup
  (:require [nextjournal.clerk :as clerk]))

(defn help []
  (println "Available commands are:")
  (println)
  (println "(start!)                  ;; Start Clerk")
  (println "(start-and-watch!)        ;; Start Clerk and reload on all changes to the `book` namespaces")
  (println "(show <notebook-name>)    ;; Show the given notebook"))

;; Tell Clerk to render all tablecloth datasets as tables
;; (clerk/add-viewers! [{:pred #(= tech.v3.dataset.impl.dataset.Dataset (type %))
;;                       ;; :fetch-fn (fn [_ file] {:nextjournal/content-type "image/png"
;;                       ;;                         :nextjournal/value (Files/readAllBytes (.toPath file))})
;;                       :render-fn v/table}])

(defn start! []
  (clerk/serve! {:browse? true :toc? true})
  :ready)

(defn start-and-watch! []
  (clerk/serve! {:browse? true :watch-paths ["book" "data"]}))

(defn show [notebook]
  (clerk/show! notebook))

(defn build! []
  (clerk/build! {:paths ["book/chapter_1_intro/*"
                         "book/chapter_2_input_output/*"
                         "book/chapter_3_data_manipulation/*"]
                 :index       "book/index.clj"}))

(help)
