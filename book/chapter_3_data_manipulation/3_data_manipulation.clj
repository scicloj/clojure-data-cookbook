(ns chapter-3-data-manipulation.3-data-manipulation
  (:require [tablecloth.api :as tc]
            [tech.v3.datatype.functional :as fun]
            [tech.v3.dataset.column :as tdsc]
            [clojure.string :as str]))

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




;;     - e.g. converting a column with numbers to a category (>5 "yes", <5 "no"), summing multiple columns into a new one

;; - Removing columns
;; - Transforming values
;;     - Working with nested data structures, really nice libraries in Clojure for doing this ([specter](https://github.com/redplanetlabs/specter), [meander](https://github.com/noprompt/meander))
;;     - All values in a column
;;     - Conditional transformation (e.g. "truncate only 11 digit phone numbers to 10 digits")
;; - Rearranging order of columns
;; - Renaming columns
;; - Filtering rows
;;     - Single filter, multiple filters
;; - Aggregating rows (counts, groups)
;; - Concatenating datasets
;; - Merging datasets
;;     - When column headers are the same or different, on multiple columns
;; - Converting between wide and long formats?
;; - Summarizing data (mean, standard deviation, confidence intervals etc.)
;; - Working with sequential data
;;     - Smoothing out data
;;         - Calculating a moving average
;;         - Averaging a sequence in blocks
;;     - Run length encoding?
;;     - Filling `nil` s with last non-`nil` value?
