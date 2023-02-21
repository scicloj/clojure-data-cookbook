(ns book.vis.svg
  (:require
   [scicloj.kindly.v3.kind :as kind]
   [scicloj.kind-clerk.api :as kind-clerk]
   [tablecloth.api :as tc]
   [scicloj.noj.v1.vis :as vis]
   [hiccup.core :as hiccup]))

(kind-clerk/setup!)

(comment
  (require 'dev)
  (dev/start!))

(-> [:svg {:height 210
           :width 500}
     [:line {:x1 0
             :y1 0
             :x2 200
             :y2 200
             :style "stroke:rgb(255,0,0);stroke-width:2"}]]
    hiccup/html
    vis/raw-html)
