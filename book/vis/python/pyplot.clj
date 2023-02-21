(ns book.vis.python.pyplot
  (:require
   [scicloj.kindly.v3.kind :as kind]
   [scicloj.kind-clerk.api :as kind-clerk]
   [tablecloth.api :as tc]
   [scicloj.noj.v1.vis :as vis]
   [scicloj.noj.v1.vis.python :as vis.python]
   [libpython-clj2.require :refer [require-python]]
   [libpython-clj2.python :refer [py. py.. py.-] :as py]
   [tech.v3.datatype :as dtype]
   [tech.v3.datatype.functional :as fun]
   [hiccup.core :as hiccup]
   [aerial.hanami.templates :as ht]))

(kind-clerk/setup!)

(comment
  (require 'dev)
  (dev/start!))


(require-python '[numpy :as np]
                '[numpy.random :as np.random]
                'matplotlib.pyplot
                '[seaborn :as sns]
                ;; 'bokeh.plotting
                ;; 'bokeh.resources
                ;; 'bokeh.embed
                'json
                ;; '[arviz :as az]
                )

(def sine-data
  (-> {:x (range 0 (* 3 np/pi) 0.1)}
      tc/dataset
      (tc/add-column :y #(fun/sin (:x %)))))

(vis.python/with-pyplot
  ;; http://gigasquidsoftware.com/blog/2020/01/18/parens-for-pyplot/
  (matplotlib.pyplot/plot
   (:x sine-data)
   (:y sine-data)))

(vis.python/pyplot
 #(matplotlib.pyplot/plot
   (:x sine-data)
   (:y sine-data)))

;; https://seaborn.pydata.org/tutorial/introduction
(let [tips (sns/load_dataset "tips")]
  (sns/set_theme)
  (vis.python/pyplot
   #(sns/relplot :data tips
                 :x "total_bill"
                 :y "tip"
                 :col "time"
                 :hue "smoker"
                 :style "smoker"
                 :size "size")))

(let [size [10 50]
      data {:normal (apply np.random/randn size)
            :gumbel (np.random/gumbel :size size)
            :student_t (np.random/standard_t :df 6
                                             :size size)
            :exponential (np.random/exponential :size size)}]
  ;; (vis.python/pyplot
  ;;  #(az/plot_forest data))
  )



:bye
