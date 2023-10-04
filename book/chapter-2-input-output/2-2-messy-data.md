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
(ns chapter-2-input-output.2-2-messy-data
  {:nextjournal.clerk/toc true}
  (:require [tablecloth.api :as tc]
            [tech.v3.datatype.functional :as fun]
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

This is a work in progress of the code examples that will make up chapter 2, section 2
of the Clojure data cookbook

# Dealing with messy data

How do you know it's messy? What do I mean by that?

## Multiple types mixed in one column

Tablecloth will handle it just fine, it will just give the column the type `:object`

```clojure
(def mixed-types
  (tc/dataset {:A ["string" "more strings" 3]
               :B [1 2 "whoops"]}))
```

<div class="printedClojure">

```clojure
"#'chapter-2-input-output.2-2-messy-data/mixed-types"
```

</div>

```clojure
(tc/info mixed-types :columns)
```

<div><div>Unimplemented: <code>:kind/dataset</code></div><code>_unnamed :column info [2 4]:

| :categorical? | :name | :datatype | :n-elems |
|---------------|-------|-----------|---------:|
|          true |    :A |   :object |        3 |
|          true |    :B |   :object |        3 |
</code></div>

To convert all values in a row to a given type:

```clojure
(tc/convert-types mixed-types :A :string)
```

<div><div>Unimplemented: <code>:kind/dataset</code></div><code>_unnamed [3 2]:

|           :A |     :B |
|--------------|--------|
|       string |      1 |
| more strings |      2 |
|            3 | whoops |
</code></div>

```clojure
(-> mixed-types
    (tc/convert-types :A :string)
    (tc/info :columns))
```

<div><div>Unimplemented: <code>:kind/dataset</code></div><code>_unnamed :column info [2 4]:

| :categorical? | :name | :datatype | :n-elems |
|---------------|-------|-----------|---------:|
|          true |    :A |   :string |        3 |
|          true |    :B |   :object |        3 |
</code></div>

If you try to convert to something that can't be cast under the hood, it will throw an exception:

e.g. (this will make your notebook fail to render)
(tc/convert-types mixed-types :B :int)

## Multiple formats for a thing that's supposed to have one (e.g. phone numbers, postal codes)

You can pass any arbitrary function to update a column

```clojure
(def misformatted
  (tc/dataset {:phone ["123-456-5654" "(304) 342 1235" "(423)-234-2342" "1234325984" "nope"]
               :postal-code ["t1n 0k2" "H9Q1L2" "H3H 8V0" "eu5h04" "just wrong"]}))
```

<div class="printedClojure">

```clojure
"#'chapter-2-input-output.2-2-messy-data/misformatted"
```

</div>

```clojure
(require '[clojure.string :as str])
```

<div class="printedClojure">

```clojure
nil
```

</div>

```clojure
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
```

<div class="printedClojure">

```clojure
"#'chapter-2-input-output.2-2-messy-data/phone-regex"
```

</div>

```clojure
(defn- normalize-phone-numbers [col]
  (map (fn [v]
         (let [[match a b c] (re-matches phone-regex v)]
           (if match
             (str "(" a ")-" b "-" c)
             "INVALID")))
       col))
```

<div class="printedClojure">

```clojure
"#'chapter-2-input-output.2-2-messy-data/normalize-phone-numbers"
```

</div>

```clojure
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
```

<div class="printedClojure">

```clojure
"#'chapter-2-input-output.2-2-messy-data/postal-code-regex"
```

</div>

```clojure
(defn- normalize-postal-codes [col]
  (map (fn [v]
         (let [[match a b c d e f] (->> v str/upper-case (re-matches postal-code-regex))]
           (if match
             (str a b c " " d e f)
             "INVALID")))
       col))
```

<div class="printedClojure">

```clojure
"#'chapter-2-input-output.2-2-messy-data/normalize-postal-codes"
```

</div>

```clojure
(-> misformatted
    (tc/update-columns {:phone normalize-phone-numbers
                        :postal-code normalize-postal-codes}))
```

<div><div>Unimplemented: <code>:kind/dataset</code></div><code>_unnamed [5 2]:

|         :phone | :postal-code |
|----------------|--------------|
| (123)-456-5654 |      T1N 0K2 |
| (304)-342-1235 |      H9Q 1L2 |
| (423)-234-2342 |      H3H 8V0 |
| (123)-432-5984 |      INVALID |
|        INVALID |      INVALID |
</code></div>

## Missing values

Tablecloth has [many built-in helpers](https://scicloj.github.io/tablecloth/index.html#Missing)
for dealing with missing values.

```clojure
(require '[tech.v3.datatype.datetime :as dt])
```

<div class="printedClojure">

```clojure
nil
```

</div>

```clojure
(def sparse
  (tc/dataset {:A [1 2 3 nil nil 6]
               :B ["test" nil "this" "is" "a" "test"]}))
```

<div class="printedClojure">

```clojure
"#'chapter-2-input-output.2-2-messy-data/sparse"
```

</div>

Drop whole rows with any missing values:

```clojure
(tc/drop-missing sparse)
```

<div><div>Unimplemented: <code>:kind/dataset</code></div><code>_unnamed [3 2]:

| :A |   :B |
|---:|------|
|  1 | test |
|  3 | this |
|  6 | test |
</code></div>

Drop whole row with any missing values in a given column:

```clojure
(tc/drop-missing sparse :A)
```

<div><div>Unimplemented: <code>:kind/dataset</code></div><code>_unnamed [4 2]:

| :A |   :B |
|---:|------|
|  1 | test |
|  2 |      |
|  3 | this |
|  6 | test |
</code></div>

Replace missing values

Table cloth includes many strategies for replacing missing values https://scicloj.github.io/tablecloth/index.html#replace

## Arbitrary values meant to indicate missing (e.g. "NONE", "N/A", false, etc.)

It's not uncommon to see missing values indicated in multiple different ways, sometimes
even within the same dataset. E.g. missing cells might be blank entirely, or they might
be populated with some arbitrary value meant to indicate "nothing", like "NONE", "N/A",
`false`, etc.
