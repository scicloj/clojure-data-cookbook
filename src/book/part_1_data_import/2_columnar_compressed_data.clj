^:kindly/hide-code
(ns book.part-1-data-import.2-columnar-compressed-data
  (:require
   [tablecloth.api :as tc]))

;; ::: {.callout-warning}
;; This is an excerpt from the work-in-progress Clojure Data Cookbook. It is under active development and subject to change in the coming months.
;; :::

;; # Columnar and Compressed Data

;; TODO - write intro

;; ## Summary

;; ## Nippy

;; Nippy is "the fastest serialization library for Clojure". Tablecloth datasets work with nippy out of the box.

;; Unlike edn
;; Edn doesn't handle binary data, doesn't always serialize certain datatypes in a round-trippable way (i.e. reading the file back in )

;; writing edn is worse than not threadsafe

;; writing prstr under the hood, and if any other thread tries to write to stdout at the same time,
;; stdout is not threadsafe

;; https://github.com/jafingerhut/jafingerhut.github.com/blob/master/clojure-info/using-edn-safely.md
;; https://nitor.com/fi/artikkelit/pitfalls-and-bumps-clojures-extensible-data-notation-edn

;; https://techascent.github.io/tech.ml.dataset/nippy-serialization-rocks.html

;; Tablecloth can just read nippy datasets directly. No extra dependencies are required.

(tc/dataset "data/tc-nippy.nippy")


;; ## Parquet

;; Working with parquet files requires a pile of dependencies. There is a very long and carefully chosen list of exclusions curated to avoid a [myriad of CVE issues in hadoop](https://clojurians.zulipchat.com/#narrow/stream/236259-tech.2Eml.2Edataset.2Edev/topic/Arrow.3A.20dataset-.3Estream!.20.26.20metadata/near/300962808) that are conveniently packaged up in the [`tech.parquet` package](https://github.com/techascent/tech.parquet). We'll add this to our `deps.edn`:

```clojure
com.techascent/tmd-parquet {:mvn/version "1.000-beta-39"}

 ```

;; Once this dependency is installed and the relevant namespace is required, tablecloth (thanks to tech.ml.dataset) will handle parquet files like any other file type:

(require '[tech.v3.libs.parquet :as pq])

;; Parquet support includes the following datatypes:
;; - all numeric types
;; - strings
;; - java.time LocalDate, Instant
;; - UUIDs (read/written as strings, the same as R's write_parquet function)

;; Given a path to a single parquet file, we get a single dataset:
(pq/parquet->ds "data/columnar_data/mtcars.parquet")

;; We can pass the `key-fn` option (like with any other file type) to format column headers on read:
(pq/parquet->ds "data/columnar_data/mtcars.parquet" {:key-fn keyword})

;; We can also use the `:column-allowlist` or `:column-blocklist` options to load only certain columns. This can be useful if the dataset is very large and you know in advance that you only need a subset of the columns. Note that the column header transformations are applied __before__ the column selection, so the column selection should use the transformed column names:
(pq/parquet->ds "data/columnar_data/mtcars.parquet"
                {:key-fn keyword
                 :column-allowlist [:mpg, :cyl]})

;; As opposed to this, which returns nothing because there are no columns with the string names "mpg" or "cyl", because of the key-fn option we passed.
(pq/parquet->ds "data/columnar_data/mtcars.parquet"
                {:key-fn keyword
                 :column-allowlist ["mpg", "cyl"]})

;; We can also inspect the metadata of a parquet file. `pq/parquet->metadata-seq` returns a sequence of maps, each containing the metadata of one row group in the dataset.
(pq/parquet->metadata-seq "data/columnar_data/mtcars.parquet")
