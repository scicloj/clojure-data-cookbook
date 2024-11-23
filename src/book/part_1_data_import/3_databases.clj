^:kindly/hide-code
(ns book.part-1-data-import.3-databases
  (:require
   [tablecloth.api :as tc]))

;; # Databases

;; ## SQL

;; The best way to connect to a SQL database is with [next-jdbc](https://github.com/seancorfield/next-jdbc). You'll also need the relevant JDBC driver for the type of db you are trying to access. [MySQL](https://search.maven.org/artifact/com.mysql/mysql-connector-j), [PostgreSQL](https://search.maven.org/artifact/org.postgresql/postgresql), [Microsoft SQL Server](https://search.maven.org/artifact/com.microsoft.sqlserver/mssql-jdbc), and [Sqlite ](https://search.maven.org/artifact/org.xerial/sqlite-jdbc) are supported. There are more details in the [next-jdbc getting started guide](https://cljdoc.org/d/com.github.seancorfield/next.jdbc/1.3.939/doc/getting-started). We'll use Sqlite here for demonstration purposes, but beyond the backing driver that's installed, the usage should be the same for any underlying database.
;;
;; First, we add the correct dependencies to our `deps.edn` file:

;; ```clojure
;; com.github.seancorfield/next.jdbc {:mvn/version "1.3.939"}
;; org.xerial/sqlite-jdbc {:mvn/version "3.46.1.0"}
;; ```

;; Then require the necessary libraries:

(require '[next.jdbc :as jdbc])

;; Next, connect to the db. We'll use the [Chinook sample database](https://github.com/lerocha/chinook-database) for demonstration purposes:

(def db {:dbname "data/databases/Chinook_Sqlite.sqlite"
         :dbtype "sqlite"})

;; Then we have to create a "datasource" from the db, against which we can run queries:

(def data-source (jdbc/get-datasource db))

;; Now we can run SQL queries against the db:

(-> data-source
    (jdbc/execute! ["SELECT * FROM artist"]))

;; Of course now that you have the power of SQL at your fingertips, there may be fewer things you need tablecloth for, but for the scenarios where it is still desirable, we can simply pass the results of any SQL query to tablecloth and it will handle it correctly, creating a dataset:

(-> data-source
    (jdbc/execute! ["SELECT * FROM artist"])
    (tc/dataset))

;; If you need to pass a parameter to a query, you can use a positional parameter (`?`)

(-> data-source
    (jdbc/execute! ["SELECT * FROM artist WHERE Name = ?" "Aerosmith" ])
    (tc/dataset))

;; You can also pass multiple parameters, they're passed in sequentially:

(-> data-source
    (jdbc/execute! ["SELECT * FROM Track
INNER JOIN Album ON Track.AlbumId = Album.AlbumId
INNER JOIN Artist ON Album.ArtistId = Artist.ArtistId
WHERE (Artist.Name = ? AND Track.Milliseconds > ?)
ORDER BY Track.Milliseconds DESC"
                    "Queen"
                    300000])

    (tc/dataset))

;; Writing SQL strings by hand like this can become unwieldy, so you can also use [honeysql](https://github.com/seancorfield/honeysql) to add some structure to your queries. It's a lightweight SQL "DSL". Really it's a very lightweight way to express SQL queries as Clojure data structures. To use it we'll add the dependency to our `deps.edn` file and require the library:

;; ```clojure
;; com.github.seancorfield/honeysql {:mvn/version "2.6.1147"}
;; ```

(require '[honey.sql :as sql])

;; The query above written using honeysql looks like this:

(def tracks-query
  {:select [:*]
   :from [:Track]
   :join [:Album [:= :Track.AlbumId :Album.AlbumId]
          :Artist [:= :Album.ArtistId :Artist.ArtistId]]
   :where [:and
           [:= :Artist.Name :?artist]
           [:> :Track.Milliseconds :?duration]]
   :order-by [[:Track.Milliseconds :desc]]})

(-> data-source
    (jdbc/execute! (sql/format tracks-query
                               {:params {:artist "Queen" :duration 300000}}))
    (tc/dataset))

;; ## SPARQL

;; There are many different ways to connect to a graph database, depending on which one you're using. One easy way to query a graph database that exposes a SPARQL endpoint is to use [sparql-endpoint](https://github.com/ont-app/sparql-endpoint). We'll also use some tools from [grafter](https://github.com/Swirrl/grafter) to help work with the resulting RDF values.

;; First, we add the dependency to our `deps.edn` file:

;; ```clojure
;; ont-app/sparql-endpoint {:mvn/version "0.2.1"}
;; io.github.swirrl/grafter.core {:mvn/version "3.0.0"}
;; ```

;; Then require the necessary libraries:

(require '[ont-app.sparql-endpoint.core :as ep])
(require '[grafter-2.rdf.protocols :as pr])

;; We can connect directly to a SPARQL endpoint using `sparql-endpoint`. We'll use [DBpedia](https://www.dbpedia.org/about/) as an example, a crowd-sourced community effort to extract structured content from Wikipedia. They expose a SPARQL endpoint that we can directly connect to:

(def endpoint "https://dbpedia.org/sparql")

;; Working with graph databases can be confusing if you don't already know what you're looking for. To get started we can explore, for example, a list of the 100 most common things in this database. Running `SELECT` queries will return tabular results that will be easier for us to work with:

(def top-100
  (let [query "SELECT DISTINCT ?uri (COUNT(?instance) as ?count)
WHERE {
    ?instance a ?uri .
}
GROUP BY ?uri
ORDER BY DESC(?count)
LIMIT 100"]
    (ep/sparql-select endpoint query)))

top-100

;; To get these values into a tablecloth dataset, we can just throw it at tabecloth and it will know what to do with this list of maps:

(def ds (tc/dataset top-100))

ds

;; You'll notice that RDF data is very rich. Every value comes with metadata that helps machines understand what to make of it, like its datatype and where to find the datatypes definition. For our purposes we only care about the values, so we can use grafter to extract those from the resulting RDF, updating the column values using tablecloth's `update-column`:

(-> ds
    (tc/update-columns :all (partial map #(get % "value"))))

;; Any data that comes as a list of maps, including SPARQL results, fits easily into tablecloth.
