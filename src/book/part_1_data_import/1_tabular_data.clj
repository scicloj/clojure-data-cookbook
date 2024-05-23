^:kindly/hide-code
(ns book.part-1-data-import.1-tabular-data
  (:require [tablecloth.api :as tc]))

;; ::: {.callout-warning}
;; This is an excerpt from the work-in-progress Clojure Data Cookbook. It is under active development and subject to change in the coming months.
;; :::

;; # Tabular Data

;; If the data you're working with is already in a tabular format, it's relatively easy to create a tablecloth dataset from it. If you have any control over the data creation and publishing parts of your project, you will have a much better time if you follow the best practices outlined in [this paper about organizing data in spreadsheets](https://www.tandfonline.com/doi/full/10.1080/00031305.2017.1375989).

;; ## Summary

;; TODO

;; Further details for how to make each of these commands work are provided below.

;; ## Delimited plain-text file formats

;; If your data is saved in a plain-text format, loading it into a tablecloth dataset is supported out of the box.

;; ### CSV & TSV

;; Standard tabular file formats like `.csv `and `.tsv` Just Work:

(tc/dataset "data/co2_over_time.csv")

(tc/dataset "data/co2_over_time.tsv")

;; ### Non-standard delimeters

;; You can also specify the column separator if it is something non-standard, but note that [it must be a single character](https://github.com/techascent/tech.ml.dataset/blob/7dbda7ab2923521d298c1be3d257b3563b4f1efc/src/tech/v3/dataset/io/univocity.clj#L45). The `data/co2_over_time.txt` file looks like this:

(slurp "data/co2_over_time.txt")

;; So we can specify the `separator` option when loading the data to create our properly-formed dataset:

(tc/dataset "data/co2_over_time.txt" {:separator "/"})

;; ## Spreadsheets

;; ### Excel

;; #### .xls

;; Loading .xls files requires the `tech.v3.libs.poi` namespace to be loaded. It is not included by default by `tech.ml.dataset` because `poi` has a hard dependency on log4j2 which the core team at `tech.ml.dataset` (upon which tablecloth is built) [did not want to impose on all users by default](https://clojurians.zulipchat.com/#narrow/stream/236259-tech.2Eml.2Edataset.2Edev/topic/working.20with.20excel.20files/near/314711378).

;; Depending on your project's dependencies, you may get an error the first time you try to load the `tech.v3.libs.poi` namespace saying something like "Execution error (ClassNotFoundException) org.apache.poi.ss.usermodel.Workbook". We need to explicitly require the dependencies for working with .xls files, adding them to our `deps.edn` file:

;; ```clojure
;;  org.apache.poi/poi {:mvn/version "5.2.5"} #<1>
;;  org.apache.poi/poi-ooxml {:mvn/version "5.2.5"} #<2>
;; ```
;; 1. [search for the latest `org.apache.poi/poi` version here](http://a-link.comhttps://search.maven.org/artifact/org.apache.poi/poi)
;; 2. [search for the latest `org.apache.poi/poi-ooxml` version here](http://a-link.comhttps://search.maven.org/artifact/org.apache.poi/poi-ooxml)
;;
;; ::: {.callout-note}
;; It is important that the `poi` and `poi-ooxml` dependencies both have the same version. [Mixing versions is not supported](https://poi.apache.org/help/faq.html#faq-N10204) and will fail in unpredictable ways.
;; :::

;; You may also start seeing messages in your REPL along the lines of "SLF4J: Defaulting to no-operation (NOP) logger implementation" and/or "ERROR StatusLogger Log4j2 could not find a logging implementation" now, too, unless you already have log4j configured. Due to the dependency mentioned above, you need to make sure your project explicitly specifies a logger dependency. These messages are harmless, but if you want to get rid of them you can add the two missing logging libraries to your `deps.edn` file:

;; ```clojure
;; org.apache.logging.log4j/log4j-core {:mvn/version "2.23.1"} #<1>
;; org.slf4j/slf4j-simple {:mvn/version "2.0.13"} #<2>
;; ```
;; 1. [search for the latest `org.apache.logging.log4j/log4j-core` version here](http://a-link.comhttps://search.maven.org/artifact/org.apache.logging.log4j/log4j-core)
;; 2. [search for the latest `org.slf4j/slf4j-simple` version here](http://a-link.comhttps://search.maven.org/artifact/org.slf4j/slf4j-simple)
;;
;; Now we should finally be able to load the `tech.v3.libs.poi` namespace without any issues:

(require '[tech.v3.libs.poi :as xls])

;; And now our excel spreadsheet should load. If there's a single sheet in the workbook, tablecloth will just work.
(tc/dataset "data/example_XLS.xls")

;; With multiple sheets we get an error:
(tc/dataset "data/example_multiple_sheets_XLS.xls")

;; So we can load the file as a sequence of datasets and work with them from there. We can load them all using `xl/workbook->datasets`.
(def xls-sheets
  (xls/workbook->datasets "data/example_multiple_sheets_XLS.xls"))

;; There are also options for configuring how column names are rendered and how values are parsed. They are a subset of the arguments for creating a new dataset, [documented here](https://techascent.github.io/tech.ml.dataset/tech.v3.libs.poi.html#var-workbook-.3Edatasets).
;;
;; Now we can work with the sequence of datasets like any other Clojure seq:
(map tc/dataset-name xls-sheets)

;; Or get a single one to work on:
(first xls-sheets)

;; If, for some reason, you know you don't want to load all the sheets as datasets pre-emptively, for example if they're very large and you only want to work with one of them, you can load the workbook itself, which allows you to iterate through the sheets without parsing them. `input->workbook` returns an implementation of `tech.v3.dataset/Spreadsheet$Workbook`:

(def xls-workbook
  (xls/input->workbook "data/example_multiple_sheets_XLS.xls"))

;; The easiest way to work with this sequence of workbooks is to convert it to a Clojure seq:

(nth (iterator-seq (.iterator xls-workbook)) 2)

;; From here, we can reify a given sheet by calling `tech.v3.dataset.io.spreadsheet/sheet->dataset`:

(require '[tech.v3.dataset.io.spreadsheet :as ssheet])

(-> xls-workbook
    .iterator
    iterator-seq
    (nth 2)
    (ssheet/sheet->dataset {}))

;; #### `.xlsx`

;; Tablecloth supports .xlsx files only if the underlying Java library for working with this file format is added as a dependency and required. It's not required by default because the core team at `tech.ml.dataset `(upon which tablecloth is built) did not want to impose it and all of its dependencies on all users by default. If you want to work with excel files, you can add the dependency to your `deps.edn` file:

;;```clj
;; org.dhatim/fastexcel-reader {:mvn/version "0.16.4" :exclusions [org.apache.poi/poi-ooxml]}
;;```
;; and then we can require the library:

(require '[tech.v3.libs.fastexcel :as xlsx])

;; And now our excel spreadsheet should load. These behave the same way as `.xls` files. For completeness, specific examples are given below.

;; If there's a single sheet, tablecloth will just work:
(tc/dataset "data/example_XLSX.xlsx")

;; With multiple sheets it throws an error:
(tc/dataset "data/example_multiple_sheets_XLSX.xlsx")

;; So we can load the file as a sequence of datasets. The same options are supported for configuring how the data gets loaded [documented here](https://techascent.github.io/tech.ml.dataset/tech.v3.libs.fastexcel.html#var-workbook-.3Edatasets):
(xlsx/workbook->datasets "data/example_multiple_sheets_XLSX.xlsx")

;; If you don't want to pre-emptively create datasets for all of your sheets (for example if they're very large), you can load them lazily with `xl.input->workbook`:
(def xlsx-workbook
  (xlsx/input->workbook "data/example_multiple_sheets_XLSX.xlsx"))
;;
;; And now work with this workbook as a lazy sequence of un-reified sheets:
(-> xlsx-workbook
    .iterator
    iterator-seq
    (nth 2)
    (ssheet/sheet->dataset {}))
