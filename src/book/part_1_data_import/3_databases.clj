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

(def ds (jdbc/get-datasource db))

;; Now we can run SQL queries against the db:

(-> ds
    (jdbc/execute! ["SELECT * FROM artist"]))

;; Of course now that you have the power of SQL at your fingertips, there may be fewer things you need tablecloth for, but for the scenarios where it is still desirable, we can simply pass the results of any SQL query to tablecloth and it will handle it correctly, creating a dataset:

(-> ds
    (jdbc/execute! ["SELECT * FROM artist"])
    (tc/dataset))

;; If you need to pass a parameter to a query, you can use a positional parameter (`?`)

(-> ds
    (jdbc/execute! ["SELECT * FROM artist WHERE Name = ?" "Aerosmith" ])
    (tc/dataset))

;; You can also pass multiple parameters, they're passed in sequentially:

(-> ds
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

(-> ds
    (jdbc/execute! (sql/format tracks-query
                               {:params {:artist "Queen" :duration 300000}}))
    (tc/dataset))



;; ;; note for SQLite specifically the concat operator is `||` not `+`

;; (-> ds
;;     (jdbc/execute! ["SELECT * FROM artist WHERE Name like '%' || ? || '%'" "man"])
;;     (tc/dataset))
