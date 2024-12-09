^:kindly/hide-code
(ns book.part-1-data-import.4-the-internet
  (:require
   [tablecloth.api :as tc]
   [charred.api :as ch]
   [clj-http.client :as http]))

;; # The Internet

;; ## Files over HTTP

;; If a dataset is already in a tabular format somewhere on the internet, getting it into tablecloth is trivially easy. You can just pass the URL directly to tablecloth and it will parse the data in to a table automatically.

;; For example this dataset of world cities is available as a CSV and Just Works:

(tc/dataset "https://raw.githubusercontent.com/datasets/world-cities/refs/heads/main/data/world-cities.csv")

;; ## APIs

;; JSON data also often comes in a format that tablecloth knows how to handle, but JSON API responses typically have results nested inside some type of data structure and require at least a little bit of parsing to extract the relevant data. How much parsing depends on the structure. Here, we'll explore how to work with a few kinds of differently-shaped data to get it into a tablecloth dataset.

;; Some of this borders on data manipulation, but the idea is that here we'll cover the bare minimum that's necessary to get some common shapes of data into a dataset, and we'll look at data wrangling more in-depth later on.

;; To make the initial request to any API endpoint we need to add the [`clj-http`](https://github.com/dakrone/clj-http) library to our project and require it. We'll also use [charred](https://github.com/cnuernber/charred), the fastest JSON and CSV parsing library for Clojure, to parse the responses:

;; ```
;; clj-http/clj-http {:mvn/version "3.13.0"}
;; com.cnuernber/charred {:mvn/version "1.034"}
;; ```

(require '[clj-http.client :as http])
(require '[charred.api :as ch])

;; ### Simple Tabular Data

;; Some APIs return data in a simple tabular format that just works with tablecloth. The Pokemon API is one such example. Requesting the first 100 Pokemon returns a response with the results nested under a "results" key. First we parse the response body as JSON, and we can see that the results are a list of JSON objects.

(def pokemon-response (http/get "https://pokeapi.co/api/v2/pokemon?limit=100"))

(-> pokemon-response
    :body
    ch/read-json)

;; Passing this list of results to tablecloth will give us a nice table by default:

(-> pokemon-response
    :body
    ch/read-json
    (get "results")
    tc/dataset)

;; While we're here, we can do a little bonus side quest to fill out this dataset and demonstrate how to recursively fetch all Pokemon by automatically following the "next" links that are included in the responses, adding each batch of results to our dataset:

(defn fetch-all-pokemon [url]
  (loop [current-url url
         ds (tc/dataset)]
    (if (nil? current-url)
      ds
      (let [{:strs [results next]} (-> current-url http/get :body ch/read-json)]
        (recur next (tc/concat ds (tc/dataset results)))))))

(fetch-all-pokemon "https://pokeapi.co/api/v2/pokemon?limit=100")

;; TODO: Are any of these worth including?
;; ### Wide Data

;; Other APIs return very wide data, as in data that is spread out across many columns.

;; Returns extensive country data with many columns
(def countries-response (http/get "https://restcountries.com/v3.1/all"))

(-> countries-response
    :body
    (ch/read-json))

(tc/dataset (-> countries-response
                :body
                (ch/read-json)))

;; (def countries-df
;;   (tc/dataset (map #(select-keys % [:name :capital :population :area :region :subregion
;;                                    :languages :currencies :timezones])
;;                    countries-data)))

;; ;; Relational data - Star Wars API
;; ;; Returns characters with nested references to films, vehicles etc
;; (def sw-data
;;   (-> (http/get "https://swapi.dev/api/people/")
;;       :body
;;       (json/parse-string true)
;;       :results))

;; (def sw-df
;;   (tc/dataset (map #(assoc % :film_count (count (:films %))) sw-data)))

;; ;; Deeply nested - OpenLibrary API
;; ;; Returns nested book data with author/publisher hierarchies
;; (def book-data
;;   (-> (http/get "https://openlibrary.org/works/OL45804W.json")
;;       :body
;;       (json/parse-string true)))

;; (def book-df
;;   (tc/dataset {:title [(:title book-data)]
;;                :subjects (get book-data :subjects)
;;                :author_count [(count (get-in book-data [:authors]))]}))

;; ;; Custom headers - NASA API
;; ;; Requires API key in header
;; (def nasa-data
;;   (-> (http/get "https://api.nasa.gov/planetary/apod"
;;                 {:query-params {"api_key" "DEMO_KEY"
;;                               "count" "10"}})
;;       :body
;;       (json/parse-string true)))

;; (def nasa-df
;;   (tc/dataset (map #(select-keys % [:title :date :explanation]) nasa-data)))


;; ;; - The Internet, including scraping web pages and processing the often hierarchical and deeply-nested data returned by many APIs.
