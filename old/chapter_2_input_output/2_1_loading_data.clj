(ns chapter-2-input-output.2-1-loading-data)

;; ;; Note the built-in pretty printing.
;; ;; TODO: Write elsewhere about kindly and notebooks, how they know how to render different things

;; ;; Easy things to tidy up at import time:

;; ;; ##### Transforming headers

;; ;; We'll require Clojure's standard string library for this example. The transformation function is
;; ;; arbitrary though, accepting a single header value and returning a single, transformed value.

;; (require '[clojure.string :as str])

;; (defn- lower-case-keyword [val]
;;   (-> val
;;       (str/replace #"\s+" "-")
;;       str/lower-case
;;       keyword))

;; (-> "data/co2_over_time.csv"
;;     (tc/dataset {:key-fn lower-case-keyword}))

;; ;; ##### Specifying separators

;; ;; Tablecloth is pretty smart about standard formats, e.g. CSV above and TSV:

;; (-> "data/co2_over_time.tsv"
;;     tc/dataset)

;; ;; But it can also accept an arbitrary separator if for some reason you have some data that uses
;; ;; a non-standard file format (have a look at `data/co2_over_time.txt`). Note the separator has to
;; ;; be a single character.



;; ;; ##### Specify file encoding

;; ;; TODO: does this really matter? test out different file encodings..

;; ;; ##### Normalize values into consistent formats and types

;; ;; Tablecloth makes it easy to apply arbitrary transformations to all values in a given column

;; ;; We can inspect the column metadata with tablecloth:

;; (def dataset
;;   (tc/dataset "data/co2_over_time.csv"))

;; (-> dataset
;;     (tc/info :columns))

;; ;; Certain types are built-in (it knows what to do to convert them, e.g. numbers:)

;; ;; TODO: Explain why numbers get rounded? Probably not here.. in addendum about numbers in Clojure

;; (-> dataset
;;     (tc/convert-types "CO2" :double)
;;     (tc/info :columns))

;; ;; The full list of magic symbols representing types tablecloth supports comes from the underlying
;; ;; `tech.ml.dataset` library:
;; (require '[tech.v3.datatype.casting :as casting])
;; @casting/valid-datatype-set

;; ;; More details on [supported types here](https://github.com/techascent/tech.ml.dataset/blob/master/topics/supported-datatypes.md).

;; ;; TODO: Explain when to use :double vs :type/numerical? What’s the difference?

;; ;; You can also process multiple columns at once, either by specifying a map of columns to data types:

;; (-> dataset
;;     (tc/convert-types {"CO2" :double
;;                        "adjusted CO2" :double})
;;     (tc/info :columns))

;; ;; Or by changing all columns of a certain type to another:

;; (-> dataset
;;     (tc/convert-types :type/numerical :double)
;;     (tc/info :columns))

;; ;; The supported column types are:

;; ;; :type/numerical - any numerical type
;; ;; :type/float - floating point number (:float32 and :float64)
;; ;; :type/integer - any integer
;; ;; :type/datetime - any datetime type

;; ;; Also the magical `:!type` qualifier exists, which will select the complement set -- all columns that
;; ;; are _not_ the specified type

;; ;; For others you need to provide a casting function yourself, e.g. adding the UTC start of day,
;; ;; accounting for local daylight savings

;; (defn to-start-of-day-UTC [local-date]
;;   (-> local-date
;;       .atStartOfDay
;;       (java.time.ZonedDateTime/ofLocal (java.time.ZoneId/systemDefault)
;;                                        (java.time.ZoneOffset/UTC))))

;; (-> dataset
;;     (tc/convert-types "Date" [[:timezone-date to-start-of-day-UTC]])
;;     (tc/info :columns))

;; ;; For full details on all the possible options for type conversion of columns see the
;; ;; [tablecloth API docs](https://scicloj.github.io/tablecloth/index.html#Type_conversion)


;; ;; ### Generating sequences

;; (defn seq-of-seqs [rows cols-per-row output-generator]
;;   (repeatedly rows (partial repeatedly cols-per-row output-generator)))

;; ;; Of random numbers:
;; (defn random-number-between-0-1000 []
;;   (rand-int 1000))

;; (seq-of-seqs 10 4 random-number-between-0-1000)

;; (defn seq-of-maps [rows cols-per-row output-generator]
;;   (let [header-data (map #(str "header-" %) (range cols-per-row))
;;         row-data (seq-of-seqs rows cols-per-row output-generator)]
;;     (map #(zipmap header-data %) row-data)))

;; (seq-of-maps 10 4 random-number-between-0-1000)

;; ;; dtype next (library underneath tech.ml.dataset, which is underneath tablecloth) also
;; ;; has a built-in sequence generator:

;; (require '[tech.v3.datatype :as dtype])

;; (dtype/make-reader :string 4 (str "cell-" idx))

;; (dtype/make-reader :int32 4 (rand-int 10))

;; ;; It is lazy, not cached, so be careful about using a computationally-heavy fn for generator

;; ;; ### Generating repeatable sequences of dummy data

;; (def consistent-data
;;   (map-indexed (fn [index _coll] (str "cell-" index))
;;                (range 10)))

;; (repeat (zipmap (range 10) consistent-data))

;; :end
