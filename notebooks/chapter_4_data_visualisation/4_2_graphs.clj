;; # Graphs

(ns chapter-4-data-visualisation.4-2-graphs
  (:require [tablecloth.api :as tc]
            [aerial.hanami.common :as hc]
            [aerial.hanami.templates :as ht]
            [scicloj.noj.v1.vis.hanami.templates :as vht]
            [scicloj.noj.v1.vis :as vis]
            [scicloj.noj.v1.stats :as stats]
            [scicloj.noj.v1.datasets :as datasets]
            [tech.v3.datatype :as dtype]
            [tech.v3.datatype.functional :as fun]
            [hiccup.core :as hiccup]
            [clojure2d.color :as color]
            [tablecloth.api :as tc]
            [scicloj.kind-clerk.api :as kind-clerk]))

(kind-clerk/setup!)


(def co2-over-time (tc/dataset "data/co2_over_time.csv"))

(-> co2-over-time
    (vis/hanami-plot ht/line-chart {:X "Date"
                                    :XTYPE "temporal"
                                    :WIDTH 750
                                    :Y "adjusted CO2"
                                    :YSCALE {:zero false}}))
(def diamonds datasets/diamonds)

(-> diamonds
    (vis/hanami-plot vht/boxplot-chart {:X :cut
                                        :XTYPE "nominal"
                                        :Y :price
                                        :WIDTH 750}))

(-> diamonds
    (vis/hanami-plot vht/boxplot-chart {:X :color
                                        :XTYPE "nominal"
                                        :Y :price
                                        :WIDTH 750}))

(-> diamonds
    (vis/hanami-plot vht/boxplot-chart {:X :clarity
                                        :XTYPE "nominal"
                                        :Y :price
                                        :WIDTH 750}))

:ok
