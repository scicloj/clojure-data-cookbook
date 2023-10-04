(ns experiments.oct-30
  (:require [scicloj.clay.v2.api :as clay]
            [scicloj.kindly.v4.kind :as kind]
            [clojure.string :as str]
            [tablecloth.api :as tc]))


(clay-setup/start!)

(comment
  (clay-setup/show-doc! "experiments/oct_30.clj" {:toc? true}))

;; https://r4ds.had.co.nz/transform.html

;; Load the data
;; Tablecloth will un-gzip our file for us
;; tech.ml.dataset

(def flights
  (-> "experiments/data/nycflights13.csv.gz"
      (tc/dataset {:key-fn keyword})))

flights

;; Count all the rows

(tc/row-count flights)

;; Pick observations by their values (filter()).
;; Find all flights on Jan 1

;; Note: This is very efficient, compared to iterating over all rows in other languages
;;   This does not create an intermediate data structure for every row, so performing row-based ops is not prohibitively slow (thanks to tech.ml.dataset)

(def jan-1
  (-> flights
      (tc/select-rows (fn [row]
                        (and (-> row :day (= 1))
                             (-> row :month (= 1)))))
    ;; tc/row-count
      ))

jan-1

;; (def get-flights-on-day [month day]
;;   )

;; Order rows

(-> jan-1
    (tc/order-by [:dep_time :dep_delay])
    (kind/pprint))

;; Note: Tablecloth sorts missing values as the lowest ones (i.e. at the top if sorted by ascending order, at the bottom if sorted by descending order)

(-> jan-1
    (tc/drop-columns [:year :month :day])
    ;; (tc/select-columns [:dep_time :sched_dep_time])
    )

(-> jan-1
    (tc/select-columns (fn [column-name]
                         (str/starts-with? (name column-name) "dep"))))

(-> jan-1
    (tc/select-columns (fn [column-name]
                         (str/includes? (name column-name) "time"))))
