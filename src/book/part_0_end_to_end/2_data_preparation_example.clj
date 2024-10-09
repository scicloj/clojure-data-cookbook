(ns book.part-0-end-to-end.2-data-preparation-example
  (:require
   [clojure.string :as str]
   [tablecloth.api :as tc]))

;; # Data Transformation Example {#sec-data-transformation-example}

;; Now that we have a "clean" dataset to work with, we'll go through the necessary steps to prepare it for use in training a model. In the previous chapter, we tidied up our dataset in many important ways:
;; - Structured format: We organized the data in a consistent, tabular format, with one column per variable and one row per observation

;; A "machine learning-ready" dataset has a few key characteristics that distinguish it from a more typical dataset you might find online, even one that is well-organized and clean:

;; - Properly handle missing values: We need to either remove, impute, or otherwise mark missing values in such way that they can be handled by our algorithms. The strategy for handling missing values needs to be consistent across the dataset.
;; - Correcting datatypes: Most algorithms require numeric values. We need to make sure our continuous variables have a valid numeric type

;; - Normalizing scales
;; - Handling outliers

;; A dataset suitable for machine learning has several key characteristics that distinguish it from a typical dataset you might find online:

;; Structured format:

;; Typically organized in a tabular format (rows and columns)
;; Consistent structure across all entries
;; Clear delineation between features (input variables) and target variables (what you're trying to predict)


;; Sufficient volume:

;; Generally larger than conventional datasets
;; Enough samples to capture patterns and variability in the data


;; Quality and cleanliness:

;; Minimal errors or inconsistencies
;; Properly labeled data, especially for supervised learning tasks


;; Representative:

;; Covers the full range of scenarios or cases the model might encounter
;; Balanced representation of different classes or outcomes


;; Relevant features:

;; Contains variables that are likely to be predictive of the target variable
;; Excludes irrelevant or redundant information


;; Appropriate data types:

;; Numerical data for continuous variables
;; Categorical data properly encoded (e.g., one-hot encoding)
;; Consistent data types within each feature


;; Normalized or standardized:

;; Numerical features often scaled to a common range
;; Helps prevent certain features from dominating due to their scale


;; Time-aware (for time-series data):

;; Proper chronological ordering
;; No data leakage from future to past


;; Split-ready:

;; Can be easily divided into training, validation, and test sets
;; These splits maintain the statistical properties of the whole dataset


;; Metadata and documentation:

;; Clear descriptions of each feature
;; Information on data collection methods and any preprocessing steps


;; Ethical considerations:

;; Respects privacy and data protection regulations
;; Unbiased representation of different demographic groups


;; Versioning:

;; Often includes version control to track changes and updates


;; Noise and outliers:

;; Contains a realistic level of noise to prevent overfitting
;; Outliers are identified and handled appropriately

(def housing-data
  (tc/dataset "data/end_to_end/housing-data.nippy"))

;; ## Select relevant features

;; List all column names

(tc/column-names housing-data)

;; Remove all the address columns, these won't be useful for training our model.

(defn remove-address-columns [ds]
  (-> ds
      (tc/drop-columns #(str/includes? (name %) "civic"))))

;; (tc/select-columns #(not (str/starts-with? (name %) "civic")))

(remove-address-columns housing-data)

;; ## Handle missing values

;; Most machine learning algorithms can't handle missing values, so we need to decide how to deal with them before training our model.

;; TODO: write about missing values

;; Get the columns that have missing values

;; (-> housing-data
;;     remove-address-columns
;;     tc/info
;;     (tc/select-rows #(pos? (:n-missing %))))

;; (-> housing-data
;;     (tc/select-rows #((some-fn nil? zero?) (:sale-price %)))
;;     (tc/group-by [:municipal-unit])
;;     (tc/aggregate {:count tc/row-count})
;;     (tc/order-by :count :desc))

;; A quick survey shows us that the rows with missing values are almost all in the HRM, which makes it easier to feel ok about dropping all of this data. There will be a huge overabundance of data from the HRM, since that's where most of the houses are. If we were losing all of our data from a given small municipality, for example, we'd want to figure something out.
;;
;; Deal with missing values. I'm just going to drop them, there are techniques we could use to fill in the missing values, but we have enough data here that it's not worth the downsides.

;; (defn drop-rows-missing-values [ds]
;;   (let [columns-with-missing-values (-> ds
;;                                         tc/info
;;                                         (tc/select-rows #(pos? (:n-missing %)))
;;                                         :col-name)]
;;     (reduce
;;       (fn [ds col-name]
;;         (tc/drop-rows ds #((some-fn nil? zero?) (col-name %))))
;;       ds
;;      columns-with-missing-values)))

;; (-> housing-data
;;     remove-address-columns
;;     drop-rows-missing-values
;;     tc/row-count)

;; (->> housing-data
;;      :year-built
;;      frequencies
;;      (sort-by first))


;; There are different approaches to cleaning up data. One is to go by category, e.g. drop all the missing values, convert all the non-numeric values, etc. But realistically we want to be more careful and clean up each column within the context of the other values in the column.

;; (-> housing-data
;;     )

;; (-> housing-data
;;     (tc/select-rows #(nil? (:construction-grade %)))
;; )

;; (def date-format (DateTimeFormatter/ofPattern "MMMM dd yyyy"))

;; (defn- to-date [s]
;;   )

;; (defn to-year [s]
;;   ((memfn getYear) (LocalDate/parse s date-format)))

;; (defn- y-n-to-int [v]
;;   (if (= v "Y") 1 0))

;; We can use a simple linear model to fill in the missing values for construction grade

;; (def clean-housing-data
;;   (-> housing-data
;;       ;; Clean up missing values
;;       (tc/drop-missing columns-to-drop)

      ;; Drop rows with a sale price of zero. These are not useful for our purposes.
      ;; (tc/drop-rows #(= 0 (:sale-price %)))

      ;; Fill in missing `:construction-grade` values:
      ;; ()

      ;; Convert the sale-date to just a year, this is granular enough for our purposes
  ;;     (tc/convert-types :sale-date [[:int to-year]]))
  ;; )


;; (def non-numeric-columns
;;   (let [numerical-cols (tc/column-names clean-housing-data :type/numerical)]
;;     (tc/column-names clean-housing-data (complement (set numerical-cols)))))

;; (def construction-grade-map
;;   {"Low" 0
;;    "Fair" 1
;;    "Average" 2
;;    "Good" 3
;;    "Manufactured Home" 3
;;    "Very Good" 4
;;    "Excellent" 5})

;; (defn update-construction-grade [v]
;;   (get construction-grade-map v))

;; (def training-data
;;   (-> clean-housing-data
;;       ;; Deal with non-numeric columns. "Y"/"N" columns can be converted to 0s and 1s:
;;       (tc/update-columns [:garage :finished-basement :under-construction]
;;                          (partial map y-n-to-int))

;; ;; Remove columns that describe the address, we don't care about these for the purposes of our model
;;       (tc/drop-columns #(str/includes? (name %) "civic"))

;;       (tc/update-columns :construction-grade (partial map update-construction-grade))
;;       :style
;;       frequencies

;;       ))

;; Making the style column numeric

;; (->> clean-housing-data
;;     :style
;;     frequencies
;;     (sort-by second))



;; (tc/drop-columns (tc/info clean-housing-data) [:first :last :mode :standard-deviation :skew :mean])

;; clean-housing-data

;; (-> clean-housing-data
;;     (haclo/layer-point #:haclo{:x :square-foot-living-area
;;                                :y :sale-price
                               ;; :xscale {:type "log"}}))

;; (haclo/layer-point #:haclo{:x :square-foot-living-area
;;                            :y :sale-price})

;; (math/correlation-table clean-housing-data)

;; (-> housing-data
;;     (tc/drop-missing :sale-price)
;;     (tc/drop-rows #(= 0 (:sale-price %)))

;;     (tc/select-rows #(< 10000000 (:sale-price %))))

;; There are a handful of properties that sold for 10M

;; (-> housing-data
;;     (tc/select-rows #(> 10000000 (:sale-price %))))
