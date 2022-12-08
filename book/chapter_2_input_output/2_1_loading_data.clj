(ns book.chapter-2-input-output.2-1-loading-data
  (:require
    [tablecloth.api :as tc]))

;; # 2.1 How to get data into the notebook

;; ## How to get data into the notebook

;; ### Reading from a delimited text file

;; Easiest with standard file formats, e.g. CSV.

;; #### With Clojure's standard CSV library

(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io])

(with-open [reader (io/reader "data/co2_over_time.csv")]
  (doall
   (csv/read-csv reader)))

;; Returns: Lazy sequence of vectors of strings (one value per cell)
;; TODO: Link to useful explainer on lazy seqs

;; #### With tablecloth

;; For most work involving tabular/columnar data, you'll use tablecloth, Clojure's go-to data
;; wrangling library. These all return a `tech.ml.dataset Dataset` object. The implementation
;; details aren't important now, but `tech.ml.dataset` is the library that allows for efficient
;; and fast operations on columnar datasets.

(require '[tablecloth.api :as tc])

(-> "data/co2_over_time.csv"
    tc/dataset)

;; Note the built-in pretty printing.
;; TODO: Write elsewhere about kindly and notebooks, how they know how to render different things

;; Easy things to tidy up at import time:

;; ##### Transforming headers

;; We'll require Clojure's standard string library for this example. The transformation function is
;; arbitrary though, accepting a single header value and returning a single, transformed value.

(require '[clojure.string :as str])

(defn- lower-case-keyword [val]
  (-> val
      (str/replace #"\s+" "-")
      str/lower-case
      keyword))

(-> "data/co2_over_time.csv"
    (tc/dataset {:key-fn lower-case-keyword}))

;; ##### Specifying separators

;; Tablecloth is pretty smart about standard formats, e.g. CSV above and TSV:

(-> "data/co2_over_time.tsv"
    tc/dataset)

;; But it can also accept an arbitrary separator if for some reason you have some data that uses
;; a non-standard file format (have a look at `data/co2_over_time.txt`). Note the separator has to
;; be a single character.

(-> "data/co2_over_time.txt"
    (tc/dataset {:separator "/"})
    type)

;; ##### Specify file encoding

;; TODO: does this really matter? test out different file encodings..

;; ##### Normalize values into consistent formats and types

;; Tablecloth makes it easy to apply arbitrary transformations to all values in a given column

;; We can inspect the column metadata with tablecloth:

(def dataset
  (tc/dataset "data/co2_over_time.csv"))

(-> dataset
    (tc/info :columns))

;; Certain types are built-in (it knows what to do convert them, e.g. numbers:)

(-> dataset
    (tc/convert-types "CO2" :double)
    (tc/info :columns))

;; The full list of magic symbols representing types tablecloth supports comes from the underlying
;; `tech.ml.dataset` library:
(require '[tech.v3.datatype.casting :as casting])
@casting/valid-datatype-set

;; More details on [supported types here](https://github.com/techascent/tech.ml.dataset/blob/master/topics/supported-datatypes.md).

;; You can also process multiple columns at once, either by specifying a map of columns to data types:

(-> dataset
    (tc/convert-types {"CO2" :double
                       "adjusted CO2" :double})
    (tc/info :columns))

;; Or by changing all columns of a certain type to another:

(-> dataset
    (tc/convert-types :type/numerical :double)
    (tc/info :columns))

;; The supported types of columns are:

;; :type/numerical - any numerical type
;; :type/float - floating point number (:float32 and :float64)
;; :type/integer - any integer
;; :type/datetime - any datetime type

;; Also the magical `:!type` qualifier exists, which will select the complement set -- all columns that
;; are _not_ the specified type

;; For others you need to provide a casting function yourself, e.g. adding the UTC start of day, accounting for local daylight savings

(defn to-start-of-day-UTC [local-date]
  (-> local-date
      .atStartOfDay
      (java.time.ZonedDateTime/ofLocal (java.time.ZoneId/systemDefault)
                                       (java.time.ZoneOffset/UTC))))

(-> dataset
    (tc/convert-types "Date" [[:timezone-date to-start-of-day-UTC]])
    (tc/info :columns))

;; For full details on all the possible options for type conversion of columns see the
;; [tablecloth API docs](https://scicloj.github.io/tablecloth/index.html#Type_conversion)

;; ### Reading from a URL

;; CSV:

(-> "https://vega.github.io/vega-lite/data/co2-concentration.csv"
    tc/dataset)

;; JSON: works as long as the data is an array of maps

(-> "https://vega.github.io/vega-lite/data/cars.json"
    tc/dataset)

;; Tablecloth can handle a string that points to any file that contains either raw or gzipped csv/tsv, json, xls(x), on the local file system or a URL.

;; ### Reading an excel file

;; these should work..

(tc/dataset "data/example_XLS.xls")
(tc/dataset "data/example_XLSX.xlsx")

(require '[dk.ative.docjure.spreadsheet :as xl])

(def xl-workbook
  (load-workbook "data/example_XLS.xls"))

;; To discover sheet names:

(->> xl-workbook xl/sheet-seq (map xl/sheet-name))

;; This will show us there is only one sheet in this workbook, named "Sheet1". You can get the data out of it like this:

(def data
  (->> xl-workbook
       (xl/select-sheet "Sheet1")
       xl/row-seq
       (map (fn [row]
              (->> row xl/cell-seq (map xl/read-cell))))))

data

;; and into a tablecloth dataset like this:

;; this `header-row?` option should also work
(tc/dataset data {:header-row? true})


;; ### Reading from a database
;; #### SQL database

;; #### SPARQL database
