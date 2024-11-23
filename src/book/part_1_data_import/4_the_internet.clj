^:kindly/hide-code
(ns book.part-1-data-import.4-the-internet
  (:require
   [tablecloth.api :as tc]))

;; # The Internet

;; ## Files over HTTP

;; If a dataset is already in a tabular format somewhere on the internet, getting it into tablecloth is trivially easy. You can just pass the URL directly to tablecloth and it will parse the data in to a table automatically.

;; For example the New York Times covid dataset is available as a CSV and Just Works:

(tc/dataset "https://raw.githubusercontent.com/nytimes/covid-19-data/master/live/us-counties.csv")



;; ## APIs

;; Retrieving data from a URL is trivially easy with tablecloth. You can simply pass the URL to tablecloth directly, and it will parse the results into a table.

(require '[clj-http.client :as http])

(http/get "https://pokeapi.co/api/v2/pokemon?limit=100")

;; Simple tabular data - Pokemon API
;; Returns clean list of pokemon with stats
(require '[charred.api :as ch])

;; (def pokemon-data
;;   (-> (http/get "https://pokeapi.co/api/v2/pokemon?limit=100")
;;       :body
;;       (json/parse-string true)
;;       :results))

;; (def pokemon-df
;;   (tc/dataset pokemon-data))

;; ;; Wide data - Countries API
;; ;; Returns extensive country data with many columns
;; (def countries-data
;;   (-> (http/get "https://restcountries.com/v3.1/all")
;;       :body
;;       (json/parse-string true)))

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

;; ;; Nested results - GitHub API
;; ;; Returns repository data nested in response envelope
;; (def github-data
;;   (-> (http/get "https://api.github.com/users/clojure/repos"
;;                 {:headers {"Accept" "application/vnd.github.v3+json"}})
;;       :body
;;       (json/parse-string true)))

;; (def github-df
;;   (tc/dataset (map #(select-keys % [:name :description :language :stargazers_count])
;;                    github-data)))

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
