(ns dev
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [nextjournal.clerk :as clerk]
   [scicloj.kindly-default.v1.api :as kindly-default]
   [scicloj.clay.v2.api :as clay]))

(kindly-default/setup!)

;; Tell Clerk to render all tablecloth datasets as tables


;; (clerk/add-viewers! [{:pred #(= tech.v3.dataset.impl.dataset.Dataset (type %))
;;                       ;; :fetch-fn (fn [_ file] {:nextjournal/content-type "image/png"
;;                       ;;                         :nextjournal/value (Files/readAllBytes (.toPath file))})
;;                       :render-fn v/table}])

(defn start! []
  (clerk/serve! {:browse? true :toc? true})
  ;; (clay/start!)
  :ready)

(defn start-and-watch! []
  (clerk/serve! {:browse? true :watch-paths ["book" "data"]}))

(defn show [notebook]
  (clerk/show! notebook)
  ;; (clay/show-namespace! notebook)
  )

(defn build! []
  (clerk/build! {:paths ["book/chapter_1_intro/*"
                         ;; "book/chapter_2_input_output/*"
                         ;; "book/chapter_3_data_manipulation/*"
                         ]
                 :index "book/index.clj"}))
