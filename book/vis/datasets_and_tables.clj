(ns book.vis.datasets-and-tables
  (:require
   [scicloj.kindly.v3.kind :as kind]
   [scicloj.kind-clerk.api :as kind-clerk]
   [tablecloth.api :as tc]))

(kind-clerk/setup!)

(comment
  (require 'dev)
  (dev/start!))

;; ## Some plain data

(def people-as-maps
  (->> (range 29)
       (mapv (fn [i]
               {:preferred-language (["clojure" "clojurescript" "babashka"]
                                     (rand-int 3))
                :age (rand-int 100)
                :mood (rand)}))))

(def field-names
  [:preferred-language :age :mood])

(def people-as-vectors
  (->> people-as-maps
       (mapv (apply juxt field-names))))

;; ## Datasets

;; [tech.ml.dataset](https://github.com/techascent/tech.ml.dataset) datasets currently use the default printing of the library,
;; rendered as markdown.

(-> people-as-maps
    tc/dataset
    (tc/reorder-columns field-names))

;; ### Tables

;; The `:kind/table` kind can be handy for an interactive table view.
;; It can be applied to row vectors, row maps, and datasets.

(kind/table
 {:column-names field-names
  :row-vectors people-as-vectors})

(kind/table
 {:column-names field-names
  :row-maps people-as-maps})

(-> people-as-maps
    tc/dataset
    kind/table)
