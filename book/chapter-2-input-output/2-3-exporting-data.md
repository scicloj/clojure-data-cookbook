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
(ns chapter-2-input-output.2-3-exporting-data
  {:nextjournal.clerk/toc true}
  (:require
   [clojure.data.csv :as csv]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [nextjournal.clerk :as clerk]
   [tablecloth.api :as tc]
   [scicloj.kind-clerk.api :as kind-clerk]))
```

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

# How to get data out of a notebook

```clojure
(def consistent-data
  (map-indexed (fn [index _coll] (str "cell-" index))
               (range 10)))
```

<div class="printedClojure">

```clojure
"#'chapter-2-input-output.2-3-exporting-data/consistent-data"
```

</div>

```clojure
(def data (take 20 (repeat (zipmap (range 10) consistent-data))))
```

<div class="printedClojure">

```clojure
"#'chapter-2-input-output.2-3-exporting-data/data"
```

</div>

## Writing to a CSV file

depends what the data looks like
for a seq of maps:

headers are not necessarily sorted, put them in whatever order you want here
Clojure maps make no guarantees about key order, make sure to order values,
i.e. use the same header row to get the values from each map

```clojure
(let [headers (-> data first keys sort)
      rows (->> data (map (fn [row]
                            (map (fn [header]
                                   (get row header)) headers))))]
  (with-open [writer (io/writer "data/csv-output.csv")]
    (csv/write-csv writer (cons headers rows))))
```

<div class="printedClojure">

```clojure
nil
```

</div>

Tablecloth can also export csvs (among other formats)

```clojure
(def tc-dataset (tc/dataset data))
```

<div class="printedClojure">

```clojure
"#'chapter-2-input-output.2-3-exporting-data/tc-dataset"
```

</div>

```clojure
(tc/write-csv! tc-dataset "data/tc-output.csv")
```

<div class="printedClojure">

```clojure
21
```

</div>

## Writing nippy

```clojure
(tc/write! tc-dataset "data/tc-nippy.nippy")
```

<div class="printedClojure">

```clojure
nil
```

</div>

Read this also with tablecloth:

```clojure
(tc/dataset "data/tc-nippy.nippy")
```

<div><div>Unimplemented: <code>:kind/dataset</code></div><code>data/tc-nippy.nippy [20 10]:

|      0 |      7 |      1 |      4 |      6 |      3 |      2 |      9 |      5 |      8 |
|--------|--------|--------|--------|--------|--------|--------|--------|--------|--------|
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
| cell-0 | cell-7 | cell-1 | cell-4 | cell-6 | cell-3 | cell-2 | cell-9 | cell-5 | cell-8 |
</code></div>

## Leave data in Clojure files

```clojure
(->> data pr-str (spit "data/clojure-output.edn"))
```

<div class="printedClojure">

```clojure
nil
```

</div>

This can be consumed later with:

```clojure
(with-open [reader (io/reader "data/clojure-output.edn")]
  (edn/read (java.io.PushbackReader. reader)))
```

<div class="printedClojure">

```clojure
({0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"}
 {0 "cell-0",
  7 "cell-7",
  1 "cell-1",
  4 "cell-4",
  6 "cell-6",
  3 "cell-3",
  2 "cell-2",
  9 "cell-9",
  5 "cell-5",
  8 "cell-8"})
```

</div>

## Notebook artifacts

Clerk supports publishing your namespaces as HTML (like this website!)
To do that call

```clojure
(comment
  (clerk-setup/build! {:paths "path/to/files..."
                 :index       "book/index.clj"}))
```

<div class="printedClojure">

```clojure
nil
```

</div>

More information in Clerk's docs: https://book.clerk.vision/#static-building

HTML pages
Other formats, options for exporting notebooks? PDFs?
Partial artifacts, e.g. export just a graph
Writing to a database?
