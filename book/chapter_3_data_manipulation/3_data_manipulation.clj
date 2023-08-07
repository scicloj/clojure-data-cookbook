(ns chapter-3-data-manipulation.3-data-manipulation
  (:require [tablecloth.api :as tc]
            [tech.v3.datatype.functional :as fun]
            [tech.v3.dataset.column :as tdsc]
            [clojure.string :as str]
            [fastmath.stats :as stats]))

;; This is a work in progress of the code examples that will make up chapter 3
;; of the Clojure data cookbook

;; # 3. Data manipulation

;; Once data is loaded and ready to work with, here's how to do some of the most common
;; data manipulation tasks.

;; ## Sorting

(def dataset (tc/dataset [{:country "Canada"
                           :size 10000000}
                          {:country "USA"
                           :size 9000000}
                          {:country "Germany"
                           :size 80000}]))

;; ### Sorting columns

;; Give the column headers in the order you want

(-> dataset
    (tc/reorder-columns [:country :size]))

;; ### Sorting rows

(-> dataset
    (tc/order-by [:size] [:desc]))

;; ### Custom sorting functions

;; e.g. length of the country name

(-> dataset
    (tc/order-by (fn [row] (-> row :country count))
                 :desc))

;; ## Selecting one column or multiple columns

(-> dataset
    (tc/select-columns [:country]))

;; ## Randomizing order

(-> dataset tc/shuffle)

;; ## Repeatable randomisation

(-> dataset (tc/shuffle {:seed 100}))

;; Finding unique rows

(def dupes (tc/dataset [{:country "Canada"
                         :size 10000000}
                        {:country "Canada"
                         :size 10000303}
                        {:country "United states"
                         :size 9000000}
                        {:country "United States"
                         :size 9000000}
                        {:country "Germany"
                         :size 80000}]))

;; (def
;;   "USA" #{"USA" "United States" "United states of America"})

;; https://scicloj.github.io/tablecloth/index.html#Unique

(-> dupes tc/unique-by)
(-> dupes (tc/unique-by :size))
(-> dupes (tc/unique-by :country))
(-> dupes (tc/unique-by #(-> % :country str/lower-case)))
(-> dupes (tc/unique-by #(-> % :country str/lower-case) {:strategy (fn [vals]
                                                                     (case (tdsc/column-name vals)
                                                                       :size (apply max vals)
                                                                       :country (last vals)))}))

;; could use this to rename vals to a canonical one (e.g. convert everything that matches set of USA to "USA")


;; Adding computed columns to data

;; "lengthening" or "widening" data, making it "tidy"

(def exp-moving-avg
  (let [data (get co2-over-time "adjusted CO2")
        moving-avg
        (->> data
             (reduce (fn [acc next]
                       (conj acc (+ (* 0.9 (last acc)) (* 0.1 next))))
                     [(first data)])
             rest
)]
    (tc/dataset [["Exponential moving average" moving-avg]])))

(tc/append co2-over-time exp-moving-avg)

;;  e.g. converting a column with numbers to a category (>5 "yes", <5 "no"), summing multiple columns into a new one

(-> dataset
    (tc/add-column :area [9000000 8000000 1000000]))

(-> dataset
    (tc/add-column :population [40000000 100000000 80000000])
    (tc/rename-columns {:size :area})
    (tc/convert-types :population :double)
    (tc/add-column :density (fn [d]
                              (fun// (:population d) (:area d)))))

;; vs, probably preferable

(-> dataset
    (tc/add-column :population [40000000 100000000 80000000])
    (tc/rename-columns {:size :area})
    (tc/add-column :density (fn [ds]
                              (fun// (fun/* 1.0 (:population ds)) (:area ds)))))


;; - Removing columns

(-> dataset
    (tc/drop-columns :size))

;; - Transforming values
;;     - Working with nested data structures, really nice libraries in Clojure for doing this ([specter](https://github.com/redplanetlabs/specter), [meander](https://github.com/noprompt/meander))
;;     - All values in a column
;;     - Conditional transformation (e.g. "truncate only 11 digit phone numbers to 10 digits")
;; - Rearranging order of columns
;; - Renaming columns

;; - Filtering rows
;;     - Single filter, multiple filters

(-> dataset
    (tc/select-rows (fn [row]
                      (< 1000000 (:size row)))))

;; - Aggregating rows (counts, groups)

(def co2-over-time (tc/dataset "data/co2_over_time.csv"))

(-> co2-over-time
    (tc/aggregate {:average-co2 (fn [ds]
                                  (/ (reduce + (get ds "CO2"))
                                     (count (get ds "CO2"))))}))

;; Group by year

(-> co2-over-time
    (tc/add-column "Year" (fn [ds]
                            (map (memfn getYear) (get ds "Date"))))
    (tc/group-by "Year"))

(-> co2-over-time
    (tc/group-by (fn [row]
                   (.getYear (get row "Date")))))

;; Get average temp per year
;; tablecloth applies the aggregate fn to every groups dataset

(defn round2
  "Round a double to the given precision (number of significant digits)"
  [precision d]
  (let [factor (Math/pow 10 precision)]
    (/ (Math/round (* d factor)) factor)))

(-> co2-over-time
    (tc/group-by (fn [row]
                   (.getYear (get row "Date"))))
    (tc/aggregate {:average-co2 (fn [ds]
                                  (round2 2
                                          (/ (reduce + (get ds "CO2"))
                                             (count (get ds "CO2")))))}))

;; Can rename the column to be more descriptive

(-> co2-over-time
    (tc/group-by (fn [row]
                   (.getYear (get row "Date"))))
    (tc/aggregate {:average-co2 (fn [ds]
                                  (/ (reduce + (get ds "CO2"))
                                     (count (get ds "CO2"))))})
    (tc/rename-columns {:$group-name :year}))

;; Concatenating datasets

(def ds1 (tc/dataset [{:id "id1" :b "val1"}
                      {:id "id2" :b "val2"}
                      {:id "id3" :b "val3"}]))

(def ds2 (tc/dataset [{:id "id1" :b "val4"}
                      {:id "id5" :b "val5"}
                      {:id "id6" :b "val6"}]))

;; Naively concats rows

(tc/concat ds1 ds2 (tc/dataset [{:id "id3" :b "other value"}]))

(tc/concat ds1 (tc/dataset [{:b "val4" :c "text"}
                            {:b "val5" :c "hi"}
                            {:b "val6" :c "test"}]))

;; De-duping

(tc/union ds1 ds2)

;; - Merging datasets
;;     - When column headers are the same or different, on multiple columns

;; TODO explain set logic and SQL joins

(def ds3 (tc/dataset {:id [1 2 3 4]
                      :b ["val1" "val2" "val3" "val4"]}))

(def ds4 (tc/dataset {:id [1 2 3 4]
                      :c ["val1" "val2" "val3" "val4"]}))

;; Keep all columns

(tc/full-join ds3 ds4 :id)

;; "Merge" datasets on a given column where rows have a value
(tc/inner-join ds3 ds4 :id)

;; Drop rows missing a value
(tc/inner-join (tc/dataset {:id [1 2 3 4]
                      :b ["val1" "val2" "val3"]})
               (tc/dataset {:id [1 2 3 4]
                      :c ["val1" "val2" "val3" "val4"]})
               :id)

(tc/right-join (tc/dataset {:id [1 2 3 ]
                            :b ["val1" "val2" "val3"]})
               (tc/dataset {:id [1 2 3 4]
                            :c ["val1" "val2" "val3" "val4"]})
               :id)

(tc/left-join (tc/dataset {:email ["asdf"]
                            :name ["asdfads"]
                            :entry-id [1 2 3]})
               (tc/dataset {:entry-id [1 2 3]
                            :upload-count [2 3 4]
                            :catgory ["art" "science"]})
               :entry-id)

(tc/dataset {:email ["asdf"]
             :name ["asdfads"]
             :entry-id [1 2 3]})

(tc/dataset {:entry-id [1 2 3]
             :upload-count [2 3 4]
             :catgory ["art" "science"]})


;; see tablecloth join stuff

;; Inner join, only keeps rows with the specified column value in common

(tc/inner-join ds1 ds2 :id)

;; - Converting between wide and long formats?

;; - Compute rolling average to be able to plot a trend line

(-> co2-over-time)

;; - Train a model to predict the next 10 years

(-> co2-over-time)


;; - Summarizing data (mean, standard deviation, confidence intervals etc.)

;;   - Standard deviation using fastmath

(def avg-co2-by-year
  (-> co2-over-time
      (tc/group-by (fn [row]
                     (.getYear (get row "Date"))))
      (tc/aggregate {:average-co2 (fn [ds]
                                    (/ (reduce + (get ds "CO2"))
                                       (count (get ds "CO2"))))
                     :standard-deviation (fn [ds]
                                           (stats/stddev (get ds "CO2")))})
      (tc/rename-columns {:$group-name :year})
      ))

;; - Overall average

(defn mean [data]
  (/ (reduce + data) (count data)))

(mean (:average-co2 avg-co2-by-year))

;; - Long term average 1991-2020

(-> avg-co2-by-year
    (tc/select-rows (fn [row] (< 1990 (:year row))))
    :average-co2
    mean)

;; - Working with sequential data
;;     - Smoothing out data
;;         - Calculating a moving average
;;         - Averaging a sequence in blocks
;;     - Run length encoding?
;;     - Filling `nil` s with last non-`nil` value?
