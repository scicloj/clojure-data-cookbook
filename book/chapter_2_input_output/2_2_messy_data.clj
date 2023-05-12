(ns chapter-2-input-output.2-2-messy-data
  {:nextjournal.clerk/toc true}
  (:require [tablecloth.api :as tc]
            [tech.v3.datatype.functional :as fun]))

;; This is a work in progress of the code examples that will make up chapter 2, section 2
;; of the Clojure data cookbook

;; # 2.2 Dealing with messy data

;; How do you know it's messy? What do I mean by that?

;; - Multiple types mixed in one column

;; Tablecloth will handle it just fine, it will just give the column the type `:object`

(def mixed-types
  (tc/dataset {:A ["string" "more strings" 3]
               :B [1 2 "whoops"]}))

(tc/info mixed-types :columns)

;; To convert all values in a row to a given type:

(tc/convert-types mixed-types :A :string)

(-> mixed-types
    (tc/convert-types :A :string)
    (tc/info :columns))

;; If you try to convert to something that can't be cast under the hood, it will throw an exception:

;; e.g. (this will make your notebook fail to render)
;; (tc/convert-types mixed-types :B :int)

;; - Multiple formats for a thing that's supposed to have one (e.g. phone numbers, postal codes)

;; You can pass any arbitrary function to update a column

(def misformatted
  (tc/dataset {:phone ["123-456-5654" "(304) 342 1235" "(423)-234-2342" "1234325984" "nope"]
               :postal-code ["t1n 0k2" "H9Q1L2" "H3H 8V0" "eu5h04" "just wrong"]}))

(require '[clojure.string :as str])

(def phone-regex
  (re-pattern
   (str
    ".*"        ; zero or more of any character
    "(\\d{3})"  ; any 3 numbers
    ".*"        ; zero or more of any character
    "(\\d{3})"  ; any 3 numbers
    ".*"        ; zero or more of any character
    "(\\d{4})"  ; any 4 numbers
    )))

(defn- normalize-phone-numbers [col]
  (map (fn [v]
         (let [[match a b c] (re-matches phone-regex v)]
           (if match
             (str "(" a ")-" b "-" c)
             "INVALID")))
       col))

(def postal-code-regex
  (re-pattern
   (str
    "([A-Z]{1})"  ; any letter
    ".*"          ; zero or more of any character
    "(\\d{1})"    ; any number
    ".*"
    "([A-Z]{1})"
    ".*"
    "(\\d{1})"
    ".*"
    "([A-Z]{1})"
    ".*"
    "(\\d{1})")))

(defn- normalize-postal-codes [col]
  (map (fn [v]
         (let [[match a b c d e f] (->> v str/upper-case (re-matches postal-code-regex))]
           (if match
             (str a b c " " d e f)
             "INVALID")))
       col))

(-> misformatted
    (tc/update-columns {:phone normalize-phone-numbers
                        :postal-code normalize-postal-codes}))

;; - Missing values

;; Tablecloth has [many built-in helpers](https://scicloj.github.io/tablecloth/index.html#Missing)
;; for dealing with missing values.

;; (require '[tech.v3.datatype.datetime :as dt])

(def sparse
  (tc/dataset {:A [1 2 3 nil nil 6]
               :B ["test" nil "this" "is" "a" "test"]}))

;; Drop whole rows with any missing values:

(tc/drop-missing sparse)

;; Drop whole row with any missing values in a given column:

(tc/drop-missing sparse :A)

;; Replace missing values

;; Table cloth includes many strategies for replacing missing values https://scicloj.github.io/tablecloth/index.html#replace

;; - Arbitrary values meant to indicate missing (e.g. "NONE", "N/A", false, etc.)

;; It's not uncommon to see missing values indicated in multiple different ways, sometimes
;; even within the same dataset. E.g. missing cells might be blank entirely, or they might
;; be populated with some arbitrary value meant to indicate "nothing", like "NONE", "N/A",
;; `false`, etc.
