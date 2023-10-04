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

```clojure
(ns index
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [scicloj.kindly.v4.kind :as kind]))
```

<div class="printedClojure">

```clojure
nil
```

</div>

# Preface

Welcome to the Clojure Data Cookbook! This is the website for the work-in-progress that will become the Clojure Data Cookbook. The goal is to provide a reference for anyone who has data to work with and an interest in doing it in Clojure, documenting the current community recommendations and default stack for data science in Clojure.

## Note! all work here is in progress, subject to change, very messy, and partially done. Please bear with me as I work on through this project :D

```clojure
^:kindly/hide-code?
(let [files (->> (file-seq (io/file "./book"))
                 (filter (fn [file] (or (str/ends-with? file ".clj")
                                        (str/ends-with? file ".md"))))
                 (remove #(str/includes? % "experiments"))
                 (map #(str/replace % #"\.\/" ""))
                 (remove #{"book/index.clj"})
                 sort
                 (map #(-> %
                           (str/replace #"\.md$" ".html")
                           (str/replace #"\.clj$" "")
                           (str/replace #"^book/" "")))
                 (group-by (fn [el] (-> el (str/split #"\/") first))))]
  (kind/hiccup
   [:div [:h2 "Contents"]
    (for [[chapter_name pages] files]
      [:section [:h3 chapter_name]
       (for [page pages]
         [:li [:a {:href page} (-> page str/capitalize)]])])]))
```

<code>[:div [:h2 &quot;Contents&quot;] ([:section [:h3 &quot;chapter-3-data-manipulation&quot;] ([:li [:a {:href &quot;chapter-3-data-manipulation/3-data-manipulation.html&quot;} &quot;Chapter-3-data-manipulation/3-data-manipulation.html&quot;]])] [:section [:h3 &quot;chapter-4-data-visualisation&quot;] ([:li [:a {:href &quot;chapter-4-data-visualisation/4-2-graphs.html&quot;} &quot;Chapter-4-data-visualisation/4-2-graphs.html&quot;]] [:li [:a {:href &quot;chapter-4-data-visualisation/noj-examples.html&quot;} &quot;Chapter-4-data-visualisation/noj-examples.html&quot;]])] [:section [:h3 &quot;chapter_1_intro&quot;] ([:li [:a {:href &quot;chapter_1_intro/1_1_welcome.html&quot;} &quot;Chapter_1_intro/1_1_welcome.html&quot;]] [:li [:a {:href &quot;chapter_1_intro/1_2_why_clojure.html&quot;} &quot;Chapter_1_intro/1_2_why_clojure.html&quot;]] [:li [:a {:href &quot;chapter_1_intro/1_3_set_up.html&quot;} &quot;Chapter_1_intro/1_3_set_up.html&quot;]])] [:section [:h3 &quot;chapter_2_input_output&quot;] ([:li [:a {:href &quot;chapter_2_input_output/2_1_loading_data&quot;} &quot;Chapter_2_input_output/2_1_loading_data&quot;]] [:li [:a {:href &quot;chapter_2_input_output/2_2_messy_data&quot;} &quot;Chapter_2_input_output/2_2_messy_data&quot;]] [:li [:a {:href &quot;chapter_2_input_output/2_3_exporting_data&quot;} &quot;Chapter_2_input_output/2_3_exporting_data&quot;]])] [:section [:h3 &quot;chapter_3_data_manipulation&quot;] ([:li [:a {:href &quot;chapter_3_data_manipulation/3_data_manipulation&quot;} &quot;Chapter_3_data_manipulation/3_data_manipulation&quot;]])] [:section [:h3 &quot;chapter_4_data_visualisation&quot;] ([:li [:a {:href &quot;chapter_4_data_visualisation/4_2_graphs&quot;} &quot;Chapter_4_data_visualisation/4_2_graphs&quot;]] [:li [:a {:href &quot;chapter_4_data_visualisation/4_2_graphs.html&quot;} &quot;Chapter_4_data_visualisation/4_2_graphs.html&quot;]] [:li [:a {:href &quot;chapter_4_data_visualisation/noj_examples&quot;} &quot;Chapter_4_data_visualisation/noj_examples&quot;]] [:li [:a {:href &quot;chapter_4_data_visualisation/noj_examples.html&quot;} &quot;Chapter_4_data_visualisation/noj_examples.html&quot;]])])]</code>

## Recommended sections

[randomizing order](chapter_3_data_manipulation/3_data_manipulation#randomizing-order)
