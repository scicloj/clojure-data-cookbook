(ns book.vis.hanami
  (:require
   [scicloj.kindly.v3.kind :as kind]
   [scicloj.kind-clerk.api :as kind-clerk]
   [clojure.string :as str]
   [tablecloth.api :as tc]
   [aerial.hanami.templates :as ht]
   [scicloj.noj.v1.vis :as vis]))

(kind-clerk/setup!)

(comment
  (require 'dev)
  (dev/start!))


(-> {:x (range 9)
     :y (map +
             (range 9)
             (repeatedly 9 rand))}
    tc/dataset
    (vis/hanami-plot ht/point-chart
                     :MSIZE 200))
