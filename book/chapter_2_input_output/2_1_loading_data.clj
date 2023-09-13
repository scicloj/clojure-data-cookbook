(ns book.chapter-2-input-output.2-1-loading-data
  {:nextjournal.clerk/visibility {:code :hide}
   :nextjournal.clerk/toc true})

;; This is a work in progress of the code examples that will make up chapter 2, section 1
;; of the Clojure data cookbook

;; # 2.1 How to get data into the notebook

;; ## How to get data into the notebook

;; ### Reading from a delimited text file

;; Easiest with standard file formats, e.g. CSV.

;; #### With Clojure's standard CSV library

(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io])

^{:nextjournal.clerk/viewer :table}
(with-open [reader (io/reader "data/co2_over_time.csv")]
  (doall
   (csv/read-csv reader)))

;; Returns: Lazy sequence of vectors of strings (one value per cell)
;; TODO: Link to useful explainer on lazy seqs, explain why we include `doall` here

;; #### With tablecloth

;; For most work involving tabular/columnar data, you'll use tablecloth, Clojure's go-to data
;; wrangling library. These all return a `tech.ml.dataset Dataset` object. The implementation
;; details aren't important now, but `tech.ml.dataset` is the library that allows for efficient
;; and fast operations on columnar datasets.

;; TODO: Be consistent about you vs we -- pick on and stick with it

(require '[tablecloth.api :as tc])

(require '[nextjournal.clerk :as clerk])

;; (clerk/add-viewers! [{:pred #(= tech.v3.dataset.impl.dataset.Dataset (type %))
;;                       ;; :fetch-fn (fn [_ file] {:nextjournal/content-type "image/png"
;;                       ;;                         :nextjournal/value (Files/readAllBytes (.toPath file))})
;;                       :render-fn v/table}])

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
    (tc/dataset {:separator "/"}))

;; ##### Specify file encoding

;; TODO: does this really matter? test out different file encodings..

;; ##### Normalize values into consistent formats and types

;; Tablecloth makes it easy to apply arbitrary transformations to all values in a given column

;; We can inspect the column metadata with tablecloth:

(def dataset
  (tc/dataset "data/co2_over_time.csv"))

(-> dataset
    (tc/info :columns))

;; Certain types are built-in (it knows what to do to convert them, e.g. numbers:)

;; TODO: Explain why numbers get rounded? Probably not here.. in addendum about numbers in Clojure

(-> dataset
    (tc/convert-types "CO2" :double)
    (tc/info :columns))

;; The full list of magic symbols representing types tablecloth supports comes from the underlying
;; `tech.ml.dataset` library:
(require '[tech.v3.datatype.casting :as casting])
@casting/valid-datatype-set

;; More details on [supported types here](https://github.com/techascent/tech.ml.dataset/blob/master/topics/supported-datatypes.md).

;; TODO: Explain when to use :double vs :type/numerical? What’s the difference?

;; You can also process multiple columns at once, either by specifying a map of columns to data types:

(-> dataset
    (tc/convert-types {"CO2" :double
                       "adjusted CO2" :double})
    (tc/info :columns))

;; Or by changing all columns of a certain type to another:

(-> dataset
    (tc/convert-types :type/numerical :double)
    (tc/info :columns))

;; The supported column types are:

;; :type/numerical - any numerical type
;; :type/float - floating point number (:float32 and :float64)
;; :type/integer - any integer
;; :type/datetime - any datetime type

;; Also the magical `:!type` qualifier exists, which will select the complement set -- all columns that
;; are _not_ the specified type

;; For others you need to provide a casting function yourself, e.g. adding the UTC start of day,
;; accounting for local daylight savings

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

;; Tablecloth can handle a string that points to any file that contains either raw or gzipped csv/tsv,
;; json, xls(x), on the local file system or a URL.

;; ### Reading an excel file

;; Tablecloth supports reading xls and xlsx files iff the underlying Java library for working with
;; excel is included:

(require '[tech.v3.libs.poi])

;; This is not included in the library by default because `poi` has a hard dependency on log4j2, along
;; with many other dependencies that the core team at `tech.ml.dataset` (upon which tablecloth is built)
;; did not want to impose on all users by default (https://clojurians.zulipchat.com/#narrow/stream/236259-tech.2Eml.2Edataset.2Edev/topic/working.20with.20excel.20files/near/314711378).

;; You can still require it here, you'll most likely just see an error that says something like
;; "Log4j2 could not find a logging implementation. Please add log4j-core to the classpath.", unless
;; you already have a valid log4j config on your class path.

;; This should work according to maintainers, does not atm

(tc/dataset "data/example_XLS.xls" {:filetype "xls"})
(tc/dataset "data/example_XLSX.xlsx" {:filetype "xlsx"})

(require '[dk.ative.docjure.spreadsheet :as xl])

(def xl-workbook
  (xl/load-workbook "data/example_XLS.xls"))

;; To discover sheet names:

(->> xl-workbook xl/sheet-seq (map xl/sheet-name))

;; This will show us there is only one sheet in this workbook, named "Sheet1". You can get the data
;; out of it like this:

;; To discover header names:

(def headers
  (->> xl-workbook
       (xl/select-sheet "Sheet1")
       xl/row-seq
       first
       xl/cell-seq
       (map xl/read-cell)))

;; To get the data out of the columns:

(def column-index->header
  (zipmap [:A :B :C :D :E :F :G :H :I] headers))

(->> xl-workbook
     (xl/select-sheet "Sheet1")
     (xl/select-columns column-index->header))

;; and into a tablecloth dataset like this:

(->> xl-workbook
     (xl/select-sheet "Sheet1")
     (xl/select-columns column-index->header)
     (drop 1) ;; don't count the header row as a row
     tc/dataset)

;; You might be tempted to just iterate over each row and read each cell, but it's more
;; convenient to think of the data as column-based rather than row-based for tablecloth's purposes.
;; Setting the dataset headers is more verbose when we're starting from a seq of seqs, since
;; the `header-row?` option does not work for a seq of seqs (this option is implemented in the
;; low-level parsing code for each supported input type and is not currently implemented for
;; a seq of seqs).

(def iterated-xl-data
  (->> xl-workbook
       (xl/select-sheet "Sheet1")
       xl/row-seq
       (map #(->> % xl/cell-seq (map xl/read-cell)))))

;; Note the `header-row?` option is not supported:

(tc/dataset iterated-xl-data  {:header-row? true})

;; Can do it manually, but just working with columns from the start is more idiomatic:

(let [headers (first iterated-xl-data)
      rows (rest iterated-xl-data)]
  (map #(zipmap headers %) rows))

;; ### Reading from a database
;; #### SQL database

;; (tc/dataset (,,, results from some SQL query))

;; requires `com.github.seancorfield/next.jdbc {:mvn/version "1.3.847"}` in `deps.edn`

;; Note you will also require the relevant driver for the type of db you are trying
;; to access. These are some available ones:


(require '[next.jdbc :as jdbc])

;; Connect to the db:

(def db {:dbname "data/Chinook_Sqlite.sqlite"
         :dbtype "sqlite"})

(def ds (jdbc/get-datasource db))

ds

;; Pass the results of a sql query to tablecloth to make a

(-> ds
    (jdbc/execute! ["SELECT * FROM artist"])
    (tc/dataset))

;; Passing a parameter to a query

(-> ds
    (jdbc/execute! ["SELECT * FROM artist WHERE Name = ?" "Aerosmith"])
    (tc/dataset))

;; note for SQLite specifically the concat operator is `||` not `+`

(-> ds
    (jdbc/execute! ["SELECT * FROM artist WHERE Name like '%' || ? || '%'" "man"])
    (tc/dataset))

;; #### SPARQL database

(require '[grafter-2.rdf4j.repository :as repo])
(require '[grafter-2.rdf.protocols :as pr])

(def sparql (repo/sparql-repo "https://query.wikidata.org/sparql"))

;; taken from: https://query.wikidata.org/#%23Public%20sculptures%20in%20Paris%0ASELECT%20DISTINCT%20%3Fitem%20%20%3FTitre%20%3Fcreateur%20%28year%28%3Fdate%29%20as%20%3FAnneeCreation%29%20%3Fimage%20%3Fcoord%0AWHERE%0A%7B%0A%20%20%20%3Fitem%20wdt%3AP31%2Fwdt%3AP279%2a%20wd%3AQ860861.%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%23%20sculpture%0A%20%20%20%3Fitem%20wdt%3AP136%20wd%3AQ557141%20.%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%23%20genre%C2%A0%3A%20art%20public%0A%20%20%20%7B%3Fitem%20wdt%3AP131%20wd%3AQ90.%7D%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%23%20...%20situ%C3%A9e%20dans%20Paris%0A%20%20%20UNION%0A%20%20%20%7B%3Fitem%20wdt%3AP131%20%3Farr.%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%23%20...%20ou%20dans%20un%20arrondissement%20de%20Paris%20%0A%20%20%20%3Farr%20wdt%3AP131%20wd%3AQ90.%20%7D%0A%20%20%20%3Fitem%20rdfs%3Alabel%20%3FTitre%20FILTER%20%28lang%28%3FTitre%29%20%3D%20%22fr%22%29.%20%20%23%20Titre%0A%20%0A%20%20%20OPTIONAL%20%7B%3Fitem%20wdt%3AP170%20%3FQcreateur.%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%23%20cr%C3%A9ateur%2Fcr%C3%A9atrice%20%28option%29%0A%20%20%20%3FQcreateur%20rdfs%3Alabel%20%3Fcreateur%20FILTER%20%28lang%28%3Fcreateur%29%20%3D%20%22fr%22%29%20.%7D%0A%20%20%20OPTIONAL%20%7B%3Fitem%20wdt%3AP571%20%3Fdate.%7D%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%23%20date%20de%20cr%C3%A9ation%20%28option%29%0A%20%20%20OPTIONAL%20%7B%3Fitem%20wdt%3AP18%20%20%3Fimage.%7D%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%23%20image%20%28option%29%0A%20%20%20OPTIONAL%20%7B%3Fitem%20wdt%3AP625%20%3Fcoord.%7D%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%23%20coordonn%C3%A9es%20g%C3%A9ographiques%20%28option%29%0A%7D

(def sparql-results
  (let [conn (repo/->connection sparql)]
    (-> conn
        (repo/query
         "# Public sculptures in Paris
SELECT DISTINCT ?item  ?title ?creator (year(?date) as ?year) ?coord
WHERE
{
   ?item wdt:P31/wdt:P279* wd:Q860861.                    # sculpture
   ?item wdt:P136 wd:Q557141 .                            # genre : art public
   {?item wdt:P131 wd:Q90.}                               # ... située dans Paris
   UNION
   {?item wdt:P131 ?arr.                                  # ... ou dans un arrondissement de Paris
   ?arr wdt:P131 wd:Q90. }
   ?item rdfs:label ?title FILTER (lang(?title) = \"fr\").  # title

   OPTIONAL {?item wdt:P170 ?Qcreateur.                   # créateur/créatrice (option)
   ?Qcreateur rdfs:label ?creator FILTER (lang(?creator) = \"fr\") .}
   OPTIONAL {?item wdt:P571 ?date.}                       # date de création (option)
   OPTIONAL {?item wdt:P18  ?image.}                      # image (option)
   OPTIONAL {?item wdt:P625 ?coord.}                      # coordonnées géographiques (option)
}"))))

;; grafter db can help format RDF values

(def sparql-ds
  (-> sparql-results
      tc/dataset
      (tc/update-columns [:coord :title :creator] (partial map pr/raw-value))))

;; ### Generating sequences

(defn seq-of-seqs [rows cols-per-row output-generator]
  (repeatedly rows (partial repeatedly cols-per-row output-generator)))

;; Of random numbers:
(defn random-number-between-0-1000 []
  (rand-int 1000))

(seq-of-seqs 10 4 random-number-between-0-1000)

(defn seq-of-maps [rows cols-per-row output-generator]
  (let [header-data (map #(str "header-" %) (range cols-per-row))
        row-data (seq-of-seqs rows cols-per-row output-generator)]
    (map #(zipmap header-data %) row-data)))

(seq-of-maps 10 4 random-number-between-0-1000)

;; dtype next (library underneath tech.ml.dataset, which is underneath tablecloth) also
;; has a built-in sequence generator:

(require '[tech.v3.datatype :as dtype])

(dtype/make-reader :string 4 (str "cell-" idx))

(dtype/make-reader :int32 4 (rand-int 10))

;; It is lazy, not cached, so be careful about using a computationally-heavy fn for generator

;; ### Generating repeatable sequences of dummy data

(def consistent-data
  (map-indexed (fn [index _coll] (str "cell-" index))
               (range 10)))

(repeat (zipmap (range 10) consistent-data))

:end
