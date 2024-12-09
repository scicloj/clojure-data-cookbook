^:kindly/hide-code
(ns book.part-0-end-to-end.1-data-import-and-cleanup-example
  (:require
   [clojure.string :as str]
   [scicloj.hanamicloth.v1.api :as haclo]
   [scicloj.hanamicloth.v1.plotlycloth :as ploclo]
   [scicloj.tcutils.api :as tcu]
   [scicloj.tcutils.strings :as tcustr]
   [tablecloth.api :as tc]
   [tech.v3.dataset.math :as math])
  (:import
   java.time.format.DateTimeFormatter
   java.time.LocalDate))

;; # Data Import Example {#sec-data-import-example}

;; TODO: Summary

;; To build a model that predicts house prices, we need data about recent sales of similar houses. Luckily the organization that tracks all of this data in my province has released it for public use under a [very permissive open data license](https://www.pvsc.ca/sites/default/files/shared/Open%20Data%20and%20Information%20Government%20Licence%20-%20PVSC%20and%20Participating%20Municipalities.pdf), so we'll use that data to find what we're looking for.
;;
;; First we need the data about actual house sales. That's available in this [Parcel Sales History](https://www.thedatazone.ca/Assessment/Parcel-Sales-History/6a95-ppg4/about_data) dataset. Standard tabular file formats like `.csv` just work in tablecloth, so there's not much to worry about here with importing the data:

(def sales-history
  (tc/dataset "data/end_to_end/parcel_sales_history.csv"))

;; We can get a quick summary of the dataset with `tc/info`:

(tc/info sales-history)

;; The column names are capitalized strings, which is fine but it's easier and idiomatic to work with kebab-cased keywords as column-names in Clojure, so we'll update our import to transform them on load. We can use the `to-clean-keyword` helper from `tcutils` to do this. It's similar to the [`make_clean_names`](https://rdrr.io/cran/janitor/src/R/make_clean_names.R) helper from R's `janitor` package, which is commonly used when datasets are loaded to, well, clean up the column names. You can see the full implementation [here](https://github.com/scicloj/tcutils/blob/a7604cb6db3553546e974f2bb9733bb98b293d39/src/scicloj/tcutils/strings.clj#L31), but basically it removes any non-ASCII characters in strings, and then kebab-case and keywordizes them.
;;
;; Tablecloth also gives each dataset a name, which defaults to the file name. We'll set ours to something more succinct here:

(def sales-history
  (tc/dataset "data/end_to_end/parcel_sales_history.csv"
              {:key-fn tcustr/to-clean-keyword
               :dataset-name "sales-history"}))

(tc/info sales-history)

;; Here we can see that this dataset includes information about the sale of the house (e.g. address, sale date, sale price, etc), but no features of the house itself (e.g. number of bedrooms, bathrooms, garages, etc.). We could build a model that predicts sale price based solely on a house's location, but basic common sense tells us there's more that goes into the value of the home. It's not obvious exactly what the biggest determinants of price are, but anyone who has shopped for a house before knows that things like the age of the home, its size and features matter. To get this information, we'll need a second dataset: [Residential Dwelling Characteristics](https://www.thedatazone.ca/Assessment/Residential-Dwelling-Characteristics/a859-xvcs/about_data).
;;
;; We can load it the same way:

(def characteristics
  (tc/dataset "data/end_to_end/residential_dwelling_characteristics.csv"
              {:key-fn tcustr/to-clean-keyword
               :dataset-name "characteristics"}))

;; Upon inspection we can see that this dataset includes the information about houses that we're looking for, and it also as a column for "Assessment Account Number", which we should be able to use to join it up to our other one.

(tc/info characteristics)

;; There's one other piece of information that I want for our model. In Nova Scotia, it's not uncommon to own relatively large plots of land, and this can make house prices seem surprising. A small or very old house might seem like it's priced too high, for example, if you don't notice the size of the lot that it's on. We can get this information, again indexed by the Assessment Account Number, from the [Parcel Land Sizes](https://www.thedatazone.ca/Assessment/Parcel-Land-Sizes/wg22-3ric/about_data) dataset:

(def land-sizes
  (tc/dataset "data/end_to_end/parcel_land_sizes.csv"
              {:key-fn tcustr/to-clean-keyword
               :dataset-name "land-sizes"}))

(tc/info land-sizes)

;; Since loading the data is so easy here, we'll take a moment to introduce tablecloth. [Tablecloth](https://github.com/scicloj/tablecloth) is the main data manipulation library in the Clojure data science ecosystem. It provides a dataframe-like abstraction for working with data and similar operations to those found in popular data analysis libraries in other languages, like pandas in Python or dplyr in R. It provides a familiar interface for data scientists used to working with tabular data in other ecosystems while leveraging Clojure's strengths in functional programming and immutability. It's built on top of [tech.ml.dataset](https://github.com/techascent/tech.ml.dataset), Clojure's main library for high-performance data processing.
;;
;; We'll use tablecloth throughout the rest of this section to clean up our data.

;: We want a single dataset that contains only information relevant to building our model. Right now the relevant information is spread across our three datasets. To see what information we care about, first we can inspect the column names from each dataset:

(tc/column-names sales-history)
(tc/column-names characteristics)
(tc/column-names land-sizes)

;; There are methods we can use to determine which features are most relevant for our target, but for now we can use common sense to omit a few, like the map coordinates. We'll use tablecloth to join all the datasets together by assessment account number:

(def housing-data
  (-> sales-history
      (tc/select-columns [:assessment-account-number
                          :municipal-unit
                          :civic-number
                          :civic-additional
                          :civic-direction
                          :civic-street-name
                          :civic-suffix
                          :civic-city-name
                          :sale-price
                          :sale-date
                          :parcels-in-sale])
      (tc/inner-join (tc/select-columns characteristics
                                        [:assessment-account-number
                                         :living-units
                                         :year-built
                                         :square-foot-living-area
                                         :style
                                         :bedrooms
                                         :bathrooms
                                         :under-construction
                                         :construction-grade
                                         :finished-basement
                                         :garage])
                     :assessment-account-number)
      (tc/inner-join (tc/select-columns land-sizes
                                        [:assessment-account-number
                                         :square-feet])
                     :assessment-account-number)))

;; Now we have a single, combined dataset to work with. We'll do some basic data cleaning and integrity checks before saving it to work with in the next steps.

;; ## Data cleaning

;; ### Correcting datatypes

;; First, we can check that the datatypes of each column make sense:

(-> housing-data
    tc/info
    (tc/select-columns [:col-name :datatype]))

;; These mostly look good except for `:sale-date`, which should probably be either a number (if it's a year) or some date format. We'll also double check some of the string columns to make sure their values are coherent.
;;
;; First, inspecting the `:sale-date` column:

(:sale-date housing-data)

;; We can see it's strings that should be full dates. Tablecloth makes it easy to parse the strings into a supported date format. First we'll define a helper to parse these strings as dates using some of Java's time utils:

(def date-format (DateTimeFormatter/ofPattern "MMMM dd yyyy"))

(defn- to-date [s]
  (LocalDate/parse s date-format))

;; Then use tablecloth's `convert-types` to efficiently process the whole column:

(-> housing-data
    (tc/convert-types :sale-date [[:packed-local-date to-date]]))

;; Now the sale date values can be worked with as dates, which is much better than strings. Next we'll look at the remaining string columns (they're always suspicious).

(-> housing-data
    (tc/convert-types :sale-date [[:packed-local-date to-date]])
    (tc/select-columns :type/string))

;; All of the civic address columns are fine; we won't use those in our model anyway. Some of the other ones -- `:under-construction`, `:finished-basement`, and `:garage` -- are boolean columns that use "Y"/"N" right now, which is ok. It's clear and human readable, but we can easily convert these values to actual booleans, which are equally compact and human readable but more machine-friendly. This also has the added benefit of ensuring there are no bogus values, since each value must be only either `true` or `false`. Booleans are one of a [few datatypes that have a built-in coercer](https://github.com/techascent/tech.ml.dataset/blob/7dbda7ab2923521d298c1be3d257b3563b4f1efc/src/tech/v3/dataset/io/column_parsers.clj#L53), so we don't need to write any special helpers to do it ourselves. We can add this to our previous type conversion using the syntax for multiple transformations:

(-> housing-data
    (tc/convert-types {:sale-date [:packed-local-date to-date]
                       :under-construction :boolean
                       :finished-basement :boolean
                       :garage :boolean}))

;; Great. Now all of the datatypes in our dataset make sense. This was the lowest-hanging fruit in cleaning up this data. Next we'll move on reshaping the dataset to make it [tidy](https://vita.had.co.nz/papers/tidy-data.pdf).

;; ### Reshaping data

;; Next, we'll look at the `:style` column. This column is not useful in it's current state. It contains a mix of information about the type of building and the number of stories, written as a string. This will require idiosyncratic and error-prone parsing by anyone who wants to do any analysis that involves this column. These are the distinct values the column contains:

(-> housing-data
    :style
    distinct)

;; The first thing to notice here is that two of these values are not like the others. "Manufactured Home" and "Additon" are ambiguous styles. Since we're not given any information about this column in the [dataset metadata](https://www.thedatazone.ca/Assessment/Residential-Dwelling-Characteristics/a859-xvcs/about_data), we have to make some assumptions about what these values indicate. Maybe the dataset creators didn't have enough information about these homes to describe them accurately, maybe they considered them somehow fundamentally different from the other house types and in a category of their own, or maybe something else. Either way, we'll take these out-of-place values to indicate that these rows should be treated differently than the rest of the dataset.
;;
;; If we were interested in carrying out this different treatment, we could move these rows to a separate dataset, but for our purposes here we can just drop the rows, building on the transformations from the previous step:

(-> housing-data
    (tc/convert-types {:sale-date [:packed-local-date to-date]
                       :under-construction :boolean
                       :finished-basement :boolean
                       :garage :boolean})
    (tc/drop-rows #(#{"Manufactured Home" "Additon"} (:style %))))

;; Removing observations about different types of units from our dataset is one step in the right direction. To move another step closer to a tidy dataset, where each variable is a column and each observation is a row, the next thing we need to do is split up the two pieces of information in this column into two new ones. To do this we can use tablecloth's reshaping tools, specifically `separate-column` in this case.
;;
;; In order to properly handle all the possible values in the column, we'll write a new helper to parse the number of stories and building type from the list of possible `:style` strings.
;;
;; This also introduces a complicated but extremely common problem. We have to start inferring more things about the data. In other words, up to this point we have only been working with the raw data we found from the source. Splitting up this `style` column into multiple ones means making some assumptions, like what kind of building type do we assign to values that only have a number of stories? Given the context, we can assume it's a detached home, but at this point we're encoding some of our own knowledge into the dataset and it's worth being explicit about it. We'll also assume that a "Split Level" or "Split Entry" home has 2 stories.
;;
;; Based on this understanding of the data, we'll start with a little function to extract the number of stories and building type from any possible value in the `:style` column:

(defn parse-building-type [val]
  (case val
    nil nil
    "Split Level" [2 "Detached"]
    "Split Entry" [2 "Detached"]
    (let [[_ stories building-type] (re-matches #"(\d+\.?\d?) Storey\s?(.+)?" val)]
      [(parse-double stories) (or building-type "Detached")])))

;; Then we can use this helper with tablecloth's `separate-column` to generate our two new columns. We'll also bind this intermediate dataset to a new symbol to facilitate working with it in future steps:

(def partially-cleaned-housing-data
  (-> housing-data
      (tc/convert-types {:sale-date [:packed-local-date to-date]
                         :under-construction :boolean
                         :finished-basement :boolean
                         :garage :boolean})
      (tc/drop-rows #(#{"Manufactured Home" "Additon"} (:style %)))
      (tc/separate-column :style [:stories :building-type] parse-building-type)))

;; ## Data Integrity

;; Now that we've fixed up the structural or technical issues with the data, there is a whole class of new problems to deal with that have much less obvious solutions. For these next types of issues, we'll need to actually look at the data and make some judgment calls about how to handle apparent errors.

;; ### Handling missing values

;; Most

;; Get columns with missing values
(-> partially-cleaned-housing-data
    tc/info
    (tc/select-rows (comp pos? :n-missing))
    :col-name)

;; Drop any numeric column with value of 0:

(def no-missing-values
  (let [cols-missing-values [:sale-price
                             :parcels-in-sale
                             :year-built
                             :square-foot-living-area
                             :stories
                             :building-type
                             :bedrooms
                             :construction-grade
                             :square-feet]
        numeric-cols (-> partially-cleaned-housing-data
                         (tc/select-columns :type/numerical)
                         tc/column-names)]
    (-> partially-cleaned-housing-data
        (tc/drop-rows #(some (fn [col-name]
                               (nil? (col-name %)))
                             cols-missing-values))
        (tc/drop-rows #(some (fn [col-name]
                               (zero? (col-name % 0)))
                             numeric-cols)))))

;; Drop missing sale price, also drop 0 sale price, these don't provide the information we're looking for

;; ## Check for duplicate values

;; ### Removing fully duplicate rows

;; The first thing we can do is check for entirely duplicate rows. The easiest way to do this is to use `scicloj.tcutils.api/duplicate-rows`

(tcu/duplicate-rows no-missing-values)

;; In this case it turns out there are none, which is good. But it's still possible we don't want to include every row in the final dataset.

;; ### Selecting relevant rows

;; One key feature of tidy datasets, as mentioned above, is that they contain values collected about only a single type of observational unit. We're interested in building a model to predict the price of single family homes in Nova Scotia, so we'll want a dataset that contains only data about those types of homes. One clue that a dataset contains values about multiple types of things is values that get repeated in many rows.
;;
;; We can inspect our dataset to see how many rows appear to be duplicates by grouping by the assessment account number and sale date. There should only be one sale per property per day, so we will want to select any rows that have more than one and investigate these rows:

(def potentially-duplicate-row-counts
  (-> no-missing-values
      (tc/group-by [:assessment-account-number :sale-date])
      (tc/aggregate {:count tc/row-count})
      (tc/select-rows #(< 1 (:count %)))))

;; (-> partially-cleaned-housing-data
;;     (tc/map-columns :sale-year :sale-date (memfn getYear))
;;     (ploclo/layer-histogram {:=x :sale-year}))

;; TODO Explain

(let [potentially-duplicate-row-ids (-> potentially-duplicate-row-counts
                                        (tc/join-columns :id
                                                         [:assessment-account-number :sale-date]
                                                         {:result-type :seq}))]
  (-> no-missing-values
      (tc/map-columns :id [:assessment-account-number :sale-date] vector)
      (tc/inner-join potentially-duplicate-row-ids :id)
      (tc/drop-columns :id)
      (tc/order-by [:assessment-account-number :sale-date])))



;; Remove rows with more than one living unit, these aren't in the category of thing that we want to model:

(let [potentially-duplicate-row-ids (-> potentially-duplicate-row-counts
                                        (tc/join-columns :id
                                                         [:assessment-account-number :sale-date]
                                                         {:result-type :seq}))]
  (-> no-missing-values
      (tc/map-columns :id [:assessment-account-number :sale-date] vector)
      (tc/inner-join potentially-duplicate-row-ids :id)
      (tc/drop-columns :id)
      (tc/order-by [:assessment-account-number :sale-date])
      (tc/drop-rows #(> (:living-units %) 1))))

;; Leaves us with 686 rows to explain

(let [potentially-duplicate-row-ids (-> potentially-duplicate-row-counts
                                        (tc/join-columns :id
                                                         [:assessment-account-number :sale-date]
                                                         {:result-type :seq}))]
  (-> no-missing-values
      (tc/map-columns :id [:assessment-account-number :sale-date] vector)
      (tc/inner-join potentially-duplicate-row-ids :id)
      (tc/drop-columns :id)
      (tc/order-by [:assessment-account-number :sale-date])
      (tc/drop-rows #(> (:living-units %) 1))))

;; Many reasons why these remaining rows are wrong, deal with them case-by-case

;; Multiple sale prices and multiple number of parcels

;; TODO: Finish this -- revisit sanity of explaining every possible error in the data. How much cleaning is necessary to do a meaningful analysis?
;; TO DROP:
(let [potentially-duplicate-row-ids (-> potentially-duplicate-row-counts
                                        (tc/join-columns :id
                                                         [:assessment-account-number :sale-date]
                                                         {:result-type :seq}))]
  (-> no-missing-values
      (tc/map-columns :id [:assessment-account-number :sale-date] vector)
      (tc/inner-join potentially-duplicate-row-ids :id)
      (tc/drop-columns :id)
      (tc/order-by [:assessment-account-number :sale-date])
      (tc/drop-rows #(> (:living-units %) 1))
      (tc/select-columns [:assessment-account-number :sale-date :sale-price :parcels-in-sale])
      (tc/fold-by [:assessment-account-number :sale-date])
      (tc/select-rows #(and (not (apply = (:sale-price %)))
                            (not (apply = (:parcels-in-sale %)))))
      (tc/unroll [:sale-price :parcels-in-sale])
      :assessment-account-number))

(let [potentially-duplicate-row-ids (-> potentially-duplicate-row-counts
                                        (tc/join-columns :id
                                                         [:assessment-account-number :sale-date]
                                                         {:result-type :seq}))]
  (-> no-missing-values
      (tc/map-columns :id [:assessment-account-number :sale-date] vector)
      (tc/inner-join potentially-duplicate-row-ids :id)
      (tc/drop-columns :id)
      (tc/order-by [:assessment-account-number :sale-date])
      (tc/drop-rows #(> (:living-units %) 1))
      ;; (tc/select-columns [:assessment-account-number :sale-date :sale-price])
      (tc/fold-by [:assessment-account-number :sale-date])
      (tc/select-rows #(not (apply = (:sale-price %))))
      (tc/unroll [:sale-price])))

































;; ----------------------------












;; We want to select the rows that indicate potentially duplicate sales in our original dataset and see what's going on, so first we'll create a new ID column that combines the assessment account number and sale date to help us identify potentially duplicate rows, then we'll select the rows from the original dataset that match these unique ID values and examine these rows to understand what's going on with the potential duplicates.
;;
;; To efficiently filter our original dataset by the new ID column we're adding, we can use an inner-join, to only include rows from the target dataset that have an id in our source dataset. First we'll create the same composite ID column on our original dataset, then join it with the one the one containing our IDs of interest to efficiently filter by these IDs.

(def duplicate-rows
  (let [potentially-duplicate-row-ids (-> potentially-duplicate-row-counts
                                          (tc/join-columns :id
                                                           [:assessment-account-number :sale-date]
                                                           {:result-type :seq}))]
    (-> partially-cleaned-housing-data
        (tc/map-columns :id [:assessment-account-number :sale-date] vector)
        (tc/inner-join potentially-duplicate-row-ids :id)
        (tc/drop-columns :id)
        (tc/order-by :assessment-account-number))))

;; We can check that this did what we expected by summing up the counts from our potentially duplicate sales dataset. It should equal the number of rows in our filtered original dataset:

(reduce + (:count potentially-duplicate-row-counts))

(tc/row-count duplicate-rows)

;; Now we need to handle these duplicate rows. We can just blindly delete them, but it's better to try to understand what's going on, in case there's some valid reason for them to be duplicated, or something else we're misunderstanding about our data. Eyeballing the data reveals some interesting things, like that some rows have a `living-units` value that isn't 1 and some have multiple different `year-built` values for the same unit, but some visualisations will help to reveal more about the data.

duplicate-rows

;; We'll use [hanamicloth](https://github.com/scicloj/hanamicloth) for our data visualisation. First we can check out the distribution of living unit sizes:

(-> duplicate-rows
    (ploclo/layer-histogram {:=x :living-units})
    )




(-> partially-cleaned-housing-data
    (tc/unique-by [:assessment-account-number :sale-date])
    tc/row-count
 )



;; clean up and steps to turn this data into [tidy data]() before saving it to work with in the next steps.
;;
;; First, we can




;; If we inspect this new, combined dataset we can see there are quite a few sparse columns:

(-> housing-data
    tc/info
    (tc/select-rows #(< 0 (:n-missing %)))
    (tc/select-columns [:col-name :n-missing]))

;; Some of these were columns that were in more than one of the original dataset and in combining them, by taking only the value from the first dataset, we may have lost some information. We can tell this by checking the descriptive stats of one of the other datasets, which shows different numbers of missing values:

(-> characteristics
    tc/info
    (tc/select-rows #(< 0 (:n-missing %)))
    (tc/select-columns [:col-name :n-missing]))

;; In this case there are more missing values in the `characteristics` dataset than in the combined one, so it may be the case that there isn't actually any extra information in the characteristics dataset, but to be sure we can check. To do that we'll combine the three datasets, then check the columns we care about for any rows where the value from the first dataset is `nil`, but either of the other two have a value.
;;
;; First, to simplify some of this code we'll define the list of columns the datasets share:

(def common-columns
  [:assessment-account-number
   :municipal-unit
   :civic-number
   :civic-additional
   :civic-direction
   :civic-street-name
   :civic-suffix
   :civic-city-name])

;; Then we can join the datasets together, keeping the duplicated columns this time. Tablecloth will default to disambiguating the duplicated column names by using the dataset names, which works fine in this case:

(def combined-potentially-duplicated-columns
  (-> sales-history
      (tc/select-columns common-columns)
      (tc/inner-join (tc/select-columns characteristics common-columns)
                     :assessment-account-number)
      (tc/inner-join (tc/select-columns land-sizes common-columns)
                     :assessment-account-number)))

;; Now we can check whether any of the other datasets have information for the missing values. For the sake of demonstration we'll check the `:municipal-unit` column first:

(-> combined-potentially-duplicated-columns
    (tc/select-rows (fn [row]
                      (let [v1 (:municipal-unit row)
                            v2 (:characteristics.municipal-unit row)
                            v3 (:land-sizes.municipal-unit row)]
                        (and (nil? v1)
                             (not (nil? (or v2 v3))))))))

;; It turns out there are actually no rows that have a missing value for `:municipal-unit` in the `sales-history` dataset but do have a value in either of the other two datasets. To check the rest of the columns more quickly we can extract our predicate into a helper and have it accept a column name as an argument. This function will take a row and a column name and return `true` if our primary dataset has no value in the given row and column and either of the secondary datasets contain do have a value:

(defn- non-nil-in-secondary-datasets? [row col-name]
  (let [v1 (col-name row)
        v2 ((->> col-name name (str "characteristics.") keyword) row)
        v3 ((->> col-name name (str "land-sizes.") keyword) row)]
    (and (nil? v1)
         (not (nil? (or v2 v3))))))

;; And it order to check all the column names at ones, we can run this check over a given list of column names we want to check:

;; (defn- check-for-values-in-secondary-datasets [ds column-names]
;;   (reduce (fn [ds col-namel]
;;             ()
;;             ds
;;             column-names)))

;; (defn secondary-datsets-have-missing-data [column-names row]
;;   (some identity
;;         (reduce (fn [row col-name]
;;                   (non-nil-in-secondary-datasets? row col-name))
;;                 row
;;                 column-names)))

;; (-> combined-potentially-duplicated-columns
;;     (tc/select-rows (partial secondary-datsets-have-missing-data common-columns)))



;; Now we'll go through and try to fill in as much information as possible. We can start by filtering out any rows that do not have missing values in our base dataset in the columns we're trying to fill in. To keep our processing pipeline neat, we'll write a simple helper that will return true for rows that fit our criteria:



;; (defn resolve [v1 v2 v3]
;;   ;; (some identity args)
;;   (or v1 v2 v3)
;;   )


;; (defn consolidate-columns [ds column-names]
;;   (reduce (fn [ds col-name]
;;             (let [characteristics-col-name (->> col-name name (str "characteristics.") keyword)
;;                   land-sizes-col-name (->> col-name name (str "land-sizes.") keyword)]
;;               (-> ds
;;                   (tc/map-columns col-name
;;                                   [col-name characteristics-col-name land-sizes-col-name]
;;                                   resolve)
;;                   (tc/drop-columns [characteristics-col-name land-sizes-col-name]))))
;;           ds
;;           column-names))

;; (-> combined-potentially-duplicated-columns
;;     ;; (tc/head 4)
;;     (consolidate-columns common-columns)
;;     tc/info
;;     (tc/select-rows #(< 0 (:n-missing %)))
;;     (tc/select-columns [:col-name :n-missing]))

;; (defn no-missing-info)

;; (-> combined-potentially-duplicated-columns
;;     (tc/head 5)
;;     (tc/drop-rows (fn [row]
;;                     (every? identity
;;                             (map (fn [col-name]
;;                                    (not (nil? (col-name row))))
;;                                  ))))
;;     ;; tc/row-count
;;     )

;; that do not have any differences across the three datasets. This is going to be verbose to inline so we'll write a small helper:

;; looking for rows where there _is_ a missing value in the main dataset but there _is_ a value for that column in another dataset

;; filter out rows where all three datasets agree

(defn datasets-concur? [row col]
  (println "*******************")
  (println (col row))
  (println ((->> col name (str "characteristic.") keyword) row))
  (println ((->> col name (str "land-sizes.") keyword) row))
  (println "*******************")

  (= (col row)
     ((->> col name (str "characteristic.") keyword) row)
     ((->> col name (str "land-sizes.") keyword) row)))

(defn datasets-same? [row col]
  (let [v1 (col row)
        v2 ((->> col name (str "characteristic.") keyword) row)
        v3 ((->> col name (str "land-sizes.") keyword) row)]
    ;; either all values are the same
    ;; (or
    ;;  (-> [v1 v2 v3] set count (= 1)))
    (->> [v1 v2 v3] set (remove nil?) count (= 1))))

(defn no-discrepancies [row]
  (every? identity
          (map (partial datasets-concur? row)
           [:municipal-unit
            :civic-number
            :civic-additional
            :civic-direction
            :civic-street-name
            :civic-suffix
            :civic-city-name])))

(defn has-discrepancy? [row col-name]
  ;; returns true if values differ and are not nil, otherwise false
  (let [v1 (col-name row)
        v2 ((->> col-name name (str "characteristic.") keyword) row)
        v3 ((->> col-name name (str "land-sizes.") keyword) row)]

    (->> [v1 v2 v3] set (remove nil?) count (<= 2))))

(defn with-discrepancies [row]
  (some identity (map (partial has-discrepancy? row) common-columns))
  )

(-> combined-potentially-duplicated-columns
    ;; (tc/head 100)
    ;; (tc/drop-rows no-discrepancies)
    (tc/select-rows with-discrepancies)

    ;; (tc/select-rows #(not= (:municipal-unit %)
    ;;                        ((->> :municipal-unit
    ;;                              name
    ;;                              (str "characteristic.")
    ;;                              keyword) %)
    ;;                        ((->> :municipal-unit
    ;;                              name
    ;;                              (str "land-sizes.")
    ;;                              keyword) %)))
    ;; tc/row-count

    )

combined-potentially-duplicated-columns





(->
  (tc/info characteristics)
 (tc/select-rows #(#{:municipal-unit
                     :civic-number
                     :civic-additional
                     :civic-direction
                     :civic-street-name
                     :civic-suffix
                     :civic-city-name}
                    (:col-name %)))
(tc/select-columns [:col-name :n-missing]))

;; (-> housing-data
;;     (haclo/layer-point #:haclo{:x :square-foot-living-area
;;                                :y :sale-price
;;                                :xscale {:type "log"}}))



;; Contains information licensed under the Open Data & Information Government Licence – PVSC & Participating Municipalities


;; Now that our dataset is assembled and cleaned up, I'll write it to a new file for easier access in the next steps.

(tc/write-nippy! housing-data "data/end_to_end/housing-data.nippy")
