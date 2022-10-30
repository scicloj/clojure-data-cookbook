;; # 2.1 How to get data into the notebook

(ns book.chapter-2-input-output.1-reading-data
  (:require [scicloj.clay.v2.api :as clay]
            ))


(clay/start!)

(comment
  (clay/show-doc! "chapter_2_input_output/1_reading_data.clj" {:toc? true}))

;; ## How to read data from a CSV

;; Clojure comes with a great standard library for many, well, standard operations, including working with CSVs. One of the simplest ways to get data into your Clojure notebook to work with is to load it from a CSV file:
