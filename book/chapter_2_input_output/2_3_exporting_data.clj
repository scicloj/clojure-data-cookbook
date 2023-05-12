(ns chapter-2-input-output.2-3-exporting-data
  {:nextjournal.clerk/toc true}
  (:require [tablecloth.api :as tc]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

;; # 2.3 How to get data out of a notebook

(def consistent-data
  (map-indexed (fn [index _coll] (str "cell-" index))
               (range 10)))

(def data (take 20 (repeat (zipmap (range 10) consistent-data))))

;; ## Writing to a CSV file

;; depends what the data looks like
;; for a seq of maps:

;; headers are not necessarily sorted, put them in whatever order you want here
;; Clojure maps make no guarantees about key order, make sure to order values,
;; i.e. use the same header row to get the values from each map
(let [headers (-> data first keys sort)
      rows (->> data (map (fn [row]
                            (map (fn [header]
                                   (get row header)) headers))))]
  (with-open [writer (io/writer "data/csv-output.csv")]
    (csv/write-csv writer (cons headers rows))))

;; Tablecloth can also export csvs (among other formats)
(def tc-dataset (tc/dataset data))

(tc/write-csv! tc-dataset "data/tc-output.csv")

;; ## Writing nippy

(tc/write! tc-dataset "data/tc-nippy.nippy")

;; Read this also with tablecloth:
(tc/dataset "data/tc-nippy.nippy")

;; ## Leave data in Clojure files

(->> data pr-str (spit "data/clojure-output.edn"))

;; This can be consumed later with:
(with-open [reader (io/reader "data/clojure-output.edn")]
  (edn/read (java.io.PushbackReader. reader)))

;; ## Notebook artifacts


;; HTML pages
;; Other formats, options for exporting notebooks? PDFs?
;; Partial artifacts, e.g. export just a graph
;; Writing to a database?
