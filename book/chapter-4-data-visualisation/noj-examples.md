---
format:
  html: {toc: true, toc-depth: 4, theme: spacelab}
highlight-style: solarized
code-block-background: true
embed-resources: false
execute: {freeze: true}
---

<style>
.printedClojure .sourceCode {
  background-color: transparent;
  border-style: none;
}

.kind_map {
  background:            lightgreen;
  display:               grid;
  grid-template-columns: repeat(2, auto);
  justify-content:       center;
  text-align:            right;
  border: solid 1px black;
  border-radius: 10px;
}

.kind_vector {
  background:            lightblue;
  display:               grid;
  grid-template-columns: repeat(1, auto);
  align-items:           center;
  justify-content:       center;
  text-align:            center;
  border:                solid 2px black;
  padding:               10px;
}

.kind_set {
  background:            lightyellow;
  display:               grid;
  grid-template-columns: repeat(auto-fit, minmax(auto, max-content));
  align-items:           center;
  justify-content:       center;
  text-align:            center;
  border:                solid 1px black;
}
</style>

<script src="https://cdn.jsdelivr.net/npm/vega@5" type="text/javascript"></script><script src="https://cdn.jsdelivr.net/npm/vega-lite@5" type="text/javascript"></script><script src="https://cdn.jsdelivr.net/npm/vega-embed@6" type="text/javascript"></script><script src="https://unpkg.com/react@18/umd/react.production.min.js" type="text/javascript"></script><script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js" type="text/javascript"></script><script src="https://scicloj.github.io/scittle/js/scittle.js" type="text/javascript"></script><script src="https://scicloj.github.io/scittle/js/scittle.reagent.js" type="text/javascript"></script><script src="/js/portal-main.js" type="text/javascript"></script>
<script><code>{:type &quot;application/x-scittle&quot;}</code>(ns main
                      (:require [reagent.core :as r]
                                [reagent.dom :as dom]))</script>

# Graphs with Noj

## Bar graphs

```clojure
(ns chapter-4-data-visualisation.noj-examples
  (:require [tablecloth.api :as tc]
            [aerial.hanami.common :as hc]
            [aerial.hanami.templates :as ht]
            [scicloj.noj.v1.vis.hanami.templates :as vht]
            [scicloj.noj.v1.vis :as vis]
            [scicloj.noj.v1.stats :as stats]
            [scicloj.noj.v1.datasets :as datasets]
            [tech.v3.datatype :as dtype]
            [tech.v3.datatype.functional :as fun]
            [scicloj.kindly.v4.kind :as kind]
            [hiccup.core :as hiccup]
            [clojure2d.color :as color]
            [scicloj.kind-clerk.api :as kind-clerk]))
```

> **stdout**
> 
> Register model:  :smile.regression/ordinary-least-square
> Register model:  :smile.regression/elastic-net
> Register model:  :smile.regression/lasso
> Register model:  :smile.regression/ridge
> Register model:  :smile.regression/gradient-tree-boost
> Register model:  :smile.regression/random-forest

> **stderr**
> 
> WARNING: test already refers to: #'clojure.core/test in namespace: scicloj.kindly.v4.kind, being replaced by: #'scicloj.kindly.v4.kind/test
> WARNING: seq already refers to: #'clojure.core/seq in namespace: scicloj.kindly.v4.kind, being replaced by: #'scicloj.kindly.v4.kind/seq
> WARNING: vector already refers to: #'clojure.core/vector in namespace: scicloj.kindly.v4.kind, being replaced by: #'scicloj.kindly.v4.kind/vector
> WARNING: set already refers to: #'clojure.core/set in namespace: scicloj.kindly.v4.kind, being replaced by: #'scicloj.kindly.v4.kind/set
> WARNING: map already refers to: #'clojure.core/map in namespace: scicloj.kindly.v4.kind, being replaced by: #'scicloj.kindly.v4.kind/map

<div class="printedClojure">

```clojure
nil
```

</div>

```clojure
(kind-clerk/setup!)
```

<div class="printedClojure">

```clojure
:ok
```

</div>

## Raw html

```clojure
(-> "<p>Hello, <i>Noj</i>.</p>"
    vis/raw-html)
```

<code>[:div {:dangerouslySetInnerHTML {:__html &quot;&lt;p&gt;Hello, &lt;i&gt;Noj&lt;/i&gt;.&lt;/p&gt;&quot;}}]</code>

```clojure
(-> [:svg {:height 210
           :width 500}
     [:line {:x1 0
             :y1 0
             :x2 200
             :y2 200
             :style "stroke:rgb(255,0,0);stroke-width:2"}]]
    hiccup/html
    vis/raw-html)
```

<code>[:div {:dangerouslySetInnerHTML {:__html &quot;&lt;svg height=&#39;&quot;210&#39;&quot; width=&#39;&quot;500&#39;&quot;&gt;&lt;line style=&#39;&quot;stroke:rgb(255,0,0);stroke-width:2&#39;&quot; x1=&#39;&quot;0&#39;&quot; x2=&#39;&quot;200&#39;&quot; y1=&#39;&quot;0&#39;&quot; y2=&#39;&quot;200&#39;&quot;&gt;&lt;/line&gt;&lt;/svg&gt;&quot;}}]</code>

## Visualizing datases with Hanami

Noj offers a few convenience functions to make [Hanami](https://github.com/jsa-aerial/hanami) plotting work smoothly with [Tablecloth](https://scicloj.github.io/tablecloth/) and [Kindly](https://scicloj.github.io/kindly/).

```clojure
(def random-walk
  (let [n 20]
    (-> {:x (range n)
         :y (->> (repeatedly n #(- (rand) 0.5))
                 (reductions +))}
        tc/dataset)))
```

<div class="printedClojure">

```clojure
"#'chapter-4-data-visualisation.noj-examples/random-walk"
```

</div>

### A simple plot

We can plot a Tablecloth datasete using a Hanami template:

```clojure
(-> random-walk
    (vis/hanami-plot ht/point-chart
                     {:MSIZE 200}))
```

<div><code>{:style {:width &quot;100%&quot;}}</code><script>vegaEmbed(document.currentScript.parentElement, {"encoding":{"y":{"field":"y","type":"quantitative"},"x":{"field":"x","type":"quantitative"}},"mark":{"type":"circle","size":200,"tooltip":true},"width":400,"background":"floralwhite","height":300,"data":{"values":"y,x\n0.31374767344737975,0\n-0.11193791816459875,1\n-0.03734349169824358,2\n0.10302338420745105,3\n-0.11024977484304788,4\n-0.1829339894154034,5\n0.0952776858879889,6\n0.4513299579744753,7\n0.505515881626261,8\n0.829247029586699,9\n0.48601527959184054,10\n0.6100374942476102,11\n0.1102529324781345,12\n0.5986911751870085,13\n0.31733790957995334,14\n0.5325022629112035,15\n0.08194437071680616,16\n0.04444997104928439,17\n-0.18842675339713777,18\n-0.5157742332930515,19\n","format":{"type":"csv"}}});</script></div>

Let us look inside the resulting vega-lite space. We can see the dataset is included as CSV:

```clojure
(-> random-walk
    (vis/hanami-plot ht/point-chart
                     {:MSIZE 200})
    kind/pprint)
```

<div class="printedClojure">

```clojure
{:encoding
 {:y {:field "y", :type "quantitative"},
  :x {:field "x", :type "quantitative"}},
 :mark {:type "circle", :size 200, :tooltip true},
 :width 400,
 :background "floralwhite",
 :height 300,
 :data
 {:values
  "y,x\n0.31374767344737975,0\n-0.11193791816459875,1\n-0.03734349169824358,2\n0.10302338420745105,3\n-0.11024977484304788,4\n-0.1829339894154034,5\n0.0952776858879889,6\n0.4513299579744753,7\n0.505515881626261,8\n0.829247029586699,9\n0.48601527959184054,10\n0.6100374942476102,11\n0.1102529324781345,12\n0.5986911751870085,13\n0.31733790957995334,14\n0.5325022629112035,15\n0.08194437071680616,16\n0.04444997104928439,17\n-0.18842675339713777,18\n-0.5157742332930515,19\n",
  :format {:type "csv"}}}
```

</div>

### Additional Hanami templates

The `scicloj.noj.v1.vis.hanami.templates` namespace add Hanami templates to Hanami's own collection.

```clojure
(-> datasets/mtcars
    (vis/hanami-plot vht/boxplot-chart
                     {:X :gear
                      :XTYPE :nominal
                      :Y :mpg}))
```

<div><code>{:style {:width &quot;100%&quot;}}</code><script>vegaEmbed(document.currentScript.parentElement, {"encoding":{"y":{"field":"mpg","type":"quantitative"},"x":{"field":"gear","type":"nominal"}},"mark":{"type":"boxplot","tooltip":true},"width":400,"background":"floralwhite","height":300,"data":{"values":"name,mpg,cyl,disp,hp,drat,wt,qsec,vs,am,gear,carb\nMazda RX4,21.0,6,160.0,110,3.9,2.62,16.46,0,1,4,4\nMazda RX4 Wag,21.0,6,160.0,110,3.9,2.875,17.02,0,1,4,4\nDatsun 710,22.8,4,108.0,93,3.85,2.32,18.61,1,1,4,1\nHornet 4 Drive,21.4,6,258.0,110,3.08,3.215,19.44,1,0,3,1\nHornet Sportabout,18.7,8,360.0,175,3.15,3.44,17.02,0,0,3,2\nValiant,18.1,6,225.0,105,2.76,3.46,20.22,1,0,3,1\nDuster 360,14.3,8,360.0,245,3.21,3.57,15.84,0,0,3,4\nMerc 240D,24.4,4,146.7,62,3.69,3.19,20.0,1,0,4,2\nMerc 230,22.8,4,140.8,95,3.92,3.15,22.9,1,0,4,2\nMerc 280,19.2,6,167.6,123,3.92,3.44,18.3,1,0,4,4\nMerc 280C,17.8,6,167.6,123,3.92,3.44,18.9,1,0,4,4\nMerc 450SE,16.4,8,275.8,180,3.07,4.07,17.4,0,0,3,3\nMerc 450SL,17.3,8,275.8,180,3.07,3.73,17.6,0,0,3,3\nMerc 450SLC,15.2,8,275.8,180,3.07,3.78,18.0,0,0,3,3\nCadillac Fleetwood,10.4,8,472.0,205,2.93,5.25,17.98,0,0,3,4\nLincoln Continental,10.4,8,460.0,215,3.0,5.424,17.82,0,0,3,4\nChrysler Imperial,14.7,8,440.0,230,3.23,5.345,17.42,0,0,3,4\nFiat 128,32.4,4,78.7,66,4.08,2.2,19.47,1,1,4,1\nHonda Civic,30.4,4,75.7,52,4.93,1.615,18.52,1,1,4,2\nToyota Corolla,33.9,4,71.1,65,4.22,1.835,19.9,1,1,4,1\nToyota Corona,21.5,4,120.1,97,3.7,2.465,20.01,1,0,3,1\nDodge Challenger,15.5,8,318.0,150,2.76,3.52,16.87,0,0,3,2\nAMC Javelin,15.2,8,304.0,150,3.15,3.435,17.3,0,0,3,2\nCamaro Z28,13.3,8,350.0,245,3.73,3.84,15.41,0,0,3,4\nPontiac Firebird,19.2,8,400.0,175,3.08,3.845,17.05,0,0,3,2\nFiat X1-9,27.3,4,79.0,66,4.08,1.935,18.9,1,1,4,1\nPorsche 914-2,26.0,4,120.3,91,4.43,2.14,16.7,0,1,5,2\nLotus Europa,30.4,4,95.1,113,3.77,1.513,16.9,1,1,5,2\nFord Pantera L,15.8,8,351.0,264,4.22,3.17,14.5,0,1,5,4\nFerrari Dino,19.7,6,145.0,175,3.62,2.77,15.5,0,1,5,6\nMaserati Bora,15.0,8,301.0,335,3.54,3.57,14.6,0,1,5,8\nVolvo 142E,21.4,4,121.0,109,4.11,2.78,18.6,1,1,4,2\n","format":{"type":"csv"}}});</script></div>

### Layers

```clojure
(-> random-walk
    (vis/hanami-layers
     {:TITLE "points and a line"}
     [(vis/hanami-plot nil
                       ht/point-chart
                       {:MSIZE 400})
      (vis/hanami-plot nil
                       ht/line-chart
                       {:MSIZE 4
                        :MCOLOR "brown"})]))
```

<div><code>{:style {:width &quot;100%&quot;}}</code><script>vegaEmbed(document.currentScript.parentElement, {"width":400,"background":"floralwhite","title":{"text":"points and a line"},"layer":[{"encoding":{"y":{"field":"y","type":"quantitative"},"x":{"field":"x","type":"quantitative"}},"mark":{"type":"circle","size":400,"tooltip":true},"width":400,"background":"floralwhite","height":300},{"encoding":{"y":{"field":"y","type":"quantitative"},"x":{"field":"x","type":"quantitative"}},"mark":{"type":"line","size":4,"color":"brown","tooltip":true},"width":400,"background":"floralwhite","height":300}],"height":300,"data":{"values":"y,x\n0.31374767344737975,0\n-0.11193791816459875,1\n-0.03734349169824358,2\n0.10302338420745105,3\n-0.11024977484304788,4\n-0.1829339894154034,5\n0.0952776858879889,6\n0.4513299579744753,7\n0.505515881626261,8\n0.829247029586699,9\n0.48601527959184054,10\n0.6100374942476102,11\n0.1102529324781345,12\n0.5986911751870085,13\n0.31733790957995334,14\n0.5325022629112035,15\n0.08194437071680616,16\n0.04444997104928439,17\n-0.18842675339713777,18\n-0.5157742332930515,19\n","format":{"type":"csv"}}});</script></div>

### Concatenation

```clojure
(-> random-walk
    (vis/hanami-vconcat
     {}
     [(vis/hanami-plot nil
                       ht/point-chart
                       {:MSIZE 400
                        :HEIGHT 100
                        :WIDTH 100})
      (vis/hanami-plot nil
                       ht/line-chart
                       {:MSIZE 4
                        :MCOLOR "brown"
                        :HEIGHT 100
                        :WIDTH 100})]))
```

<div><code>{:style {:width &quot;100%&quot;}}</code><script>vegaEmbed(document.currentScript.parentElement, {"width":400,"background":"floralwhite","vconcat":[{"encoding":{"y":{"field":"y","type":"quantitative"},"x":{"field":"x","type":"quantitative"}},"mark":{"type":"circle","size":400,"tooltip":true},"width":100,"background":"floralwhite","height":100},{"encoding":{"y":{"field":"y","type":"quantitative"},"x":{"field":"x","type":"quantitative"}},"mark":{"type":"line","size":4,"color":"brown","tooltip":true},"width":100,"background":"floralwhite","height":100}],"height":300,"data":{"values":"y,x\n0.31374767344737975,0\n-0.11193791816459875,1\n-0.03734349169824358,2\n0.10302338420745105,3\n-0.11024977484304788,4\n-0.1829339894154034,5\n0.0952776858879889,6\n0.4513299579744753,7\n0.505515881626261,8\n0.829247029586699,9\n0.48601527959184054,10\n0.6100374942476102,11\n0.1102529324781345,12\n0.5986911751870085,13\n0.31733790957995334,14\n0.5325022629112035,15\n0.08194437071680616,16\n0.04444997104928439,17\n-0.18842675339713777,18\n-0.5157742332930515,19\n","format":{"type":"csv"}}});</script></div>

```clojure
(-> random-walk
    (vis/hanami-hconcat
     {}
     [(vis/hanami-plot nil
                       ht/point-chart
                       {:MSIZE 400
                        :HEIGHT 100
                        :WIDTH 100})
      (vis/hanami-plot nil
                       ht/line-chart
                       {:MSIZE 4
                        :MCOLOR "brown"
                        :HEIGHT 100
                        :WIDTH 100})]))
```

<div><code>{:style {:width &quot;100%&quot;}}</code><script>vegaEmbed(document.currentScript.parentElement, {"hconcat":[{"encoding":{"y":{"field":"y","type":"quantitative"},"x":{"field":"x","type":"quantitative"}},"mark":{"type":"circle","size":400,"tooltip":true},"width":100,"background":"floralwhite","height":100},{"encoding":{"y":{"field":"y","type":"quantitative"},"x":{"field":"x","type":"quantitative"}},"mark":{"type":"line","size":4,"color":"brown","tooltip":true},"width":100,"background":"floralwhite","height":100}],"width":400,"background":"floralwhite","height":300,"data":{"values":"y,x\n0.31374767344737975,0\n-0.11193791816459875,1\n-0.03734349169824358,2\n0.10302338420745105,3\n-0.11024977484304788,4\n-0.1829339894154034,5\n0.0952776858879889,6\n0.4513299579744753,7\n0.505515881626261,8\n0.829247029586699,9\n0.48601527959184054,10\n0.6100374942476102,11\n0.1102529324781345,12\n0.5986911751870085,13\n0.31733790957995334,14\n0.5325022629112035,15\n0.08194437071680616,16\n0.04444997104928439,17\n-0.18842675339713777,18\n-0.5157742332930515,19\n","format":{"type":"csv"}}});</script></div>

### Linear regression

```clojure
(-> datasets/mtcars
    (stats/add-predictions :mpg [:wt]
                           {:model-type :smile.regression/ordinary-least-square})
    (vis/hanami-layers {}
                       [(vis/hanami-plot nil
                                         ht/point-chart
                                         {:X :wt
                                          :Y :mpg
                                          :MSIZE 200
                                          :HEIGHT 200
                                          :WIDTH 200})
                        (vis/hanami-plot nil
                                         ht/line-chart
                                         {:X :wt
                                          :Y :mpg-prediction
                                          :MSIZE 5
                                          :MCOLOR "purple"
                                          :YTITLE :mpg})]))
```

<div><code>{:style {:width &quot;100%&quot;}}</code><script>vegaEmbed(document.currentScript.parentElement, {"width":400,"background":"floralwhite","layer":[{"encoding":{"y":{"field":"mpg","type":"quantitative"},"x":{"field":"wt","type":"quantitative"}},"mark":{"type":"circle","size":200,"tooltip":true},"width":200,"background":"floralwhite","height":200},{"encoding":{"y":{"field":"mpg-prediction","type":"quantitative","axis":{"title":"mpg"}},"x":{"field":"wt","type":"quantitative"}},"mark":{"type":"line","size":5,"color":"purple","tooltip":true},"width":400,"background":"floralwhite","height":300}],"height":300,"data":{"values":"name,mpg,cyl,disp,hp,drat,wt,qsec,vs,am,gear,carb,mpg-prediction\nMazda RX4,21.0,6,160.0,110,3.9,2.62,16.46,0,1,4,4,23.282610646808628\nMazda RX4 Wag,21.0,6,160.0,110,3.9,2.875,17.02,0,1,4,4,21.919770395764345\nDatsun 710,22.8,4,108.0,93,3.85,2.32,18.61,1,1,4,1,24.885952118625433\nHornet 4 Drive,21.4,6,258.0,110,3.08,3.215,19.44,1,0,3,1,20.102650061038634\nHornet Sportabout,18.7,8,360.0,175,3.15,3.44,17.02,0,0,3,2,18.90014395717603\nValiant,18.1,6,225.0,105,2.76,3.46,20.22,1,0,3,1,18.79325452572158\nDuster 360,14.3,8,360.0,245,3.21,3.57,15.84,0,0,3,4,18.205362652722084\nMerc 240D,24.4,4,146.7,62,3.69,3.19,20.0,1,0,4,2,20.2362618503567\nMerc 230,22.8,4,140.8,95,3.92,3.15,22.9,1,0,4,2,20.450040713265608\nMerc 280,19.2,6,167.6,123,3.92,3.44,18.3,1,0,4,4,18.90014395717603\nMerc 280C,17.8,6,167.6,123,3.92,3.44,18.9,1,0,4,4,18.90014395717603\nMerc 450SE,16.4,8,275.8,180,3.07,4.07,17.4,0,0,3,3,15.533126866360742\nMerc 450SL,17.3,8,275.8,180,3.07,3.73,17.6,0,0,3,3,17.350247201086454\nMerc 450SLC,15.2,8,275.8,180,3.07,3.78,18.0,0,0,3,3,17.083023622450323\nCadillac Fleetwood,10.4,8,472.0,205,2.93,5.25,17.98,0,0,3,4,9.226650410547983\nLincoln Continental,10.4,8,460.0,215,3.0,5.424,17.82,0,0,3,4,8.296712356894233\nChrysler Imperial,14.7,8,440.0,230,3.23,5.345,17.42,0,0,3,4,8.71892561113933\nFiat 128,32.4,4,78.7,66,4.08,2.2,19.47,1,1,4,1,25.527288707352156\nHonda Civic,30.4,4,75.7,52,4.93,1.615,18.52,1,1,4,2,28.653804577394922\nToyota Corolla,33.9,4,71.1,65,4.22,1.835,19.9,1,1,4,1,27.47802083139593\nToyota Corona,21.5,4,120.1,97,3.7,2.465,20.01,1,0,3,1,24.111003740580646\nDodge Challenger,15.5,8,318.0,150,2.76,3.52,16.87,0,0,3,2,18.472586231358218\nAMC Javelin,15.2,8,304.0,150,3.15,3.435,17.3,0,0,3,2,18.926866315039646\nCamaro Z28,13.3,8,350.0,245,3.73,3.84,15.41,0,0,3,4,16.76235532808696\nPontiac Firebird,19.2,8,400.0,175,3.08,3.845,17.05,0,0,3,2,16.735632970223346\nFiat X1-9,27.3,4,79.0,66,4.08,1.935,18.9,1,1,4,1,26.943573674123662\nPorsche 914-2,26.0,4,120.3,91,4.43,2.14,16.7,0,1,5,2,25.847957001715514\nLotus Europa,30.4,4,95.1,113,3.77,1.513,16.9,1,1,5,2,29.198940677812637\nFord Pantera L,15.8,8,351.0,264,4.22,3.17,14.5,0,1,5,4,20.343151281811156\nFerrari Dino,19.7,6,145.0,175,3.62,2.77,15.5,0,1,5,6,22.480939910900226\nMaserati Bora,15.0,8,301.0,335,3.54,3.57,14.6,0,1,5,8,18.205362652722084\nVolvo 142E,21.4,4,121.0,109,4.11,2.78,18.6,1,1,4,2,22.427495195173\n","format":{"type":"csv"}}});</script></div>

### Histogram

```clojure
(-> datasets/iris
    (vis/hanami-histogram :sepal-width
                          {:nbins 10}))
```

<div><code>{:style {:width &quot;100%&quot;}}</code><script>vegaEmbed(document.currentScript.parentElement, {"encoding":{"y":{"field":"count","type":"quantitative"},"x":{"scale":{"zero":false},"field":"left","type":"quantitative"},"y2":{"field":0,"type":"quantitative"},"x2":{"scale":{"zero":false},"field":"right","type":"quantitative"}},"mark":"rect","width":400,"background":"floralwhite","height":300,"data":{"values":"count,left,right\n4,2.0,2.24\n7,2.24,2.48\n22,2.48,2.72\n24,2.72,2.96\n37,2.96,3.2\n31,3.2,3.4400000000000004\n10,3.4400000000000004,3.6800000000000006\n11,3.6800000000000006,3.9200000000000004\n2,3.9200000000000004,4.16\n2,4.16,4.4\n","format":{"type":"csv"}}});</script></div>

### Combining a few things together

The following is inspired by the example at Plotnine's [main page](https://plotnine.readthedocs.io/en/stable/).
Note how we add regression lines here. We take care of layout and colouring on our side, not using Vega-Lite for that.

```clojure
(let [pallete (->> :accent
                   color/palette
                   (mapv color/format-hex))]
  (-> datasets/mtcars
      (tc/group-by :gear {:result-type :as-map})
      (->> (sort-by key)
           (map-indexed
            (fn [i [group-name ds]]
              (-> ds
                  (stats/add-predictions :mpg [:wt]
                                         {:model-type :smile.regression/ordinary-least-square})
                  (tc/select-columns [:gear :wt :mpg :mpg-prediction])
                  (vis/hanami-layers {:TITLE (str "grear=" group-name)}
                                     [(vis/hanami-plot nil
                                                       ht/point-chart
                                                       {:X :wt
                                                        :Y :mpg
                                                        :MSIZE 200
                                                        :MCOLOR (pallete i)
                                                        :HEIGHT 200
                                                        :WIDTH 200})
                                      (vis/hanami-plot nil
                                                       ht/line-chart
                                                       {:X :wt
                                                        :Y :mpg-prediction
                                                        :MSIZE 5
                                                        :MCOLOR (pallete i)
                                                        :YTITLE :mpg})]
                                     ))))
           (vis/hanami-vconcat nil {}))))
```

<div><code>{:style {:width &quot;100%&quot;}}</code><script>vegaEmbed(document.currentScript.parentElement, {"width":400,"background":"floralwhite","vconcat":[{"width":400,"background":"floralwhite","title":{"text":"grear=3"},"layer":[{"encoding":{"y":{"field":"mpg","type":"quantitative"},"x":{"field":"wt","type":"quantitative"}},"mark":{"type":"circle","size":200,"color":"#7fc97f","tooltip":true},"width":200,"background":"floralwhite","height":200},{"encoding":{"y":{"field":"mpg-prediction","type":"quantitative","axis":{"title":"mpg"}},"x":{"field":"wt","type":"quantitative"}},"mark":{"type":"line","size":5,"color":"#7fc97f","tooltip":true},"width":400,"background":"floralwhite","height":300}],"height":300,"data":{"values":"gear,wt,mpg,mpg-prediction\n3,3.215,21.4,18.24575076879607\n3,3.44,18.7,17.535458674671048\n3,3.46,18.1,17.47232159963771\n3,3.57,14.3,17.125067686954367\n3,4.07,16.4,15.546640811120978\n3,3.73,17.3,16.619971086687684\n3,3.78,15.2,16.462128399104344\n3,5.25,10.4,11.821553384154186\n3,5.424,10.4,11.272260831364168\n3,5.345,14.7,11.521652277745844\n3,2.465,21.5,20.61339108254615\n3,3.52,15.5,17.282910374537707\n3,3.435,15.2,17.55124294342938\n3,3.84,13.3,16.272717174004338\n3,3.845,19.2,16.256932905246003\n","format":{"type":"csv"}}},{"width":400,"background":"floralwhite","title":{"text":"grear=4"},"layer":[{"encoding":{"y":{"field":"mpg","type":"quantitative"},"x":{"field":"wt","type":"quantitative"}},"mark":{"type":"circle","size":200,"color":"#beaed4","tooltip":true},"width":200,"background":"floralwhite","height":200},{"encoding":{"y":{"field":"mpg-prediction","type":"quantitative","axis":{"title":"mpg"}},"x":{"field":"wt","type":"quantitative"}},"mark":{"type":"line","size":5,"color":"#beaed4","tooltip":true},"width":400,"background":"floralwhite","height":300}],"height":300,"data":{"values":"gear,wt,mpg,mpg-prediction\n4,2.62,21.0,24.510455071959065\n4,2.875,21.0,22.760268076826996\n4,2.32,22.8,26.569498595643854\n4,3.19,24.4,20.59827237695797\n4,3.15,22.8,20.872811513449275\n4,3.44,19.2,18.88240277388731\n4,3.44,17.8,18.88240277388731\n4,2.2,32.4,27.39311600511777\n4,1.615,30.4,31.408250876303107\n4,1.835,33.9,29.898285625600927\n4,1.935,27.3,29.211937784372665\n4,2.78,21.4,23.412298525993847\n","format":{"type":"csv"}}},{"width":400,"background":"floralwhite","title":{"text":"grear=5"},"layer":[{"encoding":{"y":{"field":"mpg","type":"quantitative"},"x":{"field":"wt","type":"quantitative"}},"mark":{"type":"circle","size":200,"color":"#fdc086","tooltip":true},"width":200,"background":"floralwhite","height":200},{"encoding":{"y":{"field":"mpg-prediction","type":"quantitative","axis":{"title":"mpg"}},"x":{"field":"wt","type":"quantitative"}},"mark":{"type":"line","size":5,"color":"#fdc086","tooltip":true},"width":400,"background":"floralwhite","height":300}],"height":300,"data":{"values":"gear,wt,mpg,mpg-prediction\n5,2.14,26.0,25.343625025870843\n5,1.513,30.4,30.38867758620585\n5,3.17,15.8,17.05589912930775\n5,2.77,19.7,20.274433458070117\n5,3.57,15.0,13.837364800545384\n","format":{"type":"csv"}}}],"height":300});</script></div>

A similar example with histograms:

```clojure
(let [pallete (->> :accent
                   color/palette
                   (mapv color/format-hex))]
  (-> datasets/iris
      (tc/group-by :species {:result-type :as-map})
      (->> (sort-by key)
           (map-indexed
            (fn [i [group-name ds]]
              (-> ds
                  (vis/hanami-histogram :sepal-width
                                        {:nbins 10}))))
           (vis/hanami-vconcat nil {}))))
```

<div><code>{:style {:width &quot;100%&quot;}}</code><script>vegaEmbed(document.currentScript.parentElement, {"width":400,"background":"floralwhite","vconcat":[{"encoding":{"y":{"field":"count","type":"quantitative"},"x":{"scale":{"zero":false},"field":"left","type":"quantitative"},"y2":{"field":0,"type":"quantitative"},"x2":{"scale":{"zero":false},"field":"right","type":"quantitative"}},"mark":"rect","width":400,"background":"floralwhite","height":300,"data":{"values":"count,left,right\n1,2.3,2.51\n0,2.51,2.7199999999999998\n1,2.7199999999999998,2.9299999999999997\n10,2.9299999999999997,3.14\n7,3.14,3.35\n15,3.35,3.56\n6,3.56,3.7700000000000005\n6,3.7700000000000005,3.9800000000000004\n2,3.9800000000000004,4.19\n2,4.19,4.4\n","format":{"type":"csv"}}},{"encoding":{"y":{"field":"count","type":"quantitative"},"x":{"scale":{"zero":false},"field":"left","type":"quantitative"},"y2":{"field":0,"type":"quantitative"},"x2":{"scale":{"zero":false},"field":"right","type":"quantitative"}},"mark":"rect","width":400,"background":"floralwhite","height":300,"data":{"values":"count,left,right\n1,2.0,2.14\n2,2.14,2.28\n6,2.28,2.42\n4,2.42,2.56\n3,2.56,2.7\n11,2.7,2.84\n7,2.84,2.98\n11,2.98,3.12\n3,3.12,3.26\n2,3.26,3.4\n","format":{"type":"csv"}}},{"encoding":{"y":{"field":"count","type":"quantitative"},"x":{"scale":{"zero":false},"field":"left","type":"quantitative"},"y2":{"field":0,"type":"quantitative"},"x2":{"scale":{"zero":false},"field":"right","type":"quantitative"}},"mark":"rect","width":400,"background":"floralwhite","height":300,"data":{"values":"count,left,right\n1,2.2,2.3600000000000003\n4,2.3600000000000003,2.52\n2,2.52,2.68\n12,2.68,2.84\n2,2.84,3.0\n16,3.0,3.16\n8,3.16,3.3200000000000003\n2,3.3200000000000003,3.48\n1,3.48,3.6399999999999997\n2,3.6399999999999997,3.8\n","format":{"type":"csv"}}}],"height":300});</script></div>

Scatterplots and regression lines again, this time using Vega-Lite for layout and coloring (using its "facet" option).

```clojure
(-> datasets/mtcars
    (tc/group-by [:gear])
    (stats/add-predictions :mpg [:wt]
                           {:model-type :smile.regression/ordinary-least-square})
    (tc/ungroup)
    (tc/select-columns [:gear :wt :mpg :mpg-prediction])
    (vis/hanami-layers {}
                       [(vis/hanami-plot nil
                                         ht/point-chart
                                         {:X :wt
                                          :Y :mpg
                                          :MSIZE 200
                                          :COLOR "gear"
                                          :HEIGHT 100
                                          :WIDTH 200})
                        (vis/hanami-plot nil
                                         ht/line-chart
                                         {:X :wt
                                          :Y :mpg-prediction
                                          :MSIZE 5
                                          :COLOR "gear"
                                          :YTITLE :mpg})])
    ((fn [spec]
       {:facet {:row {:field "gear"}}
        :spec (dissoc spec :data)
        :data (:data spec)}))
    kind/vega-lite)
```

<div><code>{:style {:width &quot;100%&quot;}}</code><script>vegaEmbed(document.currentScript.parentElement, {"facet":{"row":{"field":"gear"}},"spec":{"width":400,"background":"floralwhite","layer":[{"encoding":{"y":{"field":"mpg","type":"quantitative"},"color":{"field":"gear","type":"nominal"},"x":{"field":"wt","type":"quantitative"}},"mark":{"type":"circle","size":200,"tooltip":true},"width":200,"background":"floralwhite","height":100},{"encoding":{"y":{"field":"mpg-prediction","type":"quantitative","axis":{"title":"mpg"}},"color":{"field":"gear","type":"nominal"},"x":{"field":"wt","type":"quantitative"}},"mark":{"type":"line","size":5,"tooltip":true},"width":400,"background":"floralwhite","height":300}],"height":300},"data":{"values":"gear,wt,mpg,mpg-prediction\n4,2.62,21.0,24.510455071959065\n4,2.875,21.0,22.760268076826996\n4,2.32,22.8,26.569498595643854\n4,3.19,24.4,20.59827237695797\n4,3.15,22.8,20.872811513449275\n4,3.44,19.2,18.88240277388731\n4,3.44,17.8,18.88240277388731\n4,2.2,32.4,27.39311600511777\n4,1.615,30.4,31.408250876303107\n4,1.835,33.9,29.898285625600927\n4,1.935,27.3,29.211937784372665\n4,2.78,21.4,23.412298525993847\n3,3.215,21.4,18.24575076879607\n3,3.44,18.7,17.535458674671048\n3,3.46,18.1,17.47232159963771\n3,3.57,14.3,17.125067686954367\n3,4.07,16.4,15.546640811120978\n3,3.73,17.3,16.619971086687684\n3,3.78,15.2,16.462128399104344\n3,5.25,10.4,11.821553384154186\n3,5.424,10.4,11.272260831364168\n3,5.345,14.7,11.521652277745844\n3,2.465,21.5,20.61339108254615\n3,3.52,15.5,17.282910374537707\n3,3.435,15.2,17.55124294342938\n3,3.84,13.3,16.272717174004338\n3,3.845,19.2,16.256932905246003\n5,2.14,26.0,25.343625025870843\n5,1.513,30.4,30.38867758620585\n5,3.17,15.8,17.05589912930775\n5,2.77,19.7,20.274433458070117\n5,3.57,15.0,13.837364800545384\n","format":{"type":"csv"}}});</script></div>

```clojure
:bye
```

<div class="printedClojure">

```clojure
:bye
```

</div>
