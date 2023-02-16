(ns dev
  (:require [nextjournal.clerk :as clerk]
            [nextjournal.clerk.viewer :as v]))

;; Tell Clerk to render all tablecloth datasets as tables


;; (clerk/add-viewers! [{:pred #(= tech.v3.dataset.impl.dataset.Dataset (type %))
;;                       ;; :fetch-fn (fn [_ file] {:nextjournal/content-type "image/png"
;;                       ;;                         :nextjournal/value (Files/readAllBytes (.toPath file))})
;;                       :render-fn v/table}])

(defn start! []
  (clerk/serve! {:browse? true})
  :ready)

(defn start-and-watch! []
  (clerk/serve! {:browse? true :watch-paths ["book" "data"]}))

(defn show [notebook]
  (clerk/show! notebook))
